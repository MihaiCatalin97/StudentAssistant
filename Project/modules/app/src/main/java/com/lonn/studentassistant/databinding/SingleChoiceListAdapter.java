package com.lonn.studentassistant.databinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lonn.studentassistant.R;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class SingleChoiceListAdapter<T> extends ArrayAdapter<T> {
	private List<T> items;

	public SingleChoiceListAdapter(Context context, List<T> items) {
		super(context, R.layout.dialog_item_text, items);
		this.items = new ArrayList<>(items);
	}

	public SingleChoiceListAdapter(Context context, T[] items) {
		super(context, R.layout.dialog_item_text, items);
		this.items = asList(items);
	}

	@Override
	public T getItem(int position) {
		return items.get(position);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		final T dataModel = getItem(position);
		View result;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.dialog_item_text, parent, false);

			result = convertView;
		}
		else {

			result = convertView;
		}

		((TextView) convertView.findViewById(R.id.dialog_item_text)).setText(dataModel.toString());

		return result;
	}
}
