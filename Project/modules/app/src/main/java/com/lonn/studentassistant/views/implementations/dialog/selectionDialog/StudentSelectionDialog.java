package com.lonn.studentassistant.views.implementations.dialog.selectionDialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.functionalIntefaces.Predicate;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.utils.Utils.displayWidth;

public class StudentSelectionDialog extends EntitySelectionDialog<StudentViewModel> {
	private CycleSpecialization selectedCycleSpecialization;
	private Integer selectedYear;

	public StudentSelectionDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getWindow() != null) {
			getWindow().setLayout((int) (displayWidth * 0.95), WRAP_CONTENT);
		}

		createFilterLayouts();
		setFilters();
	}

	public String entityToDisplayMainString(StudentViewModel student) {
		if (student.getFirstName() != null && student.getLastName() != null) {
			return student.getFirstName() + " " + student.getLastName();
		}
		return "(unregistered student)";
	}

	public String entityToDisplaySecondaryString(StudentViewModel student) {
		if (student.getCycleSpecializationYear() != null) {
			return student.getStudentId() + "\n" + student.getCycleSpecializationYear().toString() + "\n" +
					"Year " + student.getCycleSpecializationYear().getYear();
		}

		return student.getStudentId();
	}

	private void createFilterLayouts() {
		((ViewGroup) findViewById(R.id.dialogFilterLayout)).addView(createSpecializationFilterLayout());
		((ViewGroup) findViewById(R.id.dialogFilterLayout)).addView(createYearFilterLayout());
	}

	private View createSpecializationFilterLayout() {
		DialogFilterView<CycleSpecialization> dialogFilter = new DialogFilterView<>(getContext());

		dialogFilter.setTitle("Specialization")
				.setValues(CycleSpecialization.values())
				.setOnSelect(specialization -> {
					selectedCycleSpecialization = specialization;
					setFilters();
				});

		return dialogFilter;
	}

	private View createYearFilterLayout() {
		DialogFilterView<Integer> dialogFilter = new DialogFilterView<>(getContext());

		dialogFilter.setTitle("Year")
				.setValues(new Integer[]{1, 2, 3})
				.setOnSelect(year -> {
					selectedYear = year;
					setFilters();
				});

		return dialogFilter;
	}

	private void setFilters() {
		List<Predicate<StudentViewModel>> predicates = new ArrayList<>();

		if (selectedCycleSpecialization != null) {
			predicates.add(student -> student.getCycleSpecializationYear()
					.getCycleSpecialization()
					.equals(selectedCycleSpecialization));
		}

		if (selectedYear != null) {
			predicates.add(student -> student.getCycleSpecializationYear().getYear() == selectedYear);
		}

		setSelectedItems(false);
		getListAdapter().setPredicates(predicates);
	}
}
