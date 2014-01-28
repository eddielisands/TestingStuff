package com.masterofcode.android.magreader.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masterofcode.android.magreader.MainActivity;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ListOfCategoriesAdapter extends ArrayAdapter<FeedType> {
	
	private Context context;
	private int textViewResourceId;
	private List<FeedType> types;
	public int currentPosition;

	public ListOfCategoriesAdapter(Context context, int textViewResourceId,
			List<FeedType> types) {
		super(context, textViewResourceId, types);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.types = types;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(textViewResourceId, null);
		}
		
		FeedType ft = types.get(position);
		if (ft != null) { 
			currentPosition = ((MainActivity) context).getCurrentPosition();
			TextView name = (TextView) v.findViewById(R.id.category_name);
			TextView unread_count = (TextView) v.findViewById(R.id.unread_count);
			ImageView category_icon = (ImageView) v.findViewById(R.id.category_icon);
			LinearLayout category_bg_layout = (LinearLayout) v.findViewById(R.id.category_bg_layout);
			LinearLayout unread_count_layout = (LinearLayout) v.findViewById(R.id.unread_count_layout);
			if(ft.title.equals(Constants.FEED_ALL_NAME) || (position == 0)){
				category_bg_layout.setBackgroundResource(R.drawable.feed_bg_black);
				category_icon.setImageResource((position == currentPosition) ? R.drawable.icon_all_feeds_black: R.drawable.icon_all_feeds_white);
			}else if (ft.title.equals(Constants.FEED_NEWS_NAME)  || (position == 1)){
				category_bg_layout.setBackgroundResource(R.drawable.feed_bg_red);
				category_icon.setImageResource((position == currentPosition) ? R.drawable.icon_news_black : R.drawable.icon_news_white);
			} /*else if (ft.title.equals(Constants.FEED_VIDEOS_NAME) || (position == 2)){
				category_bg_layout.setBackgroundResource(R.drawable.feed_bg_red);
				category_icon.setImageResource((position == currentPosition) ? R.drawable.icon_book_black : R.drawable.icon_book_white);
			}*/else{
				category_bg_layout.setBackgroundResource(R.drawable.feed_bg_blue);
			}	
			if (currentPosition == position){
				category_bg_layout.setBackgroundResource(R.drawable.feed_bg_white);
				name.setTextColor(Color.BLACK);
			} else name.setTextColor(Color.WHITE);
			if (name != null) {
				name.setText(ft.title);
			}
			if (unread_count != null) {
				if (ft.unread_count == 0) {
					//unread_count.setVisibility(View.GONE);
					unread_count_layout.setVisibility(View.GONE);
				} else {
					//unread_count.setVisibility(View.VISIBLE);
					unread_count_layout.setVisibility(View.VISIBLE);
					unread_count.setText(String.valueOf(ft.unread_count));
				}
			}
		}
		return v;
	}
	
	@Override
	public long getItemId(int position) {
		try{
			FeedType ft = getItem(position);
			return ft.order_id;
		
		} catch (IndexOutOfBoundsException ex){
			ex.printStackTrace();
		}
		return 0;
	}

}
