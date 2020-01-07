package com.lonn.studentassistant.viewModels;

import com.lonn.studentassistant.activities.implementations.entityActivities.course.CourseEntityActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity.OtherActivityEntityActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.professor.ProfessorEntityActivity;
import com.lonn.studentassistant.utils.Utils;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.viewModels.entities.FileViewModel;
import com.lonn.studentassistant.viewModels.entities.OtherActivityViewModel;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;
import com.lonn.studentassistant.viewModels.entities.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.views.EntityViewType;

import static com.lonn.studentassistant.views.EntityViewType.FULL;

public class ScrollViewEntityViewModelFactory {
	public static ScrollViewEntityViewModel getScrollViewEntityViewModel(EntityViewType viewType,
																		 EntityViewModel entity) {
		if (entity instanceof CourseViewModel) {
			if (viewType.equals(FULL)) {
				return full((CourseViewModel) entity);
			}
			return partial((CourseViewModel) entity);
		}
		if (entity instanceof ProfessorViewModel) {
			if (viewType.equals(FULL)) {
				return full((ProfessorViewModel) entity);
			}
			return partial((ProfessorViewModel) entity);
		}
		if (entity instanceof OtherActivityViewModel) {
			if (viewType.equals(FULL)) {
				return full((OtherActivityViewModel) entity);
			}
			return partial((OtherActivityViewModel) entity);
		}
		if (entity instanceof ScheduleClassViewModel) {
			if (viewType.equals(FULL)) {
				return full((ScheduleClassViewModel) entity);
			}
			return partial((ScheduleClassViewModel) entity);
		}
		if (entity instanceof FileViewModel) {
			if (viewType.equals(FULL)) {
				return full((FileViewModel) entity);
			}
			return partial((FileViewModel) entity);
		}
		return null;
	}

	private static ScrollViewEntityViewModel partial(CourseViewModel course) {
		String title = course.getName();
		String subtitle = course.getCourseType();
		String description;

		if (course.getWebsite() == null) {
			description = course.getDescription();
		}
		else {
			description = course.getWebsite();
		}

		return new ScrollViewEntityViewModel(null, title, subtitle, description, CourseEntityActivity.class);
	}

	private static ScrollViewEntityViewModel full(CourseViewModel course) {
		String title = course.getName();
		String subtitle = course.getCourseType();
		String description = Utils.yearToString(course.getYear()) + ", " + Utils.semesterToString(course.getSemester());

		return new ScrollViewEntityViewModel(null, title, subtitle, description, CourseEntityActivity.class);
	}

	private static ScrollViewEntityViewModel partial(ProfessorViewModel professor) {
		String title = (professor.getRank() != null ? (professor.getRank() + " ") : "") +
				professor.getLastName() + " " +
				professor.getFirstName();

		return new ScrollViewEntityViewModel(professor.getProfessorImage(),
				title,
				null,
				null,
				ProfessorEntityActivity.class);
	}

	private static ScrollViewEntityViewModel full(ProfessorViewModel professor) {
		String title = (professor.getRank() != null ? (professor.getRank() + " ") : "") +
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

	private static ScrollViewEntityViewModel partial(OtherActivityViewModel otherActivity) {
		String title = otherActivity.getName();
		String subtitle = otherActivity.getType();
		String description = otherActivity.getYearSemester();

		return new ScrollViewEntityViewModel(null, title, subtitle, description, OtherActivityEntityActivity.class);
	}

	private static ScrollViewEntityViewModel full(OtherActivityViewModel otherActivity) {
		String title = otherActivity.getName();
		String subtitle = otherActivity.getYearSemester();
		String description;

		if (otherActivity.getWebsite() == null) {
			description = otherActivity.getDescription();
		}
		else {
			description = otherActivity.getWebsite();
		}

		return new ScrollViewEntityViewModel(null, title, subtitle, description, OtherActivityEntityActivity.class);
	}

	private static ScrollViewEntityViewModel partial(ScheduleClassViewModel scheduleClass) {
		String field1 = scheduleClass.getHours();
		String field2 = scheduleClass.getDisciplineName();
		String field3 = scheduleClass.getFormattedType();
		String field4 = scheduleClass.getRooms();

		return new ScrollViewEntityViewModel(null, field1, field2, field3, field4, null);
	}

	private static ScrollViewEntityViewModel full(ScheduleClassViewModel scheduleClass) {
		String field1 = scheduleClass.getHours();
		String field2 = scheduleClass.getDisciplineName();
		String field3 = scheduleClass.getFormattedType();
		String field4 = scheduleClass.getRooms();

		return new ScrollViewEntityViewModel(null, field1, field2, field3, field4, null);
	}

	private static ScrollViewEntityViewModel partial(FileViewModel fileViewModel) {
		String field1 = fileViewModel.getFileTitle();
		String field2;
		String field3;

		if (fileViewModel.getFileDescription() != null) {
			field2 = fileViewModel.getFileDescription();
			field3 = fileViewModel.getFileType().toUpperCase() +
					" file (" + fileViewModel.getFileSize() + ")";
		}
		else {
			field2 = fileViewModel.getFileType().toUpperCase() +
					" file (" + fileViewModel.getFileSize() + ")";
			field3 = null;
		}
		return new ScrollViewEntityViewModel(null, field1, field2, field3, null, null);
	}

	private static ScrollViewEntityViewModel full(FileViewModel fileViewModel) {
		String field1 = fileViewModel.getFileTitle();
		String field2;
		String field3;

		if (fileViewModel.getFileDescription() != null) {
			field2 = fileViewModel.getFileDescription();
			field3 = fileViewModel.getFileType().toUpperCase() +
					" file (" + fileViewModel.getFileSize() + ")";
		}
		else {
			field2 = fileViewModel.getFileType().toUpperCase() +
					" file (" + fileViewModel.getFileSize() + ")";
			field3 = null;
		}

		return new ScrollViewEntityViewModel(null, field1, field2, field3, null, null);
	}
}
