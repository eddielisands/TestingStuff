package com.conference.app.lib.database.tables;

public interface SessionTable {
    String TABLE_NAME = "session";

    String ID = "_id";
    String UNIQUE_ID = "uniqueId";
    String NAME = "name";
    String SHORT_NAME = "shortName";
    String START_DATE = "startDate";
    String END_DATE = "endDate";
    String TYPE = "type";
    String LEVEL = "level";
    String DETAILS = "details";
    String STATUS = "status";
    String ROOM_NAME = "roomName";
    String ROOM_FLOOR = "roomFloor";
    String REQUIREMENTS = "requirements";
    String KEY_WORDS = "keywords";
    String HASH_TAG = "hashtag";
    String URL = "url";
    String FAVORITE = "favorite";
    String SEARCH_NAME = "searchname";

    String SQL_CREATE = "create table " + TABLE_NAME + "( " + ID + " integer primary key autoincrement, " + UNIQUE_ID
            + " text not null, " + NAME + " text not null, " + SHORT_NAME + " text not null, " + START_DATE
            + " text not null, " + END_DATE + " text not null, " + TYPE + " text not null, " + LEVEL + " text, "
            + DETAILS + " text not null, " + STATUS + " text, " + ROOM_NAME + " text, " + ROOM_FLOOR + " text, "
            + REQUIREMENTS + " text, " + KEY_WORDS + " text, " + HASH_TAG + " text, " + URL + " text, " + FAVORITE
            + " BOOLEAN, " + SEARCH_NAME + " text );";

    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String[] ALL_COLUMNS = new String[] { ID, UNIQUE_ID, NAME, SHORT_NAME, START_DATE, END_DATE, TYPE, LEVEL, DETAILS,
            STATUS, ROOM_NAME, ROOM_FLOOR, REQUIREMENTS, KEY_WORDS, HASH_TAG, URL, FAVORITE, SEARCH_NAME };
}
