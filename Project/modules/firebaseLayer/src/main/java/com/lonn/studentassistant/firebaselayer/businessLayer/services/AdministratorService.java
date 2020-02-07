package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.AdministratorAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.PersonService;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AdministratorViewModel;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.ADMINISTRATORS;

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
