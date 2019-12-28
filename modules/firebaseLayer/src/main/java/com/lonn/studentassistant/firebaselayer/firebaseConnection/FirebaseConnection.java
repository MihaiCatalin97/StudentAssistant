package com.lonn.studentassistant.firebaselayer.firebaseConnection;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.config.FirebaseConfig;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer;
import com.lonn.studentassistant.firebaselayer.database.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.database.contexts.DatabaseContextWithIdentification;
import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.HashableEntity;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;
import com.lonn.studentassistant.firebaselayer.predicates.fields.IdentificationHashField;
import com.lonn.studentassistant.firebaselayer.predicates.fields.UserField;
import com.lonn.studentassistant.firebaselayer.requests.CredentialsCheckRequest;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.LoginRequest;
import com.lonn.studentassistant.firebaselayer.requests.RegisterRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

        databaseMap.put(DatabaseTableContainer.COURSES, buildContextForTable(DatabaseTableContainer.COURSES));
        databaseMap.put(DatabaseTableContainer.GRADES, buildContextForTable(DatabaseTableContainer.GRADES));
        databaseMap.put(DatabaseTableContainer.OTHER_ACTIVITIES, buildContextForTable(DatabaseTableContainer.OTHER_ACTIVITIES));
        databaseMap.put(DatabaseTableContainer.RECURRING_CLASSES, buildContextForTable(DatabaseTableContainer.RECURRING_CLASSES));
        databaseMap.put(DatabaseTableContainer.ONE_TIME_CLASSES, buildContextForTable(DatabaseTableContainer.ONE_TIME_CLASSES));
        databaseMap.put(DatabaseTableContainer.USERS, buildContextForTable(DatabaseTableContainer.USERS));
        databaseMap.put(DatabaseTableContainer.IDENTIFICATION_HASHES, buildContextForTable(DatabaseTableContainer.IDENTIFICATION_HASHES));

        databaseMap.put(DatabaseTableContainer.ADMINISTRATORS, buildContextWithIdentificationForTable(DatabaseTableContainer.ADMINISTRATORS));
        databaseMap.put(DatabaseTableContainer.PROFESSORS, buildContextWithIdentificationForTable(DatabaseTableContainer.PROFESSORS));
        databaseMap.put(DatabaseTableContainer.STUDENTS, buildContextWithIdentificationForTable(DatabaseTableContainer.STUDENTS));

        mAuth = FirebaseAuth.getInstance();
        logger = new RequestLogger();
    }

    public static FirebaseConnection getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new FirebaseConnection(applicationContext);
        }

        return instance;
    }

    public <T extends BaseEntity> void execute(GetRequest<T> request) {
        @SuppressWarnings("unchecked")
        DatabaseContext<T> context = getDatabaseMap().get(request.databaseTable());

        if (context != null) {
            context.get(request.onSuccess(), request.onError(),
                    request.predicate(), request.subscribe());
        }
    }

    public <T extends BaseEntity> void execute(SaveRequest<T> request) {
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
                    request.onSuccess().run();
                })
                .addOnFailureListener((exception) -> {
                    logger.logLoginFail(request.username(), exception.getMessage());
                    request.onError().run();
                });
    }

    public void execute(final CredentialsCheckRequest request) {
        execute(new GetRequest<IdentificationHash>()
                .databaseTable(DatabaseTableContainer.IDENTIFICATION_HASHES)
                .onSuccess((identifications) -> {
                    if (identifications.size() == 1) {
                        execute(new GetRequest<User>()
                                .databaseTable(DatabaseTableContainer.USERS)
                                .predicate(Predicate.where(UserField.IDENTIFICATION_HASH)
                                        .equalTo(request.identificationHash()))
                                .onSuccess((users) -> {
                                    if (users.size() == 0) {
                                        logger.logCredentialsCheckSuccess(request.identificationHash());
                                        request.onSuccess().consume(identifications.get(0));
                                    }
                                    else {
                                        String errorMessage = "This person already has an account";
                                        logger.logCredentialsCheckFail(request.identificationHash(),
                                                errorMessage);
                                        request.onError().consume(errorMessage);
                                    }
                                })
                                .onError((error) -> {
                                    logger.logCredentialsCheckFail(request.identificationHash(),
                                            error.getMessage());
                                    request.onError().consume(error.getMessage());
                                })
                                .subscribe(false));

                    }
                    else {
                        String errorMessage = "Invalid credentials";
                        logger.logCredentialsCheckFail(request.identificationHash(), errorMessage);
                        request.onError().consume(errorMessage);
                    }
                })
                .onError((error) -> {
                    logger.logCredentialsCheckFail(request.identificationHash(), error.getMessage());
                    request.onError().consume(error.getMessage());
                })
                .predicate(Predicate.where(IdentificationHashField.ID)
                        .equalTo(request.identificationHash()))
                .subscribe(false));
    }

    public void execute(final RegisterRequest request) {
        mAuth.createUserWithEmailAndPassword(request.email(), request.password())
                .addOnSuccessListener((authResult) -> {
                    FirebaseUser newUser = authResult.getUser();
                    logger.logRegisterSuccess(newUser.getEmail(), newUser.getUid());

                    User registeringUser = new User()
                            .setUserId(newUser.getUid())
                            .setPersonUUID(request.personUUID());

                    execute(new SaveRequest<>()
                            .databaseTable(DatabaseTableContainer.USERS)
                            .entity(registeringUser)
                            .onSuccess(() -> {
                                logger.logRegistrationLinkingSuccess(newUser.getUid(),
                                        request.personUUID());

                                request.onSuccess().consume(newUser);
                            })
                            .onError((exception) -> {
                                if (exception.getMessage() != null) {
                                    logger.logRegistrationLinkingFail(newUser.getUid(),
                                            request.personUUID(),
                                            exception.getMessage());
                                    newUser.delete();
                                    request.onError().consume(exception.getMessage());
                                }
                            }));

                })
                .addOnFailureListener((exception) -> {
                    logger.logRegisterFail(request.email(), exception.getMessage());
                    request.onError().consume(exception.getMessage());
                });
    }

    private <T extends BaseEntity> DatabaseContext<T> buildContextForTable(DatabaseTable<T> table) {
        return new DatabaseContext<>(firebaseConfig.getTableReference(table),
                table.getTableClass());
    }

    private <T extends HashableEntity> DatabaseContextWithIdentification<T> buildContextWithIdentificationForTable(DatabaseTable<T> table) {
        return new DatabaseContextWithIdentification<>(firebaseConfig,
                firebaseConfig.getTableReference(table),
                table.getTableClass());
    }
}