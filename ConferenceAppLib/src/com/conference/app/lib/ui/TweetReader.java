package com.conference.app.lib.ui;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ListView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.ConferenceTable;
import com.conference.app.lib.network.Tweet;
import com.conference.app.lib.ui.adapter.TweetAdapter;
import com.conference.app.lib.ui.adapter.TweetLoaderAdapter;
import com.conference.app.lib.util.ImageCache;

public class TweetReader extends Activity {
    private static final String TAG = TweetReader.class.getName();
    private static final boolean DEBUG = false;

    public static String hashtag;
    public static String twitterAccount;

    public static final String CONSUMER_KEY = "YWVKvjP7Om3FFYebnUcypw";
    public static final String CONSUMER_SEC = "UnErZornBhkysCjx1rVv8UeHxvwy6ZDUZiS8MWNAk";
    public static final String AUTHKEY = "1670159683-s5F7AnN2QQwfRG1O4Ht1KumGvasC7aPONt0fkil";
    public static final String AUTHSEC = "5jbtaK3wsbg5GP392tz4oKnbclY0uJ7ZjC4e2jUKs";

    private static final int DIALOG_LOAD_TWEETS = 1;

    private static final int ON_ROTATION_DURATION_IN_MS = 600;
    private static final float ROATION_FROM_DEGREES = 0f;
    private static final float ROATION_TO_DEGREES = 360f;
    private static final float PIVOT = 0.5f;

    private ListView tweetList;
    private ImageButton syncBtn;
    private TweetAdapter adapter;

    private boolean isFirstInit = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);
    }
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFirstInit = true;
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (!isFirstInit) {
			return;
		}
		isFirstInit = false;
		
		initViews();
        initTextValues();
        initViewValues();
	}

    private void initTextValues() {
        final DatabaseHelper helper = new DatabaseHelper(this);
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor cursor = db.query(ConferenceTable.TABLE_NAME, new String[] { ConferenceTable.ID, ConferenceTable.TWITTER_ACCOUNT,
                ConferenceTable.HASHTAG }, null, null, null, null, null);

        cursor.moveToFirst();

        twitterAccount = cursor.getString(cursor.getColumnIndex(ConferenceTable.TWITTER_ACCOUNT));
        hashtag = cursor.getString(cursor.getColumnIndex(ConferenceTable.HASHTAG));

        cursor.close();
        if (db.isOpen()) {
            db.close();
        }
    }

    public void onClickActionBarSync(final View view) {
        showDialog(DIALOG_LOAD_TWEETS);
        adapter = null;
        tweetList.setAdapter(adapter);
        new LoadTweets().execute();
    }

    public void onClickActionBarHome(final View view) {
        final Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // overridePendingTransition(R.anim.home_enter, R.anim.home_exit);
    }

    private void initViewValues() {
        showDialog(DIALOG_LOAD_TWEETS);
        new LoadTweets().execute();
    }

    private void initViews() {
        tweetList = (ListView) findViewById(R.id.tweetlist);
        syncBtn = (ImageButton) findViewById(R.id.twitter_sync_btn);
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        if (DIALOG_LOAD_TWEETS == id) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage(getString(R.string.twitter_dia_load_tweets));
            return pd;
        }

        return super.onCreateDialog(id);
    }

    private class LoadTweets extends AsyncTask<Void, Void, List<Tweet>> {

        private final RotateAnimation rotate;
        private final ImageCache imageCache;
        private int page;

        public LoadTweets() {
            rotate = new RotateAnimation(ROATION_FROM_DEGREES, ROATION_TO_DEGREES, Animation.RELATIVE_TO_SELF, PIVOT,
                    Animation.RELATIVE_TO_SELF, PIVOT);
            rotate.setDuration(ON_ROTATION_DURATION_IN_MS);
            rotate.setRepeatMode(Animation.RESTART);
            rotate.setRepeatCount(Animation.INFINITE);

            imageCache = new ImageCache();
        }

        @Override
        protected void onPreExecute() {
            if (syncBtn != null) {
                syncBtn.startAnimation(rotate);
                syncBtn.setClickable(false);
            }
        }

        @Override
        protected List<Tweet> doInBackground(final Void... params) {
            final List<Tweet> tweets = new ArrayList<Tweet>();
            final Twitter twitter = new TwitterFactory(new ConfigurationBuilder().setUseSSL(true).setOAuthConsumerKey(CONSUMER_KEY)
                    .setOAuthConsumerSecret(CONSUMER_SEC).build()).getInstance();

            AccessToken mAccessToken = new AccessToken(AUTHKEY, AUTHSEC);
            twitter.setOAuthAccessToken(mAccessToken);
            try {
                // final QueryResult result = twitter.search(new Query(hashtag));
                page = 1;
                Paging paging = new Paging(page, TweetLoaderAdapter.MAX_LOAD_TWEETS);
                for (twitter4j.Status tmpTweet : twitter.getUserTimeline(twitterAccount, paging)) {
                    final Tweet tweet = new Tweet();
                    tweet.setTweetId(tmpTweet.getId());
                    tweet.setDate(tmpTweet.getCreatedAt());
                    tweet.setText(tmpTweet.getText());
                    tweet.setUsername(tmpTweet.getUser().getName());
                    tweet.setUserimg(tmpTweet.getUser().getProfileImageURL());
                    tweet.setImageRessource(imageCache.getImage(tmpTweet.getUser().getProfileImageURL()));
                    tweets.add(tweet);
                }
            } catch (TwitterException te) {
                Log.e(TAG, te.getMessage(), te);
            }
            return tweets;
        }

        @Override
        protected void onPostExecute(final List<Tweet> result) {
            if (adapter == null && result != null) {
                tweetList.setAdapter(null);
                adapter = new TweetAdapter(TweetReader.this, result);
                tweetList.setAdapter(new TweetLoaderAdapter(TweetReader.this, adapter, page, imageCache, twitterAccount));
                tweetList.invalidate();
            } else if (result != null) {
                for (int i = result.size() - 1; i >= 0; i--) {
                    adapter.insert(result.get(i), 0);
                    adapter.notifyDataSetChanged();
                }
            }

            removeDialog(DIALOG_LOAD_TWEETS);

            if (syncBtn != null) {
                syncBtn.setClickable(true);
                syncBtn.clearAnimation();
            }
        }
    }
}
