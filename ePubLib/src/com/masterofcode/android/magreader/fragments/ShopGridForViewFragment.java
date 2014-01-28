package com.masterofcode.android.magreader.fragments;

import java.util.ConcurrentModificationException;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.masterofcode.android.magreader.MainShopActivity;
import com.masterofcode.android.magreader.adapters.GridOfIssuesAdapter;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ShopGridForViewFragment extends Fragment {
	
	private static final String DB_INITIALIZED = "db_initialized";
	
	private GridOfIssuesAdapter issueAdapter;
	private List<IssueItem> mIssues;
	private ActiveRecordBase _db;
	private int mCurCheckPosition = -1;
	private View detailsFrameLand;
	private View detailsFramePort;
	private GridView gridView;
	public static boolean isLandDetailShow = false;
	public static boolean isPortDetailShow = false;
	private ProgressBar shopProgressBar;
	private LinearLayout shoploadingLayout;
	private Handler mHandler;
	private static ShopGridForViewFragment Instance = null;
	
	public static ShopGridForViewFragment getInstance(){
		return Instance;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Constants.Debug)
			Log.d("purchase_test", "in onCreate() ");
		mHandler = new Handler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
	    View returnView = inflater.inflate(R.layout.shop_issues_grid, container, false);
	    return returnView; 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityCreated(Bundle savedInstance)
	{
	    super.onActivityCreated(savedInstance);
	    // 	Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
	    
        detailsFrameLand = getActivity().findViewById(R.id.issue_details_container);
        detailsFramePort = getActivity().findViewById(R.id.issue_details_container_port);
        shoploadingLayout = (LinearLayout) getActivity().findViewById(R.id.shoploadingLayout);
        if (Constants.Debug)
        	Log.d("purchase_test", "in onActivityCreated() mIssues size: ");
        if (getmCurCheckPosition() != -1) {
				showDetails(getmCurCheckPosition());
        }
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if (Constants.Debug)
			Log.d("purchase_test", "in Resume() " );
		gridView = (GridView) getView();
		gridView.setAdapter(null);
		getIssueList();
		Instance = this;
		if (mIssues != null && mIssues.size() >= 0) shoploadingLayout.setVisibility(View.GONE);
	}
	
	private void prepareShopGrid(){
		View view = getView();
		gridView = (GridView) getView();
		if (gridView != null){
		    gridView.setAdapter(null);
		    if (mIssues.size() == 0)
		    	gridView.setAdapter(null);
		    else{
		    	issueAdapter = new GridOfIssuesAdapter(getActivity().getApplicationContext(), mIssues, getActivity());
		    	gridView.setAdapter(issueAdapter);
			    gridView.setOnItemClickListener(gridViewItemClickListener);
		    }
		}
	    shoploadingLayout.setVisibility(View.GONE);
	}
	
	public void updateAdapter(List<IssueItem> mIssues){
		if (Constants.Debug)
			Log.d("purchase_test", "purchase_test updateAdapter" + mIssues.toString());
		gridView.setAdapter(null);
		issueAdapter = new GridOfIssuesAdapter(getActivity().getApplicationContext(), mIssues, getActivity());
		gridView.setAdapter(issueAdapter);
	    gridView.setOnItemClickListener(gridViewItemClickListener);
	    shoploadingLayout.setVisibility(View.GONE);
	}
	
	public GridOfIssuesAdapter getIssueAdapter() {
		return issueAdapter;
	}

	private OnItemClickListener gridViewItemClickListener = new OnItemClickListener() {
	
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			generateArrayListToAdapter();
			if (Constants.Debug)
				Log.d("purchase_test", "in onItemClick() mIssues size: " + mIssues.size() + " position = " + position);
			if (position < mIssues.size()){
				showDetails(position);
			}
	    }
	};

	
	public int getmCurCheckPosition() {
		return mCurCheckPosition;
	}

	public void setmCurCheckPosition(int mCurCheckPosition) {
		this.mCurCheckPosition = mCurCheckPosition;
	}
	
	/**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    public void showDetails(int index) {
    	((MainShopActivity) getActivity()).setmCurrentItemPosition(index);
		((MainShopActivity) getActivity()).setClicedItem(true);
        mCurCheckPosition = index;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
        	isLandDetailShow = false;
        	isPortDetailShow = detailsFramePort != null;
        	//isPortDetailShow = true;
        } else {
        		isLandDetailShow = detailsFrameLand != null;
        		//isLandDetailShow = true;
        		isPortDetailShow = false;
        }
            // Check what fragment is currently shown, replace if needed.
        	IssueDetailsFragment issueDetailsFragment = (IssueDetailsFragment) getFragmentManager().findFragmentById(R.id.issue_details_fragment);
            if (issueDetailsFragment == null || issueDetailsFragment.getShownIndex() != index) {
                // Make new fragment to show this selection.
            	issueDetailsFragment = IssueDetailsFragment.newInstance(index);
            	
                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
            	if (detailsFrameLand != null){
            		detailsFrameLand.setVisibility(View.VISIBLE);
            		ft.replace(R.id.issue_details_container, issueDetailsFragment);
            	} else
            		if (detailsFramePort != null){
            			detailsFramePort.setVisibility(View.VISIBLE);
            			//gridView.setColumnWidth(1200);
            			ShopGalleryForViewFragment shopGalleryForViewFragment = (ShopGalleryForViewFragment)getFragmentManager().findFragmentById(R.id.issue_details_gallery_fragment);
            			shopGalleryForViewFragment = ShopGalleryForViewFragment.newInstance(index); 
            			ft.replace(R.id.issue_container_port, shopGalleryForViewFragment);
                        ft.replace(R.id.issue_details_container_port, issueDetailsFragment);
            		}
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
    }
    
    public void getIssueList(){
    	if(ApplicationUtils.getPrefProperty(getActivity(), DB_INITIALIZED)){
    		if (Constants.Debug)
    			Log.d("purchase_test", "DB_INITIALIZED is true... ");
    		generateArrayListToAdapter();
    		prepareShopGrid();
    	}else{
    		 if (Constants.Debug)
    			Log.d("purchase_test", "DB_INITIALIZED is false... ");
    		 shoploadingLayout.setVisibility(View.VISIBLE);
    		 mHandler.postDelayed(new Runnable() {
                 public void run() {
                	 if (Constants.Debug)
                		 Log.d("purchase_test", "Start for generating list of issues... ");
                 	generateArrayListToAdapter();
                 	prepareShopGrid();
                 }
             }, 7000);
    	}
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
	
	
}
