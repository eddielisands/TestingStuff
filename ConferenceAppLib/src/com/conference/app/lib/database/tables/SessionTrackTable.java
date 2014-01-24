package com.conference.app.lib.database.tables;

public interface SessionTrackTable {
    String TABLE_NAME = "sessiontrack";

    String ID = "_id";
    String SESSION_ID = "sessionId";
    String TRACK_ID = "trackId";

    String SQL_CREATE = "create table " + TABLE_NAME + "( " + ID + " integer primary key autoincrement, " + SESSION_ID
            + " text not null, " + TRACK_ID + " text not null);";

    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String[] ALL_COLUMNS = new String[] { ID, SESSION_ID, TRACK_ID };
}
