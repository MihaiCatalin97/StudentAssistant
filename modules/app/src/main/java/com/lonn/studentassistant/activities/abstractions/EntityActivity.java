package com.lonn.studentassistant.activities.abstractions;

import android.os.Bundle;

import androidx.core.app.NavUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public abstract class EntityActivity<T extends EntityViewModel<? extends BaseEntity>> extends FirebaseConnectedActivity {
	protected String entityKey;

	protected abstract void loadAll(String entityKey);

	@Override
	public void onStart() {
		super.onStart();

		loadAll(entityKey);

		checkAuthenticationAndSignOut();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entityKey = getIntent().getStringExtra("entityKey");
	}

	protected void checkAuthenticationAndSignOut() {
		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			FirebaseAuth.getInstance().signOut();
			NavUtils.navigateUpFromSameTask(this);
		}
	}
}
