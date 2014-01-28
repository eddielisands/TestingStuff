package com.masterofcode.android.magreader.fragments;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.masterofcode.android.magreader.FeedsActivity;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class FeedsViewFragment extends Fragment {
	
	FeedItem 				curFeedItem;
	List<FeedItem> 			items;
	ImageButton				nextFeedBtn;
	ImageButton				prevFeedBtn;
	int						currIndex;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View returnView = inflater.inflate(R.layout.feed_view_item, container);
		
		nextFeedBtn = (ImageButton)returnView.findViewById(R.id.reading_next_button);
		prevFeedBtn = (ImageButton)returnView.findViewById(R.id.reading_prev_button);
		if(nextFeedBtn != null && prevFeedBtn != null){
			nextFeedBtn.setOnClickListener(nextFeedBtnListener);
			prevFeedBtn.setOnClickListener(prevFeedBtnListener);
		}
		
		return returnView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//setViewItem(curFeedItem);
	}
	
	private void initialButton(){
		if (nextFeedBtn != null && prevFeedBtn != null){
			currIndex = items.indexOf(curFeedItem);
			if (currIndex + 1 == items.size() - 1) nextFeedBtn.setBackgroundResource(R.drawable.nextpage_disable);
			if (currIndex == 1) prevFeedBtn.setBackgroundResource(R.drawable.prevpage_disable);
		}
	}
	
	/** ClickListeners Section**/
	OnClickListener nextFeedBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			currIndex = items.indexOf(curFeedItem);
			if(currIndex < items.size() - 1 ){
				FeedItem fItem = items.get(currIndex + 1);
				((FeedsListForViewFragment) getFragmentManager().findFragmentById(R.id.feed_list_for_view)).updateStats(currIndex + 1);
				setViewItem(fItem, null);
				prevFeedBtn.setBackgroundResource(R.drawable.prevpage);
			}
		}
	};
	
	OnClickListener prevFeedBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			currIndex = items.indexOf(curFeedItem);
			if(currIndex > 0){
				FeedItem fItem = items.get(currIndex - 1);
				((FeedsListForViewFragment) getFragmentManager().findFragmentById(R.id.feed_list_for_view)).updateStats(currIndex - 1);
				setViewItem(fItem, null);
				nextFeedBtn.setBackgroundResource(R.drawable.nextpage);
			} 
		}
	};
	/** End of ClickListeners Section**/
	
	public void setViewItem(FeedItem feedItem, String searchKeywords) {
		
		curFeedItem = feedItem;
		items = ((FeedsActivity) getActivity()).getItems();
		if (nextFeedBtn != null && prevFeedBtn != null){
			if (items.indexOf(curFeedItem) == items.size() - 1) nextFeedBtn.setBackgroundResource(R.drawable.nextpage_disable);
			if (items.indexOf(curFeedItem) == 0) prevFeedBtn.setBackgroundResource(R.drawable.prevpage_disable);
		}
		View view = getView();
		
		ScrollView itemView = (ScrollView) view.findViewById(R.id.viewOfItem);
		itemView.setVisibility(View.INVISIBLE);
		itemView.scrollTo(0, 0);
		LinearLayout loading = (LinearLayout) view.findViewById(R.id.loadingLayout);
		loading.setVisibility(View.VISIBLE);
		
		TextView pubDate = (TextView) view.findViewById(R.id.publication_date);
		TextView title = (TextView) view.findViewById(R.id.item_title);
		TextView whoPublish = (TextView) view.findViewById(R.id.item_author);
		WebView viewDescription = (WebView) view.findViewById(R.id.text_description);
		initializeWebView(viewDescription);
		viewDescription.clearCache(false);
		
		pubDate.setText(ApplicationUtils.formatDateForItemView(feedItem.publication_date));
		title.setText(feedItem.title);
		whoPublish.setText(feedItem.author);
		String description;
		if (!TextUtils.isEmpty(searchKeywords)){
			description = feedItem.description.replaceAll("(?i)(?m)("+searchKeywords+")(?=[^>]*?<)","<span class=\"hl\">$1</span>");
		} else {
			description = feedItem.description;
		}
		
		if (description == null) description = "";
			
		viewDescription.loadDataWithBaseURL(feedItem.link, modifyDataForWebView(description, viewDescription), Constants.CONTENT_TYPE_TEXT_HTML, Constants.CHAR_ENCODING_UTF8, null);
		itemView.setVisibility(View.VISIBLE);
		loading.setVisibility(View.GONE);
	}
	
	/**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	 //Log.i(this.getClass().getSimpleName().toString(), "shouldOverrideUrlLoading: "+url);
             if (url.startsWith("vnd.youtube:")){
                  	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                  return true;
              } else if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("www.")) {
					startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(url)),Constants.REQUEST_CODE_VIEW_IN_BROWSER);
                  return false; 
			  } else {      
            	 return super.shouldOverrideUrlLoading(view, url);
             }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
        	//Log.i(this.getClass().getSimpleName().toString(), "onLoadResource: "+url);
        	super.onLoadResource(view, url);        	
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
        	//Log.i(this.getClass().getSimpleName().toString(), "onPageFinished: "+url);
        	super.onPageFinished(view, url);
        }
    }
    
    private void initializeWebView(WebView webView) {
        //webView.setBackgroundColor(R.color.webview_background);
        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);
        websettings.setLoadsImagesAutomatically(true);
        websettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setClickable(true);
        websettings.setPluginState(PluginState.ON);
        websettings.setLightTouchEnabled(true);
        webView.setWebViewClient(new MyWebChromeClient());
    }
    
    private String modifyDataForWebView(String text, WebView webView) {
        //int maxScreenW = ApplicationUtils.getCurrentScreenWidth(FeedsViewFragment.this.getActivity().getBaseContext());
        
        Pattern p = Pattern.compile("(width|height)=\"([0-9]*)\"",Pattern.MULTILINE);
        Matcher m = p.matcher(text);

        /*while (m.find()) {
        	int w = 0;

			String type = m.group(1); 	
			String res = m.group(2);
			if (type.equals("width"))  w = Integer.parseInt(res);
			String s = m.group();
			text = text.replaceAll(s, "width=\""+maxScreenW+"\"");
			
			m.find();

        	int h = 0;
			type = m.group(1); 	
			res = m.group(2);			
			if (type.equals("height")) h = Integer.parseInt(res);
			
			float koef = (float)w/(float)h;
			
			int nh = (int)(maxScreenW/koef);
			s = m.group();
			text = text.replaceAll(s, "height=\""+nh+"\"");
        }

        
        // silverlight fix
        // test case
       // text = text + "<iframe src=\"http://www.microsoft.com:80/presspass/silverlightApps/videoplayer3/standalone.aspx?contentID=wp7_unveil01&amp;src=/presspass/presskits/windowsphone/channel.xml\" width=\"594\" scrolling=\"no\" frameborder=\"0\" height=\"396\"></iframe><br>\n";
        p = Pattern.compile("<iframe.*?microsoft.com.*?silverlight.*?</iframe>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        m = p.matcher(text);

        while (m.find()) {
			String type = m.group(0);
			String t = "<i class=\"littel_font\"><br>"+this.getActivity().getBaseContext().getResources().getString(R.string.videofixer_placeholder_text)+"<br><a href=\""+curFeedItem.link+"\">"+curFeedItem.link+"</a><br></i>";
        	text = text.replace(type, t);
        }

        
        // viddler fix
        // test case
        //text = text + " <object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" id=\"viddler\" width=\"594\" height=\"396\"><param name=\"movie\" value=\"http://www.viddler.com/player/\"><param name=\"allowScriptAccess\" value=\"always\"><param name=\"allowFullScreen\" value=\"true\"><param name=\"flashvars\" value=\"fake=1&amp;key=4109646f\"><embed style=\"visibility: visible;\" src=\"http://www.viddler.com/player/\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" flashvars=\"fake=1&amp;key=4109646f\" name=\"viddler\" width=\"594\" height=\"396\"></object><br>\n";
        p = Pattern.compile("<object.*?viddler.*?</object>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        m = p.matcher(text);

        while (m.find()) {
			String type = m.group(0);
			String t = "<i class=\"littel_font\"><br>"+this.getActivity().getBaseContext().getResources().getString(R.string.videofixer_placeholder_text)+"<br><a href=\""+curFeedItem.link+"\">"+curFeedItem.link+"</a><br></i>";
        	text = text.replace(type, t);
        }*/

        // vimeo fix
        // test case
       // text = text + "<object width=\"474\" height=\"310\"><param name=\"allowfullscreen\" value=\"true\"><param name=\"allowscriptaccess\" value=\"always\"><param name=\"movie\" value=\"http://vimeo.com/moogaloop.swf?clip_id=11189252&amp;server=vimeo.com&amp;show_title=1&amp;show_byline=1&amp;show_portrait=0&amp;color=&amp;fullscreen=1\"><embed style=\"visibility: visible;\" src=\"http://vimeo.com/moogaloop.swf?clip_id=11189252&amp;server=vimeo.com&amp;show_title=1&amp;show_byline=1&amp;show_portrait=0&amp;color=&amp;fullscreen=1\" type=\"application/x-shockwave-flash\" allowfullscreen=\"true\" allowscriptaccess=\"always\" width=\"474\" height=\"310\"></object><br>\n";
        p = Pattern.compile("<iframe.*?vimeo.com.*?</iframe>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        m = p.matcher(text);

        while (m.find()) {
			String type = m.group(0);
			String curLink = curFeedItem.link;
			if (curLink.contains("http://jaxenter.com"))
				curLink = curLink.replace("http://jaxenter.com", "http://www.jaxenter.com");
			String t = "<i class=\"littel_font\"><br>"+this.getActivity().getBaseContext().getResources().getString(R.string.videofixer_placeholder_text)+"<br><a href=\""+curLink+"\">"+curFeedItem.link+"</a><br></i>";
        	text = text.replace(type, t);
        }
        
        // youtube fix
        //p = Pattern.compile("<object(.*?)/object>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNIX_LINES);
        /*p = Pattern.compile("<iframe(?:.*?)youtube.com(?:.*?)v/(.{11})(?:.*?)/iframe>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNIX_LINES);
        m = p.matcher(text);

        while (m.find()) {
			String type = m.group(0); 	
        	//String vUrl = RSSItem.getVideoTeaser(type);
        	
            //Pattern p1 = Pattern.compile("^[^v]+v/(.{11}).*");
            //Matcher m1 = p1.matcher(vUrl);

            String vid="";
            //if (m1.find()) {
              vid = m.group(1);
            //}
            
        	        	
	    	String t = "<a href=\"vnd.youtube:"+vid+"\"><img src=\"http://img.youtube.com/vi/"+vid+"/0.jpg\" alt=\"YouTube thumbnail\" /></a> ";
	    	//Log.v(this.getClass().getSimpleName().toString(), "Youtube:"+t);
	    	text = text.replace(type, t);
	    } */     
        /////////////////////
        p = Pattern.compile("<iframe(?:.*?)youtube.com(?:.*?)[embed|v]/(.{11})(?:.*?)/iframe>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNIX_LINES);
        m = p.matcher(text);

        while (m.find()) {
			String type = m.group(0); 	
        	//String vUrl = RSSItem.getVideoTeaser(type);
        	
            //Pattern p1 = Pattern.compile("^[^v]+v/(.{11}).*");
            //Matcher m1 = p1.matcher(vUrl);

            String vid="";
            //if (m1.find()) {
              vid = m.group(1);
            //}
            
        	        	
	    	String t = "<a href=\"vnd.youtube:"+vid+"\"><img src=\"http://img.youtube.com/vi/"+vid+"/0.jpg\" alt=\"YouTube thumbnail\" /></a> ";
	    	//Log.v(this.getClass().getSimpleName().toString(), "Youtube:"+t);
	    	text = text.replace(type, t);
	    }
        ///////////////////////////////
	    
         
        
	    //<a href="vnd.youtube:[videoID]">thumbnail</a> 
	    // <img src="http://img.youtube.com/vi/jMbvzowbFzk/default.jpg" alt="YouTube thumbnail" /> 
	    //maxScreenW = 800;
	    //String s = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/> <style type=\"text/css\">img {max-width: " + maxScreenW + "dip;}  body { width: 400dip; }</style>";
	    String s = "<style type=\"text/css\"> .hl { BACKGROUND-COLOR: #FFFF00;} </style>"; //<meta name=\"viewport\" content=\"target-densitydpi=device-dpi\" />
	    
	    //Log.v(this.getClass().getSimpleName().toString(), "ALL:"+s+text);
	    return s + text;	    
	}
}
