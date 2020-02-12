package com.lonn.studentassistant.activities.implementations.student;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.grades;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.personalActivities;
import static com.lonn.studentassistant.BR.personalCourses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;
import static com.lonn.studentassistant.databinding.CategoryGenerator.gradeCourseCategories;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextClass;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextExam;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidPhoneNumber;

class StudentActivityFirebaseDispatcher extends Dispatcher<StudentActivity, StudentViewModel> {
    private static final Logger LOGGER = Logger.ofClass(StudentActivityFirebaseDispatcher.class);
    private StudentActivityMainLayoutBinding binding;

    private BindableHashMap<CourseViewModel> courseMap;
    private BindableHashMap<OtherActivityViewModel> otherActivityMap;
    private BindableHashMap<ProfessorViewModel> professorsMap;

    private BindableHashMap<RecurringClassViewModel> recurringClassMap;
    private BindableHashMap<OneTimeClassViewModel> oneTimeClassMap;
    private BindableHashMap<CourseViewModel> personalCoursesMap;
    private BindableHashMap<OtherActivityViewModel> personalActivitiesMap;
    private BindableHashMap<GradeViewModel> gradesMap;

    private List<String> oneTimeClassKeys = new ArrayList<>();
    private List<String> recurringClassKeys = new ArrayList<>();

    StudentActivityFirebaseDispatcher(StudentActivity studentActivity) {
        super(studentActivity);
        this.binding = studentActivity.binding;

        courseMap = new BindableHashMap<>(binding, courses);
        otherActivityMap = new BindableHashMap<>(binding, otherActivities);
        professorsMap = new BindableHashMap<>(binding, com.lonn.studentassistant.BR.professors);

        recurringClassMap = new BindableHashMap<>(binding, recurringClasses);
        oneTimeClassMap = new BindableHashMap<>(binding, oneTimeClasses);
        personalCoursesMap = new BindableHashMap<>(binding, personalCourses);
        personalActivitiesMap = new BindableHashMap<>(binding, personalActivities);
        gradesMap = new BindableHashMap<>(binding, grades);
    }

    public void loadAll(String entityKey) {
        if (entityKey != null) {
            loadStudent(entityKey);
        }

        loadCourses();
        loadOtherActivities();
        loadProfessors();
    }

    @Override
    public boolean update(StudentViewModel studentViewModel) {
        if (!isValidEmail(studentViewModel.getEmail())) {
            activity.showSnackBar("Invalid email!", 2000);
            return false;
        }
        if (!isValidPhoneNumber(studentViewModel.getPhoneNumber())) {
            activity.showSnackBar("Invalid phone number!", 2000);
            return false;
        }
        if (!isValidName(studentViewModel.getFirstName() + " " + studentViewModel.getLastName())) {
            activity.showSnackBar("Invalid name!", 2000);
            return false;
        }

        firebaseApi.getStudentService()
                .save(studentViewModel)
                .onSuccess(none -> activity.showSnackBar("Successfully updated your profile!", 1000));

        return true;
    }

    private void computePersonalCourses() {
        if (currentProfile != null) {
            List<String> coursesToRemove = new ArrayList<>();

            for (CourseViewModel personalCourse : personalCoursesMap.values()) {
                if (!currentProfile.getCourses().contains(personalCourse.getKey())) {
                    coursesToRemove.add(personalCourse.getKey());
                }
            }

            for (String personalCourseKey : coursesToRemove) {
                personalCoursesMap.remove(personalCourseKey);
            }
            for (String courseKey : currentProfile.getCourses()) {
                personalCoursesMap.put(courseMap.get(courseKey));
            }

            computeClasses();
        }
    }

    private void computePersonalActivities() {
        if (currentProfile != null) {
            List<String> activitiesToRemove = new ArrayList<>();

            for (OtherActivityViewModel personalActivity : personalActivitiesMap.values()) {
                if (!currentProfile.getOtherActivities().contains(personalActivity.getKey())) {
                    activitiesToRemove.add(personalActivity.getKey());
                }
            }
            for (String personalActivityKey : activitiesToRemove) {
                personalActivitiesMap.remove(personalActivityKey);
            }
            for (String activityKey : currentProfile.getOtherActivities()) {
                personalActivitiesMap.put(otherActivityMap.get(activityKey));
            }

            computeClasses();
        }
    }

    private void computeClasses() {
        StudentViewModel student = binding.getStudent();

        oneTimeClassKeys.clear();
        recurringClassKeys.clear();

        if (student != null) {
            for (CourseViewModel course : personalCoursesMap.values()) {
                computeClassesForDiscipline(course);
            }
            for (OtherActivityViewModel otherActivity : personalActivitiesMap.values()) {
                computeClassesForDiscipline(otherActivity);
            }
        }

        removeNonExistingEntities(recurringClassMap, recurringClassKeys);
        removeNonExistingEntities(oneTimeClassMap, oneTimeClassKeys);

        loadRecurringClasses();
        loadOneTimeClasses();
    }

    private void computeClassesForDiscipline(DisciplineViewModel<?> discipline) {
        oneTimeClassKeys.addAll(discipline.getOneTimeClasses());
        recurringClassKeys.addAll(discipline.getRecurringClasses());
    }

