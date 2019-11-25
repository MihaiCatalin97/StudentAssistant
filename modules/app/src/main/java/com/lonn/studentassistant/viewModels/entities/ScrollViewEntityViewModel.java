package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.activities.implementations.otheractivityEntity.OtherActivityEntityActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;

public class ScrollViewEntityViewModel extends BaseObservable {
    public Class entityActivityClass;
    @Bindable
    public int permissionLevel = 0, image = 0;
    @Bindable
    public String field1, field2, field3, field4;

    public ScrollViewEntityViewModel(int image, String field1, String field2, String field3, Class entityActivityClass) {
        this.image = image;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.entityActivityClass = entityActivityClass;
    }

    public ScrollViewEntityViewModel(String field1, String field2, String field3, String field4, Class entityActivityClass) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.entityActivityClass = entityActivityClass;
    }

    public ScrollViewEntityViewModel(String field1, String field2, String field3, Class entityActivityClass) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.entityActivityClass = entityActivityClass;
    }

    public static ScrollViewEntityViewModel partial(Course course) {
        String title = course.getDisciplineName();
        String subtitle = course.getPack() == 0 ? "Mandatory discipline" : "Optional discipline (pack " + course.getPack() + ")";
        String description;

        if (course.getWebsite() == null) {
            description = course.getDescription();
        }
        else {
            description = course.getWebsite();
        }

        return new ScrollViewEntityViewModel(title, subtitle, description, CourseEntityActivity.class);
    }

    public static ScrollViewEntityViewModel full(Course course) {
        String title = course.getDisciplineName();
        String subtitle = course.getPack() == 0 ? "Mandatory discipline" : "Optional discipline (pack " + course.getPack() + ")";
        String description = Utils.yearToString(course.getYear()) + ", " + Utils.semesterToString(course.getSemester());

        return new ScrollViewEntityViewModel(title, subtitle, description, CourseEntityActivity.class);
    }

    public static ScrollViewEntityViewModel partial(Professor professor) {
        String title = professor.getLevel() + " " + professor.getLastName() + " " + professor.getFirstName();
        String subtitle = "Cabinet: " + professor.getCabinet();
        String description = professor.getEmail();

        return new ScrollViewEntityViewModel(professor.getProfessorImage(), title, subtitle, description, ProfessorEntityActivity.class);
    }

    public static ScrollViewEntityViewModel full(Professor professor) {
        String title = professor.getLevel() + " " + professor.getLastName() + " " + professor.getFirstName();
        String subtitle = "Cabinet: " + professor.getCabinet();
        String description = professor.getEmail();

        return new ScrollViewEntityViewModel(professor.getProfessorImage(), title, subtitle, description, ProfessorEntityActivity.class);
    }

    public static ScrollViewEntityViewModel partial(OtherActivity otherActivity) {
        String title = otherActivity.getDisciplineName();
        String subtitle = otherActivity.getType();
        String description = Utils.yearToString(otherActivity.getYear()) + ", " + Utils.semesterToString(otherActivity.getSemester());

        return new ScrollViewEntityViewModel(title, subtitle, description, OtherActivityEntityActivity.class);
    }

    public static ScrollViewEntityViewModel full(OtherActivity otherActivity) {
        String title = otherActivity.getDisciplineName();
        String subtitle = Utils.yearToString(otherActivity.getYear()) + ", " + Utils.semesterToString(otherActivity.getSemester());
        String description;

        if (otherActivity.getWebsite() == null) {
            description = otherActivity.getDescription();
        }
        else {
            description = otherActivity.getWebsite();
        }

        return new ScrollViewEntityViewModel(title, subtitle, description, OtherActivityEntityActivity.class);
    }

    public static ScrollViewEntityViewModel partial(RecurringClass scheduleClass) {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewEntityViewModel(field1, field2, field3, field4, null);
    }

    public static ScrollViewEntityViewModel full(RecurringClass scheduleClass) {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewEntityViewModel(field1, field2, field3, field4, null);
    }

    public static ScrollViewEntityViewModel partial(BaseEntity entity) {
        if (entity instanceof Course) {
            return partial((Course) entity);
        }
        if (entity instanceof Professor) {
            return partial((Professor) entity);
        }
        if (entity instanceof OtherActivity) {
            return partial((OtherActivity) entity);
        }
        if (entity instanceof ScheduleClass) {
            return partial((ScheduleClass) entity);
        }
        return null;
    }

    public static ScrollViewEntityViewModel full(BaseEntity entity) {
        if (entity instanceof Course) {
            return full((Course) entity);
        }
        if (entity instanceof Professor) {
            return full((Professor) entity);
        }
        if (entity instanceof OtherActivity) {
            return full((OtherActivity) entity);
        }
        if (entity instanceof ScheduleClass) {
            return full((ScheduleClass) entity);
        }
        return null;
    }

    @Bindable
    public int getField2LineCount() {
        return field2.split("\n").length;
    }

    @Bindable
    public int getField3LineCount() {
        return field3.split("\n").length;
    }

    @Bindable
    public int getField4LineCount() {
        return field4.split("\n").length;
    }
}
