package com.masterofcode.android.EPubBookRender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.masterofcode.android.EPubBook.EPubBook;
import com.masterofcode.android.magreader.EpubViewerActivity;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class EPubBookTopicContainer extends Object {
	private static final int 						PAGINATION_DELAY_M = 1000;
	
	static final public int							CONTENT_PAGE_FINISHED = 1;
	static final public int							CONTENT_SCROLLING_FINISHED = 2;
	static final public int							CONTENT_PAGINATION_FINISHED = 3;
	static final public int							CONTENT_UPDATE_PAGE_INFO = 4;
//	static final public int							CONTENT_UPDATE_PAGE_INFO = 4;
	
	static final public int							REQUESTING_TOP_OFFSET_STATUS_NOT_DETERMINED = 0;
	static final public int							REQUESTING_TOP_OFFSET_STATUS_IN_PROCESS = 1;
	static final public int							REQUESTING_TOP_OFFSET_STATUS_PROCESSED = 2;

	static final public int						    CONTENT_LOADING_STATUS_NOT_LOADED = 0;
	static final public int						    CONTENT_LOADING_STATUS_LOADIN_IN_PROCESS = 1;
	static final public int						    CONTENT_LOADING_STATUS_LOADINF_FINISHED = 2;

	static final private int					    MESSAGE_CHANGE_CONTENT_OFFSET = 1;

	static final private int						MISC_MESSAGE_PAGINATION = 1;
	
	static final private String						EMPTY_HTML = "<html><head><style type='text/css'>body { background-color: #FFFFFF; width: 350px; margin: 1px }</style></head><body></body></html>";

	private int										topicId = -1;
	private boolean									isTopicActual = false;
	private WebView									mediaWebView;
	private ContentWebView							contentWebView;
	private WebViewClient							mediaWebClient;
	private WebViewClient							contentWebClient;
	private	View									parentContainer;
	private Activity								context;
	private boolean									fullScreenMode;
	private String									mediaURL, contentURL;
	private	Handler									topicHandler;
	private	Handler									parentScrollHandler;
	private String									mediaJSON;
	private HashMap<String, EmbeddedMediaElement>	mediaElements;
	private int										requestingMediaElementsTopOffsetStatus = REQUESTING_TOP_OFFSET_STATUS_NOT_DETERMINED;
	private String									contentBaseURL;
	private int										contentLoadingStatus = CONTENT_LOADING_STATUS_NOT_LOADED;
	private int										mediaLoadingStatus = CONTENT_LOADING_STATUS_NOT_LOADED;
	private float									contentOffset = 0.0f;
	private boolean									deviceOrientationIsLandscape = false;
	private int										fontChangeFactor = -1;
	private int										initialFontSize;
    private boolean									isHighlightMode = false;
    @SuppressWarnings("unused")
	private EPubBook								book = null;
    private int 									paginationPageCount = -1;						
    private boolean 								paginationFinished = false;		
    private	int										offsetHeightTries = 0;

	public EPubBookTopicContainer(final Activity context, boolean orientationIsLandscape, Handler ParentScrollHandler)
	{
		this.context = context;
		deviceOrientationIsLandscape = orientationIsLandscape;
		
		mediaElements = new HashMap<String, EmbeddedMediaElement>();
		
		LayoutInflater inflater = context.getLayoutInflater();                                                                                        
		parentContainer = inflater.inflate(R.layout.epub_container_layout, null);
		
		mediaWebView = (WebView) parentContainer.findViewById(R.id.mediaWebView);
		contentWebView = (ContentWebView) parentContainer.findViewById(R.id.contentWebView);
		
		if(!deviceOrientationIsLandscape)mediaWebView.setVisibility(View.GONE);
		
//		mediaWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mediaWebView.getSettings().setBuiltInZoomControls(false);
		//mediaWebView.setInitialScale(100);
		//mediaWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);

//		contentWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		parentScrollHandler = ParentScrollHandler;

		contentWebClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("http://embedded_select/"))
				{
					jumpToMediaWithId(url.replace("http://embedded_select/", ""));
				} else 
					if(url.startsWith("http://embedded_external/"))
					{
						showFullScreenMediaWithId(url.replace("http://embedded_external/", ""));
					} else {
						// open external url
						if(url.matches("(?i)^http(s|)://.*") || url.startsWith("www."))
						{
							((EpubViewerActivity)context).openLink(url);
						}
					}
				//Log.i("-------", "load url "+url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				//Log.i("-------", "TOPIC id="+topicId+", loaded topic "+url);
				//messageHandler.sendMessageDelayed(messageHandler.obtainMessage(MESSAGE_CHANGE_CONTENT_OFFSET, 0, 0), 1000);
				// after loadind content, set buttons
				//if(parentScrollHandler != null)parentScrollHandler.sendMessageDelayed(parentScrollHandler.obtainMessage(CONTENT_SCROLLING_FINISHED, 0, 0), 1000);
				//if(parentScrollHandler != null)parentScrollHandler.sendMessageDelayed(parentScrollHandler.obtainMessage(CONTENT_SCROLLING_FINISHED, 0, 0), 2000);
//				scrollHandler.sendMessageDelayed(scrollHandler.obtainMessage(1, 0, 0), 500);
				
				//Log.i("---", "paginationFinished="+paginationFinished+", height="+contentWebView.getActualSizeHeight());
				
			}
			
		};
		
		contentWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			    
				//storeContentWebConsoleMessage(consoleMessage.message(), consoleMessage.lineNumber(), consoleMessage.sourceId());
				return true;
			}


			@Override
			public boolean onJsTimeout() {
				return false;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress==100)
				{
					contentLoadingStatus = CONTENT_LOADING_STATUS_LOADINF_FINISHED;
					if(topicHandler != null)
					{
						topicHandler.sendMessage(topicHandler.obtainMessage(CONTENT_PAGE_FINISHED, 0, 0));
					}

					if(!paginationFinished && contentWebView.getActualSizeHeight()!=-1)
					{
						//paginateContent();
					}
					
	            	if(mediaURL!=null) requestContentMediaElementsTopOffset();
					//messageHandler.sendMessage(messageHandler.obtainMessage(MESSAGE_CHANGE_CONTENT_OFFSET, 0, 0));
	            	messageHandler.sendMessageDelayed(messageHandler.obtainMessage(MESSAGE_CHANGE_CONTENT_OFFSET, 0, 0), 100);
				}
			}
		});
		
		contentWebView.setOnSizeChangeListener(new OnSizeChangedListener() {
			@Override
			public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
				contentWebViewSizeChanged(width, height, oldWidth, oldHeight);
			}
		});
		
		contentWebView.setOnScrollChangeListener(new OnScrollChangedListener() {
			@Override
			public void OnScrollChanged(int l, int t, int oldl, int oldt) {
				contentScrollChanged(l, t, oldl, oldt);
			}
		});

		mediaWebClient = new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mediaLoadingStatus = CONTENT_LOADING_STATUS_LOADINF_FINISHED;
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("http://embedded_select/"))
				{
					showFullScreenMediaWithId(url.replace("http://embedded_select/", ""));
				}
