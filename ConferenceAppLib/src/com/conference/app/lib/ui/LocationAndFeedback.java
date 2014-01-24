package com.conference.app.lib.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.ConferenceTable;

public class LocationAndFeedback extends Activity {
    private static final String TAG = LocationAndFeedback.class.getName();
    private static final boolean DEBUG = false;

    private String feedbackEmail;
    private String feedbackSubject;
    private String feedbackText;

    private String longitude;
    private String latitude;
    private String locationName;
    private String locationStreet;

    private View floorplanButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        initViews();
        initTextValues();
    }

    private void initViews() {
        floorplanButton = findViewById(R.id.locationRoomPlan);
    }

    private void initTextValues() {
        final DatabaseHelper helper = new DatabaseHelper(this);
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor cursor = db.query(ConferenceTable.TABLE_NAME, ConferenceTable.ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();

        feedbackEmail = cursor.getString(cursor.getColumnIndex(ConferenceTable.FEEDBACK_MAIL));
        feedbackSubject = cursor.getString(cursor.getColumnIndex(ConferenceTable.FEEDBACK_MAIL_SUBJECT));
        feedbackText = cursor.getString(cursor.getColumnIndex(ConferenceTable.FEEDBACK_TEMPLATE_TEXT));

        longitude = cursor.getString(cursor.getColumnIndex(ConferenceTable.LOCATION_LONG));
        latitude = cursor.getString(cursor.getColumnIndex(ConferenceTable.LOCATION_LAT));
        locationName = cursor.getString(cursor.getColumnIndex(ConferenceTable.LOCATION_NAME));
        locationStreet = cursor.getString(cursor.getColumnIndex(ConferenceTable.STREET));

        // Disable roomplan button, if not available
        if (cursor.isNull(cursor.getColumnIndex(ConferenceTable.FLOOR_PLAN_IMAGE))
                || cursor.isNull(cursor.getColumnIndex(ConferenceTable.FLOOR_PLAN_URL))) {
            floorplanButton.setVisibility(View.GONE);
        }

        cursor.close();
        if (db.isOpen()) {
            db.close();
        }
    }

    public void onClickActionBarHome(final View view) {
        final Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // overridePendingTransition(R.anim.home_enter, R.anim.home_exit);
    }

    public void onClickMap(final View view) {
        final Uri geoUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + locationStreet + " " + locationName + "&z=16");
        final Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);
        startActivity(Intent.createChooser(mapCall, getString(R.string.location_show_map)));
    }

    public void onClickFeedback(final View view) {
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { feedbackEmail });
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, feedbackSubject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, feedbackText);
        startActivity(Intent.createChooser(intent, getString(R.string.location_send_via_email)));
    }

    public void onClickRoomplan(final View view) {
        startActivity(new Intent(this, ImageViewer.class));
    }
}
