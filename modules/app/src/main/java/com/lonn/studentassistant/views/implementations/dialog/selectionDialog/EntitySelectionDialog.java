package com.lonn.studentassistant.views.implementations.dialog.selectionDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.SelectableListAdapter;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public abstract class EntitySelectionDialog<T extends EntityViewModel<? extends BaseEntity>> extends Dialog {
	@Getter
	private Button positiveButton, negativeButton;
	@Getter
	private EditText inputEditText;
	@Getter
	private ListView dialogListView;
	@Getter
	private TextView dialogTitle;
	@Getter(AccessLevel.PROTECTED)
	private CheckBox dialogSelectAll;
	@Setter
	private Consumer<List<T>> positiveButtonAction;
	@Getter(AccessLevel.PROTECTED)
	private SelectableListAdapter<T> listAdapter;
	@Setter
	private String title;
	@Setter
	private List<T> items;
	@Setter
	private String inputHint;

	public EntitySelectionDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.entity_select_dialog);

		positiveButton = findViewById(R.id.buttonPositive);
		negativeButton = findViewById(R.id.buttonNegative);
		inputEditText = findViewById(R.id.dialogEditTextInput);
		dialogListView = findViewById(R.id.dialogListView);
		dialogTitle = findViewById(R.id.dialogTitle);
		dialogSelectAll = findViewById(R.id.dialogSelectAllCheckbox);

		if (title == null) {
			dialogTitle.setVisibility(View.GONE);
		}
		else {
			dialogTitle.setText(title);
		}

		inputEditText.setHint(inputHint);
		inputEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				listAdapter.setStringToMatch(s.toString());
				setSelectedItems(false);
			}
		});

		final EntitySelectionDialog<T> thisDialog = this;

		listAdapter = new SelectableListAdapter<T>(items, this) {
			@Override
			protected String entityToDisplayMainString(T entity) {
				return thisDialog.entityToDisplayMainString(entity);
			}

			@Override
			protected String entityToDisplaySecondaryString(T entity) {
				return thisDialog.entityToDisplaySecondaryString(entity);
			}
		};

		dialogListView.setAdapter(listAdapter);

		negativeButton.setOnClickListener((view) -> this.dismiss());
		positiveButton.setOnClickListener((view) -> {
			if (listAdapter.getSelectedItems().size() != 0) {
				positiveButtonAction.consume(listAdapter.getSelectedItems());
			}

			dismiss();
		});

		dialogSelectAll.setOnClickListener(view -> {
			listAdapter.selectAll(dialogSelectAll.isChecked());
			setSelectedItems(dialogSelectAll.isChecked());
		});
	}

	public abstract String entityToDisplayMainString(T entity);

	public abstract String entityToDisplaySecondaryString(T entity);

	public void setSelectedCount(int selected) {
		((TextView) findViewById(R.id.dialogSelectedCount)).setText(selected + " selected");
		dialogSelectAll.setChecked(listAdapter != null && selected == listAdapter.getCount());
	}

	protected void setSelectedItems(boolean selected) {
		for (int i = 0; i < getDialogListView().getChildCount(); i++) {
			((CheckBox) getDialogListView().getChildAt(i).findViewById(R.id.dialog_item_checkbox)).setChecked(selected);
		}
	}
}
