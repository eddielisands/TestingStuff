package com.conference.app.lib.network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;

import com.conference.app.lib.R;
import com.conference.app.lib.util.TimeUtil;

public class Tweet {
    private static final String TAG = Tweet.class.getName();
    private static final boolean DEBUG = false;

    private long tweetId;
    private String text;
    private String username;
    private String userimg;
    private Date date;
    private Bitmap imageRessource;

    public long getTweetId() {
        return this.tweetId;
    }

    public void setTweetId(final long tweetId) {
        this.tweetId = tweetId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUserimg() {
        return this.userimg;
    }

    public void setUserimg(final String userimg) {
        this.userimg = userimg;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Bitmap getImageRessource() {
        return this.imageRessource;
    }

    public void setImageRessource(final Bitmap imageRessource) {
        this.imageRessource = imageRessource;
    }

    public String getDateString(final Context ctx) {
        Locale language;
        final String langCode = ctx.getString(R.string.lang_code);
        if ("DE".equals(langCode)) {
            language = Locale.GERMAN;
        } else {
            language = Locale.US;
        }
        if (date != null) {
            final StringBuilder sb = new StringBuilder();
            final Long datetime = date.getTime();
            if (datetime != null) {
                final long currentTime = System.currentTimeMillis();

                if ((currentTime - datetime) >= TimeUtil.DAY_IN_MS) {
                    sb.append(ctx.getString(R.string.twitter_tweet_tweeted_at));
                    sb.append(" ");
                    sb.append(new SimpleDateFormat("HH:mm '-' EEE, MMM d", language).format(new Date(datetime)));
                } else {
                    sb.append(ctx.getString(R.string.twitter_tweet_tweeted));
                    sb.append(" ");
                    if ((currentTime - datetime) < TimeUtil.HOUR_IN_MS) {
                        final int calcMinute = (int) ((currentTime - datetime) / TimeUtil.MINUTE_IN_MS);
                        switch (calcMinute) {
                        case 0:
                            sb.append(ctx.getString(R.string.twitter_tweet_just_now));
                            break;
                        case 1:
                            sb.append(ctx.getString(R.string.twitter_tweet_about));
                            sb.append(" ");
                            sb.append(calcMinute);
                            sb.append(" ");
                            sb.append(ctx.getString(R.string.twitter_tweet_minute_ago));
                            break;
                        default:
                            sb.append(ctx.getString(R.string.twitter_tweet_about));
                            sb.append(" ");
                            sb.append(calcMinute);
                            sb.append(" ");
                            sb.append(ctx.getString(R.string.twitter_tweet_minutes_ago));
                            break;
                        }
                    } else {
                        final int calcHour = (int) ((currentTime - datetime) / TimeUtil.HOUR_IN_MS);
                        sb.append(calcHour);
                        sb.append(" ");

                        if (calcHour == 1) {
                            sb.append(ctx.getString(R.string.twitter_tweet_hour_ago));
                        } else {
                            sb.append(ctx.getString(R.string.twitter_tweet_hours_ago));
                        }
                    }
                }
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    public int hashCode() {
        return String.valueOf(tweetId).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Tweet) {
            final Tweet tweet = (Tweet) obj;
            return tweetId == tweet.getTweetId();
        }
        return false;
    }
}
