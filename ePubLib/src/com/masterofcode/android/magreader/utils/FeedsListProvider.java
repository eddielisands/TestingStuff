package com.masterofcode.android.magreader.utils;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.masterofcode.android.magreader.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.EntitiesHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fritz
 * Date: 6/16/11
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeedsListProvider {
    static final int FEEDS_COUNT = 3;
    private static FeedsListProvider ourInstance = new FeedsListProvider();
    private final ArrayList<FeedType> feeds = new ArrayList<FeedType>(FEEDS_COUNT);
    private ActiveRecordBase mDatabase;

    public static FeedsListProvider getInstance(ActiveRecordBase mdb) {

        Log.i("feed_prov", "Instance called");
        ourInstance.setDatabase(mdb);
        return ourInstance;
    }

    public static FeedsListProvider getInstance() throws NullPointerException {
        Log.i("feed_prov", "Instance called");
        if (ourInstance.mDatabase != null)
            return ourInstance;
        else
            throw new NullPointerException();
    }

    private FeedsListProvider() {
        //FeedType[] remote_feeds = new FeedType[3];
        //remote_feeds[0] = new FeedType("All-inclusive", "http://pipes.yahoo.com/pipes/pipe.run?_id=4520fbd767279168c86e15359f58ff2b&_render=json", "en_EN", 1);
        //remote_feeds[1] = new FeedType("News", "http://pipes.yahoo.com/pipes/pipe.run?_id=4cecf00f94322d2ecd30baed68e50331&_render=json", "en_EN", 2);
        //remote_feeds[2] = new FeedType("Videos", "http://pipes.yahoo.com/pipes/pipe.run?_id=f7a758de82c6f7ed6d68637d25de79f9&_render=json", "en_EN", 3);
    }

    private void setDatabase(ActiveRecordBase db) {
        mDatabase = db;
    }

    public boolean addFeedType(FeedType feedType) {

        try {
            feeds.add(feedType);
            FeedType type = mDatabase.newEntity(FeedType.class);
            EntitiesHelper.copyFieldsWithoutID(type, feedType);
            type.save();
            Log.i("feed_prov", "Feed is added");
        } catch (
                ActiveRecordException e
                )

        {
            e.printStackTrace();
        }
        return true;
    }

    public synchronized ArrayList<FeedType> getFeeds() {

        return feeds;
    }


}
