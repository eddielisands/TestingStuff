package com.conference.app.lib.database.tables;

public interface TrackTable {
    String TABLE_NAME = "track";

    String ID = "_id";
    String UNIQUE_ID = "uniqueId";
    String NAME = "name";
    String DETAILS = "details";
    String COLOR = "color";
    String ORDER = "orderNr";

    String SQL_CREATE = "create table " + TABLE_NAME + "( " + ID + " integer primary key autoincrement, " + UNIQUE_ID
            + " text not null, " + NAME + " text not null, " + DETAILS + " text, " + COLOR + " text not null, " + ORDER
            + " integer );";

    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String[] ALL_COLUMNS = new String[] { ID, UNIQUE_ID, NAME, DETAILS, COLOR, ORDER };
}
