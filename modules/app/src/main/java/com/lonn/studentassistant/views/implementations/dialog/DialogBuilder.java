package com.lonn.studentassistant.views.implementations.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.functionalIntefaces.Function;

public class DialogBuilder<T> {
	private AlertDialog.Builder builder;
	private T[] items;
	private Consumer<T>[] itemActions;
	private Consumer<T> globalItemAction;
	private Function<T, String> toString;

	public DialogBuilder(Activity activity) {
		builder = new AlertDialog.Builder(activity);
	}

	public DialogBuilder<T> withItems(T... items) {
		this.items = items;
		return this;
	}

	public DialogBuilder<T> withItemActions(Consumer<T>... itemActions) {
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
		String[] itemStrings = new String[items.length];

		for (int i = 0; i < items.length; i++) {
			String itemString;

			if (toString != null) {
				itemString = toString.apply(items[i]);
			}
			else {
				itemString = items[i].toString();
			}

			itemStrings[i] = itemString;
		}

		builder.setItems(itemStrings, (dialog, which) -> {
			if (itemActions != null &&
					itemActions.length > which &&
					itemActions[which] != null) {
				itemActions[which].consume(items[which]);
			}
			else if (globalItemAction != null) {
				globalItemAction.consume(items[which]);
			}
		});

		builder.setNegativeButton("Cancel", (dialog, which) -> {
			dialog.dismiss();
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
