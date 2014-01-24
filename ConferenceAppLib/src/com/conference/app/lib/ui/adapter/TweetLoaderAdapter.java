package com.conference.app.lib.ui.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;

import com.conference.app.lib.R;
import com.conference.app.lib.network.Tweet;
import com.conference.app.lib.ui.TweetReader;
import com.conference.app.lib.util.ImageCache;

public final class TweetLoaderAdapter extends AbstractEndlessAdapter {
    private static final String TAG = TweetLoaderAdapter.class.getName();
    private static final boolean DEBUG = false;

    public static final int MAX_LOAD_TWEETS = 15;
    private static final int ON_ROTATION_DURATION_IN_MS = 600;
    private static final float ROATION_FROM_DEGREES = 0f;
    private static final float ROATION_TO_DEGREES = 360f;
    private static final float PIVOT = 0.5f;

    private final List<Tweet> tweets = new ArrayList<Tweet>();
    private final Set<Long> ids = new HashSet<Long>();
    private final Context ctx;
    private final ImageCache imageCache;
    private final RotateAnimation rotate;
    private final String twitterAccount;

    private int page = 1;

    public TweetLoaderAdapter(final Context ctx, final ArrayAdapter<Tweet> adapter, final int page, final ImageCache imageCache, final String twitterAccount) {
        super(adapter);

        this.ctx = ctx;
        this.imageCache = imageCache;
        this.page = page;
        this.twitterAccount = twitterAccount;

        rotate = new RotateAnimation(ROATION_FROM_DEGREES, ROATION_TO_DEGREES, Animation.RELATIVE_TO_SELF, PIVOT,
                Animation.RELATIVE_TO_SELF, PIVOT);
        rotate.setDuration(ON_ROTATION_DURATION_IN_MS);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);

        for (int i = 0; i < adapter.getCount(); i++) {
            ids.add(adapter.getItem(i).getTweetId());
        }
    }

    @Override
    protected void appendCachedData() {
        if (!tweets.isEmpty()) {
            @SuppressWarnings("unchecked")
            final ArrayAdapter<Tweet> tweetAdapter = (ArrayAdapter<Tweet>) getWrappedAdapter();
            for (Tweet tweet : tweets) {
                if (!ids.contains(tweet.getTweetId())) {
                    ids.add(tweet.getTweetId());
                    tweetAdapter.add(tweet);
                }
            }
        }
    }

    @Override
    protected synchronized boolean cacheInBackground() throws Exception {
        final Twitter twitter = new TwitterFactory(new ConfigurationBuilder().setUseSSL(true)
                .setOAuthConsumerKey(TweetReader.CONSUMER_KEY).setOAuthConsumerSecret(TweetReader.CONSUMER_SEC).build()).getInstance();

        AccessToken mAccessToken = new AccessToken(TweetReader.AUTHKEY, TweetReader.AUTHSEC);
        twitter.setOAuthAccessToken(mAccessToken);

        // final Query query = new Query(TweetReader.hashtag);
        // Query query = new Query("source:twitter4j " + TweetReader.hashtag);

        // final List<Status> tmpTweets = new ArrayList<Status>(result.getTweets());

        tweets.clear();

        Paging paging = new Paging(++page, TweetLoaderAdapter.MAX_LOAD_TWEETS);
        ResponseList<Status> tmpTweets = twitter.getUserTimeline(twitterAccount, paging);

        for (twitter4j.Status tmpTweet : tmpTweets) {
            final Tweet tweet = new Tweet();
            tweet.setTweetId(tmpTweet.getId());
            tweet.setDate(tmpTweet.getCreatedAt());
            tweet.setText(tmpTweet.getText());
            tweet.setUsername(tmpTweet.getUser().getName());
            tweet.setUserimg(tmpTweet.getUser().getProfileImageURL());
            tweet.setImageRessource(imageCache.getImage(tmpTweet.getUser().getProfileImageURL()));
            tweets.add(tweet);
        }

        return tmpTweets.size() >= MAX_LOAD_TWEETS;// &&
                                                   // twitter.getUserTimeline("mobiletechcon").size().getTweets().size()
                                                   // >= MAX_LOAD_TWEETS;
    }

    @Override
    protected View getPendingView(final ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.tweet_end_item, null);

        View child = row.findViewById(R.id.tweet_end_item_root);
        child.setVisibility(View.VISIBLE);

        child = row.findViewById(R.id.tweet_end_item_throbber);
        child.startAnimation(rotate);
        return row;
    }
}
