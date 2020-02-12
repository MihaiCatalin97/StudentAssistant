package com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions;

import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.OneTimeClassService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.ProfessorService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.RecurringClassService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.StudentService;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;

import java.util.ArrayList;
import java.util.List;

public abstract class DisciplineService<T extends Discipline, V extends DisciplineViewModel<T>> extends FileAssociatedEntityService<T, Exception, V> {
    protected StudentService studentService;
    protected ProfessorService professorService;
    protected RecurringClassService recurringClassService;
    protected OneTimeClassService oneTimeClassService;

    protected DisciplineService(FirebaseConnection firebaseConnection) {
        super(firebaseConnection);
    }

    @Override
    public Future<Void, Exception> deleteById(String id) {
        Future<Void, Exception> result = new Future<>();

        getById(id, false)
                .onSuccess(discipline -> {
                    super.deleteById(id)
                            .onSuccess(none -> {
                                for (String student : discipline.getStudents()) {
                                    unlinkToStudent(student, id);
                                }
                                for (String professor : discipline.getProfessors()) {
                                    unlinkToProfessor(professor, id);
                                }

                                for (String recurringClassKey : discipline.getRecurringClasses()) {
                                    recurringClassService.deleteById(recurringClassKey);
                                }
                                for (String oneTimeClassKey : discipline.getOneTimeClasses()) {
                                    oneTimeClassService.deleteById(oneTimeClassKey);
                                }

                                result.complete(null);
                            })
                            .onError(result::completeExceptionally);
                })
                .onError(result::completeExceptionally);

        return result;
    }

    public Future<Void, Exception> addProfessor(String professorKey, String courseKey) {
        Future<Void, Exception> result = new Future<>();

        getById(courseKey, false)
                .onSuccess(course -> {
                    if (!course.getProfessors().contains(professorKey)) {
                        course.getProfessors().add(professorKey);

                        save(course)
                                .onSuccess(result::complete)
                                .onError(result::completeExceptionally);

                        professorService
                                .addCourse(professorKey, courseKey);
                    }
                    else {
                        result.complete(null);
                    }
                })
                .onError(result::completeExceptionally);

        return result;
    }

