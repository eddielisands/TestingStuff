package com.masterofcode.android.EPubBookRender;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TOCImageView extends ImageView {
	private int				topicId = -1;

	public TOCImageView(Context context) {
		super(context);
		MyInit();
	}

	public TOCImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		MyInit();
	}

	public TOCImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		MyInit();
	}
	
	private void MyInit()
	{
		
	}

	public void setTopicId(int topicId)
	{
		this.topicId = topicId;
	}

	public int getTopicId()
	{
		return topicId;
	}

}
