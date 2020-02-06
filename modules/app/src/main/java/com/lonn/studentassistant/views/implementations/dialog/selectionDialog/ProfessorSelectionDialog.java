package com.lonn.studentassistant.views.implementations.dialog.selectionDialog;

import android.content.Context;
import android.os.Bundle;

import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.utils.Utils.displayWidth;

public class ProfessorSelectionDialog extends EntitySelectionDialog<ProfessorViewModel> {
	public ProfessorSelectionDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getWindow() != null) {
			getWindow().setLayout((int) (displayWidth * 0.95), WRAP_CONTENT);
		}
	}

	public String entityToDisplayMainString(ProfessorViewModel professor) {
		return professor.getFullName();
	}

	public String entityToDisplaySecondaryString(ProfessorViewModel professor) {
		return null;
	}
}
