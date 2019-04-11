package com.lonn.studentassistant.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.common.abstractClasses.ServiceBoundActivity;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.coursesService.CourseService;
import com.lonn.studentassistant.services.studentService.StudentService;
import com.lonn.studentassistant.services.userService.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityServiceConnections
{
    private List<CustomServiceConnection> serviceConnections = new ArrayList<>();
    private List<Class> serviceClasses = new ArrayList<>();

    public ActivityServiceConnections(Class... classes)
    {
        serviceClasses.addAll(Arrays.asList(classes));
    }

    public void bind(final ServiceBoundActivity activity)
    {
        for (final Class c : serviceClasses)
        {
            CustomServiceConnection connection = new CustomServiceConnection(c, activity);

            Intent intent = new Intent(activity, getServiceClass(c));
            activity.bindService(intent, connection, Context.BIND_AUTO_CREATE);

            serviceConnections.add(connection);
        }
    }

    private Class getServiceClass(Class c)
    {
        if (c.equals(Course.class))
            return CourseService.class;
        else if (c.equals(Student.class))
            return StudentService.class;
        else if (c.equals(User.class))
            return UserService.class;

        return Object.class;
    }

    public void unbind(Activity activity)
    {
        for (CustomServiceConnection connection : serviceConnections)
        {
            if (connection.service != null) {
                connection.service.removeCallback((IServiceCallback)activity);
                connection.service = null;

                activity.unbindService(connection);

            }
        }
    }

    private class CustomServiceConnection implements ServiceConnection
    {
        LocalService service;
        private Class serviceClass;
        private IServiceCallback callback;

        CustomServiceConnection(Class serviceClass, IServiceCallback callback)
        {
            this.serviceClass = serviceClass;
            this.callback = callback;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e(serviceClass.getSimpleName() + " SERVICE CONNECT","!!!!!!!!!!!");

            service = ((LocalBinder)binder).getService();
            service.addCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service.removeCallback(callback);
        }
    }

    public LocalService getServiceByClass(Class c)
    {
        for(CustomServiceConnection connection : serviceConnections)
        {
            if (connection.serviceClass.equals(c))
                return connection.service;
        }

        return null;
    }
}
