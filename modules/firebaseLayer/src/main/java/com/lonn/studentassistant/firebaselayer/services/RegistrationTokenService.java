package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.RegistrationTokenAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.RegistrationToken;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.RegistrationTokenViewModel;

import java.util.Date;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.REGISTRATION_TOKENS;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;

public class RegistrationTokenService extends Service<RegistrationToken, Exception, RegistrationTokenViewModel> {
	private static RegistrationTokenService instance;

	private RegistrationTokenService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static RegistrationTokenService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new RegistrationTokenService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new RegistrationTokenAdapter();
	}

	private Future<RegistrationTokenViewModel, Exception> getByToken(String tokenToFind) {
		Future<RegistrationTokenViewModel, Exception> result = new Future<>();

		firebaseConnection.execute(new GetRequest<RegistrationToken, Exception>()
				.databaseTable(REGISTRATION_TOKENS)
				.subscribe(false)
				.onSuccess(tokens -> {
					if (tokens != null) {
						for (RegistrationToken token : tokens) {
							if (token.getToken().equals(tokenToFind)) {
								result.complete(adapter.adapt(token));
								return;
							}
						}
					}

					result.complete(null);
				})
				.onError(result::completeExceptionally));

		return result;
	}

	public Future<AccountType, Exception> getTypeForToken(String tokenToCheck) {
		Future<AccountType, Exception> result = new Future<>();

		getByToken(tokenToCheck)
				.onSuccess(token -> {
					if (token.getExpiresAt().after(new Date())) {
						result.complete(token.getAccountType());
						return;
					}
					result.complete(null);
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> deleteByToken(String tokenToDelete) {
		Future<Void, Exception> result = new Future<>();

		getByToken(tokenToDelete)
				.onSuccess(token -> deleteById(token.getKey())
						.onSuccess(result::complete)
						.onError(result::completeExceptionally))
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	public Future<RegistrationTokenViewModel, Exception> getById(String id, boolean subscribe) {
		Future<RegistrationTokenViewModel, Exception> result = new Future<>();

		super.getById(id, subscribe)
				.onSuccess(token -> {
					if (token.getExpiresAt().after(new Date())) {
						result.complete(token);
						return;
					}

					result.complete(null);
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> createTokenAndSendEmail(AccountType accountType, String email) {
		Future<Void, Exception> result = new Future<>();
		RegistrationTokenViewModel registrationTokenViewModel = new RegistrationTokenViewModel()
				.setAccountType(accountType);

		save(registrationTokenViewModel)
				.onSuccess(none -> EmailService.getInstance()
						.sendEmail(email,
								accountType.toString().toLowerCase(),
								registrationTokenViewModel.getToken(),
								registrationTokenViewModel.getExpiresAt().toString())
						.onSuccess(result::complete)
						.onError(result::completeExceptionally)
				)
				.onError(result::completeExceptionally);

		return result;
	}


	@Override
	protected DatabaseTable<RegistrationToken> getDatabaseTable() {
		return REGISTRATION_TOKENS;
	}

	protected PermissionLevel getPermissionLevel(RegistrationToken registrationToken) {
		return WRITE;
//		return authenticationService.getPermissionLevel(registrationToken);
	}
}
