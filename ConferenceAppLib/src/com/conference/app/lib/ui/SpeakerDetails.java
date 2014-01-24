package com.conference.app.lib.ui;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.SessionSpeakerTable;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.database.tables.SpeakerTable;
import com.conference.app.lib.ui.adapter.SessionAdapter;
import com.conference.app.lib.ui.adapter.SpeakerAdapter;
import com.conference.app.lib.util.DeviceUtil;

public class SpeakerDetails extends Activity {
	private static final String TAG = SpeakerDetails.class.getName();
	private static final boolean DEBUG = false;

	public static final String EXTRA_SPEAKER_ID = "speakerID";
	private static final int CURSOR_LOADER = 1;

	private TextView speakerName;
	private TextView speakerCompany;
	private WebView speakerDesc;
	private ImageView speakerImg;
	private ListView sessionList;

	private String uniqueSpeakerId;

	private SQLiteDatabase db;

	private boolean isFirstInit = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speakerdetails);

        final DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
        
        isFirstInit = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uniqueSpeakerId != null) {
            try {
                new LoadSessionsForSpeaker().execute(uniqueSpeakerId).get();
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (ExecutionException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        
        if (!isFirstInit) {
			return;
		}
		isFirstInit = false;
		
        extractAndInitIntentValues(getIntent());
    }

	@Override
	protected void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);
		extractAndInitIntentValues(intent);
	}

	private void extractAndInitIntentValues(final Intent intent) {
		if (intent.hasExtra(EXTRA_SPEAKER_ID)) {
			uniqueSpeakerId = intent.getStringExtra(EXTRA_SPEAKER_ID);
			initViewValues(uniqueSpeakerId);
		}
	}

	private void initViews() {
		speakerName = (TextView) findViewById(R.id.speakerDetailName);
		speakerCompany = (TextView) findViewById(R.id.speakerDetailCompany);
		speakerDesc = (WebView) findViewById(R.id.speakerDetailDesc);
		speakerImg = (ImageView) findViewById(R.id.speakerDetailImg);
		sessionList = (ListView) findViewById(R.id.speakerDetailSessionlist);
		sessionList.setScrollbarFadingEnabled(false);
	}

	private void initViewValues(final String uniqueSpeakerId) {
		final Cursor cursor = db.query(SpeakerTable.TABLE_NAME, SpeakerTable.ALL_COLUMNS, SpeakerTable.UNIQUE_ID + "='"
				+ uniqueSpeakerId + "'", null, null, null, null);

		cursor.moveToFirst();

		speakerName.setText(cursor.getString(cursor.getColumnIndex(SpeakerTable.DISPLAY_NAME)));
		speakerCompany.setText(cursor.getString(cursor.getColumnIndex(SpeakerTable.COMPANY)));
		speakerDesc.loadDataWithBaseURL("", cursor.getString(cursor.getColumnIndex(SpeakerTable.DETAILS)), "text/html", "utf-8", "");

		if (SpeakerAdapter.SPEAKER_IMAGES.containsKey(uniqueSpeakerId)) {
			speakerImg.setImageBitmap(SpeakerAdapter.SPEAKER_IMAGES.get(uniqueSpeakerId));
		} else {
			final byte[] binaryImage = cursor.getBlob(cursor.getColumnIndex(SpeakerTable.IMAGE));
			if (binaryImage != null) {
				final Bitmap bitmap = BitmapFactory.decodeByteArray(binaryImage, 0, binaryImage.length);
				speakerImg.setImageBitmap(bitmap);
				SpeakerAdapter.SPEAKER_IMAGES.put(uniqueSpeakerId, bitmap);
			}
		}

		cursor.close();

		try {
			new LoadSessionsForSpeaker().execute(uniqueSpeakerId).get();
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ExecutionException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	public void onClickActionBarHome(final View view) {
		final Intent intent = new Intent(this, Dashboard.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		// overridePendingTransition(R.anim.home_enter, R.anim.home_exit);
	}

	private class LoadSessionsForSpeaker extends AsyncTask<String, Void, Cursor> {
		@Override
		protected Cursor doInBackground(final String... params) {
			final String query = "select s." + SessionTable.ID + ", s." + SessionTable.TYPE + ", s." + SessionTable.NAME + ", s."
					+ SessionTable.SHORT_NAME + ", s." + SessionTable.UNIQUE_ID + ", s." + SessionTable.FAVORITE + ", s."
					+ SessionTable.SEARCH_NAME + ", s." + SessionTable.START_DATE + ", s." + SessionTable.END_DATE + " from "
					+ SessionTable.TABLE_NAME + " s, " + SessionSpeakerTable.TABLE_NAME + " st where st."
					+ SessionSpeakerTable.SESSION_ID + "=s." + SessionTable.UNIQUE_ID + " AND st." + SessionSpeakerTable.SPEAKER_ID
					+ "=? ORDER BY s." + SessionTable.SEARCH_NAME + " COLLATE NOCASE";
			final Cursor cursor = db.rawQuery(query, new String[] { params[0] });
			return cursor;
		}

		@Override
		protected void onPostExecute(final Cursor result) {
			if (result != null) {
				final SessionAdapter adapter = new SessionAdapter(SpeakerDetails.this, result);
				sessionList.setAdapter(adapter);
				sessionList.invalidate();
				adapter.notifyDataSetChanged();
				sessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
						final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
						final String uniqueSessionId = cursor.getString(cursor.getColumnIndex(SessionTable.UNIQUE_ID));

						final Intent intent = new Intent(SpeakerDetails.this, SessionDetails.class);
						intent.putExtra(SessionDetails.EXTRA_SESSION_ID, uniqueSessionId);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					}
				});
				setListViewHeightBasedOnChildren(sessionList);
			}
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        int totalHeight = DeviceUtil.dip2px(listView.getContext(), 10);
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT + DeviceUtil.dip2px(listView.getContext(), 5)));
            }
            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        int padding = DeviceUtil.dip2px(listView.getContext(), 5);
        listView.setPadding(padding, padding, padding, padding);
        listView.requestLayout();
    }
}
