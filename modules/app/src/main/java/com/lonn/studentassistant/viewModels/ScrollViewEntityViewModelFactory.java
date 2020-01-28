package com.lonn.studentassistant.viewModels;

import com.lonn.studentassistant.activities.implementations.entityActivities.course.CourseEntityActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.laboratory.LaboratoryEntityActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity.OtherActivityEntityActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.professor.ProfessorEntityActivity;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.utils.Utils;
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
		if (entity instanceof RecurringClassViewModel) {
			if (viewType.equals(FULL)) {
				return full((RecurringClassViewModel) entity);
			}
			return partial((RecurringClassViewModel) entity);
		}
		if (entity instanceof OneTimeClassViewModel) {
			if (viewType.equals(FULL)) {
				return full((OneTimeClassViewModel) entity);
			}
			return partial((OneTimeClassViewModel) entity);
		}
		if (entity instanceof FileMetadataViewModel) {
			if (viewType.equals(FULL)) {
				return full((FileMetadataViewModel) entity);
			}
			return partial((FileMetadataViewModel) entity);
		}
		if (entity instanceof LaboratoryViewModel) {
			if (viewType.equals(FULL)) {
				return full((LaboratoryViewModel) entity);
			}
			return partial((LaboratoryViewModel) entity);
		}
		if (entity instanceof GradeViewModel) {
			if (viewType.equals(FULL)) {
				return full((GradeViewModel) entity);
			}
			return partial((GradeViewModel) entity);
		}
		if (entity instanceof StudentViewModel) {
			if (viewType.equals(FULL)) {
				return full((StudentViewModel) entity);
			}
			return partial((StudentViewModel) entity);
		}
		return null;
	}

	private static ScrollViewEntityViewModel partial(StudentViewModel student) {
		String title = student.getFirstName() + " " + student.getLastName();
		String subtitle = Utils.hideStudentId(student.getStudentId());
		String description = student.getCycleSpecializationYear().toString() + "\n" +
				Utils.yearToString(student.getCycleSpecializationYear().getYear()) + "\n" +
				"Group " + student.getGroup();

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(description)
				.build();
	}

	private static ScrollViewEntityViewModel full(StudentViewModel student) {
		String title = student.getFirstName() + " " + student.getLastName();
		String subtitle = Utils.hideStudentId(student.getStudentId());
		String description = student.getCycleSpecializationYear().toString() + "\n" +
				Utils.yearToString(student.getCycleSpecializationYear().getYear()) + "\n" +
				"Group " + student.getGroup();

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(description)
				.build();
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
		String description = otherActivity.getSemesterString();

		return ScrollViewEntityViewModel.builder()
				.field1(title)
				.field2(subtitle)
				.field3(description)
				.entityActivityClass(OtherActivityEntityActivity.class)
				.build();
	}

	private static ScrollViewEntityViewModel full(OtherActivityViewModel otherActivity) {
		String title = otherActivity.getName();
		String subtitle = otherActivity.getSemesterString();
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

	private static ScrollViewEntityViewModel partial(OneTimeClassViewModel oneTimeClass) {
		String field1 = oneTimeClass.getHours();
		String field2 = oneTimeClass.getDisciplineName();
		String field3 = oneTimeClass.getFormattedType();
		String field4 = oneTimeClass.getRooms();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.field4(field4)
				.build();
	}

	private static ScrollViewEntityViewModel full(OneTimeClassViewModel oneTimeClass) {
		String field1 = oneTimeClass.getHours();
		String field2 = oneTimeClass.getDisciplineName();
		String field3 = oneTimeClass.getFormattedType();
		String field4 = oneTimeClass.getRooms();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.field4(field4)
				.build();
	}

	private static ScrollViewEntityViewModel partial(RecurringClassViewModel recurringClass) {
		String field1 = recurringClass.getHours();
		String field2 = recurringClass.getDisciplineName();
		String field3 = recurringClass.getFormattedType();
		String field4 = recurringClass.getRooms();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.field4(field4)
				.field5(recurringClass.getParity())
				.build();
	}

	private static ScrollViewEntityViewModel full(RecurringClassViewModel recurringClass) {
		String field1 = recurringClass.getHours();
		String field2 = recurringClass.getDisciplineName();
		String field3 = recurringClass.getFormattedType();
		String field4 = recurringClass.getRooms();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.field4(field4)
				.field5(recurringClass.getParity())
				.build();
	}

	private static ScrollViewEntityViewModel partial(FileMetadataViewModel fileViewModel) {
		String field1 = fileViewModel.getFileTitle();
		String field2;
		String field3;

		if (fileViewModel.getFileDescription() != null) {
			field2 = fileViewModel.getFileDescription();
			field3 = fileViewModel.getFileExtension().toUpperCase() +
					" file (" + fileViewModel.getFileSize() + ")";
		}
		else {
			field2 = fileViewModel.getFileExtension().toUpperCase() +
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
			field3 = fileViewModel.getFileExtension().toUpperCase() +
					" file (" + fileViewModel.getFileSize() + ")";
		}
		else {
			field2 = fileViewModel.getFileExtension().toUpperCase() +
					" file (" + fileViewModel.getFileSize() + ")";
			field3 = null;
		}

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.field3(field3)
				.build();
	}

	private static ScrollViewEntityViewModel partial(LaboratoryViewModel laboratoryViewModel) {
		String field1 = laboratoryViewModel.getTitle() != null ?
				laboratoryViewModel.getTitle() : "Laboratory " + laboratoryViewModel.getWeekNumber();
		String field2 = "Week " + laboratoryViewModel.getWeekNumber();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.entityActivityClass(LaboratoryEntityActivity.class)
				.build();
	}

	private static ScrollViewEntityViewModel full(LaboratoryViewModel laboratoryViewModel) {
		String field1 = laboratoryViewModel.getTitle() != null ?
				laboratoryViewModel.getTitle() : "Laboratory " + laboratoryViewModel.getWeekNumber();
		String field2 = "Week " + laboratoryViewModel.getWeekNumber();

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.entityActivityClass(LaboratoryEntityActivity.class)
				.build();
	}

	private static ScrollViewEntityViewModel partial(GradeViewModel gradeViewModel) {
		String field1 = gradeViewModel.getStudentId();
		String field2 = gradeViewModel.getGrade() + "";

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.build();
	}

	private static ScrollViewEntityViewModel full(GradeViewModel gradeViewModel) {
		String field1 = gradeViewModel.getStudentId();
		String field2 = gradeViewModel.getGrade() + "";

		return ScrollViewEntityViewModel.builder()
				.field1(field1)
				.field2(field2)
				.build();
	}
}