//				Log.i("-------", "load url "+url);
				return true;
			}
		};
		
		mediaWebView.setWebViewClient(mediaWebClient);
		contentWebView.setWebViewClient(contentWebClient);
		
        mediaWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        
        class ContentScriptInterface  
        {  
            @SuppressWarnings("unused")
            public void elementTop(String id, int topOffset)  
            {  
            	EmbeddedMediaElement		element = mediaElements.get(id);
            	
            	if(id!=null)
            	{
            		element.setTopOffset(topOffset);
            		checkTopOffsetEndProcessing();
//            		Log.d("-------------", "id="+id+", top="+topOffset);
            	}
            }  

            @SuppressWarnings("unused")
            public void paginationFinished(int _paginationPageCount)  
            {
//            	paginationPageCount = _paginationPageCount;
//            	paginationFinished = true;
//            	parentScrollHandler.sendMessage(parentScrollHandler.obtainMessage(CONTENT_PAGINATION_FINISHED, 0, 0));
//            	cachePaginatedContentCall();
//            	requestContentMediaElementsTopOffset();
				//messageHandler.sendMessage(messageHandler.obtainMessage(MESSAGE_CHANGE_CONTENT_OFFSET, 0, 0));
//            	messageHandler.sendMessageDelayed(messageHandler.obtainMessage(MESSAGE_CHANGE_CONTENT_OFFSET, 0, 0), 100);
            }

            @SuppressWarnings("unused")
            public void documentContent(String content)  
            {
            	if(!isHighlightMode)cachePaginatedContent(content);
            }
            
            @SuppressWarnings("unused")
            public void bodyElement(String content)
            {
//            	if(!isHighlightMode)cacheBodyContent(content);
            }
        }  
          
        contentWebView.addJavascriptInterface(new ContentScriptInterface(), "HTMLOUT"); 
        
		mediaWebView.setClickable(true);
		mediaWebView.setLongClickable(false);
		
