package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.StudentAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.services.abstractions.PersonService;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.STUDENTS;
import static java.util.UUID.randomUUID;

public class StudentService extends PersonService<Student, StudentViewModel> {
	private static StudentService instance;
	protected FileMetadataService fileMetadataService;

	private StudentService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static StudentService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new StudentService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new StudentAdapter();
		fileMetadataService = FileMetadataService.getInstance(firebaseConnection);
	}

	public Future<Void, Exception> addGradeToStudent(GradeViewModel grade) {
		Future<Void, Exception> result = new Future<>();

		getByStudentId(grade.getStudentId(), false)
				.onSuccess(student -> {
					if (student == null) {
						student = StudentViewModel.builder()
								.studentId(grade.getStudentId())
								.gradeKeys(new ArrayList<>())
								.build();

						student.setKey(randomUUID().toString());
					}

					student.getGradeKeys().add(grade.getKey());
					grade.setStudentKey(student.getKey());

					save(student)
							.onSuccess(result::complete)
							.onError(result::completeExceptionally);
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addCourse(String studentKey, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(studentKey, false)
				.onSuccess(student -> {
					if (student == null) {
						result.completeExceptionally(new Exception("No student found"));
					}
					else {
						if (!student.getCourses().contains(courseKey)) {
							student.getCourses().add(courseKey);
							save(student)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addActivity(String studentKey, String activityKey) {
		Future<Void, Exception> result = new Future<>();

		getById(studentKey, false)
				.onSuccess(student -> {
					if (student == null) {
						result.completeExceptionally(new Exception("No student found"));
					}
					else {
						if (!student.getOtherActivities().contains(activityKey)) {
							student.getOtherActivities().add(activityKey);
							save(student)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeCourse(String studentKey, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(studentKey, false)
				.onSuccess(student -> {
					if (student == null) {
						result.completeExceptionally(new Exception("No student found"));
					}
					else {
						if (student.getCourses().contains(courseKey)) {
							student.getCourses().remove(courseKey);
							save(student)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeActivity(String studentKey, String activityKey) {
		Future<Void, Exception> result = new Future<>();

		getById(studentKey, false)
				.onSuccess(student -> {
					if (student == null) {
						result.completeExceptionally(new Exception("No student found"));
					}
					else {
						if (student.getOtherActivities().contains(activityKey)) {
							student.getOtherActivities().remove(activityKey);
							save(student)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<StudentViewModel, Exception> getByStudentId(String studentId, boolean subscribe) {
		Future<StudentViewModel, Exception> result = new Future<>();

		firebaseConnection.execute(new GetRequest<Student, Exception>()
				.databaseTable(STUDENTS)
				.subscribe(subscribe)
				.onSuccess(students -> {
					for (Student student : students) {
						if (student.getStudentId().equals(studentId)) {
							result.complete(adapter.adapt(student));
							return;
						}
					}
					result.complete(null);
				})
				.onError(result::completeExceptionally));

		return result;
	}

	@Override
	protected DatabaseTable<Student> getDatabaseTable() {
		return STUDENTS;
	}

	protected PermissionLevel getPermissionLevel(Student student) {
		return authenticationService.getPermissionLevel(student);
	}
}
