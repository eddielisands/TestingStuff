package com.conference.app.lib.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.conference.app.lib.R;
import com.conference.app.lib.network.Tweet;

public class TweetAdapter extends ArrayAdapter<Tweet> {
    private static final String TAG = TweetAdapter.class.getName();
    private static final boolean DEBUG = false;

    private final Context ctx;
    private final int resid;
    private final List<Tweet> tweets;

    public TweetAdapter(final Context ctx, final List<Tweet> tweets) {
        super(ctx, R.layout.tweet_item, tweets);
        this.ctx = ctx;
        this.tweets = tweets;
        this.resid = R.layout.tweet_item;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final Tweet tweet = tweets.get(position);
        ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            final LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resid, null, true);

            viewHolder = new ViewHolder();
            viewHolder.author = (TextView) rowView.findViewById(R.id.tweet_author);
            viewHolder.text = (TextView) rowView.findViewById(R.id.tweet_text);
            viewHolder.date = (TextView) rowView.findViewById(R.id.tweet_time);
            viewHolder.image = (ImageView) rowView.findViewById(R.id.tweet_img);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if (position % 2 == 0) {
            rowView.setBackgroundResource(R.drawable.listitembg1);
        } else {
            rowView.setBackgroundResource(R.drawable.listitembg2);
        }

        viewHolder.author.setText(tweet.getUsername());
        viewHolder.text.setText(tweet.getText());
        viewHolder.date.setText(tweet.getDateString(ctx));

        if (tweet.getUserimg() != null && !tweet.getUserimg().equals(viewHolder.image.getTag())) {
            viewHolder.image.setTag(tweet.getUserimg());
            viewHolder.image.setImageBitmap(tweet.getImageRessource());
        }

        return rowView;
    }

    static class ViewHolder {
        public ImageView image;
        public TextView author;
        public TextView text;
        public TextView date;
    }
}
