package com.lonn.studentassistant.activities.abstractions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.dialog.DialogBuilder;

import java.util.List;

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

	public void tapScrollViewEntityLayout(View v) {
		ViewGroup parent = (ViewGroup) v.getParent();

		while (parent != null && !(parent instanceof EntityView)) {
			parent = (ViewGroup) parent.getParent();
		}

		Class entityActivityClass = null;

		if (parent != null &&
				((EntityView) parent).getModel() != null) {
			entityActivityClass = ((EntityView) parent).getModel().getEntityActivityClass();

		}

		if (entityActivityClass != null) {
			Intent intent = new Intent(getBaseContext(), entityActivityClass);

			intent.putExtra("entityKey", ((EntityView) parent).getEntityViewModel()
					.getKey());

			v.getContext().startActivity(intent);
		}
	}

	public void tapRemove(View v) {
		ViewGroup parent = (ViewGroup) v.getParent();

		while (parent != null && !(parent instanceof EntityView)) {
			parent = (ViewGroup) parent.getParent();
		}
	}

	public void showDialog(List<BaseEntity> entities, String title, String positiveButtonText) {
		DialogBuilder builder = new DialogBuilder(FirebaseConnectedActivity.this, entities, title, positiveButtonText);
		builder.showDialog();
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

	public void logAndShowError(String errorMessage, Exception exception, Logger logger) {
		logger.error(errorMessage, exception);
		showSnackBar(errorMessage, 1000);
	}
}
