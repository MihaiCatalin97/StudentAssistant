package com.lonn.studentassistant.firebaselayer.firebaseConnection;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.config.FirebaseConfig;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.database.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.LoginRequest;
import com.lonn.studentassistant.firebaselayer.requests.RegisterRequest;
import com.lonn.studentassistant.firebaselayer.requests.Request;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ADMINISTRATORS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_CONTENT;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_METADATA;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.GRADES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.LABORATORIES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.REGISTRATION_TOKENS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.STUDENTS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.USERS;

@Slf4j
public class FirebaseConnection {
	private static FirebaseConnection instance;
	@Getter(AccessLevel.PROTECTED)
	private Map<DatabaseTable, DatabaseContext> databaseMap = new HashMap<>();
	private FirebaseAuth mAuth;
	private RequestLogger logger;
	private FirebaseConfig firebaseConfig;

	private FirebaseConnection(Context applicationContext) {
		firebaseConfig = new FirebaseConfig(applicationContext);

		databaseMap.put(COURSES, buildContextForTable(COURSES));
		databaseMap.put(GRADES, buildContextForTable(GRADES));
		databaseMap.put(OTHER_ACTIVITIES, buildContextForTable(OTHER_ACTIVITIES));
		databaseMap.put(RECURRING_CLASSES, buildContextForTable(RECURRING_CLASSES));
		databaseMap.put(ONE_TIME_CLASSES, buildContextForTable(ONE_TIME_CLASSES));
		databaseMap.put(USERS, buildContextForTable(USERS));
		databaseMap.put(FILE_METADATA, buildContextForTable(FILE_METADATA));
		databaseMap.put(FILE_CONTENT, buildContextForTable(FILE_CONTENT));
		databaseMap.put(LABORATORIES, buildContextForTable(LABORATORIES));
		databaseMap.put(REGISTRATION_TOKENS, buildContextForTable(REGISTRATION_TOKENS));

		databaseMap.put(ADMINISTRATORS, buildContextForTable(ADMINISTRATORS));
		databaseMap.put(PROFESSORS, buildContextForTable(PROFESSORS));
		databaseMap.put(STUDENTS, buildContextForTable(STUDENTS));

		mAuth = FirebaseAuth.getInstance();
		logger = new RequestLogger();
	}

	public static FirebaseConnection getInstance(Context applicationContext) {
		if (instance == null) {
			instance = new FirebaseConnection(applicationContext);
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public void execute(Request request) {
		if (request instanceof GetRequest) {
			execute((GetRequest) request);
		}
		else if (request instanceof SaveRequest) {
			execute((SaveRequest) request);
		}
		else if (request instanceof DeleteByIdRequest) {
			execute((DeleteByIdRequest) request);
		}
		else if (request instanceof DeleteAllRequest) {
			execute((DeleteAllRequest) request);
		}
		else if (request instanceof LoginRequest) {
			execute((LoginRequest) request);
		}
		else if (request instanceof RegisterRequest) {
			execute((RegisterRequest) request);
		}
	}

	public <T extends BaseEntity> void execute(GetRequest<T, Exception> request) {
		@SuppressWarnings("unchecked")
		DatabaseContext<T> context = getDatabaseMap().get(request.databaseTable());

		if (context != null) {
			context.get(request.onSuccess(), request.onError(),
					request.predicate(), request.subscribe());
		}
	}

	public <T extends BaseEntity> void execute(SaveRequest<T, Exception> request) {
		@SuppressWarnings("unchecked")
		DatabaseContext<T> context = getDatabaseMap().get(request.databaseTable());

		if (context != null) {
			for (T entity : request.entities()) {
				context.saveOrUpdate(entity, request.onSuccess(), request.onError());
			}
		}
	}

	public void execute(DeleteByIdRequest request) {
		DatabaseContext context = getDatabaseMap().get(request.databaseTable());

		if (context != null) {
			context.delete(request.key(), request.onSuccess(), request.onError());
		}
	}

	public void execute(DeleteAllRequest request) {
		DatabaseContext context = getDatabaseMap().get(request.databaseTable());

		if (context != null) {
			context.deleteAll(request.onSuccess(), request.onError());
		}
	}

	public void execute(final LoginRequest request) {
		mAuth.signInWithEmailAndPassword(request.username(), request.password())
				.addOnSuccessListener((authResult) -> {
					logger.logLoginSuccess(request.username());
					request.onSuccess().consume(null);
				})
				.addOnFailureListener((exception) -> {
					logger.logLoginFail(request.username(), exception.getMessage());
					request.onError().consume(null);
				});
	}

	public void execute(final RegisterRequest request) {
		mAuth.createUserWithEmailAndPassword(request.email(), request.password())
				.addOnSuccessListener((authResult) -> {
					FirebaseUser newUser = authResult.getUser();
					logger.logRegisterSuccess(newUser.getEmail(), newUser.getUid());

					User registeringUser = new User()
							.setEmail(request.email())
							.setUserId(newUser.getUid())
							.setPersonUUID(request.personUUID())
							.setAccountType(request.accountType());

					execute(new SaveRequest<User, Exception>()
							.databaseTable(USERS)
							.entity(registeringUser)
							.onSuccess((u) -> {
								logger.logRegistrationLinkingSuccess(newUser.getUid(),
										request.personUUID());

								request.onSuccess().consume(newUser);
							})
							.onError((Exception exception) -> {
								if (exception.getMessage() != null) {
									logger.logRegistrationLinkingFail(newUser.getUid(),
											request.personUUID(),
											exception.getMessage());
									newUser.delete();
									request.onError().consume(exception);
								}
							}));

				})
				.addOnFailureListener((exception) -> {
					logger.logRegisterFail(request.email(), exception.getMessage());
					request.onError().consume(exception);
				});
	}

	private <T extends BaseEntity> DatabaseContext<T> buildContextForTable(DatabaseTable<T> table) {
		return new DatabaseContext<>(firebaseConfig.getTableReference(table),
				table.getTableClass());
	}
}