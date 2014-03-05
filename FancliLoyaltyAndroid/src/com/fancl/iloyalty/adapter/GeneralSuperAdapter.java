package com.fancl.iloyalty.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.fancl.iloyalty.runnable.ImageReceiverCallback;

public class GeneralSuperAdapter extends ArrayAdapter<String> implements ImageReceiverCallback{
	
	public GeneralSuperAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	public GeneralSuperAdapter(Context context, int textViewResourceId, List<String> strings) {
		super(context, textViewResourceId, strings);
	}

	protected Handler handler;
	protected Map<Integer, ImageView> imageViewList = new HashMap<Integer, ImageView>();

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public String getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}
	
	@Override
	public void updateToView(Bitmap bitmap, int position, String url) {
		// TODO Auto-generated method stub
		
	}
	
	protected void setToImageView(final Integer position, final Bitmap bitmap)
	{
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		handler.post(new Runnable(){
			@Override
			public void run() {
				final ImageView currentImageView = imageViewList.get(position);
				
				if(currentImageView != null)
				{
					Object object = currentImageView.getTag();
					if(object instanceof Integer)
					{
						int imageViewPosition = ((Integer)object).intValue();
						int callbackPosition = ((Integer)position).intValue();
						
						if(imageViewPosition == callbackPosition)
						{
							currentImageView.setImageBitmap(bitmap);
						}
					}
				}
			}
		});
	}
}
