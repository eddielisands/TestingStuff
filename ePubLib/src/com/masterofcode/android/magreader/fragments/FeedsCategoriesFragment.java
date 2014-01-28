package com.masterofcode.android.magreader.fragments;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.masterofcode.android.magreader.MainActivity;
import com.masterofcode.android.magreader.adapters.ListOfCategoriesAdapter;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class FeedsCategoriesFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View returnView = inflater.inflate(R.layout.feed_categories_list, container, false);
		((ListView) returnView).setOnItemClickListener(listViewItemClickListener);
		((ListView) returnView).setOnItemLongClickListener(listViewItemLongClickListener);
		return returnView; 
		
	}
	
	public void setListAdapter(ListOfCategoriesAdapter mListOfCategoriesAdapter){
		((ListView) getView()).setAdapter(mListOfCategoriesAdapter);
	}
	
	public ListView getListView(){
		return ((ListView) getView());
	}
	
	public void setListShown(boolean isShow){
		if (isShow)
			getListView().setVisibility(View.VISIBLE);
		else
			getListView().setVisibility(View.GONE);
	}
	
	public void setPosition(int position) {
		ListView lv = getListView();
        lv.setItemChecked(position, true);
        updateLinksList(lv.getItemIdAtPosition(position));
	}
	
	private OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			if (((MainActivity) getActivity()).getCurrentPosition() != position) {
				updateLinksList(id);
				((MainActivity) getActivity()).setCurrentPosition(position);
			}
		}
	};
	
	private OnItemLongClickListener listViewItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
			// TODO Auto-generated method stub
			if (id>3){
				createDialog(id).show();
			}
			return false;
		}
	};

	private Dialog createDialog(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(25);
        input.setFilters(FilterArray);
        input.setMaxLines(1);
        builder.setTitle("Rename")
            .setIcon(android.R.drawable.stat_sys_warning)
            .setMessage("Enter new name:")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(!TextUtils.isEmpty(input.getText())){
						renameCategory(input.getText().toString(), id);
						((MainActivity)getActivity()).updateAdapters();
					}
					
				}
			})
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        return builder.create();
    }
	
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (((MainActivity) getActivity()).getCurrentPosition() != position) {
			updateLinksList(id);
			((MainActivity) getActivity()).setCurrentPosition(position);
		}
	}
	
	private void updateLinksList(long id) {
		FeedsListFragment feedList = (FeedsListFragment) getFragmentManager().findFragmentById(R.id.feed_list_fragment);
		feedList.setItemsForCategories(id);
	}
	
	private void renameCategory(String name, long id){
		ActiveRecordBase _db;
		_db = ((JtjApplication)getActivity().getApplication()).getDatabase();
		try {
			if(!_db.isOpen()) _db.open();
			FeedType ft = _db.findByID(FeedType.class, id);
			ft.title = name;
			ft.update();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
	}
}
