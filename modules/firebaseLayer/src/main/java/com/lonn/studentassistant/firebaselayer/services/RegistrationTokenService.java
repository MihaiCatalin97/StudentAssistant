package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.entities.RegistrationToken;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;
import com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.REGISTRATION_TOKENS;

public class RegistrationTokenService {
	private static RegistrationTokenService instance;
	private final FirebaseConnection firebaseConnection;

	private RegistrationTokenService(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
	}

	public static RegistrationTokenService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new RegistrationTokenService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	private void init() {
	}

	public Future<AccountType, Exception> getTypeForToken(String token) {
		Future<AccountType, Exception> result = new Future<>();

		firebaseConnection.execute(new GetRequest<RegistrationToken, Exception>()
				.databaseTable(REGISTRATION_TOKENS)
				.predicate(Predicate.where(BaseEntityField.ID).equalTo(token))
				.onSuccess(tokens -> {
					if (tokens != null && tokens.size() == 1) {
						result.complete(tokens.get(0).getAccountType());
					}
					else {
						result.complete(null);
					}
				})
				.onError(result::completeExceptionally));

		return result;
	}
}
