package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.LaboratoryAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.LABORATORIES;

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
}