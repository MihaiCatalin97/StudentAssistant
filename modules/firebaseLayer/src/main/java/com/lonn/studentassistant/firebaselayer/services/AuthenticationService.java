package com.lonn.studentassistant.firebaselayer.services;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.adapters.AdministratorAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.CourseAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.FileContentAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.FileMetadataAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.GradeAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.LaboratoryAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.OtherActivityAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.ProfessorAdapter;
import com.lonn.studentassistant.firebaselayer.adapters.StudentAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.RequestLogger;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.ADMINISTRATOR;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.PROFESSOR;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.STUDENT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.NONE;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.READ_FULL;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.READ_PARTIAL;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.READ_PUBLIC;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE_ADD_AGGREGATED;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE_ENROLL;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE_PARTIAL;

public class AuthenticationService {
	private static final RequestLogger LOGGER = new RequestLogger();
	private static AuthenticationService instance;
	private FirebaseAuth mAuth;
	private FirebaseConnection firebaseConnection;
	@Getter
	private EntityViewModel loggedPerson;
	@Getter
	@Setter
	private AccountType accountType;
	private UserService userService;
	private ProfessorService professorService;
	private AdministratorService administratorService;
	private StudentService studentService;

	private CourseAdapter courseAdapter = new CourseAdapter();
	private OtherActivityAdapter otherActivityAdapter = new OtherActivityAdapter();
	private LaboratoryAdapter laboratoryAdapter = new LaboratoryAdapter();
	private FileMetadataAdapter fileMetadataAdapter = new FileMetadataAdapter();
	private FileContentAdapter fileContentAdapter = new FileContentAdapter();
	private AdministratorAdapter administratorAdapter = new AdministratorAdapter();
	private ProfessorAdapter professorAdapter = new ProfessorAdapter();
	private StudentAdapter studentAdapter = new StudentAdapter();
	private GradeAdapter gradeAdapter = new GradeAdapter();

	private Consumer<EntityViewModel> onLoggedPersonChange;
	@Getter
	private String loggedPersonUUID = "";

	public void setOnLoggedPersonChange(Consumer<EntityViewModel> onLoggedPersonChange) {
		this.onLoggedPersonChange = onLoggedPersonChange;
	}

	private AuthenticationService(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
		mAuth = FirebaseAuth.getInstance();
	}

