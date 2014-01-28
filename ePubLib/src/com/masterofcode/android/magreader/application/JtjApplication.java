package com.masterofcode.android.magreader.application;

import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;

import com.masterofcode.android.magreader.MainActivity;
import com.masterofcode.android.magreader.db.entity.BookmarksItem;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.db.entity.PurchaseItem;
import com.masterofcode.android.magreader.utils.constants.Constants;

import android.app.Application;
import android.content.Context;

public class JtjApplication extends Application {
	private static JtjApplication instance;
	
	public ActiveRecordBase mDatabase;
    public ActiveRecordBase mBookmarksDatabase;
    public ActiveRecordBase mLibraryDatabase;
    public ActiveRecordBase mPurchaseDatabase;
    ArrayList<FeedType> feedsCanManage = new ArrayList<FeedType>();

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        initDatabases();
    }
    
    public ArrayList<FeedType> getManageFeeds() {
    	return feedsCanManage;
    }
    
    public boolean addFeedsToManageArray(FeedType feedCategory) {
    	if (isUnique(feedCategory)) return feedsCanManage.add(feedCategory);
    	return false;
    }
    
    public boolean isUnique(FeedType item) {
    	return !feedsCanManage.contains(item);
    }
    
    public boolean updateSelectedFeed(int position, boolean isHide) {
    	return feedsCanManage.get(position).is_hide = isHide;
    }
    
    private void initDatabases()
    {
        initMainDatabase();
        initBookmarksDatabase();
        initLibraryDatabase();
        initPurchaseDatabase();
    }
    
    private void initMainDatabase()
    {
        DatabaseBuilder builder = new DatabaseBuilder(Constants.DATABASE_NAME);
        builder.addClass(FeedItem.class);
        builder.addClass(FeedType.class);
        builder.addClass(IssueItem.class);
        Database.setBuilder(builder);
        try {
            mDatabase = ActiveRecordBase.open(this, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);
        } catch (ActiveRecordException e) {
            e.printStackTrace();
        }
    }
    
    private void initBookmarksDatabase()
    {
        DatabaseBuilder builder = new DatabaseBuilder(Constants.BOOKMARKS_DATABASE_NAME);
        builder.addClass(BookmarksItem.class);
        Database.setBuilder(builder);
        try {
            mBookmarksDatabase = ActiveRecordBase.open(this, Constants.BOOKMARKS_DATABASE_NAME, Constants.BOOKMARKS_DATABASE_VERSION);
        } catch (ActiveRecordException e) {
            e.printStackTrace();
        }
    }
    
    private void initPurchaseDatabase()
    {
        DatabaseBuilder builder = new DatabaseBuilder(Constants.PURCHASE_DATABASE_NAME);
        builder.addClass(PurchaseItem.class);
        Database.setBuilder(builder);
        try {
            mPurchaseDatabase = ActiveRecordBase.open(this, Constants.PURCHASE_DATABASE_NAME, Constants.PURCHASE_DATABASE_VERSION);
        } catch (ActiveRecordException e) {
            e.printStackTrace();
        }
    }

    private void initLibraryDatabase()
    {
        DatabaseBuilder builder = new DatabaseBuilder(Constants.LIBRARY_DATABASE_NAME);
        builder.addClass(LibraryItem.class);
        Database.setBuilder(builder);
        try {
            mLibraryDatabase = ActiveRecordBase.open(this, Constants.LIBRARY_DATABASE_NAME, Constants.LIBRARY_DATABASE_VERSION);
        } catch (ActiveRecordException e) {
            e.printStackTrace();
        }
    }

    public synchronized ActiveRecordBase getDatabase()
    {
        return mDatabase;
    }

    public synchronized ActiveRecordBase getBookmarksDatabase()
    {
        return mBookmarksDatabase;
    }
    
    public synchronized ActiveRecordBase getLibraryDatabase()
    {
        return mLibraryDatabase;
    }
    public synchronized ActiveRecordBase getPurchaseDatabase()
    {
        return mPurchaseDatabase;
    }
    
    public static Context getContext()
    {
        return instance;
    }

    public static JtjApplication getInstance()
    {
        return instance;
    }
}
