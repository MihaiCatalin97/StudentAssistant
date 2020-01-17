package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.LaboratoryAdapter;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseCustomTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.LABORATORIES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class LaboratoryService extends Service<Laboratory, Exception, LaboratoryViewModel> {
	private static LaboratoryService instance;

	private LaboratoryService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new LaboratoryAdapter();
	}

	public static LaboratoryService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new LaboratoryService(firebaseConnection);
		}

		return instance;
	}

	@Override
	protected DatabaseTable<Laboratory> getDatabaseTable() {
		return LABORATORIES;
	}

	public FirebaseTask<Void, Exception> addGradeToLaboratory(String gradeKey, String laboratoryKey) {
		new FirebaseCustomTask<>(firebaseConnection,
				new GetRequest<Laboratory, Exception>()
						.databaseTable(getDatabaseTable())
						.predicate(where(ID).equalTo(laboratoryKey)));
	}
}