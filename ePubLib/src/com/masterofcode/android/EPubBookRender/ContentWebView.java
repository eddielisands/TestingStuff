package com.masterofcode.android.EPubBookRender;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ContentWebView extends WebView {
	private int						actualSizeWidth = -1, actualSizeOldWidth = -1;
	private int						actualSizeHeight = -1, actualSizeOldHeight = -1;
	private String					contentURL;
	private OnSizeChangedListener	onSizeChangeListener = null;
	private OnScrollChangedListener onScrollChangeListener = null;

	public ContentWebView(Context context) {
		super(context);
	}

	public ContentWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContentWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ContentWebView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
	}

	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		actualSizeWidth = w;
		actualSizeHeight = h;
		actualSizeOldWidth = ow;
		actualSizeOldHeight = oh;
		if(onSizeChangeListener!=null) onSizeChangeListener.onSizeChanged(w, h, ow, oh);
	}

	public int getActualSizeWidth()
	{
		return actualSizeWidth;
	}

	public int getActualSizeOldWidth()
	{
		return actualSizeOldWidth;
	}

	public int getActualSizeHeight()
	{
		return actualSizeHeight;
	}

	public int getActualSizeOldHeight()
	{
		return actualSizeOldHeight;
	}

	public String getContentURL()
	{
		return contentURL;
	}

	public void setContentURL(String contentURL)
	{
		this.contentURL = contentURL;
	}

	public OnSizeChangedListener getOnSizeChangeListener()
	{
		return onSizeChangeListener;
	}

	public void setOnSizeChangeListener(OnSizeChangedListener onSizeChangeListener)
	{
		this.onSizeChangeListener = onSizeChangeListener;
	}

	public OnScrollChangedListener getOnScrollChangeListener()
	{
		return onScrollChangeListener;
	}

	public void setOnScrollChangeListener(OnScrollChangedListener onScrollChangeListener)
	{
		this.onScrollChangeListener = onScrollChangeListener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScrollChangeListener!=null)onScrollChangeListener.OnScrollChanged(l, t, oldl, oldt);
	}
}
