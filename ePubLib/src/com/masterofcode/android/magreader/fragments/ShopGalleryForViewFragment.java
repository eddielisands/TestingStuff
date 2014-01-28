package com.masterofcode.android.magreader.fragments;

import java.util.ConcurrentModificationException;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.LinearLayout;

import com.masterofcode.android.magreader.MainShopActivity;
import com.masterofcode.android.magreader.adapters.GridOfIssuesAdapter;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ShopGalleryForViewFragment extends Fragment{
	
	private ActiveRecordBase _db;
	private List<IssueItem> mIssues;
	private View detailsFramePort;
	private Gallery galleryView;
	private GridOfIssuesAdapter issueAdapter;
	private int mCurCheckPosition = -1;
	private LinearLayout shoploadingLayout;
	private static ShopGalleryForViewFragment Instance = null;
	
	public static ShopGalleryForViewFragment getInstance(){
		return Instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		generateArrayListToAdapter();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityCreated(Bundle savedInstance)
	{
	    super.onActivityCreated(savedInstance);

	    // 	Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        detailsFramePort = getActivity().findViewById(R.id.issue_details_container_port);
        shoploadingLayout = (LinearLayout) getActivity().findViewById(R.id.shoploadingLayout);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
        } else {
        }
        if (getmCurCheckPosition() != -1) {
			showDetails(getmCurCheckPosition());
        }
	}
	
	private OnItemClickListener galleryViewItemClickListener = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			if (position < mIssues.size()){}
				showDetails(position);
				((MainShopActivity) getActivity()).setmCurrentItemPosition(position);
				((MainShopActivity) getActivity()).setClicedItem(true);
	    }
	};
	
	public void showDetails(int position) {
		// TODO Auto-generated method stub
		IssueDetailsFragment issueDetailsFragment = (IssueDetailsFragment) getFragmentManager().findFragmentById(R.id.issue_details_fragment);
        if (issueDetailsFragment == null || issueDetailsFragment.getShownIndex() != position) {
            // Make new fragment to show this selection.
        	issueDetailsFragment = IssueDetailsFragment.newInstance(position);
        	FragmentTransaction ft = getFragmentManager().beginTransaction();
        	ft.replace(R.id.issue_details_container_port, issueDetailsFragment);
        	ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{ 
	    View returnView = inflater.inflate(R.layout.shop_issues_gallery, container, false);
	    galleryView = (Gallery) returnView.findViewById(R.id.issues_gallery);
	    issueAdapter = new GridOfIssuesAdapter(getActivity().getApplicationContext(), mIssues, getActivity());
	    galleryView.setAdapter(issueAdapter);
	    galleryView.setOnItemClickListener(galleryViewItemClickListener);
	    return returnView; 
	}
	
	public GridOfIssuesAdapter getIssueAdapter() {
		return issueAdapter;
	}

	@Override
	public void onResume(){
		super.onResume();
		if (mIssues.size() >= 0) shoploadingLayout.setVisibility(View.GONE);
		Instance = this;
	}
	
	private void generateArrayListToAdapter(){
    	try {
    		_db = ((JtjApplication)getActivity().getApplication()).getDatabase();
			_db.open();
			mIssues = _db.find(IssueItem.class, false, "_id>?", new String[] { String.valueOf("0") }, null, null, null, null);
		} catch (ActiveRecordException exc) {
			exc.printStackTrace();
		} catch (ConcurrentModificationException exc) {
			exc.printStackTrace();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
    }
	
	public static ShopGalleryForViewFragment newInstance(int index) {
		ShopGalleryForViewFragment f = new ShopGalleryForViewFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
    private int getmCurCheckPosition() {
		return mCurCheckPosition;
	}

	private void setmCurCheckPosition(int mCurCheckPosition) {
		this.mCurCheckPosition = mCurCheckPosition;
	}
}
