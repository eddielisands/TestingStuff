package com.fancl.iloyalty.util;

import android.util.Log;

import com.fancl.iloyalty.Constants;

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
