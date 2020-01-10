package com.lonn.studentassistant.firebaselayer.services;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.RequestLogger;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

public class AuthenticationService {
	private static final RequestLogger LOGGER = new RequestLogger();
	private static AuthenticationService instance;
	private FirebaseAuth mAuth;
	private FirebaseConnection firebaseConnection;

	private AuthenticationService(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
		mAuth = FirebaseAuth.getInstance();
	}

	public static AuthenticationService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new AuthenticationService(firebaseConnection);
		}

		return instance;
	}

	public FirebaseTask<FirebaseUser, Throwable> register(final String personUUID,
														  final String email,
														  final String password) {
		return new FirebaseTask<FirebaseUser, Throwable>(firebaseConnection) {
			@Override
			public void onComplete(Consumer<FirebaseUser> onSuccess, Consumer<Throwable> onError) {
				mAuth.createUserWithEmailAndPassword(email, password)
						.addOnSuccessListener((authResult) -> {
							FirebaseUser newUser = authResult.getUser();
							LOGGER.logRegisterSuccess(newUser.getEmail(), newUser.getUid());

							User registeringUser = new User()
									.setUserId(newUser.getUid())
									.setPersonUUID(personUUID);

							UserService.getInstance(firebaseConnection)
									.save(registeringUser)
									.onComplete((none) -> {
												LOGGER.logRegistrationLinkingSuccess(newUser.getUid(),
														personUUID);
												onSuccess.consume(newUser);
											},
											(exception) -> {
												if (exception.getMessage() != null) {
													LOGGER.logRegistrationLinkingFail(newUser.getUid(),
															personUUID,
															exception.getMessage());
													newUser.delete();

													if (onError != null) {
														onError.consume(exception);
													}
												}
											});
						})
						.addOnFailureListener((exception) -> {
							LOGGER.logRegisterFail(email, exception.getMessage());

							if (onError != null) {
								onError.consume(exception);
							}
						});
			}
		};
	}

	public FirebaseTask<Void, Throwable> login(final String email,
											   final String password) {
		return new FirebaseTask<Void, Throwable>(firebaseConnection) {
			@Override
			public void onComplete(Consumer<Void> onSuccess, @Nullable Consumer<Throwable> onError) {
				mAuth.signInWithEmailAndPassword(email, password)
						.addOnSuccessListener((authResult) -> {
							LOGGER.logLoginSuccess(email);
							onSuccess.consume(null);
						})
						.addOnFailureListener((exception) -> {
							LOGGER.logLoginFail(email, exception.getMessage());

							if (onError != null) {
								onError.consume(null);
							}
						});
			}
		};
	}
}
