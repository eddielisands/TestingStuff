package com.conference.app.lib.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public final class ImageCache {
    private static final String TAG = ImageCache.class.getName();
    private static final boolean DEBUG = false;

    private static Map<String, SoftReference<Bitmap>> images = Collections
            .synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());

    /**
     * Clear the image cache. Call only when activity should be finished!
     */
    public synchronized void clearCache() {
        images.clear();
    }

    public synchronized Bitmap getImage(final String url) {
        if (!images.containsKey(url) || images.get(url) == null || images.get(url).get() == null) {
            loadImage(url);
        }
        return images.get(url).get();
    }

    private void loadImage(final String url) {
        Bitmap bmp = null;
        final HttpGet httpRequest = new HttpGet(url);
        final HttpClient httpclient = new DefaultHttpClient();
        try {
            final HttpResponse response = httpclient.execute(httpRequest);
            final HttpEntity entity = response.getEntity();
            final BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
            final InputStream inputStream = bufferedHttpEntity.getContent();

            bmp = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
        } catch (ClientProtocolException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        images.put(url, new SoftReference<Bitmap>(bmp));
    }
}
