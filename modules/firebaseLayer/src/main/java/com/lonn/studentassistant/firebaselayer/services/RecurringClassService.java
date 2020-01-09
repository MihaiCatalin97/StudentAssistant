package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;

public class RecurringClassService extends Service<RecurringClass, Exception, RecurringClassViewModel> {
	private static RecurringClassService instance;

	public static RecurringClassService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new RecurringClassService(firebaseConnection);
		}

		return instance;
	}

	private RecurringClassService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new RecurringClassAdapter(firebaseConnection);
	}

	@Override
	protected DatabaseTable<RecurringClass> getDatabaseTable() {
		return RECURRING_CLASSES;
	}
}
