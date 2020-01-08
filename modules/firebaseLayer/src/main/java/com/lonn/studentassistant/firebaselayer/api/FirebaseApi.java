package com.lonn.studentassistant.firebaselayer.api;

import android.content.Context;

import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;

public class FirebaseApi {
	private FirebaseConnection firebaseConnection;

	public FirebaseApi(Context applicationContext) {
		firebaseConnection = FirebaseConnection.getInstance(applicationContext);
	}
}
