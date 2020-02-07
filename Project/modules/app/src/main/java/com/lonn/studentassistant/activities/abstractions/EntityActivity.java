package com.lonn.studentassistant.activities.abstractions;

import android.content.Context;
import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.databinding.ViewDataBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import lombok.Getter;

import static com.lonn.studentassistant.BR.editing;
import static com.lonn.studentassistant.BR.editingProfile;
import static com.lonn.studentassistant.BR.entity;

public abstract class EntityActivity<T extends EntityViewModel<? extends BaseEntity>> extends FirebaseConnectedActivity {
	protected String entityKey;
	@Getter
	protected T activityEntity;
	protected boolean isEditing = false;

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

		findViewById(R.id.fabEdit).setOnClickListener(view -> onEditTapped());

		findViewById(R.id.fabDelete).setOnClickListener(view -> onDeleteTapped(view.getContext()));

		findViewById(R.id.fabDiscardChanges).setOnClickListener(view -> onDiscardTapped());

		findViewById(R.id.fabSaveChanges).setOnClickListener(view -> onSaveTapped());
	}

	protected void checkAuthenticationAndSignOut() {
		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			FirebaseAuth.getInstance().signOut();
			NavUtils.navigateUpFromSameTask(this);
		}
	}

	protected void onDiscardTapped() {
		hideKeyboard();
		getBinding().setVariable(editing, false);
		getBinding().setVariable(editingProfile, false);

		if(getBindingEntity().equals(activityEntity)){
			isEditing = false;
			return;
		}

		getBinding().setVariable(entity, activityEntity);
	}

	public void setActivityEntity(T activityEntity) {
		getBinding().setVariable(entity, activityEntity);
		this.activityEntity = (T) activityEntity.clone();
	}

	public abstract ViewDataBinding getBinding();

	protected abstract void onEditTapped();

	protected abstract void onDeleteTapped(Context context);

	protected abstract void onSaveTapped();

	@Override
	public void onBackPressed() {
		if (isEditing) {
			onEditTapped();
		}
		else {
			super.onBackPressed();
		}
	}

	protected abstract T getBindingEntity();
}
