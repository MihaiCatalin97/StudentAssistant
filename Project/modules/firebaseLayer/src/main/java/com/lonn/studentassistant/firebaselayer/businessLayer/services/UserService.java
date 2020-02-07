package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.UserAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.User;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.UserViewModel;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.USERS;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel.WRITE;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields.UserField.PERSON_UUID;

public class UserService extends Service<User, Exception, UserViewModel> {
	private static UserService instance;

	private UserService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static UserService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new UserService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new UserAdapter();
	}

	@Override
	protected DatabaseTable<User> getDatabaseTable() {
		return USERS;
	}

	public Future<Boolean, Exception> personHasAccount(String personKey) {
		Future<Boolean, Exception> result = new Future<>();

		firebaseConnection.execute(new GetRequest<User, Exception>()
				.databaseTable(USERS)
				.subscribe(false)
				.predicate(where(PERSON_UUID).equalTo(personKey))
				.onSuccess(users -> result.complete(users != null && users.size() > 0))
				.onError(result::completeExceptionally));

		return result;
	}

	protected PermissionLevel getPermissionLevel(User user) {
		return WRITE;
//		return authenticationService.getPermissionLevel(registrationToken);
	}
}
