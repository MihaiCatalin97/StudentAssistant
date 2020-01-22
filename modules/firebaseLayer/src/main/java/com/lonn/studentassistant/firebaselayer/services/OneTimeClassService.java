package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.OneTimeClassAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;

import static androidx.databinding.library.baseAdapters.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;

public class OneTimeClassService extends Service<OneTimeClass, Exception, OneTimeClassViewModel> {
	private static OneTimeClassService instance;

	private OneTimeClassService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new OneTimeClassAdapter(firebaseConnection);
	}

	public static OneTimeClassService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new OneTimeClassService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		adapter = new OneTimeClassAdapter(firebaseConnection);
	}

	@Override
	protected OneTimeClassViewModel transform(OneTimeClass oneTimeClass) {
		OneTimeClassViewModel result = super.transform(oneTimeClass);

		CourseService.getInstance(firebaseConnection)
				.getById(oneTimeClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		OtherActivityService.getInstance(firebaseConnection)
				.getById(oneTimeClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		return result;
	}

	@Override
	protected DatabaseTable<OneTimeClass> getDatabaseTable() {
		return ONE_TIME_CLASSES;
	}
}
