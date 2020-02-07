package com.lonn.studentassistant.views.implementations.dialog.inputDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.GradeViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.EXAM_ARREARS;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.LABORATORY;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PARTIAL_ARREARS;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PARTIAL_EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PROJECT;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.PROJECT_ARREARS;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidStudentId;
import static java.lang.Double.parseDouble;
import static lombok.AccessLevel.PRIVATE;

@Accessors(chain = true)
public class GradeInputDialog extends Dialog {
	private Button positiveButton, negativeButton;
	private EditText studentIdEditText, gradeEditText;
	@Setter
	private Consumer<GradeParseResult> positiveButtonAction;
	@Setter
	private List<GradeType> availableGradeTypes = new ArrayList<>();
	private Map<GradeType, RadioButton> radioButtonMap = new HashMap<>();
	@Getter
	private GradeType selectedGradeType;

	public GradeInputDialog(Activity activity) {
		super(activity, R.style.DialogTheme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grade_input_dialog);

		positiveButton = findViewById(R.id.buttonPositive);
		negativeButton = findViewById(R.id.buttonNegative);
		studentIdEditText = findViewById(R.id.gradeInputDialogStudentId);
		gradeEditText = findViewById(R.id.gradeInputDialogGrade);

		negativeButton.setOnClickListener((view) -> this.dismiss());
		positiveButton.setOnClickListener((view) ->
				positiveButtonAction.consume(parseGrade())
		);

		setupRadioButtons();
	}

	@Override
	public void show() {
		int width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.90);

		if (getWindow() != null) {
			getWindow().setLayout(width, WRAP_CONTENT);
		}

		super.show();
	}

	private void setupRadioButtons() {
		radioButtonMap.put(LABORATORY, (RadioButton) findViewById(R.id.gradeInputDialogGradeTypeLaboratory));
		radioButtonMap.put(EXAM, (RadioButton) findViewById(R.id.gradeInputDialogGradeTypeExam));
		radioButtonMap.put(PARTIAL_EXAM, (RadioButton) findViewById(R.id.gradeInputDialogGradeTypePartialExam));
		radioButtonMap.put(PROJECT, (RadioButton) findViewById(R.id.gradeInputDialogGradeTypeProject));
		radioButtonMap.put(EXAM_ARREARS, (RadioButton) findViewById(R.id.gradeInputDialogGradeTypeExamArrears));
		radioButtonMap.put(PARTIAL_ARREARS, (RadioButton) findViewById(R.id.gradeInputDialogGradeTypePartialArrears));
		radioButtonMap.put(PROJECT_ARREARS, (RadioButton) findViewById(R.id.gradeInputDialogGradeTypeProjectArrears));

		for (GradeType gradeType : radioButtonMap.keySet()) {
			RadioButton radioButton = radioButtonMap.get(gradeType);

			if (radioButton != null) {
				if (availableGradeTypes.contains(gradeType)) {
					radioButton.setOnCheckedChangeListener((button, checked) -> {
						if (checked) {
							selectedGradeType = gradeType;
							deselectOtherButtons(button);
						}
					});
				}
				else {
					radioButton.setVisibility(View.GONE);
				}
			}
		}
	}

	private void deselectOtherButtons(CompoundButton radioButton) {
		for (RadioButton radioButton1 : radioButtonMap.values()) {
			if (!radioButton1.equals(radioButton)) {
				radioButton1.setChecked(false);
			}
		}
	}

	private GradeParseResult parseGrade() {
		String studentId = studentIdEditText.getText().toString();

		double grade = 0;
		GradeParseResult result = new GradeParseResult();
		String error = null;

		try {
			grade = parseDouble(gradeEditText.getText().toString());
		}
		catch (NumberFormatException exception) {
			error = "Invalid grade";
		}

		if (!isValidStudentId(studentId)) {
			error = "Invalid student ID";
		}

		if (selectedGradeType == null || !availableGradeTypes.contains(selectedGradeType)) {
			error = "Invalid grade type";
		}

		if (error != null) {
			return new GradeParseResult()
					.setError(error);
		}
		return new GradeParseResult()
				.setGrade(new GradeViewModel()
						.setGrade(grade)
						.setGradeType(selectedGradeType)
						.setDate(new Date())
						.setStudentId(studentId));
	}

	@Getter
	@Setter(PRIVATE)
	@NoArgsConstructor(access = PRIVATE)
	public class GradeParseResult {
		private String error;
		private GradeViewModel grade;
	}
}
