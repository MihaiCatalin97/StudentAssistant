package com.lonn.scheduleparser.parsingServices;

import android.util.Log;

public class Utils {
    public static void logException(Exception exception){
        String errorMessage = exception.getMessage();

        if (errorMessage == null) {
            errorMessage = exception.toString();
        }

        Log.e("Parsing ", errorMessage);
    }
}
