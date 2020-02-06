package com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.ScheduleClassViewModel;

public abstract class ScheduleClassAdapter<T extends ScheduleClass, U extends ScheduleClassViewModel<T>> extends ViewModelAdapter<T, U> {
	protected FirebaseConnection firebaseConnection;

	public ScheduleClassAdapter(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
	}

	public U adapt(U scheduleClassViewModel, T scheduleClass) {
		scheduleClassViewModel.setStartHour(scheduleClass.getStartHour())
				.setEndHour(scheduleClass.getEndHour())
				.setGroups(scheduleClass.getGroups())
				.setType(scheduleClass.getType())
				.setRooms(scheduleClass.getRooms())
				.setGroups(scheduleClass.getGroups())
				.setProfessors(scheduleClass.getProfessors())
				.setDiscipline(scheduleClass.getDiscipline())
				.setKey(scheduleClass.getKey());

		return scheduleClassViewModel;
	}

	public T adapt(T scheduleClass, U scheduleClassViewModel) {
		scheduleClass.setStartHour(scheduleClassViewModel.startHour)
				.setEndHour(scheduleClassViewModel.endHour)
				.setGroups(scheduleClassViewModel.getGroups())
				.setType(scheduleClassViewModel.getType())
				.setRooms(scheduleClassViewModel.rooms)
				.setGroups(scheduleClassViewModel.getGroups())
				.setDiscipline(scheduleClassViewModel.getDiscipline())
				.setProfessors(scheduleClassViewModel.getProfessors());

		if (scheduleClassViewModel.getKey() != null) {
			scheduleClass.setKey(scheduleClassViewModel.getKey());
		}

		return scheduleClass;
	}
}
