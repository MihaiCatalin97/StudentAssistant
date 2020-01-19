package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;

import static androidx.databinding.library.baseAdapters.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;

public class RecurringClassService extends Service<RecurringClass, Exception, RecurringClassViewModel> {
	private static RecurringClassService instance;

	private RecurringClassService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new RecurringClassAdapter(firebaseConnection);
	}

	public static RecurringClassService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new RecurringClassService(firebaseConnection);
		}

		return instance;
	}

	@Override
	protected RecurringClassViewModel transform(RecurringClass recurringClass) {
		RecurringClassViewModel result = super.transform(recurringClass);

		CourseService.getInstance(firebaseConnection)
				.getById(recurringClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		OtherActivityService.getInstance(firebaseConnection)
				.getById(recurringClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		return result;
	}

	@Override
	protected DatabaseTable<RecurringClass> getDatabaseTable() {
		return RECURRING_CLASSES;
	}
}
