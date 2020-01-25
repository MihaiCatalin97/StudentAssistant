package com.lonn.studentassistant.views.implementations.dialog.selectionDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.DialogFilterLayoutBinding;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DialogFilterView<T> extends LinearLayout {
	private DialogFilterLayoutBinding binding;
	private T[] values;
	private String[] valuesStrings;
	private Consumer<T> onSelect;

	public DialogFilterView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

		if (inflater != null) {
			binding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter_layout, this, true);
			binding.setSelectedValue("(none)");
		}

		setButtonOnClick();
	}

	public DialogFilterView<T> setTitle(String title) {
		binding.setTitle(title);
		return this;
	}

	public DialogFilterView<T> setOnSelect(Consumer<T> onSelect) {
		this.onSelect = onSelect;
		return this;
	}

	public DialogFilterView<T> setValues(T[] values) {
		this.values = values;
		this.valuesStrings = valuesToString();
		return this;
	}

	private void setButtonOnClick() {
		binding.setOnClick(view -> {
			new AlertDialog.Builder(getContext(), R.style.DialogTheme)
					.setTitle("Select " + binding.getTitle())
					.setItems(valuesStrings, (dialog, which) -> {
						if (which < values.length) {
							onSelect.consume(values[which]);
						}
						else {
							onSelect.consume(null);
						}

						binding.setSelectedValue(valuesStrings[which]);
					})
					.setNegativeButton("Cancel", null)
					.create()
					.show();
		});
	}

	private String[] valuesToString() {
		String[] result = new String[values.length + 1];

		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].toString();
		}

		result[result.length - 1] = "(none)";

		return result;
	}
}
