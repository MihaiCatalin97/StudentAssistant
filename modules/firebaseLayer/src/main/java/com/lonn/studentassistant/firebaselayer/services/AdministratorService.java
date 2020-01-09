package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.AdministratorAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ADMINISTRATORS;

public class AdministratorService extends Service<Administrator, Exception, AdministratorViewModel> {
	private static AdministratorService instance;

	public static AdministratorService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new AdministratorService(firebaseConnection);
		}

		return instance;
	}

	private AdministratorService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new AdministratorAdapter();
	}

	@Override
	protected DatabaseTable<Administrator> getDatabaseTable() {
		return ADMINISTRATORS;
	}
}
