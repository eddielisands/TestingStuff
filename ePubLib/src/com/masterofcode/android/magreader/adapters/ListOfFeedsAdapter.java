package com.masterofcode.android.magreader.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.ImageLoader;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ListOfFeedsAdapter extends ArrayAdapter<FeedItem> {
	
	private Context context;
	private List<FeedItem> items;
	int textViewResourceId;
	public ImageLoader imageLoader;
	private Activity activity;

	public ListOfFeedsAdapter(Context context, int textViewResourceId,
			List<FeedItem> items, Activity activity) {
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
		public ImageView favicon;
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
			//holder.text = (TextView) v.findViewById(R.id.text);
			holder.author = (TextView) v.findViewById(R.id.author);
			holder.thumbnail = (ImageView) v.findViewById(R.id.feed_thumbnail);
			holder.favicon = (ImageView) v.findViewById(R.id.feed_favicon);
			v.setTag(holder);
        } else 
        	holder=(ViewHolder)v.getTag();

		FeedItem fi = items.get(position);

		if (fi != null)
		{
			String faviconlink = "http://www.google.com/s2/favicons?domain="  + ApplicationUtils.getHostName(fi.link);
			if (holder.title != null) 		holder.title.setText(fi.title);
			if (holder.pubDate != null) 	holder.pubDate.setText(ApplicationUtils.formatDateForView(fi.publication_date));
			/*if (holder.text != null && fi.description != null) {		
				holder.text.setText(Html.fromHtml(fi.description));
			}*/
			if (holder.author != null && !TextUtils.isEmpty(fi.author))	holder.author.setText(fi.author);
			if (holder.thumbnail != null) 	holder.thumbnail.setTag(fi.thumbnailslink);
			if (holder.favicon != null) 	holder.favicon.setTag(faviconlink);
			if (holder.thumbnail != null && !TextUtils.isEmpty(fi.thumbnailslink))
			{
				imageLoader.displayImage(fi.thumbnailslink, holder.thumbnail);
			}
			else if (holder.thumbnail != null && TextUtils.isEmpty(fi.thumbnailslink))
				holder.thumbnail.setImageResource(R.drawable.no_image);
			if (holder.favicon != null && !TextUtils.isEmpty(fi.link))
			{
				imageLoader.displayImage(faviconlink, holder.favicon);
			}
			
			if (fi.isRead)
			{
				holder.title.setTextColor(context.getResources().getColor(R.color.light_blue_text));
			} else {
				holder.title.setTextColor(context.getResources().getColor(R.color.dark_blue_text));
			}
		}
	
		/*if (holder.text != null)
		{
			if (fi.isRead)
			{
				holder.text.setTextColor(context.getResources().getColor(R.color.gray));
			} else {
				holder.text.setTextColor(context.getResources().getColor(R.color.black));
			}
		}*/
		
		return v;
	}
}
