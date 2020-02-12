package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.CourseAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.DisciplineService;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.COURSES;

public class CourseService extends DisciplineService<Course, CourseViewModel> {
    private static CourseService instance;
    private LaboratoryService laboratoryService;

    private CourseService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
    }

    public static CourseService getInstance(FirebaseConnection firebaseConnection) {
        if (instance == null) {
            instance = new CourseService(firebaseConnection);
            instance.init();
        }

        return instance;
    }

    @Override
    public Future<Void, Exception> deleteById(String id) {
        Future<Void, Exception> result = new Future<>();

        getById(id, false)
                .onSuccess(discipline -> {
                    for (String laboratoryKey : discipline.getLaboratories()) {
                        laboratoryService.deleteById(laboratoryKey);
                    }

                    super.deleteById(id)
                            .onSuccess(result::complete)
                            .onError(result::completeExceptionally);
                })
                .onError(result::completeExceptionally);

        return result;
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

    public Future<Void, Exception> addGradeToCourse(String gradeKey, String courseKey) {
        Future<Void, Exception> result = new Future<>();

        getById(courseKey, false)
                .onSuccess(course -> {
                    if (course == null) {
                        result.completeExceptionally(new Exception("Laboratory not found"));
                        return;
                    }

                    if (!course.getGrades().contains(gradeKey)) {
                        course.getGrades().add(gradeKey);

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

    protected void init() {
        super.init();
        adapter = new CourseAdapter();
        laboratoryService = LaboratoryService.getInstance(firebaseConnection);
    }

    @Override
    protected DatabaseTable<Course> getDatabaseTable() {
        return COURSES;
    }

    protected void linkToStudent(String studentKey, String disciplineKey) {
        studentService.addCourse(studentKey, disciplineKey);
    }

    protected void linkToProfessor(String professorKey, String disciplineKey) {
        professorService.addCourse(professorKey, disciplineKey);
    }

    protected void unlinkToStudent(String studentKey, String disciplineKey) {
        studentService.removeCourse(studentKey, disciplineKey);
    }

    protected void unlinkToStudent(StudentViewModel student, String disciplineKey) {
        if (student.getCourses().contains(disciplineKey)) {
            student.getCourses().remove(disciplineKey);

            studentService.save(student);
        }
    }

    protected void unlinkToProfessor(ProfessorViewModel professor, String disciplineKey) {
        if (professor.getCourses().contains(disciplineKey)) {
            professor.getCourses().remove(disciplineKey);

            professorService.save(professor);
        }
    }

    protected void unlinkToProfessor(String professorKey, String disciplineKey) {
        professorService.removeCourse(professorKey, disciplineKey);
    }

    protected PermissionLevel getPermissionLevel(Course course) {
        return authenticationService.getPermissionLevel(course);
    }
}
