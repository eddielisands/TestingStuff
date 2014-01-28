package com.masterofcode.android.magreader.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ImageLoader {

		private static Map<String, Bitmap> imageMap = new WeakHashMap<String, Bitmap>();

		private File cacheDir;
		private ImageQueue imageQueue = new ImageQueue();
		private Thread imageLoaderThread = new Thread(new ImageQueueManager());
		private Activity mActivity;
		
		public ImageLoader(Context context, Activity activity) {
			this.mActivity = activity;
			// Make background thread low priority, to avoid affecting UI performance
			imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

			ContextWrapper cw = new ContextWrapper(context);
	        cacheDir = cw.getDir("thumbnails", Context.MODE_PRIVATE);
		}

		public void displayImage(final String url, final ImageView imageView) {
			if(imageMap.containsKey(url)){
				imageView.setImageBitmap(imageMap.get(url));}
			else {
					queueImage(url, mActivity, imageView);
					imageView.setImageResource(R.drawable.no_image);
			}
		}

		private void queueImage(String url, Activity activity, ImageView imageView) {
			// This ImageView might have been used for other images, so we clear 
			// the queue of old tasks before starting.
			imageQueue.Clean(imageView);
			ImageRef p=new ImageRef(url, imageView);

			synchronized(imageQueue.imageRefs) {
				imageQueue.imageRefs.push(p);
				imageQueue.imageRefs.notifyAll();
			}

			// Start thread if it's not started yet
			if(imageLoaderThread.getState() == Thread.State.NEW)
				imageLoaderThread.start();
		}

		private Bitmap getBitmap(String url) {
			String filename = String.valueOf(url.hashCode());
			File f = new File(cacheDir, filename);

			// Is the bitmap in our cache?
			Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
			if(bitmap != null) return bitmap;

			// Nope, have to download it
			try {
				bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
				// save bitmap to cache for later
				writeFile(bitmap, f);

				return bitmap;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		private void writeFile(Bitmap bmp, File f) {
			FileOutputStream out = null;

			try {
				out = new FileOutputStream(f);
				if(bmp != null)
					bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally { 
				try { if (out != null ) out.close(); }
				catch(Exception ex) {} 
			}
		}

		/** Classes **/

		private class ImageRef {
			public String url;
			public ImageView imageView;

			public ImageRef(String u, ImageView i) {
				url=u;
				imageView=i;
			}
		}

		//stores list of images to download
		private class ImageQueue {
			private Stack<ImageRef> imageRefs = 
				new Stack<ImageRef>();

			//removes all instances of this ImageView
			public synchronized void Clean(ImageView view) {
				try{
					for(Iterator<ImageRef> it = imageRefs.iterator(); it.hasNext();){
						ImageRef mImageRef = it.next();
						if(mImageRef.imageView == view)
							it.remove();
					}
					/**
					for (ImageRef mImageRef : imageRefs){
						if(mImageRef.imageView == view)
							imageRefs.remove(mImageRef);
					}
					*/
				} catch(Exception exc){
					exc.printStackTrace();
				}
			}
		}

		private class ImageQueueManager implements Runnable {
			@Override
			public void run() {
				try {
					while(true) {
						// Thread waits until there are images in the 
						// queue to be retrieved
						if(imageQueue.imageRefs.size() == 0) {
							synchronized(imageQueue.imageRefs) {
								imageQueue.imageRefs.wait();
							}
						}

						// When we have images to be loaded
						if(imageQueue.imageRefs.size() != 0) {
							ImageRef imageToLoad;

							synchronized(imageQueue.imageRefs) {
								imageToLoad = imageQueue.imageRefs.pop();
							}

							Bitmap bmp = getBitmap(imageToLoad.url);
							imageMap.put(imageToLoad.url, bmp);
							if(Constants.Debug)
				       			   Log.d("favicon", "imageMap.put: " + imageToLoad.url);
							Object tag = imageToLoad.imageView.getTag();

							// Make sure we have the right view - thread safety defender
							if(tag != null && ((String)tag).equals(imageToLoad.url)) {
								BitmapDisplayer bmpDisplayer = 
									new BitmapDisplayer(bmp, imageToLoad.imageView);

								//Activity a = (Activity)imageToLoad.imageView.getContext();

								//a.runOnUiThread(bmpDisplayer);
								mActivity.runOnUiThread(bmpDisplayer);
							}
						}

						if(Thread.interrupted())
							break;
					}
				} catch (InterruptedException e) {}
			}
		}

		//Used to display bitmap in the UI thread
		private class BitmapDisplayer implements Runnable {
			Bitmap bitmap;
			ImageView imageView;

			public BitmapDisplayer(Bitmap b, ImageView i) {
				bitmap=b;
				imageView=i;
			}

			public void run() {
				if(bitmap != null)
					imageView.setImageBitmap(bitmap);
				else
					imageView.setImageResource(R.drawable.no_image);
			}
		}
		
		public static Map<String, Bitmap> getImageMap() {
			return imageMap;
		}
}