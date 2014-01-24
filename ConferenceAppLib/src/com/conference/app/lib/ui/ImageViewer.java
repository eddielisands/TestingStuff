package com.conference.app.lib.ui;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.ConferenceTable;

public class ImageViewer extends Activity {
	
    private static final int DIALOG_LOAD_IMAGE = 1;
    private static final String HTML_FORMAT = "<img src=\"data:image/jpeg;base64,%1$s\" />";
    private static final int PROGRESS_FINISHED = 100;
    private static String cacheFloorplan;

    private WebView webView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);

        initViews();

        new ImageTask().execute();
    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.imageViewer);
        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        webView.getSettings().setLoadWithOverviewMode(true); //Eddie Li Image fit to screen
        webView.getSettings().setUseWideViewPort(true); //Eddie Li Image fit to screen
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        if (DIALOG_LOAD_IMAGE == id) {
            final ProgressDialog dia = new ProgressDialog(this);
            dia.setCancelable(false);
            dia.setMessage(getString(R.string.imageviewer_dialog_load));
            return dia;
        }

        return super.onCreateDialog(id);
    }

    private class ImageTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_LOAD_IMAGE);
        }

        @Override
        protected String doInBackground(final Void... params) {
            if (cacheFloorplan == null) {
                final DatabaseHelper helper = new DatabaseHelper(ImageViewer.this);
                final SQLiteDatabase db = helper.getReadableDatabase();
                final Cursor cursor = db.query(ConferenceTable.TABLE_NAME, ConferenceTable.ALL_COLUMNS, null, null,
                        null, null, null);

                cursor.moveToFirst();

                final byte[] binaryImage = cursor.getBlob((cursor.getColumnIndex(ConferenceTable.FLOOR_PLAN_IMAGE)));

                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }

                final Bitmap originalImage = BitmapFactory.decodeByteArray(binaryImage, 0, binaryImage.length);

                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                originalImage.compress(CompressFormat.PNG, 0, bos);

                final byte[] bitmapdata = bos.toByteArray();

                final String b64Image = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                final String html = String.format(HTML_FORMAT, b64Image);

                cacheFloorplan = html;
            }

            return cacheFloorplan;
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result == null) {
                removeDialog(DIALOG_LOAD_IMAGE);
            } else {
                webView.loadData(result, "text/html", "utf-8");
                webView.setWebChromeClient(new WebChromeClient() {

                    @Override
                    public void onProgressChanged(final WebView view, final int newProgress) {
                        if (newProgress == PROGRESS_FINISHED) {
                            removeDialog(DIALOG_LOAD_IMAGE);
                        }
                    }
                });
            }
        }
    }
}
