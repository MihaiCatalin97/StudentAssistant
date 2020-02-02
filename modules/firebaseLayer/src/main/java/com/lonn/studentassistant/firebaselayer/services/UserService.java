package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.UserAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.UserViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.USERS;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.UserField.PERSON_UUID;

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
