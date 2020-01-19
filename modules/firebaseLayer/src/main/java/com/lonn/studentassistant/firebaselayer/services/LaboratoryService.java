package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.LaboratoryAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.LABORATORIES;

public class LaboratoryService extends FileAssociatedEntityService<Laboratory, Exception, LaboratoryViewModel> {
    private static LaboratoryService instance;
    private StudentService studentService;
    private CourseService courseService;

    private LaboratoryService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
        adapter = new LaboratoryAdapter();
        studentService = StudentService.getInstance(firebaseConnection);
        courseService = CourseService.getInstance(firebaseConnection);
    }

    public static LaboratoryService getInstance(FirebaseConnection firebaseConnection) {
        if (instance == null) {
            instance = new LaboratoryService(firebaseConnection);
        }

        return instance;
    }

    public Future<Void, Exception> saveAndLinkLaboratory(LaboratoryViewModel laboratory) {
        Future<Void, Exception> result = new Future<>();

        save(laboratory).onSuccess(none ->
                courseService
                        .linkLaboratoryToCourse(laboratory.getCourseKey(), laboratory.getKey())
                        .onSuccess(result::complete)
                        .onError(error -> {
                            deleteById(laboratory.getKey());
                            result.completeExceptionally(error);
                        }))
                .onError(result::completeExceptionally);

        return result;
    }

    public Future<Void, Exception> addGradeToLaboratory(String gradeKey, String laboratoryKey) {
        Future<Void, Exception> result = new Future<>();

        getById(laboratoryKey, false)
                .onSuccess(laboratory -> {
                    if (laboratory == null) {
                        result.completeExceptionally(new Exception("Laboratory not found"));
                        return;
                    }

                    if (!laboratory.getGradeKeys().contains(gradeKey)) {
                        laboratory.getGradeKeys().add(gradeKey);

                        save(laboratory)
                                .onSuccess(result::complete)
                                .onError(result::completeExceptionally);
                    }
                    else {
                        result.complete(null);
                    }
                })
                .onError(result::completeExceptionally);

        return result;
    }

    @Override
    protected DatabaseTable<Laboratory> getDatabaseTable() {
        return LABORATORIES;
    }
}