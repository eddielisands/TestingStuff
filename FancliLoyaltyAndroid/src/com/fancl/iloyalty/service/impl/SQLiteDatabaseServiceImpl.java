package com.fancl.iloyalty.service.impl;

import android.database.sqlite.SQLiteDatabase;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.item.SQLiteHelper;
import com.fancl.iloyalty.service.SQLiteDatabaseService;

public class SQLiteDatabaseServiceImpl implements SQLiteDatabaseService {
private SQLiteDatabase sqliteDatabase;
private SQLiteDatabase userLogDatabase;
private SQLiteDatabase tillIdDatabase;
	
	@Override
	public SQLiteDatabase getSQLiteDatabase() {
		
		if(sqliteDatabase == null)
		{
			SQLiteHelper dbHelper = new SQLiteHelper(AndroidProjectApplication.application, Constants.DATABASE_FOLDER + Constants.DATABASE_FILE_NAME, null, 2);
			sqliteDatabase = dbHelper.getWritableDatabase();
		}
		
		if(sqliteDatabase != null)
		{
			if(sqliteDatabase.isOpen())
			{
				return sqliteDatabase;
			}
			else
			{
				sqliteDatabase = null;
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public void closeSQLiteDatabase() {
		if(sqliteDatabase != null)
		{
			if(sqliteDatabase.isOpen())
			{
				sqliteDatabase.close();
				sqliteDatabase = null;
			}
		}
	}

	@Override
	public SQLiteDatabase getUserLogDatabase() {
		// TODO Auto-generated method stub
		if(userLogDatabase == null)
		{
			SQLiteHelper dbHelper = new SQLiteHelper(AndroidProjectApplication.application, Constants.DATABASE_FOLDER + Constants.LOG_DATABASE_FILE_NAME, null, 2);
			userLogDatabase = dbHelper.getWritableDatabase();
		}
		
		if(userLogDatabase != null)
		{
			if(userLogDatabase.isOpen())
			{
				return userLogDatabase;
			}
			else
			{
				userLogDatabase = null;
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public SQLiteDatabase getTillIdDatabase() {
		// TODO Auto-generated method stub
		if(tillIdDatabase == null)
		{
			SQLiteHelper dbHelper = new SQLiteHelper(AndroidProjectApplication.application, Constants.DATABASE_FOLDER + Constants.TILL_ID_DATABASE_FILE_NAME, null, 2);
			tillIdDatabase = dbHelper.getWritableDatabase();
		}
		
		if(tillIdDatabase != null)
		{
			if(tillIdDatabase.isOpen())
			{
				return tillIdDatabase;
			}
			else
			{
				tillIdDatabase = null;
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public void closeTillIdDatabase() {
		// TODO Auto-generated method stub
		if(tillIdDatabase != null)
		{
			if(tillIdDatabase.isOpen())
			{
				tillIdDatabase.close();
				tillIdDatabase = null;
			}
		}
	}

}
