package com.lonn.studentassistant.activities.abstractions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.dialogBuilders.DialogBuilder;

import java.util.List;

public abstract class FirebaseConnectedActivity extends AppCompatActivity {
    protected FirebaseConnection firebaseConnection;
    private Handler handler = new Handler();

    public void showSnackBar(String message) {
        showSnackBar(message, Snackbar.LENGTH_INDEFINITE);
    }

    public void showSnackBar(String message, int length) {
        Snackbar snackbar;

//        if (findViewById(R.id.fab) != null) {
//            snackbar = Snackbar.make(findViewById(R.id.fab), message, length);
//        }
//        else {
        snackbar = Snackbar.make(findViewById(android.R.id.content), message, length);
//        }

        snackbar.show();
    }

    public void tapScrollViewEntityLayout(View v) {
        ViewGroup parent = (ViewGroup) v.getParent();

        while (parent != null && !(parent instanceof EntityView)) {
            parent = (ViewGroup) parent.getParent();
        }

        if (parent != null) {
            Intent intent = new Intent(getBaseContext(), ((EntityView) parent).getActivityClass());

            intent.putExtra("entity", ((EntityView) parent).getEntity());

            getBaseContext().startActivity(intent);
        }
    }

    public void tapRemove(View v) {
        ViewGroup parent = (ViewGroup) v.getParent();

        while (parent != null && !(parent instanceof EntityView)) {
            parent = (ViewGroup) parent.getParent();
        }
    }

    public void tapAdd(View v) {
    }

    public void showDialog(List<BaseEntity> entities, String title, String positiveButtonText) {
        DialogBuilder builder = new DialogBuilder(FirebaseConnectedActivity.this, entities, title, positiveButtonText);
        builder.showDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseConnection = FirebaseConnection.getInstance(getBaseContext());
        inflateLayout();
    }

    protected abstract void inflateLayout();

    protected void executeWithDelay(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }

    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = getCurrentFocus();

        if (view == null) {
            view = new View(this);
        }

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
