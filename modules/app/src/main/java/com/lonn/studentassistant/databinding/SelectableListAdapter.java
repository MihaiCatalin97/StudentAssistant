package com.lonn.studentassistant.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.functionalIntefaces.Predicate;
import com.lonn.studentassistant.views.implementations.dialog.selectionDialog.EntitySelectionDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public abstract class SelectableListAdapter<T extends EntityViewModel<? extends BaseEntity>> extends ArrayAdapter<T> {
	private List<T> fullDataSet;
	private List<T> visibleDataSet;
	@Getter
	private List<T> selectedItems;
	private List<Predicate<T>> predicates;
	private String stringToMatch;
	private EntitySelectionDialog<T> dialog;

	public SelectableListAdapter(List<T> fullDataSet,
								 EntitySelectionDialog<T> dialog) {
		super(dialog.getContext(), R.layout.dialog_item_text, fullDataSet);
		this.dialog = dialog;
		this.fullDataSet = fullDataSet;
		this.visibleDataSet = new ArrayList<>(fullDataSet);

		selectedItems = new LinkedList<>();
		predicates = new ArrayList<>();
		dialog.setSelectedCount(selectedItems.size());
	}

	public void setPredicates(List<Predicate<T>> predicates) {
		this.predicates = predicates;
		filterDataSet();
	}

	public void setStringToMatch(String stringToMatch) {
		this.stringToMatch = stringToMatch;
		filterDataSet();
	}

	private void filterDataSet() {
		selectedItems.clear();
		dialog.setSelectedCount(selectedItems.size());
		visibleDataSet.clear();

		Predicate<T> searchPredicate = entity -> stringToMatch == null || stringToMatch.length() == 0 ||
				(entityToFilterString(entity).contains(stringToMatch));

		for (T entity : fullDataSet) {
			if (searchPredicate.test(entity)) {
				boolean shouldContain = true;

				for (Predicate<T> predicate : predicates) {
					if (!predicate.test(entity)) {
						shouldContain = false;
						break;
					}
				}

				if (shouldContain) {
					visibleDataSet.add(entity);
				}
			}
		}

		notifyDataSetChanged();
	}

	@Override
	public T getItem(int position) {
		return visibleDataSet.get(position);
	}

	@Override
	public int getCount() {
		return visibleDataSet.size();
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		final T dataModel = getItem(position);
		View result;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.dialog_item_selectable, parent, false);

			result = convertView;
		}
		else {

			result = convertView;
		}

		((TextView) convertView.findViewById(R.id.dialog_item_text)).setText(entityToDisplayMainString(dataModel));
		((TextView) convertView.findViewById(R.id.dialog_item_secondary_text)).setText(entityToDisplaySecondaryString(dataModel));
		CheckBox checkBox = convertView.findViewById(R.id.dialog_item_checkbox);
		checkBox.setChecked(selectedItems.contains(dataModel));

		View.OnClickListener checkBoxListener = view -> {
			if (checkBox.isChecked()) {
				selectedItems.remove(dataModel);
			}
			else {
				selectedItems.add(dataModel);
			}

			checkBox.setChecked(!checkBox.isChecked());
			dialog.setSelectedCount(selectedItems.size());
		};

		convertView.setOnClickListener(checkBoxListener);
		checkBox.setClickable(false);

		if (parent.getMeasuredWidth() != 0 &&
				parent.getMeasuredWidth() != result.getMeasuredWidth()) {
			ViewGroup.LayoutParams params = result.getLayoutParams();
			params.width = parent.getMeasuredWidth();
			result.setLayoutParams(params);
		}

		return result;
	}

	protected abstract String entityToDisplayMainString(T entity);

	protected abstract String entityToDisplaySecondaryString(T entity);

	private String entityToFilterString(T entity) {
		return (entityToDisplayMainString(entity) + entityToDisplaySecondaryString(entity)).toLowerCase();
	}

	public void selectAll(boolean selectAll) {
		selectedItems.clear();

		if (selectAll) {
			selectedItems.addAll(visibleDataSet);
		}

		dialog.setSelectedCount(selectedItems.size());
	}
}
