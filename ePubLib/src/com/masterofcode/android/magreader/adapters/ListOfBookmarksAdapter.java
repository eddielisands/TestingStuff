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

import com.masterofcode.android.magreader.db.entity.BookmarksItem;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.ImageLoader;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ListOfBookmarksAdapter extends ArrayAdapter<BookmarksItem> {
	private Context 			context;
	private List<BookmarksItem>	items;
	int							textViewResourceId;
	public ImageLoader			imageLoader;

	public ListOfBookmarksAdapter(Context context, int textViewResourceId,
			List<BookmarksItem> items, Activity activity) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
		this.textViewResourceId = textViewResourceId;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
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

		BookmarksItem bookmarkItem = items.get(position);
		
		
		// set bookmarking date as pubDate
		if (holder.pubDate != null)
		{
			String		bookmarkingDateRepresentation;
			
			bookmarkingDateRepresentation = ApplicationUtils.formatDateForBoomarkingItemView(bookmarkItem.bookmarking_date);
			holder.pubDate.setText(bookmarkingDateRepresentation);
		}

		if (bookmarkItem != null) {
			// Feed
			if(bookmarkItem.item_type==Constants.BOOKMARKS_ITEM_TYPE_FEED)
			{
				FeedItem fi = bookmarkItem.getFeedItem();
				
				if (fi != null)
				{
					if (holder.title != null) 		holder.title.setText(fi.title);
					if (holder.text != null) 		holder.text.setText(Html.fromHtml(fi.description));
					if (holder.author != null) 		holder.author.setText("by " + fi.author);
					if (holder.thumbnail != null) 	holder.thumbnail.setTag(fi.thumbnailslink);
					if (holder.thumbnail != null && !TextUtils.isEmpty(fi.thumbnailslink))
					{
						imageLoader.displayImage(fi.thumbnailslink, holder.thumbnail);
					}
					else if (holder.thumbnail != null && TextUtils.isEmpty(fi.thumbnailslink))
						holder.thumbnail.setImageResource(R.drawable.no_image);
					
					holder.title.setTextColor(context.getResources().getColor(R.color.dark_blue_text));
				}
			} else  // magazine
				if(bookmarkItem.item_type==Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE)
				{
					if (holder.title != null) holder.title.setText(bookmarkItem.magazine_title);
					if (holder.text != null) holder.text.setText(bookmarkItem.magazine_topic_title);
					if (holder.thumbnail != null)
					{
						if(bookmarkItem.magazine_cover_path!=null)
						{
							String url = "file://"+bookmarkItem.magazine_cover_path;
							holder.thumbnail.setTag(url);
							imageLoader.displayImage(url, holder.thumbnail);
						}
					}
				}
		}
		return v;
	}
	
	public void removeItemAtIndex(int index)
	{
		if(index<0) return;
		if(items==null) return;
		if(index >= items.size()) return;
		items.remove(index);
	}
}
