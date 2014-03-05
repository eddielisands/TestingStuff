package com.gt.snssharinglibrary.util;

import com.gt.snssharinglibrary.Config;

import android.util.Log;

public class LogController {
	
	public static void log(String message)
	{
		if(Config.DEBUG_LOGGER)
		{
			LogController.printMessageLog(message);
		}
	}
	
	public static void log(String message, Exception e)
	{
		if(Config.DEBUG_LOGGER)
		{
			LogController.printMessageWithException(message, e);
		}
	}
	
	public static void logForProduction(String message)
	{
		if(Config.PRODUCTION_LOGGER)
		{
			LogController.printMessageLog(message);
		}
	}
	
	public static void logForProduction(String message, Exception e)
	{
		if(Config.PRODUCTION_LOGGER)
		{
			LogController.printMessageWithException(message, e);
		}
	}
	
	private static void printMessageLog(String message)
	{
		Log.d(Config.LOG_TAG, message);
	}
	
	private static void printMessageWithException(String message, Exception e)
	{
		if(e != null)
		{
			Log.d(Config.LOG_TAG, message, e);
		}
		else
		{
			Log.d(Config.LOG_TAG, message);
		}
	}
}
