package com.conference.app.lib.database.tables;

public interface SessionSpeakerTable {
    String TABLE_NAME = "sessionspeaker";

    String ID = "_id";
    String SESSION_ID = "sessionId";
    String SPEAKER_ID = "speakerId";

    String SQL_CREATE = "create table " + TABLE_NAME + "( " + ID + " integer primary key autoincrement, " + SESSION_ID
            + " text not null, " + SPEAKER_ID + " text not null);";

    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String[] ALL_COLUMNS = new String[] { ID, SESSION_ID, SPEAKER_ID };
}
