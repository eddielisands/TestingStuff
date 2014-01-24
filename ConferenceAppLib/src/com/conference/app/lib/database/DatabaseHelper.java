package com.conference.app.lib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.conference.app.lib.R;
import com.conference.app.lib.database.tables.ConferenceTable;
import com.conference.app.lib.database.tables.SessionSpeakerTable;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.database.tables.SessionTrackTable;
import com.conference.app.lib.database.tables.SpeakerTable;
import com.conference.app.lib.database.tables.TrackTable;
import com.conference.app.lib.util.Preferences;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getName();
    private static final boolean DEBUG = false;

    private static final int DATABASE_VERSION = 1;

    private final Context ctx;

    public DatabaseHelper(final Context ctx) {
        super(ctx,ctx.getString(R.string.database_name), null, DATABASE_VERSION);
        this.ctx = ctx;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(ConferenceTable.SQL_CREATE);
        db.execSQL(SessionTable.SQL_CREATE);
        db.execSQL(SpeakerTable.SQL_CREATE);
        db.execSQL(TrackTable.SQL_CREATE);
        db.execSQL(SessionSpeakerTable.SQL_CREATE);
        db.execSQL(SessionTrackTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(ConferenceTable.SQL_DROP);
        db.execSQL(SessionTable.SQL_DROP);
        db.execSQL(SpeakerTable.SQL_DROP);
        db.execSQL(TrackTable.SQL_DROP);
        db.execSQL(SessionSpeakerTable.SQL_DROP);
        db.execSQL(SessionTrackTable.SQL_DROP);

        final Preferences pref = new Preferences(ctx);
        pref.setVersion(-1);
        pref.setDownloadFinished(false);

        onCreate(db);
    }
}
