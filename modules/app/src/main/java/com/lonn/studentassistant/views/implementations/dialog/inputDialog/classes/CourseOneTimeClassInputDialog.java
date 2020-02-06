package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions.OneTimeClassInputDialog;

import java.util.List;

public class CourseOneTimeClassInputDialog extends OneTimeClassInputDialog<CourseViewModel> {

	public CourseOneTimeClassInputDialog(Context context, CourseViewModel course,
										 List<ProfessorViewModel> courseProfessors) {
		super(context, course, courseProfessors);
	}

	protected void showGroupSelectionDialog() {
		List<CycleSpecializationYear> cycleSpecializationYears = getDiscipline().getCycleSpecializationYears();

		if (cycleSpecializationYears.size() == 1) {
			SingleChoiceListAdapter<String> groupAdapter = new SingleChoiceListAdapter<>(getContext(),
					cycleSpecializationYears.get(0).getGroups());

			new AlertDialog.Builder(getContext(), R.style.DialogTheme)
					.setTitle("Select group")
					.setAdapter(groupAdapter, (dialog2, which2) -> {
						addGroup(groupAdapter.getItem(which2));
					})
					.create()
					.show();
		}
		else {
			SingleChoiceListAdapter<CycleSpecializationYear> yearAdapter = new SingleChoiceListAdapter<>(getContext(),
					cycleSpecializationYears);

			new AlertDialog.Builder(getContext(), R.style.DialogTheme)
					.setTitle("Select year")
					.setAdapter(yearAdapter, (dialog1, which1) -> {
						SingleChoiceListAdapter<String> groupAdapter = new SingleChoiceListAdapter<>(getContext(),
								yearAdapter.getItem(which1).getGroups());

						new AlertDialog.Builder(getContext(), R.style.DialogTheme)
								.setTitle("Select group")
								.setAdapter(groupAdapter, (dialog2, which2) -> {
									addGroup(groupAdapter.getItem(which2));
								})
								.create()
								.show();
					})
					.create()
					.show();
		}
	}
}
