package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.databinding.LaboratoryInputActivityLayoutBinding;
import com.lonn.studentassistant.logging.Logger;

import java.util.UUID;

import static com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel.builder;
import static java.util.UUID.randomUUID;

public class LaboratoryInputActivity extends FirebaseConnectedActivity {
	private static final Logger LOGGER = Logger.ofClass(LaboratoryInputActivity.class);
	LaboratoryInputActivityLayoutBinding binding;
	private String courseKey;
	private String courseName;

	public void save(View view) {
		int weekNumber;
		String title = ((EditText) findViewById(R.id.laboratoryTitleEditText)).getText().toString();
		String description = ((EditText) findViewById(R.id.laboratoryDescriptionEditText)).getText().toString();

		try {
			weekNumber = Integer.parseInt(((EditText) findViewById(R.id.laboratoryWeekEditText)).getText().toString());
		}
		catch (NumberFormatException exception) {
			showSnackBar("Invalid week number. It must be from 1 to 16", 2000);
			return;
		}

		UUID laboratoryUUID = randomUUID();

		showSnackBar("Saving laboratory...");

		firebaseApi.getLaboratoryService()
				.getByCourseKeyAndWeek(courseKey, weekNumber, false)
				.onSuccess(laboratory -> {
					if (laboratory != null) {
						showSnackBar("A laboratory for this discipline for week " + weekNumber + " already exists!",
								1000);
						return;
					}

					firebaseApi.getLaboratoryService()
							.saveAndLinkLaboratory(builder()
									.courseKey(courseKey)
									.description(description)
									.title(title)
									.weekNumber(weekNumber)
									.build()
									.setKey(laboratoryUUID.toString()))
							.onSuccess(n -> {
								showSnackBar("Successfully added a laboratory to " + courseName, 1000);
								executeWithDelay(this::onBackPressed, 1250);
							});

				})
				.onError(error -> logAndShowErrorSnack("An error occurred while creating the laboratory!",
						error,
						LOGGER));
	}

	public void cancel(View view) {
		super.onBackPressed();
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.laboratory_input_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		courseName = getIntent().getStringExtra("courseName");
		courseKey = getIntent().getStringExtra("courseKey");

		binding.setCourseName(courseName);
	}
}
