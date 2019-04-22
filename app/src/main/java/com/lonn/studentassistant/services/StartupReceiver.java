package com.lonn.studentassistant.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lonn.studentassistant.services.implementations.coursesService.CourseService;

public class StartupReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        Log.e("Receiver", "Boot");

        if (intent.getAction() != null && !intent.getAction().equals(""))
        {
            context.startService(new Intent(context, CourseService.class));
        }
    }
}