    public Future<Void, Exception> addStudent(String studentKey, String disciplineKey) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (!discipline.getStudents().contains(studentKey)) {
                        discipline.getStudents().add(studentKey);

                        save(discipline)
                                .onSuccess(result::complete)
                                .onError(result::completeExceptionally);

                        linkToStudent(studentKey, disciplineKey);
                    }
                    else {
                        result.complete(null);
                    }
                })
                .onError(result::completeExceptionally);

        return result;
    }

    public Future<Void, Exception> addStudents(List<StudentViewModel> students, String disciplineKey) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (students != null) {

                        for (StudentViewModel student : students) {
                            if (!discipline.getStudents().contains(student.getKey())) {
                                discipline.getStudents().add(student.getKey());
                                discipline.getPendingStudents().remove(student.getKey());

                                linkToStudent(student.getKey(), disciplineKey);
                            }
                        }

                        save(discipline)
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

    public Future<Void, Exception> addRecurringClass(String disciplineKey, String recurringClassKey) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (discipline == null) {
                        result.completeExceptionally(new Exception("No discipline found"));
                    }
                    else {
                        if (!discipline.getRecurringClasses().contains(recurringClassKey)) {
                            discipline.getRecurringClasses().add(recurringClassKey);
                            save(discipline)
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

    public Future<Void, Exception> removeRecurringClass(String disciplineKey, String recurringClassKey) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (discipline == null) {
                        result.completeExceptionally(new Exception("No discipline found"));
                    }
                    else {
                        if (discipline.getRecurringClasses().contains(recurringClassKey)) {
                            discipline.getRecurringClasses().remove(recurringClassKey);
                            save(discipline)
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

    public Future<Void, Exception> addOneTimeClass(String disciplineKey, String oneTimeClassKey) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (discipline == null) {
                        result.completeExceptionally(new Exception("No discipline found"));
                    }
                    else {
                        if (!discipline.getOneTimeClasses().contains(oneTimeClassKey)) {
                            discipline.getOneTimeClasses().add(oneTimeClassKey);
                            save(discipline)
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

    public Future<Void, Exception> removeOneTimeClass(String disciplineKey, String oneTimeClassKey) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (discipline == null) {
                        result.completeExceptionally(new Exception("No discipline found"));
                    }
                    else {
                        if (discipline.getOneTimeClasses().contains(oneTimeClassKey)) {
                            discipline.getOneTimeClasses().remove(oneTimeClassKey);
                            save(discipline)
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

    public Future<Void, Exception> addProfessors(List<ProfessorViewModel> professors, String disciplineKey) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (professors != null) {

                        for (ProfessorViewModel professor : professors) {
                            if (!discipline.getProfessors().contains(professor.getKey())) {
                                discipline.getProfessors().add(professor.getKey());

                                linkToProfessor(professor.getKey(), disciplineKey);
                            }
                        }

                        save(discipline)
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

    public Future<Void, Exception> removeStudent(V discipline, StudentViewModel student) {
        Future<Void, Exception> result = new Future<>();

        discipline.getStudents().remove(student.getKey());

        save(discipline).onError(result::completeExceptionally)
                .onSuccess(none -> {
                    unlinkToStudent(student, discipline.getKey());
                    result.complete(none);
                });

        unlinkToStudent(student, discipline.getKey());

        return result;
    }

    public Future<Void, Exception> removeAllStudents(V discipline) {
        Future<Void, Exception> result = new Future<>();

        List<String> students = new ArrayList<>(discipline.getStudents());
        discipline.getStudents().clear();

        save(discipline).onError(result::completeExceptionally)
                .onSuccess(none -> {
                    for (String student : students) {
                        unlinkToStudent(student, discipline.getKey());
                    }
                    result.complete(none);
                });

        return result;
    }

    public Future<Void, Exception> removeAllProfessors(V discipline) {
        Future<Void, Exception> result = new Future<>();

        List<String> professors = new ArrayList<>(discipline.getProfessors());
        discipline.getProfessors().clear();

        save(discipline).onError(result::completeExceptionally)
                .onSuccess(none -> {
                    for (String professor : professors) {
                        unlinkToStudent(professor, discipline.getKey());
                    }
                    result.complete(none);
                });

        return result;
    }

    public Future<Void, Exception> removeProfessor(V discipline, ProfessorViewModel professor) {
        Future<Void, Exception> result = new Future<>();

        discipline.getProfessors().remove(professor.getKey());

        save(discipline).onError(result::completeExceptionally)
                .onSuccess(none -> {
                    unlinkToProfessor(professor, discipline.getKey());
                    result.complete(none);
                });

        unlinkToProfessor(professor, discipline.getKey());

        return result;
    }

    public Future<Void, Exception> removeProfessor(String disciplineKey, ProfessorViewModel professor) {
        Future<Void, Exception> result = new Future<>();

        getById(disciplineKey, false)
                .onSuccess(discipline -> {
                    if (discipline.getProfessors().contains(professor.getKey())) {
                        discipline.getProfessors().remove(professor.getKey());

                        save(discipline).onError(result::completeExceptionally)
                                .onSuccess(none -> {
                                    unlinkToProfessor(professor, discipline.getKey());
                                    result.complete(none);
                                });

                        unlinkToProfessor(professor, discipline.getKey());
                    }
                })
                .onError(result::completeExceptionally);

        return result;
    }

    public Future<Void, Exception> addEnrollmentRequest(V discipline, String studentKey) {
        Future<Void, Exception> result = new Future<>();

        if (!discipline.getPendingStudents().contains(studentKey) &&
                !discipline.getStudents().contains(studentKey)) {
            discipline.getPendingStudents().add(studentKey);

            save(discipline)
                    .onSuccess(none -> {
                        unlinkToStudent(studentKey, discipline.getKey());
                        result.complete(none);
                    })
                    .onError(result::completeExceptionally);
        }
        else {
            if (discipline.getPendingStudents().contains(studentKey)) {
                result.completeExceptionally(new Exception("You have already send an enrollment request"));
            }
            else {
                result.completeExceptionally(new Exception("You are already enrolled"));
            }
        }

        return result;
    }

    public Future<Void, Exception> approveEnrollmentRequest(V discipline, String studentKey) {
        Future<Void, Exception> result = new Future<>();

        if (discipline.getPendingStudents().contains(studentKey) &&
                !discipline.getStudents().contains(studentKey)) {

            discipline.getPendingStudents().remove(studentKey);
            discipline.getStudents().add(studentKey);

            save(discipline)
                    .onSuccess(none -> {
                        linkToStudent(studentKey, discipline.getKey());
                        result.complete(none);
                    })
                    .onError(result::completeExceptionally);
        }
        else {
            if (!discipline.getPendingStudents().contains(studentKey)) {
                result.completeExceptionally(new Exception("Could not find the enrollment request"));
            }
            else {
                result.completeExceptionally(new Exception("This student is already enrolled"));
            }
        }

        return result;
    }

    public Future<Void, Exception> deleteEnrollmentRequest(V discipline, String studentKey) {
        Future<Void, Exception> result = new Future<>();

        if (discipline.getPendingStudents().contains(studentKey)) {
            discipline.getPendingStudents().remove(studentKey);

            save(discipline).onError(result::completeExceptionally)
                    .onSuccess(result::complete);
        }
        else {
            result.completeExceptionally(new Exception("Could not find the enrollment request"));
        }

        return result;
    }

    protected void init() {
        super.init();
        studentService = StudentService.getInstance(firebaseConnection);
        professorService = ProfessorService.getInstance(firebaseConnection);
        recurringClassService = RecurringClassService.getInstance(firebaseConnection);
        oneTimeClassService = OneTimeClassService.getInstance(firebaseConnection);
    }

    protected abstract void linkToStudent(String studentKey, String disciplineKey);

    protected abstract void linkToProfessor(String professorKey, String disciplineKey);

    protected abstract void unlinkToStudent(StudentViewModel student, String disciplineKey);

    protected abstract void unlinkToStudent(String studentKey, String disciplineKey);

    protected abstract void unlinkToProfessor(ProfessorViewModel professor, String disciplineKey);

    protected abstract void unlinkToProfessor(String professorKey, String disciplineKey);
}
