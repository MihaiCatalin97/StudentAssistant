package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.StudentAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.STUDENTS;

public class StudentService extends Service<Student, Exception, StudentViewModel> {
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
		adapter = new StudentAdapter();
		fileMetadataService = FileMetadataService.getInstance(firebaseConnection);
	}

	public Future<Void, Exception> addOrReplaceImage(String studentId,
													 FileMetadataViewModel fileMetadata,
													 FileContentViewModel fileContent) {
		Future<Void, Exception> result = new Future<>();

		fileMetadataService.saveMetadataAndContent(fileMetadata, fileContent)
				.onSuccess(none -> getById(studentId, false)
						.onSuccess(student -> {
							String previousImage = student.getImageMetadataKey();
							student.setImageMetadataKey(fileMetadata.getKey());

							save(student)
									.onSuccess(none2 -> {
										result.complete(none2);

										if (previousImage != null) {
											fileMetadataService.deleteMetadataAndContent(previousImage);
										}
									})
									.onError(result::completeExceptionally);
						})
						.onError(result::completeExceptionally))
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addGradeToStudent(GradeViewModel grade) {
		Future<Void, Exception> result = new Future<>();

		getByStudentId(grade.getStudentId(), false)
				.onSuccess(student -> {
					if (student == null) {
						student = StudentViewModel.builder()
								.studentId(grade.getStudentId())
								.build();
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

	public Future<String, Exception> getKeyOfStudentId(String studentId) {
		Future<String, Exception> result = new Future<>();

		firebaseConnection.execute(new GetRequest<Student, Exception>()
				.databaseTable(STUDENTS)
				.subscribe(false)
				.onSuccess(students -> {
					for (Student student : students) {
						if (student.getStudentId().equals(studentId)) {
							result.complete(student.getKey());
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
}
