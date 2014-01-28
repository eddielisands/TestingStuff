package com.masterofcode.android.magreader.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.search.SearchResultItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.ImageLoader;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class SearchResultsAdapter extends ArrayAdapter<SearchResultItem> {
	
	private Context context;
	private List<SearchResultItem> items;
	private FeedItem mFeedItem;
	int textViewResourceId;
	public ImageLoader imageLoader;
	private Activity activity;

	public SearchResultsAdapter(Context context, int textViewResourceId,
			List<SearchResultItem> items, Activity activity) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
		this.textViewResourceId = textViewResourceId;
		this.activity = activity;
		imageLoader = new ImageLoader(context, activity);
	}
	
	public static class ViewHolder{
		public TextView title;
		public TextView pubDate;
		public TextView text;
		public TextView author;
		public ImageView thumbnail;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		ViewHolder holder;
		if (v == null)
		{
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(textViewResourceId, null);
            holder = new ViewHolder();
			holder.title = (TextView) v.findViewById(R.id.item_title);
			holder.pubDate = (TextView) v.findViewById(R.id.pub_date);
			holder.text = (TextView) v.findViewById(R.id.text);
			holder.author = (TextView) v.findViewById(R.id.author);
			holder.thumbnail = (ImageView) v.findViewById(R.id.feed_thumbnail);
			v.setTag(holder);
        } else 
        	holder=(ViewHolder)v.getTag();

			SearchResultItem 	mSearchResultItem = items.get(position);
			int					resultType = mSearchResultItem.getSearchResultType();
			
			if(resultType == Constants.SEARCH_TYPE_FEEDS)
			{
				mFeedItem = mSearchResultItem.getFeedItem(); 

				if (mFeedItem != null)
				{
					populateFeedItem(mFeedItem, holder, imageLoader);
				}
			} else if(resultType == Constants.SEARCH_TYPE_LIBRARY)
					{
						populateLibraryItem(mSearchResultItem, holder, imageLoader);
					}
		return v;
	}
	
	private void populateLibraryItem(SearchResultItem mItem, ViewHolder holder, ImageLoader mImageLoader)
	{
		if (holder.title != null) 		holder.title.setText(mItem.getLibraryItem().magazine_title);
		if (holder.pubDate != null) 	holder.pubDate.setVisibility(View.GONE);
		if (holder.thumbnail != null)
		{
			String			url = "file://" + mItem.getLibraryItem().magazine_cover_filepath;
			holder.thumbnail.setTag(url);
			mImageLoader.displayImage(url, holder.thumbnail);
		}
		if (holder.text != null) 		holder.text.setText(mItem.getLibraryItemTopicTitle());
	}
	
	private void populateFeedItem(FeedItem mFeedItem, ViewHolder holder, ImageLoader mImageLoader){
		if (holder.title != null) 		holder.title.setText(mFeedItem.title);
		if (holder.pubDate != null) 	holder.pubDate.setText(ApplicationUtils.formatDateForView(mFeedItem.publication_date));
		if (holder.text != null) 		holder.text.setText(Html.fromHtml(mFeedItem.description));
		if (holder.author != null) 		holder.author.setText("by " + mFeedItem.author);
		if (holder.thumbnail != null) 	holder.thumbnail.setTag(mFeedItem.thumbnailslink);
		if (holder.thumbnail != null && !TextUtils.isEmpty(mFeedItem.thumbnailslink))
		{
			mImageLoader.displayImage(mFeedItem.thumbnailslink, holder.thumbnail);
		}
		else if (holder.thumbnail != null && TextUtils.isEmpty(mFeedItem.thumbnailslink))
			holder.thumbnail.setImageResource(R.drawable.no_image);
		
		if (mFeedItem.isRead)
		{
			holder.title.setTextColor(context.getResources().getColor(R.color.light_blue_text));
		} else {
			holder.title.setTextColor(context.getResources().getColor(R.color.dark_blue_text));
		}
		if (holder.text != null)
		{
			if (mFeedItem.isRead)
			{
				holder.text.setTextColor(context.getResources().getColor(R.color.gray));
			} else {
				holder.text.setTextColor(context.getResources().getColor(R.color.black));
			}
		}
	}
}
