package com.masterofcode.android.magreader.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.EntitiesHelper;

import com.masterofcode.android.EPubBook.EPubBook;
import com.masterofcode.android.magreader.MainLibraryActivity;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.DownloadManager;
import com.masterofcode.android.magreader.utils.constants.Constants;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;

public class LibraryManager {
	private static LibraryManager			instance = null;
	private ActiveRecordBase				dbLibrary, _db;
	public LibraryManager()
	{
		super();
		this.dbLibrary = JtjApplication.getInstance().getLibraryDatabase();
		this._db	   = JtjApplication.getInstance().getDatabase();
	}

	static public LibraryManager GetInstance()
	{
		if(instance==null)
		{
			instance = new LibraryManager();
		}
		return instance;
	}

	public boolean addNewMagazine(Context context, String filePath, String coverPath, String title, String issueUrl, boolean isDownloaded, int magazineType, String magazine_id, String googlecheckoutid)
	{
		
		LibraryItem			newMagazine = new LibraryItem(filePath, coverPath, title, issueUrl, isDownloaded, magazineType, magazine_id, googlecheckoutid);
		// TODO: check if need this for feeds
		try {
			if(!dbLibrary.isOpen()) dbLibrary.open();
			LibraryItem			newMagazineItem = dbLibrary.newEntity(LibraryItem.class);
			EntitiesHelper.copyFieldsWithoutID(newMagazineItem, newMagazine);
			newMagazineItem.save();
			dbLibrary.close();
			return true;
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateMagazine(Context context, String filePath, boolean isDownload){
		
		String googleCheckoutId = null;
		// TODO: check if need this for feeds
		try {
			if(!dbLibrary.isOpen()) dbLibrary.open();
			//List<LibraryItem> mLibraryItem = dbLibrary.find(LibraryItem.class, "magazineurl=?", new String[] {issueUrl});
			List<LibraryItem> mLibraryItem = dbLibrary.find(LibraryItem.class, "magazinefilepath=?", new String[] {filePath});
			if(mLibraryItem != null && !mLibraryItem.isEmpty()){
				//mLibraryItem.get(0).magazine_filepath = libraryPath(context) + File.separator + ApplicationUtils.getEpubFileNameFromUrl(new URL(issueUrl));
				mLibraryItem.get(0).isDownloaded = isDownload;
				googleCheckoutId = mLibraryItem.get(0).googlecheckoutid;
				int i = mLibraryItem.get(0).update();
				if (Constants.Debug)
					Log.d("deleteIssue", "issue was updatet. i= " + i);
			}
			dbLibrary.close();
			if (!TextUtils.isEmpty(googleCheckoutId)){
				if(!_db.isOpen()) _db.open();
				List<IssueItem> mIssueItem = _db.find(IssueItem.class, "googlecheckoutid=?", new String[] {googleCheckoutId});
				if (mIssueItem != null && !mIssueItem.isEmpty()){
					mIssueItem.get(0).isDownloaded = isDownload;
					int i = mIssueItem.get(0).update();
					if (Constants.Debug)
						Log.d("updateIssue", "issue was updatet. i= " + i);
				}
				_db.close();
			}
			return true;
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	public LibraryItem getLibraryItemByGoogleCheckoutId(String googleCheckoutId){
		LibraryItem mLibraryItem = null;
		try {
			if(!dbLibrary.isOpen()) dbLibrary.open();
			List<LibraryItem> mLibraryItems = dbLibrary.find(LibraryItem.class, "googlecheckoutid=?", new String[] {googleCheckoutId});
			if(mLibraryItems != null && !mLibraryItems.isEmpty()){
				mLibraryItem = mLibraryItems.get(0);
			}
			dbLibrary.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		return mLibraryItem;
	}
	
	public List<LibraryItem> queryLibraryItems()
	{
		List<LibraryItem>		libraryItems = null;
		
		try {
			if(!dbLibrary.isOpen()) dbLibrary.open();
			libraryItems = dbLibrary.find(LibraryItem.class, false, null, null, null, null, "_ID DESC", null);
			dbLibrary.close();
			return libraryItems;
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String libraryPath(Context context)
	{
		File	userDir = context.getDir(Constants.LIBRARY_DIR, 0);		
		return userDir.getAbsolutePath();
	}
	
	public String magazineCopiedMarkFilePath(Context context, String fileName)
	{
		return libraryPath(context) + File.separator + fileName + Constants.MAGAZINE_COPIED_MARK_SUFFIX;
	}
	
	private boolean isMagazineIsCopied(Context context, String fileName)
	{
		File        file = new File(magazineCopiedMarkFilePath(context, fileName));
		return		file.canRead();
	}
	
	public void createMagazineCopiedMark(Context context, String fileName)
	{
		File        file = new File(magazineCopiedMarkFilePath(context, fileName));

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String allMagazinesCopiedMarkFilePath(Context context)
	{
		return libraryPath(context) + File.separator + Constants.MAGAZINE_COPIED_ALL_RESOURCES_FILE_NAME;
	}
	
	public boolean isAllMagazinesIsCopied(Context context)
	{
		File        file = new File(allMagazinesCopiedMarkFilePath(context));
		return		file.canRead();
	}
	
	private void createAllMagazineCopiedMark(Context context)
	{
		File        file = new File(allMagazinesCopiedMarkFilePath(context));

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copyMagazineFromResources(Context mContext, String path){
		DownloadManager dm = new DownloadManager(mContext);
		dm.startCopyFromResources(path);
		updateMagazine(mContext, (mContext.getDir("library", Context.MODE_PRIVATE)).getAbsolutePath() + File.separator + path, true);
	}

	public void downloadMagazine(Context mContext, LibraryItem mLibraryItem){
		DownloadManager dm = new DownloadManager(mContext);
		dm.startDownloadIssue(mLibraryItem.magazine_url);
		updateMagazine(mContext, mLibraryItem.magazine_filepath, true);
	}
	
	public void deleteMagazine(Context mContext, LibraryItem mLibraryItem){
		File 		file = new File(mLibraryItem.magazine_filepath);
		if(file.exists()){
			boolean isDelete = file.delete();
			Log.d("deleteIssue", "isDelete = " + isDelete);
		}
		
		// delete extracted content
		EPubBook.deleteContent(mContext, mLibraryItem.magazine_filepath);
		
		updateMagazine(mContext, mLibraryItem.magazine_filepath, false);
	}
	
	public void copyOneMagazineFromResources(Context context, String magazineForCopying){
		
		final int BUFFER_SIZE = 1024 * 256;                                                                                                     
        
        try {
        	// copy epub
        	String				destinationFilePath = libraryPath(context) + File.separator + magazineForCopying;                                                                
        	File                destinationFile = new File(destinationFilePath);                                                                 
        	FileOutputStream 	destinationStream = new FileOutputStream(destinationFile);                                                                          
        	AssetManager        aManager = context.getAssets();                                                                         
        	InputStream 		sourceStream = aManager.open(magazineForCopying);
        	long				totalLength = 0, copied = 0;
                                                                                                                                
        	byte[] buffer = new byte[BUFFER_SIZE];                                                                                      
        	int len = 0;                                                                                                                

        	// determine length
        	while ( (len = sourceStream.read(buffer)) > 0 ) {
        		totalLength += len;
        	}
        	
        	// copying
        	sourceStream.close();
        	sourceStream = aManager.open(magazineForCopying);

        	while ( (len = sourceStream.read(buffer)) > 0 ) {                                                                                     
        		destinationStream.write(buffer, 0, len);
        	}                                                                                                                           
                                                                                                                                
        	sourceStream.close();
        	destinationStream.close();
        	
        	createMagazineCopiedMark(context, magazineForCopying);
        	
        } catch (Exception e) {                                                                                                 
            e.printStackTrace();                                                                                            
        }
	}
	
	private void copyCoverMagazineFromResources(Context context, String magazineForCopying){
		
		final int BUFFER_SIZE = 1024 * 256; 
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			AssetManager        aManager = context.getAssets(); 
			// copy cover
			String				destinationFilePath = libraryPath(context) + File.separator + magazineForCopying;
	    	String				coverDestinationFilePath = libraryPath(context) + File.separator + magazineForCopying + Constants.MAGAZINE_COVER_SUFFIX;                                                                
	    	File                coverDestinationFile = new File(coverDestinationFilePath);                                                                 
	    	FileOutputStream 	coverDestinationStream = new FileOutputStream(coverDestinationFile);                                                                          
	    	InputStream 		coverSourceStream = aManager.open(magazineForCopying + Constants.MAGAZINE_COVER_SUFFIX);
	                         
	    	int len = 0; 
	    	
	    	
	    	

	    	
	    	
	    	while ( (len = coverSourceStream.read(buffer)) > 0 ) {                                                                                     
	    		coverDestinationStream.write(buffer, 0, len);
	    	}                                                                                                                           
	    	coverSourceStream.close();
	    	coverDestinationStream.close();                                                                                                                
	
	    	// mark as copied
	    	createMagazineCopiedMark(context, magazineForCopying);
	    	
	    	// title for journals
	    	String title = null;
	    	String id = null;
	    	boolean 	isDownloaded = false;
	    	try {
	    		//try to load json file
		    	JsonReader issuesConfigJSONFileReader = new JsonReader(new InputStreamReader(aManager.open(magazineForCopying + Constants.MAGAZINE_JSON_SUFFIX), "UTF-8"));
		    	issuesConfigJSONFileReader.beginObject();
		        while (issuesConfigJSONFileReader.hasNext()) {
		          String name = issuesConfigJSONFileReader.nextName();
		          if (name.equals(Constants.ISSUE_METADATA_ATTRIBUTE_ID_ID)) {
		            id = issuesConfigJSONFileReader.nextString();
		          } else if (name.equals(Constants.ISSUE_METADATA_ATTRIBUTE_TITLE_ID)) {
		            title = issuesConfigJSONFileReader.nextString();
		          } else {
		        	  issuesConfigJSONFileReader.skipValue();
		          }
		        }
		        issuesConfigJSONFileReader.endObject();
			} catch (Exception e) {
				Log.e("Magazine Reader", "Problem loading json file for pre installed issue" + e.getMessage());
				e.printStackTrace();
			}
	    	
	    	//we set the title of the installed issue only if we have not got the value from the json file
	    	if(magazineForCopying.equalsIgnoreCase("default_magazine.epub") && (title == null)) {
	    		title = "Sample Issue";
	    	}
	    	
	    	//
	    	/*if(!ApplicationUtils.getPrefPropertyBoolean(context, "issueFromResourcesWasCopied", false)){
	    		copyOneMagazineFromResources(context, magazineForCopying);
	    		isDownloaded = true;
	    	}*/
	    	///
	    	// add journal to db
	    	addNewMagazine(context, destinationFilePath, coverDestinationFilePath, title, null, isDownloaded, Constants.MAGAZINE_TYPE_FROM_RESOURCES, id, null);
		} catch (Exception e) {                                                                                                 
            e.printStackTrace();                                                                                            
        }
	}
	
	private ArrayList<String> getMagazineCoversListNameFromResources(Context context){

		final AssetManager		asManager = context.getAssets();
	        
		String[] 		assetsFiles = new String[]{};
		try {
			 assetsFiles = asManager.list("");
		} catch (IOException e) {
			 e.printStackTrace();
		}
		
		final ArrayList<String>	magazinesForCopying = new ArrayList<String>();
        
		// create list magazines from assets, that are not already copied
        for(int i=0; i<assetsFiles.length; i++)
        {
        	if(assetsFiles[i].startsWith("default_magazine") && assetsFiles[i].endsWith(".epub"))
        	{
        		if(! isMagazineIsCopied(context, assetsFiles[i]))
        		{
        			magazinesForCopying.add(assetsFiles[i]);
        		}
        	}
        }
        return magazinesForCopying;
	}
	
	public void copyCoversFromRes(Context context){
		
		if(!ApplicationUtils.getPrefPropertyBoolean(context, "issueFromResourcesWasCopied", false)){
			ArrayList<String> covers = getMagazineCoversListNameFromResources(context);
			for (String coverName : covers){
				copyCoverMagazineFromResources(context, coverName);
			}
			ApplicationUtils.setPrefProperty(context, "issueFromResourcesWasCopied", true);
		}
		
	}
}
