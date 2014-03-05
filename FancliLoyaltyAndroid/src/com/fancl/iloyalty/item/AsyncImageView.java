package com.fancl.iloyalty.item;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.runnable.ImageReceiver;
import com.fancl.iloyalty.runnable.ImageReceiverCallback;
import com.fancl.iloyalty.service.ThreadService;

public class AsyncImageView extends ImageView implements ImageReceiverCallback{
	
	protected ThreadService threadService;
	
	protected Map<String, Bitmap> thumbnailCacheList;
	
	protected String requestingUrl = null;
	
	protected Handler handler;
	
	public AsyncImageView(Context context)
	{
		super(context);
		init();
	}
	
	public AsyncImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}
	
	private void init()
	{
		threadService = GeneralServiceFactory.getThreadService();
	}

	public void setThumbnailCacheList(Map<String, Bitmap> thumbnailCacheList)
	{
		this.thumbnailCacheList = thumbnailCacheList;
	}
	
	protected boolean isInList(String url)
	{
		if(thumbnailCacheList == null)
		{
			return false;
		}
		
		if(thumbnailCacheList.size() == 0)
		{
			return false;
		}
		
		Bitmap bitmap = thumbnailCacheList.get(url);
		if(bitmap != null)
		{
			this.setImageBitmap(bitmap);
			return true;
		}
		
		return false;
	}
	
	public void setRequestingUrl(Handler handler, String url, String savePath)
	{
		requestingUrl = url;
		this.handler = handler;
		
		if(isInList(url))
		{
			return;
		}
		else
		{
			if(url != null)
			{
				ImageReceiver imageReceiver = new ImageReceiver(getContext(), url, savePath, this, 0, true);
				threadService.executImageRunnable(imageReceiver);
			}
			else
			{
				setImageBitmap(null);
			}
		}
	}
	
	public void beautyVideoSetRequestingUrl(Handler handler, String url, String savePath)
	{
		requestingUrl = url;
		this.handler = handler;
		
		if(isInList(url))
		{
			return;
		}
		else
		{
			if(url != null)
			{
				ImageReceiver imageReceiver = new ImageReceiver(getContext(), url, savePath, this, 0, true, true);
				threadService.executImageRunnable(imageReceiver);
			}
			else
			{
				setImageBitmap(null);
			}
		}
	}

	@Override
	public void updateToView(final Bitmap bitmap, final int position, final String url) {
		handler.post(new Runnable(){

			@Override
			public void run() {				
				if(url != null && requestingUrl != null && bitmap != null)
				{
					if(url.equals(requestingUrl))
					{
						if(thumbnailCacheList != null)
						{
							thumbnailCacheList.put(url, bitmap);
						}
						setBackgroundColor(getContext().getResources().getColor(R.color.Transparent));
						setImageBitmap(bitmap);
						invalidate();
					}
				}
			}
		});
	}
}
