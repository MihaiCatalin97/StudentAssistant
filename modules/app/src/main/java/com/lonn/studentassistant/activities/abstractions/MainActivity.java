package com.lonn.studentassistant.activities.abstractions;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Person;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.ProfileImageUploadDialog;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lonn.studentassistant.utils.Utils.getVisibleChildren;
import static com.lonn.studentassistant.utils.Utils.hideViews;

public abstract class MainActivity<T extends EntityViewModel<? extends Person>> extends NavBarActivity<T> {
	protected ProfileImageUploadDialog imageUploadDialog;
	protected String personId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getExtras() != null) {
			personId = getIntent().getExtras().getString("personId");
			entityKey = personId;
		}

		imageUploadDialog = getImageUploadDialog();

		findViewById(R.id.imageUploadButton).setOnClickListener((v) -> {
			imageUploadDialog.show();
		});

		findViewById(R.id.imageDeleteButton).setOnClickListener((v) -> {
			new AlertDialog.Builder(this, R.style.DialogTheme)
					.setTitle("Delete image")
					.setMessage("Are you sure you want to delete this image?\nThis action cannot be undone.")
					.setPositiveButton("Delete", (dialog, which) -> {
						deleteProfileImage();
						dialog.dismiss();
					})
					.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
					.create()
					.show();
		});

		executeWithDelay(this::hideLoadingScreen, 1000);
	}

	public void handleNavBarAction(int id) {
		if (id == R.id.nav_home) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutHome).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_profile) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutProfile).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_schedule) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutSchedule).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_grades) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutGrades).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_messages) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutMessages).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_professors) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutProfessors).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_courses) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutCourses).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_administrative) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutAdministrative).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_otherActivities) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutOtherActivities).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_registration) {
			hideViews(getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutRegistrationTokens).setVisibility(VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		if (findViewById(R.id.layoutHome).getVisibility() == VISIBLE) {
			super.onBackPressed();
		}
		else {
			handleNavBarAction(R.id.nav_home);
		}
	}

	public void hideLoadingScreen() {
		executeWhenLayoutSettles(() -> {
			View loadingScreen = findViewById(R.id.loadingScreen);

			if (loadingScreen != null) {
				AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
				fadeOut.setFillBefore(true);
				fadeOut.setFillAfter(true);
				fadeOut.setInterpolator(new LinearInterpolator());
				fadeOut.setDuration(750);

				loadingScreen.startAnimation(fadeOut);
				executeWithDelay(() -> {
					loadingScreen.setVisibility(GONE);
					loadingScreen.setClickable(false);
					loadingScreen.setFocusable(false);
				}, 800);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		imageUploadDialog.setFile(requestCode, resultCode, data);
	}

	protected abstract ProfileImageUploadDialog getImageUploadDialog();

	protected abstract void deleteProfileImage();
}
