package com.conference.app.lib.database.tables;

public interface SpeakerTable {
    String TABLE_NAME = "speaker";

    String ID = "_id";
    String UNIQUE_ID = "uniqueId";
    String DISPLAY_NAME = "displayName";
    String FIRST_NAME = "firstName";
    String LAST_NAME = "lastName";
    String COMPANY = "company";
    String DETAILS = "details";
    String IMAGE_URL = "imageUrl";
    String IMAGE = "image";
    String URL = "url";
    String EMAIL = "email";
    String TWITTER_ACCOUNT = "twitterAccount";

    String SQL_CREATE = "create table " + TABLE_NAME + "( " + ID + " integer primary key autoincrement, " + UNIQUE_ID
            + " text not null, " + DISPLAY_NAME + " text not null, " + FIRST_NAME + " text not null, " + LAST_NAME
            + " text not null, " + COMPANY + " text not null, " + DETAILS + " text not null, " + IMAGE + " blob, " + IMAGE_URL
            + " text, " + URL + " text, " + EMAIL + " text, " + TWITTER_ACCOUNT + " text );";

    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String[] ALL_COLUMNS = new String[] { ID, UNIQUE_ID, DISPLAY_NAME, FIRST_NAME, LAST_NAME, COMPANY, DETAILS, IMAGE, IMAGE_URL, URL,
            EMAIL, TWITTER_ACCOUNT };
}
