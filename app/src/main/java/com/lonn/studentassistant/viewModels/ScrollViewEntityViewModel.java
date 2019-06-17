package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.activities.implementations.otheractivityEntity.OtherActivityEntityActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.entities.ScheduleClass;

public class ScrollViewViewModel extends BaseObservable
{
    public Class entityActivityClass;
    @Bindable
    public int permissionLevel = 0, image = 0;
    @Bindable
    public String field1, field2, field3, field4;

    public ScrollViewViewModel(int image, String field1, String field2, String field3, Class entityActivityClass)
    {
        this.image = image;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.entityActivityClass = entityActivityClass;
    }

    public ScrollViewViewModel(String field1, String field2, String field3, String field4, Class entityActivityClass)
    {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.entityActivityClass = entityActivityClass;
    }

    public ScrollViewViewModel(String field1, String field2, String field3, Class entityActivityClass)
    {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.entityActivityClass = entityActivityClass;
    }

    public static ScrollViewViewModel partial(Course course)
    {
        String title = course.courseName;
        String subtitle = course.pack==0?"Mandatory course":"Optional course (pack " + Integer.toString(course.pack) + ")";
        String description;

        if(course.website == null)
            description = course.description;
        else
            description = course.website;

        return new ScrollViewViewModel(title, subtitle, description, CourseEntityActivity.class);
    }

    public static ScrollViewViewModel full(Course course)
    {
        String title = course.courseName;
        String subtitle = course.pack==0?"Mandatory course":"Optional course (pack " + Integer.toString(course.pack) + ")";
        String description = Utils.yearToString(course.year) + ", " + Utils.semesterToString(course.semester);

        return new ScrollViewViewModel(title, subtitle, description, CourseEntityActivity.class);
    }

    public static ScrollViewViewModel partial(Professor professor)
    {
        String title = professor.level + " " + professor.lastName + " " + professor.firstName;
        String subtitle = "Cabinet: " + professor.cabinet;
        String description = professor.email;

        return new ScrollViewViewModel(professor.professorImage, title, subtitle, description, ProfessorEntityActivity.class);
    }

    public static ScrollViewViewModel full(Professor professor)
    {
        String title = professor.level + " " + professor.lastName + " " + professor.firstName;
        String subtitle = "Cabinet: " + professor.cabinet;
        String description = professor.email;

        return new ScrollViewViewModel(professor.professorImage, title, subtitle, description, ProfessorEntityActivity.class);
    }

    public static ScrollViewViewModel partial(OtherActivity otherActivity)
    {
        String title = otherActivity.activityName;
        String subtitle = otherActivity.type;
        String description = Utils.yearToString(otherActivity.year) + ", " + Utils.semesterToString(otherActivity.semester);

        return new ScrollViewViewModel(title, subtitle, description, OtherActivityEntityActivity.class);
    }

    public static ScrollViewViewModel full(OtherActivity otherActivity)
    {
        String title = otherActivity.activityName;
        String subtitle = Utils.yearToString(otherActivity.year) + ", " + Utils.semesterToString(otherActivity.semester);
        String description;

        if(otherActivity.website == null)
            description = otherActivity.description;
        else
            description = otherActivity.website;

        return new ScrollViewViewModel(title, subtitle, description, OtherActivityEntityActivity.class);
    }

    public static ScrollViewViewModel partial(ScheduleClass scheduleClass)
    {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewViewModel(field1, field2, field3, field4, null);
    }

    public static ScrollViewViewModel full(ScheduleClass scheduleClass)
    {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewViewModel(field1, field2, field3, field4, null);
    }

    public static ScrollViewViewModel partial(BaseEntity entity)
    {
        if(entity instanceof Course)
            return partial((Course)entity);
        if(entity instanceof Professor)
            return partial((Professor)entity);
        if(entity instanceof OtherActivity)
            return partial((OtherActivity)entity);
        return null;
    }

    public static ScrollViewViewModel full(BaseEntity entity)
    {
        if(entity instanceof Course)
            return full((Course)entity);
        if(entity instanceof Professor)
            return full((Professor)entity);
        if(entity instanceof OtherActivity)
            return full((OtherActivity)entity);
        return null;
    }
}
