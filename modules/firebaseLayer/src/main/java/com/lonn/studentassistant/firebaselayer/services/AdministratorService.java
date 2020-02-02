package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.AdministratorAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.services.abstractions.PersonService;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ADMINISTRATORS;

public class AdministratorService extends PersonService<Administrator, AdministratorViewModel> {
	private static AdministratorService instance;
	protected FileMetadataService fileMetadataService;

	private AdministratorService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static AdministratorService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new AdministratorService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new AdministratorAdapter();
		fileMetadataService = FileMetadataService.getInstance(firebaseConnection);
	}

	@Override
	protected DatabaseTable<Administrator> getDatabaseTable() {
		return ADMINISTRATORS;
	}

	protected PermissionLevel getPermissionLevel(Administrator administrator) {
		return authenticationService.getPermissionLevel(administrator);
	}
}
