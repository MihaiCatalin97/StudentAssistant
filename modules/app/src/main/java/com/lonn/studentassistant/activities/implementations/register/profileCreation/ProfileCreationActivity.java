package com.lonn.studentassistant.activities.implementations.register.profileCreation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.accountCreation.AccountCreationActivity;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Person;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.logging.Logger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class ProfileCreationActivity<T extends EntityViewModel<? extends Person>> extends FirebaseConnectedActivity {
	private static final Logger LOGGER = Logger.ofClass(ProfileCreationActivity.class);
	@Getter(AccessLevel.PROTECTED)
	private AccountType accountType;
	private String registrationToken;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private T personProfile;

	protected ProfileCreationActivity(AccountType accountType) {
		this.accountType = accountType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firebaseApi = FirebaseApi.getApi(this);
		inflateLayout();
		registrationToken = getIntent().getStringExtra("registrationToken");
	}

	public abstract void tapContinue(View view);

	protected abstract T mergeExistingProfile(T existingProfile, T newProfile);

	protected void startAccountCreationActivity(T personProfile) {
		Intent accountCreationActivityIntent = new Intent(this, getNextActivityClass());

		accountCreationActivityIntent.putExtra("personProfile", personProfile);
		accountCreationActivityIntent.putExtra("accountType", accountType.toString());
		accountCreationActivityIntent.putExtra("registrationToken", registrationToken);

		startActivity(accountCreationActivityIntent);
	}

	protected void checkIfPersonHasAccountAndStartActivity(T existingProfile) {
		firebaseApi.getUserService()
				.personHasAccount(existingProfile.getKey())
				.onSuccess(result -> {
					if (result) {
						showSnackBar("This student id already has an account associated to id!", 1000);
					}
					else {
						mergeExistingProfile(existingProfile, getPersonProfile());
						startAccountCreationActivity(existingProfile);
					}
				})
				.onError(error -> logAndShowErrorSnack("An error occurred, please try again later",
						error,
						LOGGER));
	}

	protected abstract Class<? extends AccountCreationActivity<T>> getNextActivityClass();
}
