package com.lonn.studentassistant.views.implementations.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import java.util.LinkedList;
import java.util.List;

public class DialogBuilder extends AlertDialog.Builder {
	private List<Integer> checkedItems = new LinkedList<>();

	public DialogBuilder(Context context, final List<BaseEntity> entities, String title, String positiveButtonText) {
		super(context, R.style.DialogTheme);

		String[] titles = new String[entities.size()];

		for (int i = 0; i < entities.size(); i++) {
			titles[i] = entities.get(i).toString();
		}

		setTitle(title);
		setMultiChoiceItems(titles, null,
				(dialog, which, isChecked) -> {
					if (isChecked) {
						checkedItems.add(which);
					}
					else {
						checkedItems.remove(Integer.valueOf(which));
					}
				}
		);

		setPositiveButton(positiveButtonText, (dialog, which) -> {
		});

		setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
	}

	public void showDialog() {
		final AlertDialog dialog = create();

		dialog.setOnShowListener(arg0 -> {
			Window dialogWindow = dialog.getWindow();

			dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

			if (dialogWindow != null) {
				dialogWindow.setBackgroundDrawableResource(R.drawable.dark_stroke_solid_background);
			}
		});

		dialog.show();
	}
}
