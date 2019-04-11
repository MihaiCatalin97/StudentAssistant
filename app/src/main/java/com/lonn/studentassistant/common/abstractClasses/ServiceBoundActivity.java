package com.lonn.studentassistant.common.abstractClasses;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.ActivityServiceConnections;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;


public abstract class ServiceBoundActivity extends AppCompatActivity implements IServiceCallback
{
    protected ActivityServiceConnections serviceConnections;
    protected Snackbar snackbar;

    public ServiceBoundActivity(Class... entityClasses)
    {
        serviceConnections = new ActivityServiceConnections(entityClasses);
    }

    @Override
    public void onStart() {
        super.onStart();

        serviceConnections.bind(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        serviceConnections.unbind(this);
    }

    protected void showSnackbar(String message)
    {
        if (snackbar == null)
            snackbar = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);
        else
            snackbar.setText(message);

        snackbar.show();
    }

    protected void hideSnackbar()
    {
        if (snackbar != null)
            snackbar.dismiss();
    }
}
