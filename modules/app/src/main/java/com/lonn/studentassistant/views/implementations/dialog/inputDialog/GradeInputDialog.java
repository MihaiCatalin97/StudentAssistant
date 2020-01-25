package com.lonn.studentassistant.views.implementations.dialog.inputDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.lonn.studentassistant.R;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class GradeInputDialog extends Dialog {
	@Getter
	private Button positiveButton, negativeButton;
	@Getter
	private EditText studentIdEditText, gradeEditText;
	@Setter
	private Runnable positiveButtonAction;

	public GradeInputDialog(Activity activity) {
		super(activity);
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
}
