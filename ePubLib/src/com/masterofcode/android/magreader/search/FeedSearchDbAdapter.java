package com.masterofcode.android.magreader.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.masterofcode.android.magreader.utils.constants.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FeedSearchDbAdapter {
    public static final String LOG_TAG = "FeddSearch";
    private static final String DATABASE_NAME = "FeedSearch_db";
    private static final int DATABASE_VERSION = 1; // Our internal database version (e.g. to control upgrades)
    private static final String TABLE_NAME = "feed_for_search_tbl";
    public static final String KEY_ID = "id";
    public static final String KEY_FEED_TEXT = "feed_text";
    public static final String KEY_FEED_GUID = "email";
    public static long GENERIC_ERROR = -1;
    public static long GENERIC_NO_RESULTS = -2;
    public static long ROW_INSERT_FAILED = -3;
    private final Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase sqlDatabase;
  
    public FeedSearchDbAdapter(Context context) {
        this.context = context;
    }
    
    private static class DbHelper extends SQLiteOpenHelper {
        private boolean databaseCreated=false;
        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
        	if(Constants.Debug)
        		if(Constants.Debug) Log.d(LOG_TAG, "Creating the application database");
            
            try{
                // Create the full text search 3 virtual table
                db.execSQL(
                    "CREATE VIRTUAL TABLE ["+TABLE_NAME+"] USING FTS3 (" +
            //            "["+KEY_ID+"] INTEGER," +
                        "["+KEY_FEED_GUID+"] VARCHAR(100)," +
                        "["+KEY_FEED_TEXT+"] TEXT" +
                    ");"
                );
                this.databaseCreated = true;
            } catch (Exception e) {
                Log.e(LOG_TAG, "An error occurred while creating the database: "+e.toString(), e);
                this.deleteDatabaseStructure(db);
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	if(Constants.Debug)
        		Log.d(LOG_TAG, "Updating the database from the version " + oldVersion + " to " + newVersion + "...");
            this.deleteDatabaseStructure(db); // in this example we purge the previous data on upgrade
            this.onCreate(db);
        }
        public boolean databaseCreated(){
            return this.databaseCreated;
        }
        private boolean deleteDatabaseStructure(SQLiteDatabase db){
            try{
                db.execSQL("DROP TABLE IF EXISTS ["+TABLE_NAME+"];");
                
                return true;
            }catch (Exception e) {
                Log.e(LOG_TAG, "An error occurred while deleting the database: "+e.toString(), e);
            }
            return false;
        }
    }
    
    /**
     * Open the database; if the database can't be opened, try to create it
     * 
     * @return {@link Boolean} true if the database was successfuly opened/created, false otherwise
     * @throws {@link SQLException] if an error ocorred
     */
    public boolean open() throws SQLException {
        try{
            this.dbHelper = new DbHelper(this.context);
            this.sqlDatabase = this.dbHelper.getWritableDatabase();
            return this.sqlDatabase.isOpen();
        }catch (SQLException e) {
            throw e;
        }
    }
    
    /**
     * Close the database connection
     * @return {@link Boolean} true if the connection was terminated, false otherwise
     */
    public boolean close() {
        this.dbHelper.close();
        return !this.sqlDatabase.isOpen();
    }
    
    /**
     * Check if the database is opened
     * 
     * @return {@link Boolean} true if it was, false otherwise
     */
    public boolean isOpen(){
        return this.sqlDatabase.isOpen();
    }
    
    /**
     * Check if the database was created
     * 
     * @return {@link Boolean} true if it was, false otherwise
     */
    public boolean databaseCreated(){
        return this.dbHelper.databaseCreated();
    }
    
    
    /**
     * Insert a new row on the table
     * 
     * @param username {@link String} with the username
     * @param fullname {@link String} with the fullname
     * @param email {@link String} with the email
     * @return {@link Long} with the row id or ROW_INSERT_FAILED (bellow 0 value) on error
     */
    public long insertRow(String feed_guid, String feed_description) {
        try{
            // Prepare the values
            ContentValues values = new ContentValues();
            //values.put(KEY_ID, id);
            values.put(KEY_FEED_GUID, feed_guid);
            values.put(KEY_FEED_TEXT, feed_description);
            // Try to insert the row
            return this.sqlDatabase.insert(TABLE_NAME, null, values);
        }catch (Exception e) {
            Log.e(LOG_TAG, "An error occurred while inserting the row: "+e.toString(), e);
        }
        return ROW_INSERT_FAILED;
    }
    
    /**
     * The search method
     * Uses the full text search 3 virtual table and the MATCH function from SQLite to search for data
     * @see http://www.sqlite.org/fts3.html to know more about the syntax
     * @param search {@link String} with the search expression
     * @return {@link LinkedList} with the {@link String} search results
     */
    public HashSet<String> search(String search) {
        
    	HashSet<String> results = new HashSet<String>();
        Cursor cursor = null;
        try{
            cursor = this.sqlDatabase.query(true, TABLE_NAME, new String[] { KEY_FEED_GUID }, KEY_FEED_TEXT + " MATCH ?", new String[] { "'" + search.trim() + "'" }, null, null, null, null);
            
            if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()){
                int idColumnGUID = cursor.getColumnIndex(KEY_FEED_GUID);
                do{
                    results.add(
                        new String(
                            cursor.getString(idColumnGUID)
                        )
                    );
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            Log.e(LOG_TAG, "An error occurred while searching for "+search+": "+e.toString(), e);
        }finally{
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
                close();
            }
        }
        
        return results;
    }
}
