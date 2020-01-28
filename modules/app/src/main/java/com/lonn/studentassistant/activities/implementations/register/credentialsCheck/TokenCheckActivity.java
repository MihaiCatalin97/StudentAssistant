package com.lonn.studentassistant.activities.implementations.register.credentialsCheck;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;

public class TokenCheckActivity extends FirebaseConnectedActivity {
	protected void inflateLayout() {
		DataBindingUtil.setContentView(this, R.layout.token_check_activity_layout);
	}
}
