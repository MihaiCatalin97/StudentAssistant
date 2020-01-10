package com.lonn.studentassistant.viewModels;

import com.lonn.studentassistant.activities.implementations.entityActivities.course.CourseEntityActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity.OtherActivityEntityActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.professor.ProfessorEntityActivity;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.views.EntityViewType;

import static com.lonn.studentassistant.utils.Utils.semesterToString;
import static com.lonn.studentassistant.utils.Utils.yearToString;
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
		if (entity instanceof FileMetadataViewModel) {
			if (viewType.equals(FULL)) {
				return full((FileMetadataViewModel) entity);
			}
			return partial((FileMetadataViewModel) entity);
		}
		return null;
	}

	private static ScrollViewEntityViewModel partial(CourseViewModel course) {
		String title = course.getName();
		String subtitle = course.getCourseType();
		String description = semesterToString(course.getSemester());

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(description)
				.entityActivityClass(CourseEntityActivity.class)
				.build();
	}

	private static ScrollViewEntityViewModel full(CourseViewModel course) {
		String title = course.getName();
		String subtitle = course.getCourseType();

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < course.getCycleSpecializationYears().size(); i++) {
			CycleSpecializationYear cycleSpecializationYear = course.getCycleSpecializationYears()
					.get(i);

			stringBuilder.append(cycleSpecializationYear.getCycleSpecialization().toString())
					.append(", ")
					.append(yearToString(cycleSpecializationYear.getYear()))
					.append(", ")
					.append(semesterToString(course.getSemester()));

			if (i + 1 < course.getCycleSpecializationYears().size()) {
				stringBuilder.append("\n");
			}
		}

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(stringBuilder.toString())
				.entityActivityClass(CourseEntityActivity.class)
				.build();
	}

	private static ScrollViewEntityViewModel partial(ProfessorViewModel professor) {
		String title = (professor.getRank() != null ? (professor.getRank() + " ") : "") +
				professor.getLastName() + " " +
				professor.getFirstName();

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.entityActivityClass(ProfessorEntityActivity.class)
				.build();
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

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(description)
				.entityActivityClass(ProfessorEntityActivity.class)
				.build();
	}

	private static ScrollViewEntityViewModel partial(OtherActivityViewModel otherActivity) {
		String title = otherActivity.getName();
		String subtitle = otherActivity.getType();
		String description = otherActivity.getYearSemester();

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(description)
				.entityActivityClass(OtherActivityEntityActivity.class)
				.build();
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

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(description)
				.entityActivityClass(OtherActivityEntityActivity.class)
				.build();
	}

	private static ScrollViewEntityViewModel partial(ScheduleClassViewModel scheduleClass) {
		String field1 = scheduleClass.getHours();
		String field2 = scheduleClass.getDisciplineName();
		String field3 = scheduleClass.getFormattedType();
		String field4 = scheduleClass.getRooms();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.field4(field4)
				.field5(scheduleClass.getParity())
				.build();
	}

	private static ScrollViewEntityViewModel full(ScheduleClassViewModel scheduleClass) {
		String field1 = scheduleClass.getHours();
		String field2 = scheduleClass.getDisciplineName();
		String field3 = scheduleClass.getFormattedType();
		String field4 = scheduleClass.getRooms();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.field4(field4)
				.field5(scheduleClass.getParity())
				.build();
	}

	private static ScrollViewEntityViewModel partial(FileMetadataViewModel fileViewModel) {
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

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.build();
	}

	private static ScrollViewEntityViewModel full(FileMetadataViewModel fileViewModel) {
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

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.build();
	}
}
