package com.lonn.studentassistant.views.implementations.dialog.inputDialog;

import android.app.Activity;
import android.app.AlertDialog;

import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.functionalIntefaces.Function;

import java.util.List;

public class DialogBuilder<T> {
	private AlertDialog.Builder builder;
	private List<T> items;
	private List<Consumer<T>> itemActions;
	private Consumer<T> globalItemAction;
	private Function<T, String> toString;

	public DialogBuilder(Activity activity) {
		builder = new AlertDialog.Builder(activity);
	}

	public DialogBuilder<T> withItems(List<T> items) {
		this.items = items;
		return this;
	}

	public DialogBuilder<T> withItemActions(List<Consumer<T>> itemActions) {
		this.itemActions = itemActions;
		return this;
	}

	public DialogBuilder<T> withTitle(String title) {
		builder.setTitle(title);
		return this;
	}

	public DialogBuilder<T> withGlobalItemAction(Consumer<T> itemAction) {
		this.globalItemAction = itemAction;
		return this;
	}

	public DialogBuilder<T> withItemToString(Function<T, String> toString) {
		this.toString = toString;
		return this;
	}

	public void show() {
		String[] itemStrings = new String[items.size()];

		for (int i = 0; i < items.size(); i++) {
			String itemString;

			if (toString != null) {
				itemString = toString.apply(items.get(i));
			}
			else {
				itemString = items.get(i).toString();
			}

			itemStrings[i] = itemString;
		}

		builder.setItems(itemStrings, (dialog, which) -> {
			if (itemActions != null &&
					itemActions.size() > which &&
					itemActions.get(which) != null) {
				itemActions.get(which).consume(items.get(which));
			}
			else if (globalItemAction != null) {
				globalItemAction.consume(items.get(which));
			}
		});

		builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
