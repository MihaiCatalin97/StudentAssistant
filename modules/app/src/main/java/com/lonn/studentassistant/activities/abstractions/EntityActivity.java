package com.lonn.studentassistant.activities.abstractions;

import android.os.Bundle;

import androidx.core.app.NavUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public abstract class EntityActivity<T extends EntityViewModel<? extends BaseEntity>> extends FirebaseConnectedActivity {
	protected String entityKey;

	protected abstract void loadAll(String entityKey);

	@Override
	public void onStart() {
		super.onStart();

		checkAuthenticationAndSignOut();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entityKey = getIntent().getStringExtra("entityKey");

		findViewById(R.id.fab_edit).setOnClickListener(view -> {
			onEditTapped();
		});

		findViewById(R.id.fab_delete).setOnClickListener(view -> {
			onDeleteTapped();
		});
	}

	protected void checkAuthenticationAndSignOut() {
		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			FirebaseAuth.getInstance().signOut();
			NavUtils.navigateUpFromSameTask(this);
		}
	}

	protected abstract void onEditTapped();

	protected abstract void onDeleteTapped();
}
