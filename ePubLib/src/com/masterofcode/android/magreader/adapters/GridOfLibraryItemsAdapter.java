package com.masterofcode.android.magreader.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.ImageLoader;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class GridOfLibraryItemsAdapter extends BaseAdapter {

	private Context mContext;
	private List<LibraryItem> issues;
	private LayoutInflater inflater;
	private int inc = 0;
	private ImageLoader imageLoader;
	
	public GridOfLibraryItemsAdapter(Context mContext, List<LibraryItem> issues, Activity activity){
		this.mContext = mContext;
		this.issues = issues;
		imageLoader = new ImageLoader(mContext, activity);
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public static class ViewHolder{
		public TextView issueDate;
		public TextView issuePrice;
		public ImageView issueImage;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int numColumn = 0;
		int remainder = 0;
		if (ApplicationUtils.isLandscape(mContext)){
			numColumn = (ApplicationUtils.getCurrentScreenWidth(mContext) + 14) / 220;
			remainder = issues.size() % numColumn;
			if (remainder == 0){
				inc = 0;
			} else {
				inc = numColumn - remainder;
			}
		} else {
			numColumn = (ApplicationUtils.getCurrentScreenWidth(mContext) + 14) / 220;
			remainder = issues.size() % numColumn;
			if (remainder == 0){
				inc = 0;
			} else {
				inc = numColumn - remainder;
			}
		}
		
		return issues.size() + inc;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		ViewHolder holder;
        //try to inflate the view
		if(v == null)
		{
			try
	        {
	            v = inflater.inflate(R.layout.shop_grid_item, null);
	        }
	        catch (NullPointerException npe)
	        {
	            npe.printStackTrace();
	        }
			holder = new ViewHolder();
			holder.issueDate = (TextView) v.findViewById(R.id.dateTextView);
			holder.issuePrice = (TextView) v.findViewById(R.id.priceTextView);
			holder.issueImage = (ImageView) v.findViewById(R.id.issue_image);
			
			v.setTag(holder);
		} else
			holder = (ViewHolder)v.getTag();
		
		if (position < issues.size()){
			LibraryItem issue = issues.get(position);
			Log.i("MagazineReader", "loaded libray item with id: " + issue.magazine_id);
			if (holder.issueDate != null)
			{
				if ((issue.magazine_title != null) && (issue.magazine_title != "")) {
					holder.issueDate.setText(issue.magazine_title);
				}
			}
			
			if (holder.issuePrice != null) {
				if (!issue.isDownloaded){
					holder.issuePrice.setText("DOWNLOAD");
				} else{
					holder.issuePrice.setText("READ");
				}
			};
			
			if (holder.issueImage != null)
			{
				holder.issueImage.setImageDrawable(null);
				if(issue.magazine_cover_filepath!=null)
				{
					String		url = "file://"+issue.magazine_cover_filepath;
					holder.issueImage.setTag(url);
					imageLoader.displayImage( url, holder.issueImage);
				}
			}
		} else {
			try {
	            	holder.issueImage.setVisibility(View.GONE);
	            	holder.issueDate.setVisibility(View.GONE);
	            	holder.issuePrice.setVisibility(View.GONE);
	        }
	        catch (NullPointerException npe) {
	            npe.printStackTrace();
	        }
		}
        return v;
	}
}
