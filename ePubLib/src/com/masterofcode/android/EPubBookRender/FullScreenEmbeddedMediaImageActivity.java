package com.masterofcode.android.EPubBookRender;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class FullScreenEmbeddedMediaImageActivity extends Activity {
	private ImageView		imageView;
	private	int				contentType;
	private String			imageURL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.fullscreen_embedded_media_image_layout);
		
		imageView = (ImageView) findViewById(R.id.fsImageView);
		
		if (savedInstanceState==null) {                                                                         
            Intent intent = this.getIntent();                              

            contentType = intent.getIntExtra(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, EmbeddedMediaElement.MEDIA_TYPE_NONE);
            imageURL = intent.getStringExtra(EPubBookRender.FULLSCREEN_IMAGE_URL_KEY);
		} else {
			contentType = savedInstanceState.getInt(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, EmbeddedMediaElement.MEDIA_TYPE_NONE);
			imageURL = savedInstanceState.getString(EPubBookRender.FULLSCREEN_IMAGE_URL_KEY);
		}

        if(contentType == EmbeddedMediaElement.MEDIA_TYPE_NONE)
        {
        	finish();
        } else  if(contentType == EmbeddedMediaElement.MEDIA_TYPE_IMAGE)
        		{
        			if(imageURL!=null) loadImage();
        		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putInt(EPubBookRender.FULLSCREEN_CONTENT_TYPE_KEY, contentType);
		if(contentType==EmbeddedMediaElement.MEDIA_TYPE_IMAGE)
		{
			outState.putString(EPubBookRender.FULLSCREEN_IMAGE_URL_KEY, imageURL);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			FullScreenEmbeddedMediaImageActivity.this.finish();
		}
		return super.onTouchEvent(event);
	}

	private void loadImage()
	{
		imageView.setImageURI(Uri.parse(imageURL));
		imageView.setVisibility(View.VISIBLE);
	}
}
