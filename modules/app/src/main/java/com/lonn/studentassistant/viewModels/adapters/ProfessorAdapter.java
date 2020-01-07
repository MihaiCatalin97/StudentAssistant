package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;
import static com.lonn.studentassistant.viewModels.entities.ProfessorViewModel.builder;

public class ProfessorAdapter extends ViewModelAdapter<Professor, ProfessorViewModel> {
	public ProfessorAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
		super(firebaseConnectedActivity);
	}

	public ProfessorViewModel adaptOne(Professor professor) {
		return (ProfessorViewModel) builder()
				.firstName(professor.getFirstName())
				.lastName(professor.getLastName())
				.cabinet(professor.getCabinet())
				.email(professor.getEmail())
				.phoneNumber(professor.getPhoneNumber())
				.professorImage(professor.getProfessorImageMetadataKey())
				.rank(professor.getLevel())
				.website(professor.getWebsite())
				.build()
				.setKey(professor.getKey());
	}

	protected ProfessorViewModel resolveLinks(ProfessorViewModel professorViewModel, Professor professor) {
		linkCourses(professorViewModel, professor);
		linkOtherActivities(professorViewModel, professor);
		linkRecurringClasses(professorViewModel, professor);
		linkOneTimeClasses(professorViewModel, professor);

		return professorViewModel;
	}

	private void linkCourses(ProfessorViewModel viewModel, Professor entity) {
		CourseAdapter courseAdapter = new CourseAdapter(firebaseConnectedActivity);

		for (String courseId : entity.getCourses()) {
			firebaseConnectedActivity.getFirebaseConnection()
					.execute(new GetRequest<Course>()
							.databaseTable(COURSES)
							.predicate(where(ID).equalTo(courseId))
							.onSuccess(courses -> {
								if (viewModel.courses == null) {
									viewModel.courses = new ArrayList<>();
								}

								viewModel.courses.addAll(courseAdapter.adapt(courses, false));
								viewModel.notifyPropertyChanged(_all);
							})
							.subscribe(false));
		}
	}

	private void linkOtherActivities(ProfessorViewModel viewModel, Professor entity) {
		OtherActivityAdapter otherActivityAdapter = new OtherActivityAdapter(firebaseConnectedActivity);

		for (String otherActivityId : entity.getOtherActivities()) {
			firebaseConnectedActivity.getFirebaseConnection()
					.execute(new GetRequest<OtherActivity>()
							.databaseTable(OTHER_ACTIVITIES)
							.predicate(where(ID).equalTo(otherActivityId))
							.onSuccess(otherActivities -> {
								if (viewModel.otherActivities == null) {
									viewModel.otherActivities = new ArrayList<>();
								}

								viewModel.otherActivities.addAll(otherActivityAdapter.adapt(otherActivities, false));
								viewModel.notifyPropertyChanged(_all);
							})
							.subscribe(false));
		}
	}

	private void linkRecurringClasses(ProfessorViewModel viewModel, Professor entity) {
		RecurringClassAdapter recurringClassAdapter = new RecurringClassAdapter(firebaseConnectedActivity);

		for (String scheduleClassId : entity.getScheduleClasses()) {
			firebaseConnectedActivity.getFirebaseConnection()
					.execute(new GetRequest<RecurringClass>()
							.databaseTable(RECURRING_CLASSES)
							.predicate(where(ID).equalTo(scheduleClassId))
							.onSuccess(recurringClasses -> {
								if (viewModel.recurringClasses == null) {
									viewModel.recurringClasses = new ArrayList<>();
								}

								viewModel.recurringClasses.addAll(recurringClassAdapter.adapt(recurringClasses));
								viewModel.notifyPropertyChanged(_all);
							})
							.subscribe(false));
		}
	}

	private void linkOneTimeClasses(ProfessorViewModel viewModel, Professor entity) {
		OneTimeClassAdapter oneTimeClassAdapter = new OneTimeClassAdapter(firebaseConnectedActivity);

		for (String scheduleClassId : entity.getScheduleClasses()) {
			firebaseConnectedActivity.getFirebaseConnection()
					.execute(new GetRequest<OneTimeClass>()
							.databaseTable(ONE_TIME_CLASSES)
							.predicate(where(ID).equalTo(scheduleClassId))
							.onSuccess(oneTimeClasses -> {
								if (viewModel.oneTimeClasses == null) {
									viewModel.oneTimeClasses = new ArrayList<>();
								}

								viewModel.oneTimeClasses.addAll(oneTimeClassAdapter.adapt(oneTimeClasses));
								viewModel.notifyPropertyChanged(_all);
							})
							.subscribe(false));
		}
	}
}
