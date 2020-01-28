package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions.RecurringClassInputDialog;

import java.util.List;

public class OtherActivityRecurringClassInputDialog extends RecurringClassInputDialog<OtherActivityViewModel> {

	public OtherActivityRecurringClassInputDialog(Context context, OtherActivityViewModel otherActivity,
												  List<ProfessorViewModel> activityProfessors) {
		super(context, otherActivity, activityProfessors);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		findViewById(R.id.classDialogGroupSelectionLayout).setVisibility(View.GONE);
		findViewById(R.id.classDialogGroups).setVisibility(View.GONE);
	}

	protected void showGroupSelectionDialog() {
	}
}