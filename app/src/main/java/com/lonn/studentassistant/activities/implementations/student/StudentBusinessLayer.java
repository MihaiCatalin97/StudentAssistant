package com.lonn.studentassistant.activities.implementations.student;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.lonn.studentassistant.activities.abstractions.IBusinessLayer;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.CourseCallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.OtherActivityCallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.ProfessorsCallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.ScheduleClassCallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.StudentCallback;
import com.lonn.studentassistant.common.ConnectionBundle;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.common.requests.EditRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.services.implementations.otherActivityService.OtherActivityService;
import com.lonn.studentassistant.services.implementations.professorService.ProfessorService;
import com.lonn.studentassistant.services.implementations.scheduleService.ScheduleClassService;
import com.lonn.studentassistant.services.implementations.studentService.StudentService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StudentBusinessLayer implements IBusinessLayer<Student>
{
    private ConnectionBundle serviceConnections;
    private StudentActivity activity;
    private Student activityEntity;

    private CourseCallback courseCallback;
    private ProfessorsCallback professorsCallback;
    private OtherActivityCallback otherActivityCallback;
    private ScheduleClassCallback scheduleClassCallback;
    private StudentCallback studentCallback;

    public StudentBusinessLayer(StudentActivity activity)
    {
        this.activity = activity;
        serviceConnections = new ConnectionBundle(activity.getBaseContext());

        init();
    }

    private void init()
    {
        professorsCallback = new ProfessorsCallback(activity);
        courseCallback = new CourseCallback(activity);
        otherActivityCallback = new OtherActivityCallback(activity);

        scheduleClassCallback = new ScheduleClassCallback(activity, this);
        studentCallback = new StudentCallback(activity, this);

    }

    public void addEntityToList(BaseEntity entity)
    {
        Student newStudent = activityEntity.clone();

        if (entity instanceof Course)
        {
            if (!activityEntity.optionalCourses.contains(entity.getKey()))
                newStudent.optionalCourses.add(entity.getKey());
        }
        else if (entity instanceof OtherActivity)
        {
            if(!activityEntity.otherActivities.contains(entity.getKey()))
                newStudent.otherActivities.add(entity.getKey());
        }

        serviceConnections.postRequest(StudentService.class, new EditRequest<>(Collections.singletonList(newStudent)), studentCallback);
    }

    public void removeEntityFromList(BaseEntity entity)
    {
        Student newStudent = activityEntity.clone();

        if(entity instanceof Course)
            newStudent.optionalCourses.remove(entity.getKey());
        else if (entity instanceof OtherActivity)
            newStudent.otherActivities.remove(entity.getKey());

        serviceConnections.postRequest(StudentService.class, new EditRequest<>(Collections.singletonList(newStudent)), studentCallback);
    }

    public void editActivityEntity(Student newStudent)
    {
        List<String> oldCourses = new LinkedList<>();
        List<String> oldActivities = new LinkedList<>();

        if(activityEntity == null || !activityEntity.firstName.equals(newStudent.firstName) || !activityEntity.lastName.equals(newStudent.lastName))
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newStudent.lastName + " " + newStudent.firstName).build();

            if(user != null)
                user.updateProfile(profileUpdates);
        }

        if(activityEntity != null)
        {
            oldCourses = new LinkedList<>(activityEntity.optionalCourses);
            oldActivities = new LinkedList<>(activityEntity.otherActivities);

            for (String key : activityEntity.optionalCourses)
            {
                if (!newStudent.optionalCourses.contains(key))
                    activity.coursesProfileCategory.delete(key);
            }
            for (String key : activityEntity.otherActivities)
            {
                if (!newStudent.otherActivities.contains(key))
                    activity.otherActivitiesProfileCategory.delete(key);
            }

        }

        activityEntity = newStudent;

        for(String key : newStudent.optionalCourses)
        {
            if(!oldCourses.contains(key))
                refreshEnrolledOptionalCourse(key);
        }
        for(String key : newStudent.otherActivities)
        {
            if(!oldActivities.contains(key))
                refreshEnrolledOtherActivity(key);
        }

        activity.updateStudent(activityEntity);
    }

    public boolean isPersonalClass(ScheduleClass scheduleClass)
    {
        if(scheduleClass.pack != 0 && !activityEntity.optionalCourses.contains(scheduleClass.courseKey))
            return false;

        List<String> studentGroupTags = Utils.getStudentGroupTags(activityEntity);

        for(String group : scheduleClass.groups)
        {
            if(studentGroupTags.contains(group))
            {
                return true;
            }
        }

        return false;
    }

    private void refreshEnrolledOtherActivity(String key)
    {
        serviceConnections.postRequest(OtherActivityService.class, new GetByIdRequest<>(key), otherActivityCallback);
    }

    private void refreshEnrolledOptionalCourse(String key)
    {
        serviceConnections.postRequest(CourseService.class, new GetByIdRequest<>(key), courseCallback);
    }

    public void refreshAll()
    {
        serviceConnections.postRequest(CourseService.class, new GetAllRequest<Course>(), courseCallback);
        serviceConnections.postRequest(ProfessorService.class, new GetAllRequest<Professor>(), professorsCallback);
        serviceConnections.postRequest(OtherActivityService.class, new GetAllRequest<OtherActivity>(), otherActivityCallback);
        serviceConnections.postRequest(ScheduleClassService.class, new GetAllRequest<ScheduleClass>(), scheduleClassCallback);
    }

    public void unbindServices()
    {
        serviceConnections.unbind(courseCallback);
        serviceConnections.unbind(professorsCallback);
        serviceConnections.unbind(otherActivityCallback);
        serviceConnections.unbind(scheduleClassCallback);
    }

    public int getPermissionLevel(Class classToModifyInProfile)
    {
        if(classToModifyInProfile.equals(Course.class))
            return 1;
        return 0;
    }

    public boolean containsReferenceToEntity(String key)
    {
        return activityEntity.otherActivities.contains(key) || activityEntity.optionalCourses.contains(key)
                || activityEntity.grades.contains(key);
    }

    public void getDialogEntities(String dialogType)
    {
        switch (dialogType)
        {
            case "optional course":
            {
                serviceConnections.postRequest(CourseService.class, new GetAllRequest<Course>(), new ICallback<GetAllResponse<Course>>()
                {
                    @Override
                    public void processResponse(GetAllResponse<Course> response)
                    {
                        List<BaseEntity> dialogEntities = new LinkedList<>();

                        for(Course course : response.getItems())
                        {
                            if(course.pack != 0 && !activityEntity.optionalCourses.contains(course.getKey()))
                                dialogEntities.add(course);
                        }

                        activity.showDialog(dialogEntities, "Optional courses", "Enroll");
                        serviceConnections.unbind(this);
                    }
                });
                break;
            }
            case "other actvity":
            {
                break;
            }
        }
    }
}
