package com.masterofcode.android.magreader.fragments;

import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.masterofcode.android.magreader.FeedsActivity;
import com.masterofcode.android.magreader.adapters.ListOfFeedsAdapter;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class FeedsListFragment extends ListFragment {
	
	private ActiveRecordBase _db;
	private Context mContext;
	private int categoryId = 0;
	private int layoutForList = R.layout.list_feeds_hard;
	private List<FeedItem> feeds = null;
	private Activity mActivity;
	
	public void setItemsForCategories(long id) {
		mContext = getActivity().getApplicationContext();
		mActivity = getActivity();
		setListShown(false);
		try {
			categoryId = (int) id;
			_db = ((JtjApplication)getActivity().getApplication()).getDatabase();
            if (!_db.isOpen()) _db.open();
			feeds = _db.find(FeedItem.class, false, "CHANNELID=?", new String[] { String.valueOf(this.categoryId) }, null, null, "PUBLICATIONDATE DESC", null);//_db.findByColumn(FeedItem.class, "CHANNELID", String.valueOf(channelId));
			setListAdapter(new ListOfFeedsAdapter(mContext, layoutForList, feeds, mActivity));
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		} finally {
			if (getListAdapter().getCount() == 0) {
				setListShown(false);
			} else {
				setListShown(true);
			}
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		FeedItem feed = feeds.get(position);
		if (feed.isRead != true) {
			List<FeedItem> feedsForUpdate = null;
			List<FeedType> lft = null;
			try {
				if (!_db.isOpen()) _db.open();
				feedsForUpdate = _db.find(FeedItem.class, false, "GUID=?", new String[] { String.valueOf(feed.guid) }, null, null, "PUBLICATIONDATE DESC", null);
				if (!_db.isOpen()) _db.open();
				lft = _db.findAll(FeedType.class);
			} catch (ActiveRecordException e1) {
				e1.printStackTrace();
			}
			for (FeedItem curFeed:feedsForUpdate) {
				curFeed.isRead = true;
				try {
					if (!_db.isOpen()) _db.open();
					curFeed.save();
					//FeedType ft = lft.get(Integer.parseInt(curFeed.channel_id) - 1);
					//ft.unread_count = ft.unread_count - 1;
					//ft.update();
				} catch (ActiveRecordException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		Intent feedsActivity = new Intent(mContext, FeedsActivity.class);
		feedsActivity.putExtra("category_id", categoryId);
		feedsActivity.putExtra("item_guid", feed.guid);
		startActivity(feedsActivity);
	}
}
