package com.conference.app.lib.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.SpeakerTable;
import com.conference.app.lib.ui.adapter.SpeakerAdapter;

public class Speaker extends Activity {
    private static final String TAG = Speaker.class.getName();
    private static final boolean DEBUG = false;

    private ListView speakerList;
    private SQLiteDatabase db;

    private boolean isFirstInit = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker);

        final DatabaseHelper helper = new DatabaseHelper(Speaker.this);
        db = helper.getReadableDatabase();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstInit) {
			return;
		}
		isFirstInit = false;
		
		initViews();
        initViewValues();
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
        speakerList = (ListView) findViewById(R.id.speakerlist);
        speakerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapter, final View view, final int position, final long id) {
                final Cursor cursor = (Cursor) adapter.getItemAtPosition(position);
                final String uniqueSpeakerId = cursor.getString(cursor.getColumnIndex(SpeakerTable.UNIQUE_ID));

                final Intent intent = new Intent(Speaker.this, SpeakerDetails.class);
                intent.putExtra(SpeakerDetails.EXTRA_SPEAKER_ID, uniqueSpeakerId);
                startActivity(intent);
            }
        });
    }

    private void initViewValues() {
        new LoadSpeakers().execute();
    }

    public void onClickActionBarHome(final View view) {
        final Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // overridePendingTransition(R.anim.home_enter, R.anim.home_exit);
    }

    private class LoadSpeakers extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(final Void... params) {
            final Cursor cursor = db.query(SpeakerTable.TABLE_NAME, new String[] { SpeakerTable.ID, SpeakerTable.UNIQUE_ID,
                    SpeakerTable.DISPLAY_NAME, SpeakerTable.COMPANY, SpeakerTable.LAST_NAME }, null, null, null, null,
                    SpeakerTable.LAST_NAME + " ASC");
            startManagingCursor(cursor);

            return cursor;
        }

        @Override
        protected void onPostExecute(final Cursor result) {
            if (result != null) {
                final SpeakerAdapter adapter = new SpeakerAdapter(Speaker.this, result);
                speakerList.setAdapter(adapter);
                speakerList.invalidate();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
