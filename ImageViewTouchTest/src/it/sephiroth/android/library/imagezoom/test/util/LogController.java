package it.sephiroth.android.library.imagezoom.test.util;

import it.sephiroth.android.library.imagezoom.test.Constants;

import android.util.Log;

public class LogController {
	
	public static void log(String message)
	{
		if(Constants.isDebug)
		{
			LogController.printMessageLog(message);
		}
	}
	
	public static void log(String message, Exception e)
	{
		if(Constants.isDebug)
		{
			LogController.printMessageWithException(message, e);
		}
	}
	
	public static void logForProduction(String message)
	{
		if(Constants.isProductionLogEnable)
		{
			LogController.printMessageLog(message);
		}
	}
	
	public static void logForProduction(String message, Exception e)
	{
		if(Constants.isProductionLogEnable)
		{
			LogController.printMessageWithException(message, e);
		}
	}
	
	private static void printMessageLog(String message)
	{
		Log.d(Constants.LOG_TAG, message);
	}
	
	private static void printMessageWithException(String message, Exception e)
	{
		if(e != null)
		{
			Log.d(Constants.LOG_TAG, message, e);
		}
		else
		{
			Log.d(Constants.LOG_TAG, message);
		}
	}
}
