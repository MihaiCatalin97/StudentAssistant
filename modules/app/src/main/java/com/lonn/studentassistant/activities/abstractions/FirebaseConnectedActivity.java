package com.lonn.studentassistant.activities.abstractions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.viewModels.ScrollViewEntityViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public abstract class FirebaseConnectedActivity extends AppCompatActivity {
	protected FirebaseApi firebaseApi;
	private Handler handler = new Handler();

	public FirebaseApi getFirebaseApi() {
		return firebaseApi;
	}

	public void showSnackBar(String message) {
		showSnackBar(message, Snackbar.LENGTH_INDEFINITE);
	}

	public void showSnackBar(String message, int length) {
		Snackbar snackbar;

		snackbar = Snackbar.make(findViewById(android.R.id.content), message, length);

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
		handler.postDelayed(runnable, delay);
	}

	protected void hideKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

		View view = getCurrentFocus();

		if (view == null) {
			view = new View(this);
		}

		if (inputMethodManager != null) {
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public void logAndShowErrorSnack(String errorMessage, Exception exception, Logger logger) {
		logger.error(errorMessage, exception);
		showSnackBar(errorMessage, 1000);
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
		logger.error(errorMessage, exception);
		makeText(this, errorMessage, LENGTH_SHORT).show();
	}
}
