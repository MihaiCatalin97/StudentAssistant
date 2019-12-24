package com.lonn.studentassistant.viewModels;

import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.activities.implementations.otheractivityEntity.OtherActivityEntityActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.viewModels.entities.ScheduleClassViewModel;
import com.lonn.studentassistant.viewModels.entities.ScrollViewEntityViewModel;
import com.lonn.studentassistant.views.EntityViewType;

import static com.lonn.studentassistant.views.EntityViewType.FULL;

public class ScrollViewEntityViewModelFactory {
    public static ScrollViewEntityViewModel getScrollViewEntityViewModel(EntityViewType viewType,
                                                                         BaseEntity entity) {
        if (entity instanceof Course) {
            if (viewType.equals(FULL)) {
                return full((Course) entity);
            }
            return partial((Course) entity);
        }
        if (entity instanceof Professor) {
            if (viewType.equals(FULL)) {
                return full((Professor) entity);
            }
            return partial((Professor) entity);
        }
        if (entity instanceof OtherActivity) {
            if (viewType.equals(FULL)) {
                return full((OtherActivity) entity);
            }
            return partial((OtherActivity) entity);
        }
        if (entity instanceof RecurringClass) {
            if (viewType.equals(FULL)) {
                return full((RecurringClass) entity);
            }
            return partial((RecurringClass) entity);
        }
        return null;
    }

    private static ScrollViewEntityViewModel partial(Course course) {
        String title = course.getDisciplineName();
        String subtitle = course.getPack() == 0 ? "Mandatory discipline" : "Optional discipline (pack " + course.getPack() + ")";
        String description;

        if (course.getWebsite() == null) {
            description = course.getDescription();
        }
        else {
            description = course.getWebsite();
        }

        return new ScrollViewEntityViewModel(null, title, subtitle, description, CourseEntityActivity.class);
    }

    private static ScrollViewEntityViewModel full(Course course) {
        String title = course.getDisciplineName();
        String subtitle = course.getPack() == 0 ? "Mandatory discipline" : "Optional discipline (pack " + course.getPack() + ")";
        String description = Utils.yearToString(course.getYear()) + ", " + Utils.semesterToString(course.getSemester());

        return new ScrollViewEntityViewModel(null, title, subtitle, description, CourseEntityActivity.class);
    }

    private static ScrollViewEntityViewModel partial(Professor professor) {
        String title = (professor.getLevel() != null ? (professor.getLevel() + " ") : "") +
                professor.getLastName() + " " +
                professor.getFirstName();

        String subtitle;
        if (professor.getCabinet() != null) {
            subtitle = "Cabinet: " + professor.getCabinet();
        }
        else {
            subtitle = professor.getWebsite();
        }

        String description = professor.getEmail();

        return new ScrollViewEntityViewModel(professor.getProfessorImage(),
                title,
                subtitle,
                description,
                ProfessorEntityActivity.class);
    }

    private static ScrollViewEntityViewModel full(Professor professor) {
        String title = (professor.getLevel() != null ? (professor.getLevel() + " ") : "") +
                professor.getLastName() + " " +
                professor.getFirstName();

        String subtitle;
        if (professor.getCabinet() != null) {
            subtitle = "Cabinet: " + professor.getCabinet();
        }
        else {
            subtitle = professor.getWebsite();
        }

        String description = professor.getEmail();

        return new ScrollViewEntityViewModel(professor.getProfessorImage(),
                title,
                subtitle,
                description,
                ProfessorEntityActivity.class);
    }

    private static ScrollViewEntityViewModel partial(OtherActivity otherActivity) {
        String title = otherActivity.getDisciplineName();
        String subtitle = otherActivity.getType();
        String description = Utils.yearToString(otherActivity.getYear()) + ", " + Utils.semesterToString(otherActivity.getSemester());

        return new ScrollViewEntityViewModel(null, title, subtitle, description, OtherActivityEntityActivity.class);
    }

    private static ScrollViewEntityViewModel full(OtherActivity otherActivity) {
        String title = otherActivity.getDisciplineName();
        String subtitle = Utils.yearToString(otherActivity.getYear()) + ", " + Utils.semesterToString(otherActivity.getSemester());
        String description;

        if (otherActivity.getWebsite() == null) {
            description = otherActivity.getDescription();
        }
        else {
            description = otherActivity.getWebsite();
        }

        return new ScrollViewEntityViewModel(null, title, subtitle, description, OtherActivityEntityActivity.class);
    }

    private static ScrollViewEntityViewModel partial(RecurringClass scheduleClass) {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewEntityViewModel(field1, field2, field3, field4, null);
    }

    private static ScrollViewEntityViewModel full(RecurringClass scheduleClass) {
        ScheduleClassViewModel model = new ScheduleClassViewModel(scheduleClass);
        String field1 = model.getHours();
        String field2 = model.getCourseName();
        String field3 = model.getFormattedType();
        String field4 = model.getRooms();

        return new ScrollViewEntityViewModel(field1, field2, field3, field4, null);
    }
}
