package com.lonn.studentassistant.activities.abstractions;

import android.content.Intent;

import androidx.core.app.NavUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public abstract class EntityActivity<T extends EntityViewModel<? extends BaseEntity>> extends FirebaseConnectedActivity {
	protected abstract void loadAll();

	@Override
	public void onStart() {
		super.onStart();

		loadAll();

		checkAuthenticationAndSignOut();
	}

	public T getEntityFromIntent(Intent intent) {
		return (T) intent.getSerializableExtra("entityViewModel");
	}

	protected void checkAuthenticationAndSignOut() {
		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			FirebaseAuth.getInstance().signOut();
			NavUtils.navigateUpFromSameTask(this);
		}
	}
}
