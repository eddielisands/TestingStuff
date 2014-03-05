package com.fancl.iloyalty.service;

import android.database.sqlite.SQLiteDatabase;

public interface SQLiteDatabaseService {
	public SQLiteDatabase getSQLiteDatabase();
	
	public void closeSQLiteDatabase();
	
	public SQLiteDatabase getUserLogDatabase();
	
	public SQLiteDatabase getTillIdDatabase();
	
	public void closeTillIdDatabase();
}