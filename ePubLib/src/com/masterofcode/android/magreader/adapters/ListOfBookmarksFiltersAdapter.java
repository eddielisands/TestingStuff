package com.masterofcode.android.magreader.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ListOfBookmarksFiltersAdapter extends ArrayAdapter<String> {
	
	private Context context;
	private int textViewResourceId;
	private List<String> filters;
	private int currentPosition = 0;

	public ListOfBookmarksFiltersAdapter(Context context, int textViewResourceId,
			List<String> filters) {
		super(context, textViewResourceId, filters);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.filters = filters;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.bookmarks_filters_list_item, null);
		}
		
		String filter = filters.get(position);
		if (filter != null)
		{ 
			TextView name = (TextView) v.findViewById(R.id.bookmarks_filter_name);
			name.setText(filter);
			
			LinearLayout   parentLayout = (LinearLayout) v.findViewById(R.id.bookmarks_filter_linear_layout);
			
			if (currentPosition == position)
			{
				parentLayout.setBackgroundResource(R.drawable.feed_bg_black);
//				name.setTextColor(Color.WHITE);
			} else {
				parentLayout.setBackgroundResource(R.drawable.feed_bg_red);
//				name.setTextColor(Color.WHITE);
			}
		}
		return v;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getCurrentPosition()
	{
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition)
	{
		this.currentPosition = currentPosition;
	}
}