    private void loadCourses() {
        firebaseApi.getCourseService()
                .getAll()
                .onComplete(receivedCourses -> {
                            courseMap = new BindableHashMap<>(binding, courses, receivedCourses);
                            computePersonalCourses();
                        },
                        error -> activity.logAndShowErrorSnack("An error occurred while loading courses.", error, LOGGER));
    }

    private void loadOtherActivities() {
        firebaseApi.getOtherActivityService()
                .getAll()
                .onComplete(receivedOtherActivities -> {
                            otherActivityMap = new BindableHashMap<>(binding, otherActivities, receivedOtherActivities);
                            computePersonalActivities();
                        },
                        error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
    }

    private void loadProfessors() {
        firebaseApi.getProfessorService()
                .getAll()
                .onComplete(receivedProfessors -> professorsMap = new BindableHashMap<>(binding, professors, receivedProfessors),
                        error -> activity.logAndShowErrorSnack("An error occurred while loading professors.", error, LOGGER));
    }

    private void loadRecurringClasses() {
        for (String recurringClassKey : recurringClassKeys) {
            firebaseApi.getRecurringClassService()
                    .getById(recurringClassKey, true)
                    .onSuccess(recurringClass -> {
                        if (recurringClassKeys.contains(recurringClass.getKey()) &&
                                groupMatchesScheduleGroups(binding.getStudent().getGroup(),
                                        recurringClass.getGroups())) {
                            recurringClassMap.put(recurringClass);
                        }
                        else {
                            recurringClassMap.remove(recurringClass);
                        }
                        activity.setNextClass(getNextClass(recurringClassMap.values(),
                                oneTimeClassMap.values()));
                        activity.setNextExam(getNextExam(oneTimeClassMap.values()));
                    })
                    .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading regular classes.", error, LOGGER));
        }
    }

    private void loadOneTimeClasses() {
        for (String oneTimeClassKey : oneTimeClassKeys) {
            firebaseApi.getOneTimeClassService()
                    .getById(oneTimeClassKey, true)
                    .onSuccess(oneTimeClass -> {
                        if (oneTimeClassKeys.contains(oneTimeClass.getKey()) &&
                                groupMatchesScheduleGroups(binding.getStudent().getGroup(),
                                        oneTimeClass.getGroups())) {
                            oneTimeClassMap.put(oneTimeClass);
                        }
                        else {
                            oneTimeClassMap.remove(oneTimeClass);
                        }
                        activity.setNextClass(getNextClass(recurringClassMap.values(),
                                oneTimeClassMap.values()));
                        activity.setNextExam(getNextExam(oneTimeClassMap.values()));
                    })
                    .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading special classes.", error, LOGGER));
        }
    }

    private void loadStudent(String key) {
        firebaseApi.getStudentService()
                .getById(key, true)
                .onSuccess(student -> {
                    currentProfile = student.clone();

                    if (student.getImageMetadataKey() == null ||
                            student.getImageMetadataKey().length() == 0) {
                        binding.setProfileImageContent(null);
                    }
                    else if (binding.getStudent() == null ||
                            !binding.getStudent().getImageMetadataKey()
                                    .equals(student.getImageMetadataKey())) {
                        loadImage(student.getImageMetadataKey());
                    }

                    binding.setStudent(student);
                    computePersonalCourses();
                    computePersonalActivities();
                    loadGrades(student.getGradeKeys());
                })
                .onError(error -> activity.logAndShowErrorSnack("Error loading your personal data", error, LOGGER));
    }

    private void loadImage(String profileImageKey) {
        firebaseApi.getFileMetadataService()
                .getById(profileImageKey, true)
                .onSuccess(metadata -> firebaseApi.getFileContentService()
                        .getById(metadata.getFileContentKey(), true)
                        .onSuccess(binding::setProfileImageContent)
                        .onError(error -> activity.logAndShowErrorSnack(
                                "Unable to load the profile image",
                                error,
                                LOGGER)))
                .onError(error -> activity.logAndShowErrorSnack(
                        "Unable to load the profile image",
                        error,
                        LOGGER));
    }

    private void loadGrades(List<String> gradeKeys) {
        if (gradeKeys.size() != gradesMap.size()) {
            gradesMap.clear();

            firebaseApi.getGradeService()
                    .getByIds(gradeKeys, false)
                    .onSuccess(grades -> {
                        setGradesAndCategoriesCategories(grades);
                    });
        }
    }

    private void setGradesAndCategoriesCategories(List<GradeViewModel> grades) {
        List<String> gradeCourseKeys = new ArrayList<>();

        for (GradeViewModel grade : grades) {
            if (!gradeCourseKeys.contains(grade.getCourseKey())) {
                gradeCourseKeys.add(grade.getCourseKey());
            }
        }

        firebaseApi.getCourseService()
                .getByIds(gradeCourseKeys, false)
                .onSuccess(courses -> {
                    ScrollViewCategory gradesParentCategory = activity.findViewById(R.id.gradesCategory);

                    gradesParentCategory.setChildCategories(gradeCourseCategories(
                            gradesParentCategory.getViewModel(),
                            courses));

                    for (GradeViewModel grade : grades) {
                        gradesMap.put(grade);
                    }
                });
    }

    private boolean groupMatchesScheduleGroups(String group, List<String> scheduleGroups) {
        if (scheduleGroups != null) {
            for (String scheduleGroup : scheduleGroups) {
                if (group.toLowerCase().startsWith(scheduleGroup.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
