package com.lonn.studentassistant.activities.abstractions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.authentication.AuthenticationActivity;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.dialogBuilders.DialogBuilder;

import java.util.List;

public abstract class ServiceBoundActivity<T extends BaseEntity> extends AppCompatActivity {
    private static ServiceBoundActivity instance;
    protected Snackbar snackbar;
    private long timeLastSnack;

    public ServiceBoundActivity() {
    }

    public static ServiceBoundActivity getCurrentActivity() {
        return instance;
    }

    public void showSnackbar(String message) {
        if (snackbar == null) {
            if (findViewById(R.id.fab) != null) {
                snackbar = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);
            }
            else {
                snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
            }
        }
        else {
            snackbar.setText(message);
        }

        snackbar.show();
        timeLastSnack = System.currentTimeMillis();
    }

    public void showSnackbar(String message, int length) {
        if (snackbar == null) {
            if (findViewById(R.id.fab) != null) {
                snackbar = Snackbar.make(findViewById(R.id.fab), message, length);
            }
            else {
                snackbar = Snackbar.make(findViewById(android.R.id.content), message, length);
            }
        }
        else {
            snackbar.setText(message);
        }

        snackbar.show();
        timeLastSnack = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateLayout();
    }

    protected abstract void inflateLayout();

    @Override
    public void onStart() {
        super.onStart();

        if (!(this instanceof AuthenticationActivity) && FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            NavUtils.navigateUpFromSameTask(this);
        }

        instance = this;
    }

    public void hideSnackbar() {
        if (snackbar != null) {
            if (System.currentTimeMillis() - timeLastSnack >= 1500) {
                snackbar.dismiss();
            }
            else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideSnackbar();
                    }
                }, 1500 - System.currentTimeMillis() + timeLastSnack);
            }
        }
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
        DialogBuilder builder = new DialogBuilder(ServiceBoundActivity.this, entities, title, positiveButtonText);
        builder.showDialog();
    }
}
