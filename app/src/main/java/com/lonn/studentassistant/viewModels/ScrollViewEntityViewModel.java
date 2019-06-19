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

public class ScrollViewEntityViewModel extends BaseObservable
{
    public Class entityActivityClass;
    @Bindable
    public int permissionLevel = 0, image = 0;
    @Bindable
    public String field1, field2, field3, field4;

    public ScrollViewEntityViewModel(int image, String field1, String field2, String field3, Class entityActivityClass)
    {
        this.image = image;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.entityActivityClass = entityActivityClass;
    }

    public ScrollViewEntityViewModel(String field1, String field2, String field3, String field4, Class entityActivityClass)
    {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.entityActivityClass = entityActivityClass;
    }

    public ScrollViewEntityViewModel(String field1, String field2, String field3, Class entityActivityClass)
    {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.entityActivityClass = entityActivityClass;
    }

    public static ScrollViewEntityViewModel partial(Course course)
    {
        String title = course.courseName;
        String subtitle = course.pack==0?"Mandatory course":"Optional course (pack " + Integer.toString(course.pack) + ")";
        String description;

        if(course.website == null)
            description = course.description;
        else
            description = course.website;

        return new ScrollViewEntityViewModel(title, subtitle, description, CourseEntityActivity.class);
    }

    public static ScrollViewEntityViewModel full(Course course)
    {
        String title = course.courseName;
        String subtitle = course.pack==0?"Mandatory course":"Optional course (pack " + Integer.toString(course.pack) + ")";
        String description = Utils.yearToString(course.year) + ", " + Utils.semesterToString(course.semester);

        return new ScrollViewEntityViewModel(title, subtitle, description, CourseEntityActivity.class);
    }

    public static ScrollViewEntityViewModel partial(Professor professor)
    {
        String title = professor.level + " " + professor.lastName + " " + professor.firstName;
        String subtitle = "Cabinet: " + professor.cabinet;
        String description = professor.email;

        return new ScrollViewEntityViewModel(professor.professorImage, title, subtitle, description, ProfessorEntityActivity.class);
    }

    public static ScrollViewEntityViewModel full(Professor professor)
    {
        String title = professor.level + " " + professor.lastName + " " + professor.firstName;
        String subtitle = "Cabinet: " + professor.cabinet;
        String description = professor.email;

        return new ScrollViewEntityViewModel(professor.professorImage, title, subtitle, description, ProfessorEntityActivity.class);
    }

    public static ScrollViewEntityViewModel partial(OtherActivity otherActivity)
    {
        String title = otherActivity.activityName;
        String subtitle = otherActivity.type;
        String description = Utils.yearToString(otherActivity.year) + ", " + Utils.semesterToString(otherActivity.semester);

        return new ScrollViewEntityViewModel(title, subtitle, description, OtherActivityEntityActivity.class);
    }

    public static ScrollViewEntityViewModel full(OtherActivity otherActivity)
    {
        String title = otherActivity.activityName;
        String subtitle = Utils.yearToString(otherActivity.year) + ", " + Utils.semesterToString(otherActivity.semester);
        String description;

        if(otherActivity.website == null)
            description = otherActivity.description;
        else
            description = otherActivity.website;

        return new ScrollViewEntityViewModel(title, subtitle, description, OtherActivityEntityActivity.class);
    }

    public static ScrollViewEntityViewModel partial(ScheduleClass scheduleClass)
    {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewEntityViewModel(field1, field2, field3, field4, null);
    }

    public static ScrollViewEntityViewModel full(ScheduleClass scheduleClass)
    {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewEntityViewModel(field1, field2, field3, field4, null);
    }

    public static ScrollViewEntityViewModel partial(BaseEntity entity)
    {
        if(entity instanceof Course)
            return partial((Course)entity);
        if(entity instanceof Professor)
            return partial((Professor)entity);
        if(entity instanceof OtherActivity)
            return partial((OtherActivity)entity);
        if(entity instanceof ScheduleClass)
            return partial((ScheduleClass)entity);
        return null;
    }

    public static ScrollViewEntityViewModel full(BaseEntity entity)
    {
        if(entity instanceof Course)
            return full((Course)entity);
        if(entity instanceof Professor)
            return full((Professor)entity);
        if(entity instanceof OtherActivity)
            return full((OtherActivity)entity);
        if(entity instanceof ScheduleClass)
            return full((ScheduleClass)entity);
        return null;
    }

    @Bindable
    public int getField2LineCount()
    {
        return field2.split("\n").length;
    }

    @Bindable
    public int getField3LineCount()
    {
        return field3.split("\n").length;
    }

    @Bindable
    public int getField4LineCount()
    {
        return field4.split("\n").length;
    }
}