//		contentWebView.isS
		
	}
	
	
	protected void contentScrollChanged(int l, int t, int oldl, int oldt)
	{
		if(requestingMediaElementsTopOffsetStatus==REQUESTING_TOP_OFFSET_STATUS_PROCESSED)scrollToPageMedia();
	}


	private void cachePaginatedContentCall()
	{
		if(isHighlightMode)return;
		if(topicURLIsCaching(contentURL)) return;
		//contentWebView.loadUrl("javascript:window.HTMLOUT.documentContent(document.getElementsByTagName('html')[0].outerHTML);");
		contentWebView.loadUrl("javascript:jtj_get_body();");
	}

	private void cachePaginatedContent(String content)
	{
		//Log.i("---", content);
	}

	private String cacheFileNameFromOriginalLocalURL(String urlStr)
	{
		URL url;
		try {
			url = new URL(urlStr);
			return url.getFile() + String.format(".cache.%s.xhtml", deviceOrientationIsLandscape ? "l" : "p");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean topicURLIsCaching(String urlStr)
	{
		if(urlStr.contains(".cache.l") || urlStr.contains(".cache.p")) return true;
		return false;
	}
	
	private void cacheBodyContent(final String content)
	{
		Thread background = new Thread (new Runnable()
		{
            public void run()
            {
            	String			cacheFileName = cacheFileNameFromOriginalLocalURL(contentURL);
            	
            	if(cacheFileName!=null)
            	{
					try {
						URL      	curl = new URL(contentURL);
						String      srcFilePath = curl.getFile();
						File		file = new File(srcFilePath);
						
						if(file.canRead())
						{
							BufferedReader r;
							r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
							StringBuilder		total = new StringBuilder();
							String 				line;
							while ((line = r.readLine()) != null) {
							    total.append(line);
							}
							
							String oldContent = total.toString();
							String newContent = oldContent.replaceAll("(?i)<body.*</body>", content);
							
							OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(cacheFileName));
							out.write(newContent);
							out.close();

							out = new OutputStreamWriter(new FileOutputStream(cacheFileName+".done"));
							out.write(".");
							out.close();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
            }
        });
        background.start();
	}
	
	public void loadBlankContent()
	{
		String			blankContent = "<html><head></head><body><div style='width:2000; height:2000; overflow: hidden;'>&nbsp;</body>";
		contentWebView.loadData(blankContent, "text/html", "UTF-8");
	}

	private boolean tryLoadCachedContent()
	{
		if(isHighlightMode)return false;
		
		if(topicURLIsCaching(contentURL))
		{
//			Log.d("-------", "Topic URL os caching="+contentURL);
			return false;
		}
		String		cachFileName = cacheFileNameFromOriginalLocalURL(contentURL);
		File		file = new File(cachFileName + ".done");
		
		if(file.canRead())
		{
			contentURL = "file://" + cachFileName;
			return true;
		}
		
		return false;
	}

	private void storeContentWebConsoleMessage(String message, int lineNumber, String sourceID)
	{
		Log.i("jtj", message + " -- From line " + lineNumber + " of " + sourceID);
	    try {
			URL      url = new URL(contentURL);
			
			File	            file = new File(url.getFile()+".log");
			OutputStreamWriter  os = new OutputStreamWriter(new FileOutputStream(file, true));
			String			    msg = lineNumber + ":\n" + message + "\n";
			
			os.write(msg);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isPaginationFinished()
	{
		return paginationFinished;
	}


	private void clearContentWebConsoleMessageLog()
	{
	    try {
			URL      url = new URL(contentURL);
			
			File	            file = new File(url.getFile()+".log");
			OutputStreamWriter  os = new OutputStreamWriter(new FileOutputStream(file, false));
			os.write("\n");
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setHighlightMode(boolean mode)
	{
		isHighlightMode = mode;
	}

	private void contentWebViewSizeChanged(int width, int height, int oldWidth, int oldHeight)
	{
		/*
//		Log.i("------", String.format("TOPIC %d: size changed to %d x %d  from  %d x %d", topicId, width, height, oldWidth, oldHeight));
		if(oldHeight!=0)
		{
			//contentWebView.loadUrl(contentURL);
			return;
		}
		
		if(contentWebView.getProgress()==100)
		{
			paginationFinished = false;
			paginationPageCount = -1;
			//paginateContent();
		}*/
	}
	
	private void paginateContent()
	{
		if(paginationFinished) return;
		
		miscHandler.sendMessageDelayed(miscHandler.obtainMessage(MISC_MESSAGE_PAGINATION, 0, 0), PAGINATION_DELAY_M);
	}

	private void paginateContentActual()
	{
//		Log.i("-----", "PAGINATE CALL FOR TOPIC "+topicId+", with height="+contentWebView.getActualSizeHeight()+", progress="+contentWebView.getProgress());
		contentWebView.loadUrl("javascript:jtj_paginate("+contentWebView.getActualSizeHeight()+");");
	}

	public void setParentScrollHandler(Handler parentScrollHandler)
	{
		this.parentScrollHandler = parentScrollHandler;
	}

	public int getContentLoadingStatus()
	{
		return contentLoadingStatus;
	}

	public int getMediaLoadingStatus()
	{
		return mediaLoadingStatus;
	}

	public Handler getParentScrollHandler()
	{
		return parentScrollHandler;
	}

	public void changeFontWithFactor(int factor)  // factor is absolute, not relative
	{
		if(fontChangeFactor==-1)
		{
			initialFontSize = contentWebView.getSettings().getDefaultFontSize(); 
		}
		fontChangeFactor = factor;
		contentWebView.getSettings().setDefaultFontSize(initialFontSize + fontChangeFactor);
		mediaWebView.getSettings().setDefaultFontSize(initialFontSize + fontChangeFactor);
	}
	
	protected void jumpToMediaWithId(String id)
	{
		if(mediaURL!=null)
		{
			mediaWebView.loadUrl("javascript:scrollToElement('" + id + "')");
		}
	}

	public WebView getMediaWebView()
	{
		return mediaWebView;
	}

	public WebView getContentWebView()
	{
		return contentWebView;
	}

	public View getParentContainer()
	{
		return parentContainer;
	}

	public void setParentContainer(View parentContainer)
	{
		this.parentContainer = parentContainer;
	}

	private void parseMediaJSON()
	{
		if(mediaJSON==null)return;

		try {
			JSONArray		array = new JSONArray(mediaJSON);
			
			for(int i=0; i<array.length(); i++)
			{
				JSONObject					curObj = array.getJSONObject(i);
				EmbeddedMediaElement		element = new EmbeddedMediaElement(curObj);
				String						elId = element.getId();
				
				if(elId!=null)
				{
					mediaElements.put(elId, element);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getTopicId()
	{
		return topicId;
	}
	
	public void setSpineIndex(EPubBook book, int index)
	{
		this.book = book; 
		offsetHeightTries = 0;
		if(topicId==index) 			// this container already contain actual topic data
		{
			isTopicActual = true;
			//if(parentScrollHandler != null)parentScrollHandler.sendMessage(parentScrollHandler.obtainMessage(CONTENT_SCROLLING_FINISHED, 0, 0));
			// check scroll buttons
			return;
		}
		isTopicActual = false;
		paginationFinished = false;
			
		contentLoadingStatus = CONTENT_LOADING_STATUS_NOT_LOADED;
		mediaLoadingStatus = CONTENT_LOADING_STATUS_NOT_LOADED;
		requestingMediaElementsTopOffsetStatus = REQUESTING_TOP_OFFSET_STATUS_NOT_DETERMINED;

		contentBaseURL = null;
		mediaURL = null;
		topicId = index;
		
		// new content
		contentURL = book.getTopicOriginalURLByIndex(index);

		String			proposedContentPath = null, proposedContentURL = null;
		String			proposedContentPathReserve = null, proposedContentURLReserve = null;
		
		if(isHighlightMode)
		{
			if(deviceOrientationIsLandscape)		// with media cutted
			{
				proposedContentPath = book.getTopicWithoutMediaHighlightedPathByIndex(index);
				proposedContentURL = book.getTopicWithoutMediaHighlightedURLByIndex(index);
				proposedContentPathReserve = book.getTopicOriginalHighlightedPathByIndex(index);
				proposedContentURLReserve = book.getTopicOriginalHighlightedURLByIndex(index);
			} else {								// with original (uncutted media)
				proposedContentPath = book.getTopicOriginalHighlightedPathByIndex(index);
				proposedContentURL = book.getTopicOriginalHighlightedURLByIndex(index);
			}
		} else {
			if(deviceOrientationIsLandscape)
			{
				proposedContentPath = book.getTopicContentPathByIndex(index);
				proposedContentURL = book.getTopicContentURLByIndex(index);
			} 
		}
		
		boolean		needLoadReserve = false;
		if(proposedContentPath!=null && proposedContentURL!=null)
		{
			File		contentFile = new File(proposedContentPath);
			if(contentFile.canRead())
			{
				contentURL = proposedContentURL;
			} else
				needLoadReserve = true;
		} else {
			needLoadReserve = true;
		}
		
		if(needLoadReserve)
		{
			if(proposedContentPathReserve!=null && proposedContentURLReserve!=null)
			{
				File		contentFile = new File(proposedContentPathReserve);
				if(contentFile.canRead())
				{
					contentURL = proposedContentURLReserve;
				}
			}
		}

		// new media
		File		mediaFile = new File(book.getTopicMediaPathByIndex(index));
		clearMediaJSON();
		if(mediaFile.canRead())
		{
			mediaURL = book.getTopicMediaURLByIndex(index);
			mediaJSON = book.getTopicMediaInfoJSONByIndex(index);
			parseMediaJSON();
			//Log.i("----", "media loaded="+mediaElements.size());
		}

		if(fullScreenMode || mediaURL==null)mediaWebView.setVisibility(View.GONE);
		else if(deviceOrientationIsLandscape) mediaWebView.setVisibility(View.VISIBLE);

		// old code, load json only if landsape and not full screen
/*		fullScreenMode = book.getTopicFullscreenFlag(index);
		if(deviceOrientationIsLandscape)		// with media
		{
			File		mediaFile = new File(book.getTopicMediaPathByIndex(index));
			if(mediaFile.canRead())
			{
				mediaURL = book.getTopicMediaURLByIndex(index);
				mediaJSON = book.getTopicMediaInfoJSONByIndex(index);
				parseMediaJSON();
			} else {
				clearMediaJSON();
			}
		}
		if(fullScreenMode)mediaWebView.setVisibility(View.GONE);
		else if(deviceOrientationIsLandscape) mediaWebView.setVisibility(View.VISIBLE);*/
	}

	private void clearMediaJSON()
	{
		mediaJSON = null;
		mediaElements.clear();
	}
	
	private void showFullScreenMediaWithId(String id)
	{
		EmbeddedMediaElement	element = mediaElements.get(id);
		
		if(element!=null)
		{
			int			contentType = element.getType(); 

			if(contentType==EmbeddedMediaElement.MEDIA_TYPE_IMAGE)
			{
				Intent		intent = new Intent(context, FullScreenEmbeddedMediaImageActivity.class);
				String		imageURL = element.getImageURL();
				intent.putExtra(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, contentType);

				if(imageURL != null)
				{
					intent.putExtra(EPubBookRender.FULLSCREEN_IMAGE_URL_KEY, imageURL);
					context.startActivity(intent);
				}
			} else
				if (contentType == EmbeddedMediaElement.MEDIA_TYPE_EXTERNAL)
				{
					
					Intent		intent = new Intent(context, FullScreenEmbeddedMediaExternalActivity.class);
					intent.putExtra(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, contentType);
					String		externalContent = element.getExternalContent();

					if(externalContent != null)
					{
						intent.putExtra(EPubBookRender.FULLSCREEN_EXTERNAL_CONTENT_KEY, externalContent);
						intent.putExtra(EPubBookRender.FULLSCREEN_BASE_URL_KEY, contentBaseURL);
						context.startActivity(intent);
					}
				}
		}
	}
	
	public void load()
	{
		// check reusing
		if(isTopicActual) return;
		
		contentLoadingStatus = CONTENT_LOADING_STATUS_LOADIN_IN_PROCESS;
		mediaLoadingStatus = CONTENT_LOADING_STATUS_LOADIN_IN_PROCESS;
		contentWebView.clearView();

		mediaWebView.clearView();
		
		clearContentWebConsoleMessageLog();
		
		//tryLoadCachedContent();
		
		contentWebView.loadUrl(contentURL);
		if(!fullScreenMode)
		{
			if(mediaURL!=null)
			{
				mediaWebView.loadUrl(mediaURL);
			} else {
				mediaWebView.loadDataWithBaseURL("", EMPTY_HTML, null, "UTF-8", null);
			}
		}
	}

	public Handler getTopicHandler()
	{
		return topicHandler;
	}

	public void setTopicHandler(Handler topicHandler)
	{
		this.topicHandler = topicHandler;
	}

	public void pageDown()
	{
		// old behavoir - scrolling using android WebView method
		/*
		contentWebView.pageDown(false);
		if(parentScrollHandler != null)parentScrollHandler.sendMessageDelayed(parentScrollHandler.obtainMessage(CONTENT_SCROLLING_FINISHED, 0, 0), 1000);
		scrollHandler.sendMessageDelayed(scrollHandler.obtainMessage(1, 0, 0), 1000);
		*/
		int				webViewHeight = contentWebView.getHeight(),
						contentHeight = contentWebView.getContentHeight(),
						yPos = contentWebView.getScrollY(),
						newYPos = 0;
		
		newYPos = yPos + webViewHeight;
		if(newYPos < contentHeight-1)contentWebView.scrollTo(0, newYPos);
		
		if(requestingMediaElementsTopOffsetStatus==REQUESTING_TOP_OFFSET_STATUS_PROCESSED)scrollToPageMedia();
	}

	public int paginationPageCount()
	{
		int		webViewHeight = contentWebView.getHeight(),
				contentHeight = contentWebView.getContentHeight() - 1,
				pages;

		pages = (int) Math.ceil((double)contentHeight / webViewHeight); 

		if(paginationFinished)
		{
			if(paginationPageCount==-1)
			{
				return pages == 0 ? 1 : pages;
				
			}
			return paginationPageCount;
		}

		return pages == 0 ? 1 : pages;
	}
	
	public int paginationCurrentPage()
	{
		int		webViewHeight = contentWebView.getHeight(),
				yPos = contentWebView.getScrollY();
		
		return (yPos / webViewHeight) + 1;
	}
	
	public void pageUp()
	{
		/*contentWebView.pageUp(false);
		if(parentScrollHandler != null)parentScrollHandler.sendMessageDelayed(parentScrollHandler.obtainMessage(CONTENT_SCROLLING_FINISHED, 0, 0), 1000);
		scrollHandler.sendMessageDelayed(scrollHandler.obtainMessage(1, 0, 0), 1000);
		*/
		int				webViewHeight = contentWebView.getHeight(),
						yPos = contentWebView.getScrollY(),
						newYPos = 0;

		newYPos = yPos - webViewHeight;
		if(newYPos < 0)newYPos = 0;
		contentWebView.scrollTo(0, newYPos);

		if(requestingMediaElementsTopOffsetStatus==REQUESTING_TOP_OFFSET_STATUS_PROCESSED)scrollToPageMedia();
	}
	
	// id of media that topOffset in specified range is smallest
	private String topmostMediaId(int topOffset, int bottomOffset)
	{
		if(requestingMediaElementsTopOffsetStatus!=REQUESTING_TOP_OFFSET_STATUS_PROCESSED) return null; // now is not determining topOffsets
		
		Set<String>			keys = mediaElements.keySet();
		Iterator<String>	iterator = keys.iterator();
		String				resultId = null;
		int					resultOffset = contentWebView.getContentHeight() + 1000;
		
		while ( iterator.hasNext() )
		{
			String						id = iterator.next();
			EmbeddedMediaElement		element = mediaElements.get(id);
			
			if(id!=null && element!=null)
			{
				int				elementTop = element.getTopOffset();
				
				if(elementTop>=topOffset && elementTop<bottomOffset)
				{
					if(resultId != null)
					{
						if(elementTop < resultOffset)
						{
							resultId = id;
							resultOffset = elementTop;
						}
					} else {
						resultId = id;
						resultOffset = elementTop;
					}
				}
			}
		}
		
		return resultId;
	}
	
	// scroll to media that first on this page
	private void scrollToPageMedia()
	{
		int		pageTop = contentWebView.getScrollY();
		int		pageBottom = pageTop + contentWebView.getHeight();
		String	id = topmostMediaId(pageTop, pageBottom);
		
		//Log.d("-----", "scroll to id="+id);
		//if(pageTop==0)return;				// we are in top, dont need jump to media
		if(id!=null)jumpToMediaWithId(id);
	}
	
	private void requestContentMediaElementsTopOffset()
	{
		if(requestingMediaElementsTopOffsetStatus == REQUESTING_TOP_OFFSET_STATUS_IN_PROCESS) return;
		requestingMediaElementsTopOffsetStatus = REQUESTING_TOP_OFFSET_STATUS_IN_PROCESS;
		
		Set<String>			keys = mediaElements.keySet();
		Iterator<String>	iterator = keys.iterator();

//		Log.i("------", "media_req_size="+mediaElements.size());
		
		while ( iterator.hasNext() ){
			String		id = iterator.next();
			//Log.i("------", "media_id="+id);
			contentWebView.loadUrl("javascript:elementTop=0;" +
					"delement = document.getElementById(\""+id+"\");"+
					"while (delement.offsetParent){elementTop += delement.offsetTop; delement = delement.offsetParent;};"+
					"window.HTMLOUT.elementTop(\""+id+"\", elementTop);");
		}
	}
	
	private void checkTopOffsetEndProcessing()
	{
		Set<String>			keys = mediaElements.keySet();
		Iterator<String>	iterator = keys.iterator();
		int					count = 0, processed = 0;
		
		while ( iterator.hasNext() ){
			String						id = iterator.next();
			EmbeddedMediaElement		element = mediaElements.get(id);
			
			if(id!=null && element!=null)
			{
				if(element.getTopOffset()>=0)processed++;
				count++;
			}
		}
		
		if(count>0 && count==processed)
		{
			requestingMediaElementsTopOffsetStatus = REQUESTING_TOP_OFFSET_STATUS_PROCESSED;
			scrollToPageMedia();
		}
	}
	
	public boolean isCanScrollUp()
	{
//		Log.i("----------------", "IS UP SCROLLY="+contentWebView.getScrollY());
		return contentWebView.getScrollY() > 0;
	}

	public boolean isCanScrollDown()
	{
		int		contentHeight = contentWebView.getContentHeight();
		int		webViewHeight = contentWebView.getHeight();
		int		pos = contentWebView.getScrollY();

//		Log.i("----------------", "IS DOWN SCROLL conth="+contentHeight+", wh="+webViewHeight+", y="+pos);

		if(contentHeight < webViewHeight) return false;    // too small topic
		return  pos < contentHeight - webViewHeight;
	}
	
	Handler scrollHandler = new Handler() {
        public void handleMessage(Message msg) {
    		/*if(requestingMediaElementsTopOffsetStatus==REQUESTING_TOP_OFFSET_STATUS_NOT_DETERMINED)
    		{
    			requestContentMediaElementsTopOffset();
    		} else if(requestingMediaElementsTopOffsetStatus==REQUESTING_TOP_OFFSET_STATUS_PROCESSED){
    			scrollToPageMedia();
    			
    		}*/
//			if(parentScrollHandler != null)parentScrollHandler.sendMessageDelayed(parentScrollHandler.obtainMessage(CONTENT_SCROLLING_FINISHED, 0, 0), 1000);
        }
    };

    Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
        	if(msg.what == MESSAGE_CHANGE_CONTENT_OFFSET)
        	{
        		setContentOffset();
        	}
        }
    };   

	public int getContentHeight()
	{
		return contentWebView.getContentHeight();
	}

	public float getContentOffset()
	{
		int contentHeight = contentWebView.getContentHeight();
		
		if(contentHeight==0) return 0;
		return ((float)contentWebView.getScrollY()) / contentHeight;
	}

	private void setContentOffset()
	{
		int			contentHeight = contentWebView.getContentHeight();

		offsetHeightTries++;
		if(contentHeight==0)
		{
			if(offsetHeightTries < 12)
			{
            	messageHandler.sendMessageDelayed(messageHandler.obtainMessage(MESSAGE_CHANGE_CONTENT_OFFSET, 0, 0), 100);
			}
			return;
		}

   // 	Log.i("-----", "TRIES="+offsetHeightTries);

    	float		ypos =  contentOffset * contentHeight;
		int			visHeight = contentWebView.getHeight();
		int			page;
		
		// try to determine appropriate page
		//page = (int) Math.ceil(ypos / visHeight);
		
		// check over-page
		//if(page+1 > paginationPageCount) page = paginationPageCount - 1;
		//if(page < 0) page = 0;
		
		if(ypos < 0)ypos = 0;
		if(ypos > contentHeight-visHeight-1)ypos = contentHeight-visHeight-1; 
		
		contentWebView.scrollTo(0, (int) Math.ceil(ypos));
//		Log.i("----", "set OFFSET ("+contentOffset+"). cheight="+contentHeight+", ypos="+ypos+", visHeight="+visHeight+", page="+page);
    	parentScrollHandler.sendMessage(parentScrollHandler.obtainMessage(CONTENT_UPDATE_PAGE_INFO, 0, 0));
    	requestContentMediaElementsTopOffset();
	}
	
	public void setContentOffset(float offset)
	{
		contentOffset = offset;
		//if(contentLoadingStatus==CONTENT_LOADING_STATUS_LOADINF_FINISHED)setContentOffset();
	}
	
	Handler miscHandler = new Handler() {
        public void handleMessage(Message msg) {
        	if(msg.what==MISC_MESSAGE_PAGINATION)
        	{
        		//paginateContentActual();
        	}
        }
    };
}
