package com.lonn.studentassistant.activities.implementations.student;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.Utils;

import static android.view.View.VISIBLE;

public class StudentActivity extends NavBarActivity {
	private static final Logger LOGGER = Logger.ofClass(StudentActivity.class);
	StudentActivityMainLayoutBinding binding;

	private StudentActivityFirebaseDispatcher dispatcher;

	@Override
	public void onStart() {
		super.onStart();
	}

	public void handleNavBarAction(int id) {
		if (id == R.id.nav_home) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutHome).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_profile) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutProfile).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_schedule) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutSchedule).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_grades) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutGrades).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_messages) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutMessages).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_professors) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutProfessors).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_courses) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutCourses).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_administrative) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutAdministrative).setVisibility(VISIBLE);
		}
		else if (id == R.id.nav_otherActivities) {
			Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
			findViewById(R.id.layoutOtherActivities).setVisibility(VISIBLE);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String studentKey = null;

		if(getIntent().getExtras() != null){
			 studentKey = getIntent().getExtras().getString("personId");
		}

		dispatcher.loadAll(studentKey);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.student_activity_main_layout);
		dispatcher = new StudentActivityFirebaseDispatcher(this);
	}
}
