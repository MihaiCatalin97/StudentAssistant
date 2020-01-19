package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.CourseAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;

public class CourseService extends FileAssociatedEntityService<Course, Exception, CourseViewModel> {
    private static CourseService instance;

    private CourseService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
        adapter = new CourseAdapter();
    }

    public static CourseService getInstance(FirebaseConnection firebaseConnection) {
        if (instance == null) {
            instance = new CourseService(firebaseConnection);
        }

        return instance;
    }

    public Future<Void, Exception> linkLaboratoryToCourse(String courseKey, String laboratoryKey) {
        Future<Void, Exception> result = new Future<>();

        getById(courseKey, false)
                .onSuccess(course -> {
                    if (!course.getLaboratories().contains(laboratoryKey)) {
                        course.getLaboratories().add(laboratoryKey);

                        save(course)
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

    public Future<Void, Exception> addStudent(String studentId, String courseKey) {
        Future<Void, Exception> result = new Future<>();

        getById(courseKey, false)
                .onSuccess(course -> {
                    if (!course.getStudents().contains(studentId)) {
                        course.getStudents().add(studentId);

                        save(course)
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
    protected DatabaseTable<Course> getDatabaseTable() {
        return COURSES;
    }
}
