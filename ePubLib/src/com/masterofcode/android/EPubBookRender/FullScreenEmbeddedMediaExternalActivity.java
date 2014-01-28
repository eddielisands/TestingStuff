package com.masterofcode.android.EPubBookRender;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.itri.html5webview.HTML5WebView;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class FullScreenEmbeddedMediaExternalActivity extends Activity {
	
	final private String		MEDIA_PAGE_HTML_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
	 "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"+
	 "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"+
	 "<head>\n"+
	 "<title>Media</title>"+
	 "<style type='text/css'>\n"+
	 "   body { margin: 1px; padding: 1px; align: center; height: 100px; position:relative; }\n"+
	 "</style>\n"+
	 "</head>\n"+
	 "<body><table border='0' width='100%' height='100%' cellpadding='10' cellspacing='20'><tr><td align='center' valign='middle'>\n";

	final private String		MEDIA_PAGE_HTML_POSTFIX = "</td></tr></table></body></html>";

	private HTML5WebView	webView;

	private	 int			contentType;
	private String			externalContent;
	private String			baseURL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

//		setContentView(R.layout.fullscreen_embedded_media_external_layout);
		
//		webView = (HTML5WebView) findViewById(R.id.fsWebView);
		webView = new HTML5WebView(this);
		
		if (savedInstanceState==null) {                                                                         
            Intent intent = this.getIntent();                              

            contentType = intent.getIntExtra(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, EmbeddedMediaElement.MEDIA_TYPE_NONE);
            externalContent = intent.getStringExtra(EPubBookRender.FULLSCREEN_EXTERNAL_CONTENT_KEY);
            baseURL = intent.getStringExtra(EPubBookRender.FULLSCREEN_BASE_URL_KEY);
            
    	} else {
			contentType = savedInstanceState.getInt(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, EmbeddedMediaElement.MEDIA_TYPE_NONE);
			externalContent = savedInstanceState.getString(EPubBookRender.FULLSCREEN_EXTERNAL_CONTENT_KEY);
			baseURL = savedInstanceState.getString(EPubBookRender.FULLSCREEN_BASE_URL_KEY);
			
//			webView.restoreState(savedInstanceState);
			
//			webView.relayout();
//			webView.setVisibility(View.VISIBLE);
//	        setContentView(webView.getLayout()); 
//	        webView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

        if(contentType == EmbeddedMediaElement.MEDIA_TYPE_NONE)
        {
        	finish();
        } else  if(contentType == EmbeddedMediaElement.MEDIA_TYPE_EXTERNAL)
        		{
        			if(externalContent != null)	loadExternalContent();
       			}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putInt(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, contentType);
		if (contentType==EmbeddedMediaElement.MEDIA_TYPE_EXTERNAL)
		{
			outState.putString(EPubBookRender.FULLSCREEN_EXTERNAL_CONTENT_KEY, externalContent);
			outState.putString(EPubBookRender.FULLSCREEN_BASE_URL_KEY, baseURL);
		}
//		webView.saveState(outState);
	}
	
	@Override                                                                                                                                     
    public void onStop() {                                                                                                                        
        super.onStop();                                                                                                                           
        webView.stopLoading(); 
        callHiddenWebViewMethod("onPause");
    }
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(webView!=null)webView.relayout();
	}


	// these method back to small size if	
/*	
	@Override                                                                                                                                     
    public boolean onKeyDown(int keyCode, KeyEvent event) {                                                                                       
        if (keyCode == KeyEvent.KEYCODE_BACK) {                                                                                                   
            if (webView.inCustomView()) {                                                                                                        
                webView.hideCustomView();                                                                                                        
                return true;                                                                                                                      
            }                                                                                                                                     
        }                                                                                                                                         
        return super.onKeyDown(keyCode, event);                                                                                                   
    }
*/
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			// check full screen mode
			if(webView!=null)
			{
				
				if (!webView.inCustomView())
				{
					webView.stopLoading();
					FullScreenEmbeddedMediaExternalActivity.this.finish();
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private void loadExternalContent()
	{
		String		content = MEDIA_PAGE_HTML_PREFIX + externalContent + MEDIA_PAGE_HTML_POSTFIX;

		if(externalContent.contains("slideshare.net/slideshow/"))
		{
			DocumentBuilderFactory 	dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				InputStream				source = new ByteArrayInputStream(content.getBytes("UTF-8"));
				builder = dbFactory.newDocumentBuilder();
				Document dom = builder.parse(source);
				
				NodeList		headList = dom.getElementsByTagName("a");
				for(int i=0; i<headList.getLength(); i++)
				{
					Node			node = headList.item(i);
					
					if(node!=null)
					{
						NamedNodeMap	attrs = node.getAttributes();
						
						if(attrs!=null)
						{
							Node			link = attrs.getNamedItem("href");
							
							if(link!=null)
							{
								String		linkValue = link.getTextContent();
								
								if(linkValue!=null)
								{
									if(linkValue.matches("http://.*slideshare.net/[^/]+/[^\"']+"))
									{
//										Log.i("=======", "GO! "+linkValue);
										webView.setVisibility(View.VISIBLE);
								        setContentView(webView.getLayout()); 
										
								        webView.loadUrl(linkValue);                                                             
								        webView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
								        return;
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		webView.setVisibility(View.VISIBLE);
        setContentView(webView.getLayout()); 
		
        webView.loadDataWithBaseURL(baseURL, content, "text/html", "UTF-8", null);                                                             
        webView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	private void callHiddenWebViewMethod(String name){
	    if( webView != null ){
	        try {
	            Method method = WebView.class.getMethod(name);
	            method.invoke(webView);
	        } catch (NoSuchMethodException e) {
//	            Log.error("No such method: " + name, e);
	        } catch (IllegalAccessException e) {
//	            Log.error("Illegal Access: " + name, e);
	        } catch (InvocationTargetException e) {
//	            Log.error("Invocation Target Exception: " + name, e);
	        }
	    }
	}
}
