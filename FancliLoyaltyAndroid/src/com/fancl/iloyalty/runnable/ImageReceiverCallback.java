package com.fancl.iloyalty.runnable;

import android.graphics.Bitmap;

public interface ImageReceiverCallback {
	public void updateToView(Bitmap bitmap, int position, String url);
}