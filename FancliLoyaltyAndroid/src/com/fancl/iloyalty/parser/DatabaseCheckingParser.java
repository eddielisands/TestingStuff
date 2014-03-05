package com.fancl.iloyalty.parser;

import com.fancl.iloyalty.pojo.DatabaseVersionCheckResult;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;

public class DatabaseCheckingParser {
	
	
	public DatabaseVersionCheckResult parseDatabaseChecking(PList plist)
	{
		/**
		 * Need to implement Parse API response
		 */
		
		Dict rootDict = (Dict)plist.getRootElement();
		
		String issue = rootDict.getConfiguration("issue").getValue();
		String version = rootDict.getConfiguration("version").getValue();
		String link = rootDict.getConfiguration("link").getValue();
		
		LogController.log("issue : " + issue);
		LogController.log("version : " + version);
		LogController.log("link : " + link);
		
		DatabaseVersionCheckResult databaseVersionCheckResult = new DatabaseVersionCheckResult(issue, version, link);
		
		return databaseVersionCheckResult;
	}
	
}
