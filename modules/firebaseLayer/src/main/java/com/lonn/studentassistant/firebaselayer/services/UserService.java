package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.UserViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.USERS;

public class UserService extends Service<User, Exception, UserViewModel> {
	private static UserService instance;

	public static UserService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new UserService(firebaseConnection);
		}

		return instance;
	}

	private UserService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	@Override
	protected DatabaseTable<User> getDatabaseTable() {
		return USERS;
	}
}