	public static AuthenticationService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new AuthenticationService(firebaseConnection);
			instance.init(firebaseConnection);
		}

		return instance;
	}

	private void init(FirebaseConnection firebaseConnection) {
		userService = UserService.getInstance(firebaseConnection);

		professorService = ProfessorService.getInstance(firebaseConnection);
		administratorService = AdministratorService.getInstance(firebaseConnection);
		studentService = StudentService.getInstance(firebaseConnection);
	}

	public Future<Void, Exception> deleteFirebaseAccount() {
		Future<Void, Exception> result = new Future<>();
		FirebaseUser currentUser = mAuth.getCurrentUser();

		if(currentUser != null) {
			currentUser.delete()
					.onSuccessTask(task -> {
						result.complete(null);
						return null;
					});
		}

		return result;
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
							if (newUser != null) {
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
							}
							else {
								onError.consume(new Exception("An error occurred!"));
							}
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

							setCurrentLoggedPersons(authResult.getUser())
									.onSuccess(onSuccess)
									.onError(error -> {
										if (onError != null) {
											onError.consume(error);
										}
									});
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

	public void logout() {
		FirebaseAuth.getInstance().signOut();
		loggedPerson = null;
		accountType = null;
	}

	private Future<Void, Exception> setCurrentLoggedPersons(FirebaseUser firebaseUser) {
		Future<Void, Exception> result = new Future<>();

		if (firebaseUser != null) {
			userService.getById(firebaseUser.getUid(), true)
					.onSuccess(user -> {
						if (user != null) {
							this.accountType = user.getAccountType();
							this.loggedPersonUUID = user.getPersonUUID();

							switch (user.getAccountType()) {
								case STUDENT: {
									studentService.getById(loggedPersonUUID, true)
											.onSuccess(student -> {
												if (this.getLoggedPerson() == null) {
													this.setLoggedPerson(student);
													result.complete(null);
												}
												else {
													this.setLoggedPerson(student);
												}
											})
											.onError(result::completeExceptionally);
									break;
								}
								case PROFESSOR: {
									professorService.getById(loggedPersonUUID, true)
											.onSuccess(professor -> {
												if (this.getLoggedPerson() == null) {
													this.setLoggedPerson(professor);
													result.complete(null);
												}
												else {
													this.setLoggedPerson(professor);
												}
											})
											.onError(result::completeExceptionally);
									break;
								}
								case ADMINISTRATOR: {
									administratorService.getById(loggedPersonUUID, true)
											.onSuccess(administrator -> {
												if (this.getLoggedPerson() == null) {
													this.setLoggedPerson(administrator);
													result.complete(null);
												}
												else {
													this.setLoggedPerson(administrator);
												}
											})
											.onError(result::completeExceptionally);
									break;
								}
							}
						}
					});
		}
		else {
			result.completeExceptionally(new Exception("No firebase user received"));
		}

		return result;
	}

	private void setLoggedPerson(EntityViewModel loggedPerson) {
		this.loggedPerson = loggedPerson;

		if (onLoggedPersonChange != null) {
			onLoggedPersonChange.consume(loggedPerson);
		}
	}

	public PermissionLevel getPermissionLevel(Course course) {
		if (accountType == null || loggedPerson == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			if (course.getStudents().contains(loggedPersonUUID)) {
				return READ_PARTIAL;
			}
			return WRITE_ENROLL;
		}
		if (accountType.equals(PROFESSOR)) {
			if (((ProfessorViewModel) loggedPerson).getCourses().contains(course.getKey())) {
				return WRITE;
			}
			return READ_PUBLIC;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(OtherActivity activity) {
		if (accountType == null || loggedPerson == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			if (activity.getStudents().contains(loggedPersonUUID)) {
				return READ_PARTIAL;
			}
			return WRITE_ENROLL;
		}
		if (accountType.equals(PROFESSOR)) {
			if (((ProfessorViewModel) loggedPerson).getOtherActivities().contains(activity.getKey())) {
				return WRITE;
			}
			return READ_FULL;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(Laboratory laboratory) {
		if (accountType == null || loggedPerson == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			if (((StudentViewModel) loggedPerson).getCourses().contains(laboratory.getCourse())) {
				return READ_FULL;
			}
			return NONE;
		}
		if (accountType.equals(PROFESSOR)) {
			if (((ProfessorViewModel) loggedPerson).getCourses().contains(laboratory.getCourse())) {
				return WRITE;
			}
			return NONE;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(FileMetadata file) {
		if (accountType == null || loggedPerson == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			if (file.getAssociatedEntityKey().equals(loggedPersonUUID)) {
				return WRITE;
			}
			if (((StudentViewModel) loggedPerson).getCourses().contains(file.getAssociatedEntityKey()) ||
					((StudentViewModel) loggedPerson).getOtherActivities().contains(file.getAssociatedEntityKey())) {
				return READ_FULL;
			}
			return READ_PUBLIC;
		}
		if (accountType.equals(PROFESSOR)) {
			if (((ProfessorViewModel) loggedPerson).getCourses().contains(file.getAssociatedEntityKey()) ||
					((ProfessorViewModel) loggedPerson).getOtherActivities().contains(file.getAssociatedEntityKey()) ||
					file.getAssociatedEntityKey().equals(loggedPersonUUID)) {
				return WRITE;
			}

			return WRITE;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(FileContent file) {
		if (accountType == null || loggedPerson == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			if (file.getAssociatedEntityKey().equals(loggedPersonUUID)) {
				return WRITE;
			}
			if (((StudentViewModel) loggedPerson).getCourses().contains(file.getAssociatedEntityKey()) ||
					((StudentViewModel) loggedPerson).getOtherActivities().contains(file.getAssociatedEntityKey())) {
				return READ_FULL;
			}

			return READ_PUBLIC;
		}
		if (accountType.equals(PROFESSOR)) {
			if (((ProfessorViewModel) loggedPerson).getCourses().contains(file.getAssociatedEntityKey()) ||
					((ProfessorViewModel) loggedPerson).getOtherActivities().contains(file.getAssociatedEntityKey()) ||
					file.getAssociatedEntityKey().equals(loggedPersonUUID)) {
				return WRITE;
			}
			//Todo: Investigate how to change this if it is a double join
			return WRITE;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(Administrator administrator) {
		if (accountType == null || mAuth.getCurrentUser() == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			return NONE;
		}
		if (accountType.equals(PROFESSOR)) {
			return NONE;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(Professor professor) {
		if (accountType == null || mAuth.getCurrentUser() == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			return READ_PUBLIC;
		}
		if (accountType.equals(PROFESSOR)) {
			if (loggedPersonUUID.equals(professor.getKey())) {
				return WRITE_PARTIAL;
			}
			if (loggedPerson != null && listsHaveAtLeastOneElementInCommon(professor.getCourses(),
					((ProfessorViewModel) loggedPerson).getCourses()) ||
					listsHaveAtLeastOneElementInCommon(professor.getOtherActivities(),
							((ProfessorViewModel) loggedPerson).getOtherActivities())) {
				return WRITE_ADD_AGGREGATED;
			}
			if (loggedPerson == null) {
				return WRITE_PARTIAL;
			}
			return WRITE_ADD_AGGREGATED;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(Student student) {
		if (accountType == null || mAuth.getCurrentUser() == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			if (loggedPersonUUID.equals(student.getKey())) {
				return WRITE_PARTIAL;
			}
			if (loggedPerson != null &&
					(listsHaveAtLeastOneElementInCommon(((StudentViewModel) loggedPerson).getCourses(),
							student.getCourses()) ||
							(listsHaveAtLeastOneElementInCommon(((StudentViewModel) loggedPerson).getOtherActivities(),
									student.getOtherActivities())))) {
				return READ_PUBLIC;
			}
			if (loggedPerson == null) {
				return WRITE_PARTIAL;
			}
			return READ_PUBLIC;

		}
		if (accountType.equals(PROFESSOR)) {
			return WRITE;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(Grade grade) {
		if (accountType == null || mAuth.getCurrentUser() == null) {
			return NONE;
		}
		if (accountType.equals(STUDENT)) {
			if (loggedPerson != null && loggedPersonUUID.equals(grade.getStudentKey())) {
				return READ_PUBLIC;
			}
			return NONE;
		}
		if (accountType.equals(PROFESSOR)) {
			if (loggedPerson != null && ((ProfessorViewModel) loggedPerson).getCourses().contains(grade.getCourseKey())) {
				return WRITE;
			}
			return NONE;
		}
		if (accountType.equals(ADMINISTRATOR)) {
			return WRITE;
		}

		return NONE;
	}

	public PermissionLevel getPermissionLevel(CourseViewModel course) {
		return getPermissionLevel(courseAdapter.adapt(course));
	}

	public PermissionLevel getPermissionLevel(OtherActivityViewModel activity) {
		return getPermissionLevel(otherActivityAdapter.adapt(activity));
	}

	public PermissionLevel getPermissionLevel(LaboratoryViewModel laboratory) {
		return getPermissionLevel(laboratoryAdapter.adapt(laboratory));
	}

	public PermissionLevel getPermissionLevel(FileMetadataViewModel file) {
		return getPermissionLevel(fileMetadataAdapter.adapt(file));
	}

	public PermissionLevel getPermissionLevel(FileContentViewModel file) {
		return getPermissionLevel(fileContentAdapter.adapt(file));
	}

	public PermissionLevel getPermissionLevel(AdministratorViewModel administrator) {
		return getPermissionLevel(administratorAdapter.adapt(administrator));
	}

	public PermissionLevel getPermissionLevel(ProfessorViewModel professor) {
		return getPermissionLevel(professorAdapter.adapt(professor));
	}

	public PermissionLevel getPermissionLevel(StudentViewModel student) {
		return getPermissionLevel(studentAdapter.adapt(student));
	}

	public PermissionLevel getPermissionLevel(GradeViewModel grade) {
		return getPermissionLevel(gradeAdapter.adapt(grade));
	}

	private boolean listsHaveAtLeastOneElementInCommon(List<String> list1, List<String> list2) {
		List<String> auxList = new LinkedList<>(list1);

		auxList.removeAll(list2);

		return auxList.size() != list1.size();
	}
}
