package com.lonn.studentassistant.activities.abstractions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.FirebaseApi;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.viewModels.ScrollViewEntityViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.widget.Toast.makeText;

public abstract class FirebaseConnectedActivity extends AppCompatActivity {
	private static final Logger LOGGER = Logger.ofClass(FirebaseConnectedActivity.class);
	protected FirebaseApi firebaseApi;
	protected Handler delayHandler = new Handler();
	protected Handler viewTreeHandler = new Handler();

	public FirebaseApi getFirebaseApi() {
		return firebaseApi;
	}

	public void showSnackBar(String message) {
		showSnackBar(message, Snackbar.LENGTH_INDEFINITE);
	}

	public void showSnackBar(String message, int length) {
		Snackbar snackbar;

		snackbar = Snackbar.make(findViewById(android.R.id.content), message, length);
		((TextView)snackbar.getView().findViewById(R.id.snackbar_text))
				.setMaxLines(5);

		snackbar.show();
	}

	public boolean tapScrollViewEntityLayout(View v) {
		ViewGroup parent = (ViewGroup) v.getParent();

		while (parent != null && !(parent instanceof EntityView)) {
			parent = (ViewGroup) parent.getParent();
		}

		String entityKey = null;
		Class entityActivityClass = null;
		ScrollViewEntityViewModel model;

		if (parent != null) {
			model = ((EntityView) parent).getModel();

			if (model != null) {
				entityActivityClass = ((EntityView) parent).getModel().getEntityActivityClass();
				entityKey = ((EntityView) parent).getEntityViewModel().getKey();
			}
		}

		if (entityActivityClass != null &&
				entityKey != null) {
			Context context = v.getContext();

			if (hasSuperclass(entityActivityClass, EntityActivity.class)) {
				startEntityActivity(context,
						entityKey,
						entityActivityClass);

				return true;
			}
		}

		return false;
	}

	public void tapRemove(View v) {
		ViewGroup parent = (ViewGroup) v.getParent();

		while (parent != null && !(parent instanceof EntityView)) {
			parent = (ViewGroup) parent.getParent();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firebaseApi = FirebaseApi.getApi(getBaseContext());
		inflateLayout();
	}

	protected abstract void inflateLayout();

	protected void executeWithDelay(Runnable runnable, long delay) {
		delayHandler.postDelayed(runnable, delay);
	}

	protected void hideKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		View view = getCurrentFocus();

		if (view == null) {
			view = new View(this);
		}

		if (inputMethodManager != null) {
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public void logAndShowErrorSnack(String errorMessage, Exception exception, Logger logger) {
		String completeErrorMessage = errorMessage;

		if (exception != null) {
			if (exception.getMessage() != null) {
				completeErrorMessage += ":\n" + exception.getMessage();
			}

			logger.error(errorMessage, exception);
		}

		showSnackBar(completeErrorMessage, 3000);
	}

	private void startEntityActivity(Context context,
									 String entityKey,
									 Class activityClass) {
		Intent intent = new Intent(getBaseContext(), activityClass);

		intent.putExtra("entityKey", entityKey);

		context.startActivity(intent);
	}

	private boolean hasSuperclass(Class classToTest, Class superclass) {
		do {
			if (classToTest.equals(superclass)) {
				return true;
			}

			classToTest = classToTest.getSuperclass();
		} while (classToTest != null);

		return false;
	}

	@NoArgsConstructor
	@Accessors(chain = true)
	protected class DialogButtonFunctionality {
		@Getter
		@Setter
		private String title;
		@Getter
		@Setter
		private Runnable onClick;
	}

	public void logAndShowErrorToast(String errorMessage, Exception exception, Logger logger) {
		String completeErrorMessage = errorMessage;

		if (exception != null) {
			if (exception.getMessage() != null) {
				completeErrorMessage += ":\n" + exception.getMessage();
			}

			LOGGER.error(errorMessage, exception);
		}

		showSnackBar(completeErrorMessage, 2000);
	}

	public void tapLink(View view) {
		final String link = ((EditText) view).getText().toString();

		if (!view.isFocusable()) {
			new AlertDialog.Builder(this, R.style.DialogTheme)
					.setTitle("Confirm navigation to link")
					.setMessage("Leave the application and visit " + link + "?")
					.setPositiveButton("Visit link", (dialog, which) -> {
						dialog.dismiss();
						openBrowser(link);
					})
					.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
					.create()
					.show();
		}
		else {
			tapEditText(view);
		}
	}

	private void openBrowser(String link) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
		browserIntent.setData(Uri.parse(link));
		startActivity(browserIntent);
	}

	public void copyToClipboard(View view) {
		final String text = ((EditText) view).getText().toString();

		if (!view.isFocusable()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

			if (clipboard != null) {
				ClipData clip = ClipData.newPlainText(text, text);
				clipboard.setPrimaryClip(clip);
				showSnackBar("Copied to clipboard", 1000);
			}
		}
		else {
			tapEditText(view);
		}
	}

	public void tapEditText(View view) {
		view.requestFocusFromTouch();

		InputMethodManager lManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		if (lManager != null) {
			lManager.showSoftInput(view, 0);
		}
	}
}
