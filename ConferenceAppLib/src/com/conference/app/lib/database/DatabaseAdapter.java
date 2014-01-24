package com.conference.app.lib.database;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.conference.app.lib.database.tables.ConferenceTable;
import com.conference.app.lib.database.tables.SessionSpeakerTable;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.database.tables.SessionTrackTable;
import com.conference.app.lib.database.tables.SpeakerTable;
import com.conference.app.lib.database.tables.TrackTable;

public class DatabaseAdapter {
    private static final String TAG = DatabaseAdapter.class.getName();
    private static final boolean DEBUG = false;

    private static final Map<String, String> TRACK_COLOR_CACHE = new HashMap<String, String>();

    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(final Context ctx) {
        dbHelper = new DatabaseHelper(ctx);
    }

    private void openDB() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    private void closeDB() {
        if (database.isOpen()) {
            dbHelper.close();
        }
    }

    // public String getTrackColorByUniqueSessionId(final String uniqueSessionId) {
    // if (!TRACK_COLOR_CACHE.containsKey(uniqueSessionId)) {
    // openDB();
    // final String query = "select s." + TrackTable.ID + ", s." + TrackTable.COLOR + ", s."
    // + TrackTable.UNIQUE_ID + " from " + TrackTable.TABLE_NAME + " s, " + SessionTrackTable.TABLE_NAME
    // + " st where st." + SessionTrackTable.SESSION_ID + "=? AND s." + TrackTable.UNIQUE_ID + " = st."
    // + SessionTrackTable.TRACK_ID + " ORDER BY s." + TrackTable.ID + " ASC";
    //
    // final Cursor cursor = database.rawQuery(query, new String[] { uniqueSessionId });
    // cursor.moveToFirst();
    // final String result = cursor.getString(cursor.getColumnIndex(TrackTable.COLOR));
    // cursor.close();
    // closeDB();
    //
    // TRACK_COLOR_CACHE.put(uniqueSessionId, result);
    // }
    // return TRACK_COLOR_CACHE.get(uniqueSessionId);
    // }

    public void setFavorite(final String unqiueSessionId, final boolean isFavorite) {
        final ContentValues values = new ContentValues();
        values.put(SessionTable.FAVORITE, isFavorite);

        openDB();

        try {
            database.beginTransaction();
            database.update(SessionTable.TABLE_NAME, values, SessionTable.UNIQUE_ID + "='" + unqiueSessionId + "'", null);
            database.setTransactionSuccessful();
        } catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            database.endTransaction();
        }

        closeDB();
    }

    public void createSessionSpeaker(final ContentValues values) {
        final boolean entityExists = existsSessionSpeakerEntity(values);
        openDB();

        try {
            database.beginTransaction();
            if (!entityExists) {
                database.insert(SessionSpeakerTable.TABLE_NAME, null, values);
                database.setTransactionSuccessful();
            }
        } catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            database.endTransaction();
        }

        closeDB();
    }

    private boolean existsSessionSpeakerEntity(final ContentValues values) {
        final SQLiteDatabase readDB = dbHelper.getReadableDatabase();
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SessionSpeakerTable.TABLE_NAME);

        final Cursor cursor = queryBuilder.query(readDB, SessionSpeakerTable.ALL_COLUMNS,
                SessionSpeakerTable.SESSION_ID + "='" + values.getAsString(SessionSpeakerTable.SESSION_ID) + "' AND "
                        + SessionSpeakerTable.SPEAKER_ID + "='" + values.getAsString(SessionSpeakerTable.SPEAKER_ID) + "'", null,
                null, null, null);
        final boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public void createSessionTrack(final ContentValues values) {
        final boolean entityExists = existsSessionTrackEntity(values);
        openDB();

        try {
            database.beginTransaction();
            if (!entityExists) {
                database.insert(SessionTrackTable.TABLE_NAME, null, values);
                database.setTransactionSuccessful();
            }
        } catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            database.endTransaction();
        }

        closeDB();
    }

    private boolean existsSessionTrackEntity(final ContentValues values) {
        final SQLiteDatabase readDB = dbHelper.getReadableDatabase();
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SessionTrackTable.TABLE_NAME);

        final Cursor cursor = queryBuilder.query(readDB, SessionTrackTable.ALL_COLUMNS,
                SessionTrackTable.SESSION_ID + "='" + values.getAsString(SessionTrackTable.SESSION_ID) + "' AND "
                        + SessionTrackTable.TRACK_ID + "='" + values.getAsString(SessionTrackTable.TRACK_ID) + "'", null, null, null,
                null);
        final boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public void createOrUpdateConference(final ContentValues values) {
        createOrUpdateEntity(values, ConferenceTable.TABLE_NAME, ConferenceTable.UNIQUE_ID);
    }

    public void createOrUpdateSpeaker(final ContentValues values) {
        createOrUpdateEntity(values, SpeakerTable.TABLE_NAME, SpeakerTable.UNIQUE_ID);
    }

    public void createOrUpdateSession(final ContentValues values) {
        createOrUpdateEntity(values, SessionTable.TABLE_NAME, SessionTable.UNIQUE_ID);
    }

    public void createOrUpdateTracks(final ContentValues values) {
        createOrUpdateEntity(values, TrackTable.TABLE_NAME, TrackTable.UNIQUE_ID);
    }

    private void createOrUpdateEntity(final ContentValues values, final String table, final String identColumn) {
        final boolean conferenceExists = existsEntity(values.getAsString(identColumn), table, identColumn);

        openDB();

        try {
            database.beginTransaction();
            if (conferenceExists) {
                final String uniqueId = values.getAsString(identColumn);
                database.update(table, values, identColumn + "='" + uniqueId + "'", null);
            } else {
                database.insert(table, null, values);
            }
            database.setTransactionSuccessful();
        } catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            database.endTransaction();
        }

        closeDB();
    }

    private boolean existsEntity(final String key, final String table, final String column) {
        final SQLiteDatabase readDB = dbHelper.getReadableDatabase();
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(table);

        final Cursor cursor = queryBuilder.query(readDB, new String[] { column }, column + "='" + key + "'", null, null, null, null);
        final boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}
