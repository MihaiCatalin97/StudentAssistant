package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.GradeAdapter;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseVoidTask;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.requests.Request;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.GRADES;

public class GradeService extends Service<Grade, Exception, GradeViewModel> {
	private static GradeService instance;

	private GradeService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new GradeAdapter();
	}

	public static GradeService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new GradeService(firebaseConnection);
		}

		return instance;
	}

	@Override
	public DatabaseTable<Grade> getDatabaseTable() {
		return GRADES;
	}

	@Override
	public FirebaseTask<Void, Exception> save(GradeViewModel entityViewModel) {
		return new FirebaseVoidTask<Exception>(firebaseConnection,
				new SaveRequest<Grade, Exception>()
						.databaseTable(getDatabaseTable())
						.entity(adapter.adapt(entityViewModel))) {
			@Override
			protected Request<Void, Exception> createRequest(Request<Void, Exception> request,
															 Consumer<Void> onSuccess,
															 Consumer<Exception> onError) {
				return request.onSuccess(none -> {
					if (onSuccess != null) {
						onSuccess.consume(null);
					}
				}).onError(onError);
			}
		};
	}
}
