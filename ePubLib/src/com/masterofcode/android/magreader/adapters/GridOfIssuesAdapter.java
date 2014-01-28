package com.masterofcode.android.magreader.adapters;

import java.util.Iterator;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterofcode.android.magreader.MainShopActivity;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.ImageLoader;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class GridOfIssuesAdapter extends BaseAdapter {

	private Context mContext;
	private List<IssueItem> issues;
	private LayoutInflater inflater;
	private int inc = 0;
	private ImageLoader imageLoader;
	private ActiveRecordBase dbLibrary;
	private List<LibraryItem> libraryItems;
	
	public GridOfIssuesAdapter(Context mContext, List<IssueItem> issues, Activity activity){
		this.mContext = mContext;
		this.issues = issues;
		imageLoader = new ImageLoader(mContext, activity);
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.dbLibrary = JtjApplication.getInstance().getLibraryDatabase();
		try {
			if(!dbLibrary.isOpen()) dbLibrary.open();
			List<LibraryItem> mLibraryItem = dbLibrary.findAll(LibraryItem.class);
			Log.i("MagazinReader", "Loaded " + mLibraryItem.size() + " LibraryItems");
			libraryItems = mLibraryItem;
			dbLibrary.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
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
			if (MainShopActivity.isClicedItem){
				if (issues.size() % 2 != 0)
					inc = 1;
				else
					inc = 0;
			} else {
				numColumn = (ApplicationUtils.getCurrentScreenWidth(mContext) + 14) / 220;
				remainder = issues.size() % numColumn;
				if (remainder == 0){
					inc = 0;
				} else {
					inc = numColumn - remainder;
				}
			}
		} else {
			if (MainShopActivity.isClicedItem){
				inc = 0;
			} else {
				numColumn = (ApplicationUtils.getCurrentScreenWidth(mContext) + 14) / 220;
				remainder = issues.size() % numColumn;
				if (remainder == 0){
					inc = 0;
				} else {
					inc = numColumn - remainder;
				}
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
		if(v == null){
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
		Log.d("adapter", "position= " + position + " " + holder.toString());
		if (position < issues.size()){
			IssueItem issue = issues.get(position);
			/*if (((MainShopActivity) getActivity()).getmCurrentItemPosition() == position){
				View rLayout = v.findViewById(R.id.issueGridItemRelativeLay);
				rLayout.setBackgroundColor(R.color.gray);
			}*/
			if (holder.issueDate != null) {
				if ((issue.title != null) && (issue.title != "")) {
					holder.issueDate.setText(issue.title);
				} else {
					holder.issueDate.setText(ApplicationUtils.getPublicationDate(issue.publicationDate));
				}
				holder.issueDate.setVisibility(View.VISIBLE);
			};
			if (holder.issuePrice != null) {
				
				Log.i("MagazinReader", "Checking if this issue has been installed already to the library: " + issue.issueID);
				if (issue.issueID != null) {
					for (Iterator libraryItemsIterator = libraryItems.iterator(); libraryItemsIterator
							.hasNext();) {
						LibraryItem libItem = (LibraryItem) libraryItemsIterator.next();
						
						if ((libItem.magazine_id != null) && (libItem.magazine_id.equals(issue.issueID))) {
							if (Constants.Debug) {
								Log.i("MagazinReader", "We found an issue from shop which was already installed: " + libItem.magazine_id);
								Log.i("MagazinReader", "Implement change here!!!!");
							}
							//Set the button to read here, read button should open the issue as if i would click on read iin library view
						}
						
				
					}
				}
				if(!issue.downloadable){
					if(!issue.isBuyed){
						holder.issuePrice.setText("€" + issue.androidreadergooglecheckoutprice);
					} else {
						if(!issue.isDownloaded){
							holder.issuePrice.setText("DOWNLOAD");
						} else {
							holder.issuePrice.setText("READ");
						}
					}
				}
				else{
					holder.issuePrice.setBackgroundResource(R.drawable.shop_yellow_price_rec);
					if(!issue.isBuyed){
						holder.issuePrice.setText("FREE");
					} else {
						if(!issue.isDownloaded){
							holder.issuePrice.setText("DOWNLOAD");
						} else {
							holder.issuePrice.setText("READ");
						}
					}
				}
			};
			if (holder.issueImage != null) {
				holder.issueImage.setTag(issue.detailCoverUrl);
				imageLoader.displayImage( issue.detailCoverUrl, holder.issueImage);
				holder.issueImage.setVisibility(View.VISIBLE);
			}
		}
		if (position > issues.size() || position == issues.size()) { 
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
