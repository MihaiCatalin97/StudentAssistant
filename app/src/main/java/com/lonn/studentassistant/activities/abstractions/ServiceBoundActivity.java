package com.lonn.studentassistant.activities.abstractions;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.ConnectionBundle;

public abstract class ServiceBoundActivity extends AppCompatActivity
{
    protected ConnectionBundle serviceConnections;
    protected Snackbar snackbar;
    private long timeLastSnack;

    public ServiceBoundActivity()
    {
    }

    public void showSnackbar(String message)
    {
        if (snackbar == null)
        {
            if(findViewById(R.id.fab) != null)
                snackbar = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);
            else
                snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        }
        else
            snackbar.setText(message);

        snackbar.show();
        timeLastSnack = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        serviceConnections = new ConnectionBundle(getBaseContext());
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signOut();
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    @Override
    public void onStop()
    {
        unbindServices();
        super.onStop();
    }

    public void hideSnackbar()
    {
        if (snackbar != null)
        {
            if(System.currentTimeMillis() - timeLastSnack >= 1500)
                snackbar.dismiss();
            else
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        hideSnackbar();
                    }
                }, 1500 - System.currentTimeMillis() + timeLastSnack);
        }
    }

    protected abstract void unbindServices();
}
