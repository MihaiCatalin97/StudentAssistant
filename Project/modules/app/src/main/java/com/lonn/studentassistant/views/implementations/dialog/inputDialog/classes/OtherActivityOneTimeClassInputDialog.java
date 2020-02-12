package com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.abstractions.OneTimeClassInputDialog;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.CONSULTATIONS;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.COURSE;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.LABORATORY;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.SCIENTIFIC_CLASS;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.SEMINAR;

public class OtherActivityOneTimeClassInputDialog extends OneTimeClassInputDialog<OtherActivityViewModel> {

	public OtherActivityOneTimeClassInputDialog(Context context, OtherActivityViewModel otherActivity,
												List<ProfessorViewModel> activityProfessors) {
		super(context, otherActivity, activityProfessors);
		setAvailableScheduleClassTypes(new ScheduleClassType[]{CONSULTATIONS, SCIENTIFIC_CLASS, OTHER_ACTIVITIES});
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