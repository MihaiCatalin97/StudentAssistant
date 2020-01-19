package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.StudentAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
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
        adapter = new StudentAdapter();
        fileMetadataService = FileMetadataService.getInstance(firebaseConnection);
    }

    public static StudentService getInstance(FirebaseConnection firebaseConnection) {
        if (instance == null) {
            instance = new StudentService(firebaseConnection);
        }

        return instance;
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

                    student.getGrades().add(grade.getKey());
                    grade.setStudentKey(student.getKey());

                    save(student)
                            .onSuccess(result::complete)
                            .onError(result::completeExceptionally);
                })
                .onError(result::completeExceptionally);

        return result;
    }

    protected Future<StudentViewModel, Exception> getByStudentId(String studentId, boolean subscribe) {
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
}
