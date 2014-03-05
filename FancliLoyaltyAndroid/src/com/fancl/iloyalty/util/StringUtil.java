package com.fancl.iloyalty.util;

public class StringUtil {
	public static boolean isStringEmpty(String string)
	{
		if(string != null)
		{
			if(string.trim().length() > 0)
			{
				return false;
			}
		}
		
		return true;
	}
}
