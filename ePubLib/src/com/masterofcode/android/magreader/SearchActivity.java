package com.masterofcode.android.magreader;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.masterofcode.android.magreader.adapters.SearchResultsAdapter;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.search.SearchManager;
import com.masterofcode.android.magreader.search.SearchResultItem;
import com.masterofcode.android.magreader.utils.ActionBarView;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class SearchActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
	private Context 				mContext;
	private String 					mSearchKeyword;
	private int 					mSearchType;
	private ProgressBar				searchProgressBar;
	private EditText				searchCriteriaEditText;
	private CheckBox				searchTypeCheckBoxEverywhere;
	private CheckBox				searchTypeCheckBoxInNews;
	private CheckBox				searchTypeCheckBoxInMagazines;
	private ListView				searchResultsListView;
	private List<SearchResultItem>  searchResults = null;
	private int 					layoutForList = R.layout.list_search_results;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_view);
		
		this.mContext = this;
		ActionBar actionBar = getActionBar();
		
		ActionBarView.setActionBarStandartView(actionBar, mContext);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		 if (savedInstanceState==null) {                                                                         
            Intent intent = this.getIntent();                              
            mSearchKeyword = intent.getStringExtra(Constants.BUNDLE_KEY_SEARCH_KEYWORD);
        	mSearchType = intent.getIntExtra(Constants.BUNDLE_KEY_SEARCH_TYPE, Constants.SEARCH_TYPE_FEEDS);
        } else {
            mSearchKeyword = savedInstanceState.getString(Constants.BUNDLE_KEY_SEARCH_KEYWORD);
            mSearchType = savedInstanceState.getInt(Constants.BUNDLE_KEY_SEARCH_TYPE, Constants.SEARCH_TYPE_FEEDS);
        }
		 
	    searchCriteriaEditText = (EditText) findViewById(R.id.search_criteria_edit_text);
	    searchCriteriaEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                in.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

					if(!mSearchKeyword.equals(searchCriteriaEditText.getText().toString()))
					{
						mSearchKeyword = searchCriteriaEditText.getText().toString();
						
		                doSearch();
						return true;
					}
				} else {
				}
				return false;
			}
		});
	    
	    searchCriteriaEditText.setText(mSearchKeyword);
	        
	    searchProgressBar = (ProgressBar) findViewById(R.id.search_progress_bar);
	    
	    searchTypeCheckBoxEverywhere = (CheckBox) findViewById(R.id.search_type_check_box_everywhere);
	    searchTypeCheckBoxInNews = (CheckBox) findViewById(R.id.search_type_check_box_news);
	    searchTypeCheckBoxInMagazines = (CheckBox) findViewById(R.id.search_type_check_box_magazines);
	    searchResultsListView = (ListView) findViewById(R.id.search_results_list_view);
	    
	    showSearchTypeSelection();
	    
	    searchTypeCheckBoxEverywhere.setTag(Integer.valueOf(Constants.SEARCH_TYPE_EVERYWHERE));
	    searchTypeCheckBoxInNews.setTag(Integer.valueOf(Constants.SEARCH_TYPE_FEEDS));
	    searchTypeCheckBoxInMagazines.setTag(Integer.valueOf(Constants.SEARCH_TYPE_LIBRARY));
	    
	    searchTypeCheckBoxEverywhere.setOnCheckedChangeListener(this);
	    searchTypeCheckBoxInNews.setOnCheckedChangeListener(this);
	    searchTypeCheckBoxInMagazines.setOnCheckedChangeListener(this);
	    
	    searchResultsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				clickOnSearchItem(position);
			}
		});
	    
	    Button			clearSearchCriteria = (Button) findViewById(R.id.clear_search_criteria_button);
	    clearSearchCriteria.setOnClickListener(new View.OnClickListener()
	    {
			@Override
			public void onClick(View v)
			{
				clearSearchCriteria();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(Constants.BUNDLE_KEY_SEARCH_KEYWORD, mSearchKeyword);
		outState.putInt(Constants.BUNDLE_KEY_SEARCH_TYPE, mSearchType);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		doSearch();
	}

    private void showSearchTypeSelection()
    {
	    searchTypeCheckBoxEverywhere.setChecked(mSearchType==Constants.SEARCH_TYPE_EVERYWHERE);
	    searchTypeCheckBoxInNews.setChecked(mSearchType==Constants.SEARCH_TYPE_FEEDS);
	    searchTypeCheckBoxInMagazines.setChecked(mSearchType==Constants.SEARCH_TYPE_LIBRARY);
	    
	    searchTypeCheckBoxEverywhere.setClickable(mSearchType!=Constants.SEARCH_TYPE_EVERYWHERE);
	    searchTypeCheckBoxInNews.setClickable(mSearchType!=Constants.SEARCH_TYPE_FEEDS);
	    searchTypeCheckBoxInMagazines.setClickable(mSearchType!=Constants.SEARCH_TYPE_LIBRARY);
    }

	private void clearSearchCriteria()
	{
		searchCriteriaEditText.setText("");
		mSearchKeyword = "";
		searchResults = null;
		searchResultsListView.setAdapter(null);

		if(SearchActivity.this.getCurrentFocus() instanceof EditText)
		{
			InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			in.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

    private void doSearch()
    {
    	if(mSearchKeyword==null) return;
    	if(mSearchKeyword.equals("")) return;
    	new getSearchedItems().execute(String.valueOf(mSearchType), mSearchKeyword);
    }
    
    private class getSearchedItems extends AsyncTask<String, Void, List<SearchResultItem>> {

		@Override
		protected List<SearchResultItem> doInBackground(String... params) {

			int mTaskSearchType = Integer.parseInt(params[0]);
			String mTaskSearchKeyword = params[1];
			
			return SearchManager.getInstance().searchWithType(mContext, mTaskSearchType, mTaskSearchKeyword);
		}
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			searchProgressBar.setVisibility(View.VISIBLE);
			searchResultsListView.setAdapter(null);
		}
		@Override
	    protected void onPostExecute(List<SearchResultItem> searchResultList) {
			super.onPostExecute(searchResultList);
			searchProgressBar.setVisibility(View.GONE);

			searchResults = searchResultList;
			
			if(searchResults==null) searchResultsListView.setAdapter(null);
			else searchResultsListView.setAdapter(new SearchResultsAdapter(mContext, layoutForList, searchResults, SearchActivity.this));
	    }
    }
    
    private void clickOnSearchItem(long position){
    	SearchResultItem selectedSearchResultItem = searchResults.get((int) position);
    	
    	if(selectedSearchResultItem.getSearchResultType() == Constants.SEARCH_TYPE_FEEDS)
    	{
    		FeedItem		selectedFeedItem = selectedSearchResultItem.getFeedItem();
    		
    		if(selectedFeedItem!=null)
    		{
				Intent feedsActivity = new Intent(mContext, FeedsActivity.class);
				feedsActivity.putExtra("category_id", Integer.parseInt(selectedFeedItem.channel_id));
				feedsActivity.putExtra("item_guid", selectedFeedItem.guid);
				feedsActivity.putExtra("item_guid", selectedFeedItem.guid);
				feedsActivity.putExtra("search_keyword", mSearchKeyword);
				startActivity(feedsActivity);
			}
    	} else 
    		if(selectedSearchResultItem.getSearchResultType() == Constants.SEARCH_TYPE_LIBRARY)
    		{
    			Intent magazineActivity = new Intent(mContext, EpubViewerActivity.class);
				
    			magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_FILE_PATH, selectedSearchResultItem.getLibraryItem().magazine_filepath);
				magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_COVER_FILE_PATH, selectedSearchResultItem.getLibraryItem().magazine_cover_filepath);
				magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_TOPIC_INDEX, selectedSearchResultItem.getLibraryItemTopicIndex());

				magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_MODE, true);
				magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_KEYWORD, mSearchKeyword);
				magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_TOPICS, selectedSearchResultItem.getLibraryItemTopics());

				startActivity(magazineActivity);
    		}
    			
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.getTag() instanceof Integer)
		{
			int			checkedSearchType = ((Integer)buttonView.getTag()).intValue();
			
			if(checkedSearchType==Constants.SEARCH_TYPE_EVERYWHERE ||
			   checkedSearchType==Constants.SEARCH_TYPE_FEEDS ||
			   checkedSearchType==Constants.SEARCH_TYPE_LIBRARY)
			{
				if(isChecked)
				{
					mSearchType = checkedSearchType;
					showSearchTypeSelection();
					doSearch();
				}
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
