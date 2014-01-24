package com.conference.app.lib.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.database.tables.SessionTrackTable;
import com.conference.app.lib.database.tables.TrackTable;
import com.conference.app.lib.ui.adapter.SessionAdapter;
import com.conference.app.lib.ui.adapter.SessionSelectionAdapter;

public class Session extends Activity {
    private static final String TAG = Session.class.getName();
    private static final boolean DEBUG = false;

    public static final String EXTRA_TRACK_SEL = "tracksel";
    public static final String EXTRA_TRACK_NAME = "trackname";
    private static final String EXTRA_TRACK_ALL_FLAG = "ALL";

    private ListView sessionsList;
    private TextView sessionsTitle;

    private SQLiteDatabase db;

    private boolean isFirstInit = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions);

        final DatabaseHelper helper = new DatabaseHelper(Session.this);
        db = helper.getReadableDatabase();

        initViews();
    }
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isFirstInit) {
			return;
		}
		isFirstInit = false;
		
		final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TRACK_SEL)) {
            if (intent.hasExtra(EXTRA_TRACK_NAME) && intent.getStringExtra(EXTRA_TRACK_NAME) != null) {
                sessionsTitle.setText(intent.getStringExtra(EXTRA_TRACK_NAME));
                sessionsTitle.setSelected(true);
            }
            new LoadSessions().execute(intent.getStringExtra(EXTRA_TRACK_SEL));
        } else {
            new LoadTracks().execute();
        }
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
        isFirstInit = true;
    }

    private void initViews() {
        sessionsList = (ListView) findViewById(R.id.sessionslistview);
        sessionsTitle = (TextView) findViewById(R.id.sessions_title_text);
    }

    public void onClickActionBarHome(final View view) {
        final Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // overridePendingTransition(R.anim.home_enter, R.anim.home_exit);
    }

    private class LoadTracks extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(final Void... params) {
            final Cursor cursor = db.query(TrackTable.TABLE_NAME, new String[] { TrackTable.ID, TrackTable.COLOR, TrackTable.NAME,
                    TrackTable.UNIQUE_ID, TrackTable.ORDER }, null, null, null, null, TrackTable.ORDER + " ASC");
            // startManagingCursor(cursor);
            return cursor;
        }

        @Override
        protected void onPostExecute(final Cursor result) {
            if (result != null) {
                final SessionSelectionAdapter adapter = new SessionSelectionAdapter(Session.this, result);
                sessionsList.setAdapter(adapter);
                sessionsList.invalidate();
                adapter.notifyDataSetChanged();
                sessionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                        if (position == 0) {
                            final Intent intent = new Intent(Session.this, Session.class);
                            intent.putExtra(EXTRA_TRACK_SEL, EXTRA_TRACK_ALL_FLAG);
                            intent.putExtra(EXTRA_TRACK_NAME, getString(R.string.sessions_title));
                            startActivity(intent);
                        } else {
                            final Cursor cursor = (Cursor) parent.getItemAtPosition(position - 1);
                            final String uniqueTrackId = cursor.getString(cursor.getColumnIndex(TrackTable.UNIQUE_ID));
                            final String trackName = cursor.getString(cursor.getColumnIndex(TrackTable.NAME));

                            final Intent intent = new Intent(Session.this, Session.class);
                            intent.putExtra(EXTRA_TRACK_SEL, uniqueTrackId);
                            intent.putExtra(EXTRA_TRACK_NAME, trackName);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    private class LoadSessions extends AsyncTask<String, Void, Cursor> {

    	@Override
        protected Cursor doInBackground(final String... params) {
            Cursor cursor;
            if (EXTRA_TRACK_ALL_FLAG.equals(params[0])) {
                cursor = db.query(SessionTable.TABLE_NAME, new String[] { SessionTable.ID, SessionTable.TYPE, SessionTable.NAME,
                        SessionTable.SHORT_NAME, SessionTable.SEARCH_NAME, SessionTable.UNIQUE_ID, SessionTable.FAVORITE,
//                        SessionTable.START_DATE, SessionTable.END_DATE }, null, null, null, null, SessionTable.SEARCH_NAME
//                        + " COLLATE NOCASE"); //Eddie Li Change the order sequence
                        SessionTable.START_DATE, SessionTable.END_DATE }, null, null, null, null, SessionTable.START_DATE
                        + " ASC");
            } else {
                final String query = "select s." + SessionTable.ID + ", s." + SessionTable.TYPE + ", s." + SessionTable.NAME + ", s."
                        + SessionTable.SHORT_NAME + ", s." + SessionTable.UNIQUE_ID + ", s." + SessionTable.FAVORITE + ", s."
                        + SessionTable.SEARCH_NAME + ", s." + SessionTable.START_DATE + ", s." + SessionTable.END_DATE + " from "
                        + SessionTable.TABLE_NAME + " s, " + SessionTrackTable.TABLE_NAME + " st where st."
                        + SessionTrackTable.SESSION_ID + "=s." + SessionTable.UNIQUE_ID + " AND st." + SessionTrackTable.TRACK_ID
//                        + "=? ORDER BY s." + SessionTable.SEARCH_NAME + " COLLATE NOCASE"; //Eddie Li Change the order sequence
                        + "=? ORDER BY s." + SessionTable.START_DATE + " ASC";
                Log.d(TAG, query);
                cursor = db.rawQuery(query, new String[] { params[0] });
            }
            startManagingCursor(cursor);

            return cursor;
        }

        @Override
        protected void onPostExecute(final Cursor result) {
            if (result != null) {
                final SessionAdapter adapter = new SessionAdapter(Session.this, result);
                sessionsList.setAdapter(adapter);
                sessionsList.invalidate();
                adapter.notifyDataSetChanged();
                sessionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                        final String uniqueSessionId = cursor.getString(cursor.getColumnIndex(SessionTable.UNIQUE_ID));

                        final Intent intent = new Intent(Session.this, SessionDetails.class);
                        intent.putExtra(SessionDetails.EXTRA_SESSION_ID, uniqueSessionId);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
