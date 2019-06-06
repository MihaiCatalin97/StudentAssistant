package com.lonn.studentassistant.activities.abstractions;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.ConnectionBundle;

public abstract class ServiceBoundActivity extends AppCompatActivity
{
    protected ConnectionBundle serviceConnections;
    protected Snackbar snackbar;

    public ServiceBoundActivity()
    {
    }

    public void showSnackbar(String message)
    {
        if (snackbar == null)
            snackbar = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);
        else
            snackbar.setText(message);

        snackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        serviceConnections = new ConnectionBundle(getBaseContext());
    }

    @Override
    public void onStop()
    {
        Log.e("Onstop", "called");
        unbindServices();
        super.onStop();
    }

    public void hideSnackbar()
    {
        if (snackbar != null)
            snackbar.dismiss();
    }

    protected abstract void unbindServices();
}
