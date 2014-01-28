package com.masterofcode.android.magreader.fragments;

import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.masterofcode.android.magreader.FeedsActivity;
import com.masterofcode.android.magreader.adapters.ListOfFeedsAdapter;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.utils.SaveToDbQueue;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class FeedsListForViewFragment extends ListFragment {
	
	private Context mContext;
	List<FeedItem> items;
	private int layoutForList = R.layout.list_feeds_simple;
	private String searchKeywords;
	private ActiveRecordBase _db;
	private Activity mActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		_db = ((JtjApplication)getActivity().getApplication()).getDatabase();
	}
	
	public void setItemsToList(List<FeedItem> items, int curPosition, String searchKeywords) {
		mContext = getActivity().getApplicationContext();
		this.items = items;
		this.searchKeywords = searchKeywords;
		setListAdapter(new ListOfFeedsAdapter(mContext, layoutForList, items, mActivity));
		
		ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setCacheColorHint(Color.WHITE);
        
        setPosition(curPosition);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (!(((FeedsActivity) getActivity()).getCurrentPosition() == position)) {
			updateStats(position);			
		}
	}
	
	public void updateStats(int position){
		if (items == null)
			items = ((FeedsActivity) getActivity()).getItems();
		updateUnreadItemsInDb(position);
		
		updateLinksList(position, items.get(position));
		((FeedsActivity) getActivity()).setCurrentPosition(position);
		((FeedsActivity) getActivity()).setCurrentGid(items.get(position).guid);
	}
	
	@SuppressWarnings("unchecked")
	private void updateUnreadItemsInDb(int position) {
		FeedItem feed = items.get(position);
		if (feed.isRead != true) {
			List<FeedItem> feedsForUpdate = null;
			try {
				if (!_db.isOpen()) _db.open();
				feedsForUpdate = _db.find(FeedItem.class, false, "GUID=?", new String[] { String.valueOf(feed.guid) }, null, null, "PUBLICATIONDATE DESC", null);
			} catch (ActiveRecordException e1) {
				e1.printStackTrace();
			}
			for (FeedItem curFeed:feedsForUpdate) {
				curFeed.isRead = true;
				try {
					if (!_db.isOpen()) _db.open();
					SaveToDbQueue.saveToDbAsyncQueue(curFeed, null);//curFeed.save();
				} catch (ActiveRecordException e) {
					e.printStackTrace();
				}
			}
			items.get(position).isRead = true;
			if (getListView() != null && getListView().getAdapter() != null)
				((ArrayAdapter<FeedItem>) getListView().getAdapter()).notifyDataSetChanged();
		}
	}
	
	private void setPosition(int position) {
		ListView lv = getListView();
		lv.setSelection(position);
        lv.setItemChecked(position, true);       
        try{
        	updateLinksList(position, items.get(position));
        } catch (IndexOutOfBoundsException e) {
        	e.printStackTrace();
        }
	}

	private void updateLinksList(int position, FeedItem feedItem) {
		FeedsViewFragment viewFragment = (FeedsViewFragment) getFragmentManager().findFragmentById(R.id.feed_view_fragment);
		viewFragment.setViewItem(feedItem, searchKeywords);
	}
}
