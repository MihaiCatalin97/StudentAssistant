package com.lonn.studentassistant.activities.implementations.professorEntity.businessLayer;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.lonn.studentassistant.activities.abstractions.IBusinessLayer;
import com.lonn.studentassistant.activities.abstractions.callbacks.ICallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
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

public class ProfessorBusinessLayer implements IBusinessLayer<Professor>
{
    private ConnectionBundle serviceConnections;
    private ProfessorEntityActivity activity;
    private Professor activityEntity;

    private CourseCallback courseCallback;
    private OtherActivityCallback otherActivityCallback;
    private ScheduleClassCallback scheduleClassCallback;
    private ProfessorCallback professorCallback;

    public ProfessorBusinessLayer(ProfessorEntityActivity activity, Professor professor)
    {
        this.activity = activity;
        this.activityEntity = professor.clone();
        serviceConnections = new ConnectionBundle(activity.getBaseContext());
        Log.e("Creating entity", activityEntity.getKey());

        init();
    }

    private void init()
    {
        courseCallback = new CourseCallback(activity, this);
        otherActivityCallback = new OtherActivityCallback(activity, this);
        professorCallback = new ProfessorCallback(activity, this);
        scheduleClassCallback = new ScheduleClassCallback(activity, this);
    }

    public void addEntityToList(BaseEntity entity)
    {
        Professor newEntity = activityEntity.clone();

        if (entity instanceof Course)
        {
            if (!activityEntity.courses.contains(entity.getKey()))
                newEntity.courses.add(entity.getKey());
        }
        else if (entity instanceof OtherActivity)
        {
            if(!activityEntity.otherActivities.contains(entity.getKey()))
                newEntity.otherActivities.add(entity.getKey());
        }
        else if (entity instanceof ScheduleClass)
        {
            if(!activityEntity.scheduleClasses.contains(entity.getKey()))
                newEntity.scheduleClasses.add(entity.getKey());
        }

        serviceConnections.postRequest(ProfessorService.class, new EditRequest<>(Collections.singletonList(newEntity)), professorCallback);
    }

    public void removeEntityFromList(BaseEntity entity)
    {
        Professor newEntity = activityEntity.clone();

        if(entity instanceof Course)
            newEntity.courses.remove(entity.getKey());
        else if (entity instanceof OtherActivity)
            newEntity.otherActivities.remove(entity.getKey());

        serviceConnections.postRequest(StudentService.class, new EditRequest<>(Collections.singletonList(newEntity)), professorCallback);
    }

    public void editActivityEntity(Professor newEntity)
    {
        List<String> oldCourses = new LinkedList<>();
        List<String> oldActivities = new LinkedList<>();
        List<String> oldSchedule = new LinkedList<>();

        if(activityEntity == null || !activityEntity.firstName.equals(newEntity.firstName) || !activityEntity.lastName.equals(newEntity.lastName))
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newEntity.lastName + " " + newEntity.firstName).build();

            if(user != null)
                user.updateProfile(profileUpdates);
        }

        if(activityEntity != null)
        {
            oldCourses = new LinkedList<>(activityEntity.courses);
            oldActivities = new LinkedList<>(activityEntity.otherActivities);
            oldSchedule = new LinkedList<>(activityEntity.scheduleClasses);

            for (String key : activityEntity.courses)
            {
                if (!newEntity.courses.contains(key))
                    activity.courseBaseCategory.delete(key);
            }
            for (String key : activityEntity.otherActivities)
            {
                if (!newEntity.otherActivities.contains(key))
                    activity.otherActivityBaseCategory.delete(key);
            }
            for (String key : activityEntity.scheduleClasses)
            {
                if (!newEntity.scheduleClasses.contains(key))
                    activity.scheduleClassBaseCategory.delete(key);
            }
        }

        activityEntity = newEntity;

        for(String key : newEntity.courses)
        {
            if(!oldCourses.contains(key))
                refreshEnrolledOptionalCourse(key);
        }
        for(String key : newEntity.otherActivities)
        {
            if(!oldActivities.contains(key))
                refreshEnrolledOtherActivity(key);
        }
        for(String key : newEntity.scheduleClasses)
        {
            if(!oldSchedule.contains(key))
                refreshSchedule(key);
        }

        Log.e("Updating entity", activityEntity.getKey());
        activity.updateEntity(activityEntity);
    }

    public boolean isPersonalClass(ScheduleClass scheduleClass)
    {
        return activityEntity.scheduleClasses.contains(scheduleClass.getKey());
    }

    private void refreshEnrolledOtherActivity(String key)
    {
        serviceConnections.postRequest(OtherActivityService.class, new GetByIdRequest<>(key), otherActivityCallback);
    }

    private void refreshEnrolledOptionalCourse(String key)
    {
        serviceConnections.postRequest(CourseService.class, new GetByIdRequest<>(key), courseCallback);
    }

    private void refreshSchedule(String key)
    {
        serviceConnections.postRequest(ScheduleClassService.class, new GetByIdRequest<>(key), scheduleClassCallback);
    }

    public void refreshAll()
    {
        serviceConnections.postRequest(ProfessorService.class, new GetByIdRequest<>(activityEntity.getKey()), professorCallback);
        serviceConnections.postRequest(CourseService.class, new GetAllRequest<Course>(), courseCallback);
        serviceConnections.postRequest(OtherActivityService.class, new GetAllRequest<OtherActivity>(), otherActivityCallback);
        serviceConnections.postRequest(ScheduleClassService.class, new GetAllRequest<ScheduleClass>(), scheduleClassCallback);
    }

    public void unbindServices()
    {
        serviceConnections.unbind(courseCallback);
        serviceConnections.unbind(professorCallback);
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
        return activityEntity.otherActivities.contains(key) || activityEntity.courses.contains(key)
                || activityEntity.scheduleClasses.contains(key);
    }

    public void getDialogEntities(String dialogType)
    {

    }
}
