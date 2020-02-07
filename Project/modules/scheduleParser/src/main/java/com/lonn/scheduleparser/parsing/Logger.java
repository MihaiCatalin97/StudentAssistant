package com.lonn.scheduleparser.parsing;

import android.util.Log;

public class Logger {
	private String tag;

	private Logger(String tag) {
		this.tag = tag;
	}

	public static Logger ofClass(Class loggedClass) {
		return new Logger(loggedClass.getName());
	}

	public void error(String message) {
		Log.e(tag, message);
	}

	public void error(String message, Exception exception) {
		Log.e(tag, message, exception);
	}
}
