package com.lonn.studentassistant.views.implementations.dialog.inputDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.enums.GradeType;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.EXAM;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.EXAM_ARREARS;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.LABORATORY;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PARTIAL_ARREARS;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PARTIAL_EXAM;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PROJECT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PROJECT_ARREARS;

@Accessors(chain = true)
public class GradeInputDialog extends Dialog {
	@Getter
	private Button positiveButton, negativeButton;
	@Getter
	private EditText studentIdEditText, gradeEditText;
	@Setter
	private Runnable positiveButtonAction;
	@Setter
	private GradeType selectedGradeType;
	@Setter
	private GradeType[] availableGradeTypes;
	@Setter
	private int laboratoryNumber;
	private Map<GradeType, RadioButton> radioButtonMap = new HashMap<>();

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
		positiveButton.setOnClickListener((view) -> positiveButtonAction.run());
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
	}
}
