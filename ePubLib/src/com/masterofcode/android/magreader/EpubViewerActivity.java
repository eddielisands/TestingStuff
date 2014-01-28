package com.masterofcode.android.magreader;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.masterofcode.android.EPubBook.EPubBook;
import com.masterofcode.android.EPubBook.EPubBookProcessingListener;
import com.masterofcode.android.EPubBookRender.EPubBookRender;
import com.masterofcode.android.magreader.bookmarks.BookmarksManager;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class EpubViewerActivity extends Activity implements OnGesturePerformedListener {
	final public String			BUNDLE_KEY_EPUB_FILE_PATH = "epub_file_path";
	final public String			BUNDLE_KEY_EPUB_READING = "epub_reading";
	final public String			BUNDLE_KEY_EPUB_TOC_SHOW_STATUS = "epub_toc_show_status";
	final public String			BUNDLE_KEY_EPUB_BOOKMARKED_TOPICS = "epub_toc_bookmarked_topics";  // topics that bookmarked in current magazine
	final public String			BUNDLE_KEY_EPUB_FONT_CHANGE_FACTOR = "epub_font_change_factor";
	final private int			TOC_BASE_ID = 100;
	final private int			FONT_CHANGE_FACTOR_MIN = -8;
	final private int			FONT_CHANGE_FACTOR_MAX = 8;

	private int					EPUB_DOWNLOADING = 1;
	private int					EPUB_EXTRACTING = 2;
	private int					EPUB_PROCESSING_MEDIA = 3;
	private int					EPUB_COPYING = 4;
	private int					EPUB_HIGHLIGHT_TOPICS = 5;
	private int					THREAD_PROGRESS = 0;
	private int					THREAD_FINISH = 1;

	String						epubUrl;
	String						epubFilePath;
	String						epubCoverFilePath;
	boolean						epubLoaded = false;
	EPubBook					ePubBook = null;
	EPubBookRender				ePubBookRender = null;
	float						msx, msy;
	private ProgressDialog		bookExtractingProgressDialog = null;
	private ProgressDialog		bookDownloadProgressDialog = null;
	private ProgressDialog		bookCopyProgressDialog = null;
	private ProgressDialog		bookProcessMediaProgressDialog = null;
	private ProgressDialog		processingProgressDialog = null;
	private boolean				epubReading = false;
	private int					topicIndex = 0;
	private float				topicContentOffset = 0.0f;
	private boolean				tocIsShowed = false;
	private ArrayList<String> 	topicTitles;
	private ArrayList<Integer>	bookmarkedTopics = null;
	private int					previousTopicIndex = -1;
	private int					fontSizeChangeFactor = 0;
	private boolean				isHighlightingMode = false;
	private String				highlightingKeyword = null;
	private ArrayList<Integer>	highlightingTopics = null;
	private boolean				isRestored = false;
	private ActionBar 			actionBar;
	private GestureLibrary		gestureLib;
	private GestureOverlayView  gestureOverlayView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//        setContentView(R.layout.epub_view);
		gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.epub_view, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			finish();
		}
		setContentView(gestureOverlayView);

		gestureOverlayView.setEventsInterceptionEnabled(true);
		gestureOverlayView.setGestureVisible(false);

		if (savedInstanceState==null) {                                                                         
			Intent intent = this.getIntent();                              
			epubFilePath = intent.getStringExtra(Constants.BUNDLE_KEY_EPUB_FILE_PATH);
			epubCoverFilePath = intent.getStringExtra(Constants.BUNDLE_KEY_EPUB_COVER_FILE_PATH);
			topicIndex = intent.getIntExtra(Constants.BUNDLE_KEY_EPUB_TOPIC_INDEX, 0);
			topicContentOffset = intent.getFloatExtra(Constants.BUNDLE_KEY_EPUB_TOPIC_CONTENT_OFFSET, 0);

			isHighlightingMode = intent.getBooleanExtra(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_MODE, false);
			highlightingKeyword = intent.getStringExtra(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_KEYWORD);
			highlightingTopics = intent.getIntegerArrayListExtra(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_TOPICS);

		} else {
			epubReading = savedInstanceState.getBoolean(BUNDLE_KEY_EPUB_READING);
			epubCoverFilePath = savedInstanceState.getString(Constants.BUNDLE_KEY_EPUB_COVER_FILE_PATH);
			if(epubReading)
			{
				epubFilePath = savedInstanceState.getString(Constants.BUNDLE_KEY_EPUB_FILE_PATH);
				topicIndex = savedInstanceState.getInt(Constants.BUNDLE_KEY_EPUB_TOPIC_INDEX, 0);
				topicContentOffset = savedInstanceState.getFloat(Constants.BUNDLE_KEY_EPUB_TOPIC_CONTENT_OFFSET);
				tocIsShowed = savedInstanceState.getBoolean(BUNDLE_KEY_EPUB_TOC_SHOW_STATUS);
				bookmarkedTopics = savedInstanceState.getIntegerArrayList(BUNDLE_KEY_EPUB_BOOKMARKED_TOPICS);
				fontSizeChangeFactor = savedInstanceState.getInt(BUNDLE_KEY_EPUB_FONT_CHANGE_FACTOR, 0);
				isRestored = true;
			} else {
				epubFilePath = savedInstanceState.getString(Constants.BUNDLE_KEY_EPUB_FILE_PATH);
			}

			isHighlightingMode = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_MODE, false);
			if(isHighlightingMode)
			{
				highlightingKeyword = savedInstanceState.getString(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_KEYWORD);
				highlightingTopics = savedInstanceState.getIntegerArrayList(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_TOPICS);
			}
		}

		if(isHighlightingMode)
		{
			if(highlightingKeyword==null || highlightingTopics==null) isHighlightingMode = false;
		}

		this.actionBar = getActionBar();

		loadEpub();
		extractEpub();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if(epubReading)
		{
			outState.putInt(Constants.BUNDLE_KEY_EPUB_TOPIC_INDEX, ePubBookRender.getCurrentTopicIndex());
			outState.putFloat(Constants.BUNDLE_KEY_EPUB_TOPIC_CONTENT_OFFSET, ePubBookRender.getCurrentTopicContentOffset());
			outState.putBoolean(BUNDLE_KEY_EPUB_TOC_SHOW_STATUS, ePubBookRender.isTOCShow());
			outState.putInt(BUNDLE_KEY_EPUB_FONT_CHANGE_FACTOR, fontSizeChangeFactor);
		}

		outState.putBoolean(BUNDLE_KEY_EPUB_READING, epubReading);
		outState.putString(Constants.BUNDLE_KEY_EPUB_FILE_PATH, epubFilePath);
		outState.putString(Constants.BUNDLE_KEY_EPUB_COVER_FILE_PATH, epubCoverFilePath);
		outState.putIntegerArrayList(BUNDLE_KEY_EPUB_BOOKMARKED_TOPICS, bookmarkedTopics);

		outState.putBoolean(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_MODE, isHighlightingMode);
		if(isHighlightingMode)
		{
			outState.putString(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_KEYWORD, highlightingKeyword);
			outState.putIntegerArrayList(Constants.BUNDLE_KEY_EPUB_HIGHLIGHTING_TOPICS, highlightingTopics);
		}
	}


	private void loadFontChangeFactor()
	{
		if(epubLoaded)
		{
			fontSizeChangeFactor = ApplicationUtils.getPrefPropertyInt(this, ePubBook.getBookName()+".font_change_factor");			
		}
	}

	private void saveFontChangeFactor()
	{
		if(epubLoaded)
		{
			ApplicationUtils.setPrefProperty(this, ePubBook.getBookName()+".font_change_factor", fontSizeChangeFactor);			
		}
	}

	public void startReadingEpub()
	{
		epubReading = true;
		ePubBookRender = new EPubBookRender(ePubBook, this, (ViewGroup) findViewById(R.id.renderContainer));
		ePubBookRender.setActionBar(actionBar);
		ePubBookRender.setRenderController((ViewGroup) findViewById(R.id.renderController));
		ePubBookRender.setRenderView((ViewGroup) findViewById(R.id.renderView));
		ePubBookRender.setCurrentTopicIndex(topicIndex);
		ePubBookRender.setFontChangeFactor(fontSizeChangeFactor);
		ePubBookRender.setHighlightMode(isHighlightingMode);
		ePubBookRender.setTocEventsHandler(TOCEventsHandler);
		if(isHighlightingMode)
		{
			ePubBookRender.setHighlightningTopics(highlightingTopics);
		}
		ePubBookRender.render();
		ePubBookRender.setCurrentTopicContentOffset(topicContentOffset);
		ePubBookRender.setTopicChangeHandler(topicChangeHandler);

		if(bookmarkedTopics==null)
			bookmarkedTopics = BookmarksManager.GetInstance().queryBookmarkedTopics(ePubBook.getBookName());

		if(tocIsShowed)ePubBookRender.showTOC();

		// topic titles
		ePubBook.loatTopicTitles();
		topicTitles = ePubBook.getTopicTitles();

		if(topicTitles!=null)
		{
			this.invalidateOptionsMenu();
		}
	}

	public void extractEpub()
	{
		if(!ePubBook.isNeedExtract())
		{
			processEPubMedia();
			return;
		}

		if(bookExtractingProgressDialog==null) bookExtractingProgressDialog = new ProgressDialog(this);
		bookExtractingProgressDialog.setCancelable(false);
		bookExtractingProgressDialog.setMessage("Extracting...");
		bookExtractingProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		bookExtractingProgressDialog.setProgress(0);
		bookExtractingProgressDialog.setMax(100);
		bookExtractingProgressDialog.show();

		Thread background = new Thread (new Runnable() {
			public void run() {
				ePubBook.extractContentToExternalStorage(new EPubBookProcessingListener() {
					@Override
					public void onProgress(long percentsExtracted) {
						progressHandler.sendMessage(progressHandler.obtainMessage(EPUB_EXTRACTING, THREAD_PROGRESS, (int) percentsExtracted));

					}
				});
				progressHandler.sendMessage(progressHandler.obtainMessage(EPUB_EXTRACTING, THREAD_FINISH, 0));
			}
		});
		background.start();
	}

	private void hightlightMagazineTopics()
	{
		if(!isHighlightingMode)
		{
			startReadingEpub();
			return;
		}

		if(processingProgressDialog==null) processingProgressDialog = new ProgressDialog(this);
		processingProgressDialog.setCancelable(false);
		processingProgressDialog.setMessage("Highlight topics keywords...");
		processingProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		processingProgressDialog.setProgress(0);
		processingProgressDialog.setMax(highlightingTopics.size());
		processingProgressDialog.show();

		Thread background = new Thread (new Runnable() {
			public void run() {
				ePubBook.hightlightTopics(highlightingTopics, highlightingKeyword, new EPubBookProcessingListener() {
					@Override
					public void onProgress(long topicsHighlighted) {
						progressHandler.sendMessage(progressHandler.obtainMessage(EPUB_HIGHLIGHT_TOPICS, THREAD_PROGRESS, (int) topicsHighlighted));

					}
				});
				progressHandler.sendMessage(progressHandler.obtainMessage(EPUB_HIGHLIGHT_TOPICS, THREAD_FINISH, 0));
			}
		});
		background.start();
	}

	private void processEPubMedia()
	{
		if(!ePubBook.isNeedProcessingMedia())
		{
			if(isHighlightingMode && !isRestored)hightlightMagazineTopics();
			else startReadingEpub();
			return;
		}

		Log.i("---", "start processing");

		if(bookProcessMediaProgressDialog==null) bookProcessMediaProgressDialog = new ProgressDialog(this);
		bookProcessMediaProgressDialog.setCancelable(false);
		bookProcessMediaProgressDialog.setMessage("Processing media...");
		bookProcessMediaProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		bookProcessMediaProgressDialog.setProgress(0);
		bookProcessMediaProgressDialog.setMax(100);
		bookProcessMediaProgressDialog.show();

		Thread background = new Thread (new Runnable() {
			public void run() {
				ePubBook.processMedia(new EPubBookProcessingListener() {
					@Override
					public void onProgress(long percentsExtracted) {
						progressHandler.sendMessage(progressHandler.obtainMessage(EPUB_PROCESSING_MEDIA, THREAD_PROGRESS, (int) percentsExtracted));
					}
				});
				progressHandler.sendMessage(progressHandler.obtainMessage(EPUB_PROCESSING_MEDIA, THREAD_FINISH, 0));
			}
		});
		background.start();
	}

	public void loadEpub()
	{
		ePubBook = new EPubBook(this, epubFilePath, true);
		try {
			ePubBook.load();
			epubLoaded = true;
			if(topicIndex<0 || topicIndex>=ePubBook.getTopicsCount() ) topicIndex = 0;
			loadFontChangeFactor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unused")
	private void doFontSizeDecrease()
	{
		if(fontSizeChangeFactor==FONT_CHANGE_FACTOR_MIN)return;
		if(!epubReading)return;
		fontSizeChangeFactor--;
		ePubBookRender.changeFontWithFactor(fontSizeChangeFactor);
		saveFontChangeFactor();
	}

	@SuppressWarnings("unused")
	private void doFontSizeIncrease()
	{
		if(fontSizeChangeFactor==FONT_CHANGE_FACTOR_MAX)return;
		if(!epubReading)return;
		fontSizeChangeFactor++;
		ePubBookRender.changeFontWithFactor(fontSizeChangeFactor);
		saveFontChangeFactor();
	}

	private boolean isTopicIndexBookmarked(int index)
	{
		if(bookmarkedTopics!=null)
		{
			for(int i=0; i<bookmarkedTopics.size(); i++)
			{
				if(bookmarkedTopics.get(i).intValue()==index) return true;
			}
		}
		return false;
	}

	private void doBookmark()
	{
		if(epubReading)
		{
			int			topicIndex = ePubBookRender.getCurrentTopicIndex();
			if(!isTopicIndexBookmarked(topicIndex))
			{
				String topicTitle = null;

				if(topicTitles!=null)
				{
					if(topicIndex>0 && topicIndex<topicTitles.size()) topicTitle = topicTitles.get(topicIndex);
				}

				String magazineTitle = ePubBook.getTitle();

				BookmarksManager.GetInstance().BookmarkMagazine(magazineTitle, ePubBook.getBookName(), epubFilePath, epubCoverFilePath, ePubBookRender.getCurrentTopicFilePath(), topicTitle, topicIndex, ePubBookRender!=null ? ePubBookRender.getCurrentTopicContentOffset() : 0);
				Toast.makeText(this, R.string.bookmarks_topic_added, Toast.LENGTH_SHORT).show();
				bookmarkedTopics.add(Integer.valueOf(topicIndex));
				invalidateOptionsMenu();
			}
		}
	}

	private void goToTopicViaTextTOC(int index)
	{
		if(ePubBookRender!=null) ePubBookRender.goToTopicViaTextTOC(index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.magazine_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		// check bookmarking status
		MenuItem itemBookmark = menu.findItem(R.id.menu_magazine_bookmark);
		if(itemBookmark!=null)
		{
			if(epubReading)
			{
				if(ePubBookRender!=null)
				{
					if(isTopicIndexBookmarked(ePubBookRender.getCurrentTopicIndex()))
					{
						itemBookmark.setEnabled(false);
						itemBookmark.setVisible(false);
					} else {
						itemBookmark.setEnabled(true);
						itemBookmark.setVisible(true);
					}
				}
			}
		}

		// prepare toc
		MenuItem item = menu.findItem(R.id.menu_magazine_toc);

		if(item!=null)
		{
			SubMenu m = item.getSubMenu();

			if(m!=null)
			{
				if(topicTitles!=null)
				{
					// remove old
					ArrayList<Integer>		forRemove = new ArrayList<Integer>();
					for(int i=0; i<m.size(); i++)
					{
						MenuItem		cur = m.getItem(i);
						forRemove.add(Integer.valueOf(cur.getItemId()));
					}

					for(int i=0; i<forRemove.size(); i++)
					{
						m.removeItem(forRemove.get(i).intValue());
					}

					// add new
					for(int i=0; i<topicTitles.size(); i++)
					{
						//						MenuItem	newItem = 
						m.add(Menu.NONE, TOC_BASE_ID+i, i, topicTitles.get(i));

						//						if(i==2)
						//						{
						//							newItem.setCheckable(true);
						//							newItem.setChecked(true);
						//						}
					}
				}
			}
		}

		//search
		final SearchView searchView = (SearchView) menu.findItem(R.id.menu_magazine_search).getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchView.setIconified(true);
				Intent		intent = new Intent(EpubViewerActivity.this, SearchActivity.class);

				intent.putExtra(Constants.BUNDLE_KEY_SEARCH_KEYWORD, query);
				intent.putExtra(Constants.BUNDLE_KEY_SEARCH_TYPE, Constants.SEARCH_TYPE_LIBRARY);
				startActivity(intent);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int			itemId = item.getItemId();

		if(topicTitles!=null)
		{
			if(itemId>=TOC_BASE_ID && itemId<TOC_BASE_ID + topicTitles.size())
			{
				goToTopicViaTextTOC(itemId - TOC_BASE_ID);
				return true;
			}
		}

		if (itemId == R.id.menu_magazine_bookmark) {
			doBookmark();
			return true;
		} else if (itemId == android.R.id.home) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}


	Handler topicChangeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==0)
			{
				int			newTopicIndex = msg.arg1;

				//        		Log.i("===============", "topic changed="+newTopicIndex);

				if(previousTopicIndex==-1)
				{
					invalidateOptionsMenu();		// there is no previous topic
				} else {
					if(isTopicIndexBookmarked(previousTopicIndex)!=isTopicIndexBookmarked(newTopicIndex))
						invalidateOptionsMenu();
				}

				previousTopicIndex = newTopicIndex;
			} else if(msg.what==1)
			{
				invalidateOptionsMenu();
			}
		}
	};

	Handler TOCEventsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==EPubBookRender.TOC_EVENT_SHOW)
			{
				gestureOverlayView.setEventsInterceptionEnabled(false);
			} else if(msg.what==EPubBookRender.TOC_EVENT_HIDE)
			{
				gestureOverlayView.setEventsInterceptionEnabled(true);
			}  
		}
	};
	/*
	Handler renderEventsHandler = new Handler() {
        public void handleMessage(Message msg) {
        	if(msg.what==0)
        	{

        	}
        }
	};
	 */	
	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==EPUB_EXTRACTING)
			{
				if(msg.arg1==THREAD_PROGRESS)
				{
					bookExtractingProgressDialog.setProgress(msg.arg2);
				} else if (msg.arg1==THREAD_FINISH) {
					bookExtractingProgressDialog.dismiss();
					processEPubMedia();
				}
			} else if (msg.what==EPUB_DOWNLOADING){
				if(msg.arg1==THREAD_FINISH) {
					bookDownloadProgressDialog.dismiss();
					loadEpub();
					extractEpub();
				}
			} else if (msg.what==EPUB_PROCESSING_MEDIA){
				if(msg.arg1==THREAD_FINISH)
				{
					bookProcessMediaProgressDialog.dismiss();
					if(isHighlightingMode)hightlightMagazineTopics();
					else startReadingEpub();
				} else {
					bookProcessMediaProgressDialog.setProgress(msg.arg2);
				}
			} else if (msg.what==EPUB_COPYING){
				if(msg.arg1==THREAD_FINISH) {
					bookCopyProgressDialog.dismiss();
					loadEpub();
					extractEpub();
				}
			} else if (msg.what==EPUB_HIGHLIGHT_TOPICS){
				if(msg.arg1==THREAD_FINISH) {
					processingProgressDialog.dismiss();
					startReadingEpub();
				} else {
					processingProgressDialog.setProgress(msg.arg2);
				}
			}
		}
	};

	public void openLink(String url) {
		DialogFragment newFragment = OpenExternalLinkDialogFragment.newInstance(R.string.magazine_open_link_question_subtitle, url, this);
		newFragment.show(getFragmentManager(), "dialog");
	}

	public void openExternalLink(String url) {
		try{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		} catch (Exception e)
		{

		}
	}

	public static class OpenExternalLinkDialogFragment extends DialogFragment {

		public static OpenExternalLinkDialogFragment newInstance(int title, String url, Activity context) {
			OpenExternalLinkDialogFragment frag = new OpenExternalLinkDialogFragment();
			Bundle args = new Bundle();
			args.putInt("title", title);
			args.putString("url", url);

			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int title = getArguments().getInt("title");
			final String url = getArguments().getString("url");

			return new AlertDialog.Builder(getActivity())
			.setTitle(title)
			.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					((EpubViewerActivity)getActivity()).openExternalLink(url);
				}
			}
					)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}
							)
							.create();
		}
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture)
	{
		if(ePubBookRender!=null)
		{
			if(ePubBookRender.isTOCShow())return;
		}

		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);

		if(predictions!=null)
		{
			for (Prediction prediction : predictions) {
				if (prediction.score > 1.0) {
					if(prediction.name.equalsIgnoreCase("prev_topic"))
					{
						if(ePubBookRender!=null)ePubBookRender.loadPrevTopic();
						break;
					}
					else if(prediction.name.equalsIgnoreCase("next_topic"))
					{
						if(ePubBookRender!=null)ePubBookRender.loadNextTopic();
						break;
					} 
				}
			}	
		}
	}
}
