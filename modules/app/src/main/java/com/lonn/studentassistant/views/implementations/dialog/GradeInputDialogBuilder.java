package com.lonn.studentassistant.views.implementations.dialog;

import android.app.Activity;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GradeInputDialogBuilder {
	private GradeInputDialog gradeInputDialog;
	private Activity activity;

	public GradeInputDialogBuilder(Activity activity) {
		gradeInputDialog = new GradeInputDialog(activity);
		this.activity = activity;
	}

	public GradeInputDialogBuilder positiveButtonAction(Runnable positiveButtonAction) {
		gradeInputDialog.setPositiveButtonAction(positiveButtonAction);
		return this;
	}

	public void show() {
		gradeInputDialog.show();

		int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

		if (gradeInputDialog.getWindow() != null) {
			gradeInputDialog.getWindow().setLayout(width, WRAP_CONTENT);
		}
	}

	public GradeInputDialog create() {
		return gradeInputDialog;
	}
}
