package com.conference.app.lib.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseAdapter;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.SessionSpeakerTable;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.database.tables.SpeakerTable;
import com.conference.app.lib.ui.adapter.SpeakerAdapter;
import com.conference.app.lib.util.DeviceUtil;

public class SessionDetails extends Activity {
    private static final String TAG = SessionDetails.class.getName();
    private static final boolean DEBUG = false;

    public static final String EXTRA_SESSION_ID = "sessionid";

    private ImageView sessionImg;
    private TextView sessionTrack;
    private TextView sessionTitle;
    private WebView sessionDesc;
    private TextView sessionTime;
    private TextView sessionLocation;
    private ListView speakerList;
    private ToggleButton sessionFav;

    private DatabaseAdapter dbAdapter;
    private String uniqueSessionId;
    private SQLiteDatabase db;

private boolean isFirstInit = true;
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessiondetails);

        final DatabaseHelper helper = new DatabaseHelper(this);
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

        extractAndInitIntentValues(getIntent());
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
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        extractAndInitIntentValues(intent);
    }

    private void extractAndInitIntentValues(final Intent intent) {
        if (intent.hasExtra(EXTRA_SESSION_ID)) {
            uniqueSessionId = intent.getStringExtra(EXTRA_SESSION_ID);
            initViewValues(uniqueSessionId);
        }
    }

    private void initViews() {
        dbAdapter = new DatabaseAdapter(this);

        sessionTitle = (TextView) findViewById(R.id.sessionDetailTitle);
        sessionTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionTitle.setSingleLine(false);
                sessionTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                sessionTitle.setCompoundDrawables(null, null, null, null);
            }
        });
        sessionDesc = (WebView) findViewById(R.id.sessionDetailDesc);
        sessionTrack = (TextView) findViewById(R.id.sessionDetailTrack);
        sessionTime = (TextView) findViewById(R.id.sessionDetailTime);
        sessionImg = (ImageView) findViewById(R.id.sessionDetailImg);
        sessionLocation = (TextView) findViewById(R.id.sessionDetailLocation);

        speakerList = (ListView) findViewById(R.id.sessionDetailSpeakerlist);
        speakerList.setScrollbarFadingEnabled(false);
        speakerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                final String uniqueSpeakerId = cursor.getString(cursor.getColumnIndex(SpeakerTable.UNIQUE_ID));

                final Intent intent = new Intent(SessionDetails.this, SpeakerDetails.class);
                intent.putExtra(SpeakerDetails.EXTRA_SPEAKER_ID, uniqueSpeakerId);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        sessionFav = (ToggleButton) findViewById(R.id.sessionDetailFavorite);
        sessionFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                dbAdapter.setFavorite(uniqueSessionId, isChecked);
            }
        });
    }

    private void initViewValues(final String uniqueSessionId) {
        try {
            new LoadSessionDetailValues().execute(uniqueSessionId).get();
            new LoadSpeakers().execute(uniqueSessionId).get();
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

    private class LoadSessionDetailValues extends AsyncTask<String, Void, Cursor> {
        private String trackName;
        private String room;

        @Override
        protected Cursor doInBackground(final String... params) {
            final Cursor cursor = db.query(SessionTable.TABLE_NAME, SessionTable.ALL_COLUMNS, SessionTable.UNIQUE_ID + "='"
                    + params[0] + "'", null, null, null, null);

            // final String query = "select s." + TrackTable.ID + ", s." + TrackTable.NAME + ", s." +
            // TrackTable.UNIQUE_ID + " from "
            // + TrackTable.TABLE_NAME + " s, " + SessionTrackTable.TABLE_NAME + " st where st." +
            // SessionTrackTable.TRACK_ID
            // + "=s." + TrackTable.UNIQUE_ID + " AND st." + SessionTrackTable.SESSION_ID + "=? ORDER BY s." +
            // TrackTable.ID
            // + " ASC";
            // final Cursor trackCursor = db.rawQuery(query, new String[] { params[0] });
            // trackCursor.moveToFirst();
            // trackCursor.close();

            cursor.moveToFirst();
            trackName = cursor.getString(cursor.getColumnIndex(SessionTable.TYPE));

            final String roomName = cursor.getString(cursor.getColumnIndex(SessionTable.ROOM_NAME));
            final String roomFloor = cursor.getString(cursor.getColumnIndex(SessionTable.ROOM_FLOOR));
            if (roomFloor != null && !"null".equals(roomFloor) && roomFloor.length() > 0) {
                room = roomName + "(" + roomFloor + ")";
            } else {
                room = roomName;
            }

            return cursor;
        }

        @Override
        protected void onPostExecute(final Cursor result) {
            if (result != null) {
                sessionTrack.setText(trackName);
                sessionLocation.setText(getString(R.string.room_label) + " " + room);
                sessionFav.setChecked(result.getInt(result.getColumnIndex(SessionTable.FAVORITE)) != 0);
                sessionTitle.setText(result.getString(result.getColumnIndex(SessionTable.NAME)));
                sessionDesc.loadDataWithBaseURL("", result.getString(result.getColumnIndex(SessionTable.DETAILS)), "text/html",
                        "utf-8", "");

                String session = "SESSION".toLowerCase();
                String workshop = "WORKSHOP".toLowerCase();
                String keynote = "KEYNOTE".toLowerCase();
                String sessionType = result.getString(result.getColumnIndex(SessionTable.TYPE)).toLowerCase();
                if (session.toLowerCase().equals(sessionType)) {
                    sessionImg.setImageResource(R.drawable.session_icon);
                } else if (workshop.toLowerCase().equals(sessionType)) {
                    sessionImg.setImageResource(R.drawable.workshop_icon);
                } else if (keynote.toLowerCase().equals(sessionType)) {
                    sessionImg.setImageResource(R.drawable.keynote_icon);
                }

                try {
                    final SimpleDateFormat formater = new SimpleDateFormat("EEE dd/MM/yyyy / HH:mm", Locale.getDefault());
                    final SimpleDateFormat formater2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    final SimpleDateFormat dbEntryFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

                    final String time = formater.format(dbEntryFormater.parse(result.getString(result
                            .getColumnIndex(SessionTable.START_DATE))))
                            + " - "
                            + formater2.format(dbEntryFormater.parse(result.getString(result.getColumnIndex(SessionTable.END_DATE))));
                    sessionTime.setText(time);
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                result.close();
            }
        }
    }

    private class LoadSpeakers extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(final String... params) {
            final String query = "select s." + SpeakerTable.ID + ", s." + SpeakerTable.UNIQUE_ID + ", s." + SpeakerTable.DISPLAY_NAME
                    + ", s." + SpeakerTable.COMPANY + ", s." + SpeakerTable.LAST_NAME + " from " + SpeakerTable.TABLE_NAME + " s, "
                    + SessionSpeakerTable.TABLE_NAME + " st where st." + SessionSpeakerTable.SPEAKER_ID + "=s."
                    + SpeakerTable.UNIQUE_ID + " AND st." + SessionSpeakerTable.SESSION_ID + "=? ORDER BY s." + SpeakerTable.LAST_NAME
                    + " ASC";

            final Cursor cursor = db.rawQuery(query, new String[] { params[0] });
            startManagingCursor(cursor);
            return cursor;
        }

        @Override
        protected void onPostExecute(final Cursor result) {
            if (result != null) {
                final SpeakerAdapter adapter = new SpeakerAdapter(SessionDetails.this, result);
                speakerList.setAdapter(adapter);
                speakerList.invalidate();
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(speakerList);
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
