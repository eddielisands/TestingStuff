package com.conference.app.lib.database.tables;

public interface ConferenceTable {
    String TABLE_NAME = "conference";

    String ID = "_id";
    String UNIQUE_ID = "uniqueId";
    String NAME = "name";
    String START_DATE = "startDate";
    String END_DATE = "endDate";
    String LOGO_IMAGE_LARGE = "logoImageLarge";
    String LOGO_IMAGE_SMALL = "logoImageSmall";
    String DATA_URL = "dataUrl";
    String AD_IMAGE = "adImageUrl";
    String FLOOR_PLAN_URL = "floorplanImageUrl";
    String FLOOR_PLAN_IMAGE = "floorplanImage";
    String CONFERENCE_URL = "websiteUrl";
    String MAIL = "mail";
    String FEEDBACK_MAIL = "feedbackEmail";
    String FEEDBACK_MAIL_SUBJECT = "feedbackemailsubject";
    String FEEDBACK_TEMPLATE_TEXT = "feedbackTemplateText";
    String PROVIDER_NAME = "providerName";
    String TWITTER_ACCOUNT = "twitterAccount";
    String HASHTAG = "hashtag";
    String DETAILS = "details";
    String STREET = "street";
    String ZIP_CODE = "zipCode";
    String CITY = "city";
    String LOCATION_NAME = "locationName";
    String LOCATION_LAT = "locationLat";
    String LOCATION_LONG = "locationLong";
    String LOCATION_ALT = "locationAlt";

    String SQL_CREATE = "create table " + TABLE_NAME + "( " + ID + " integer primary key autoincrement, " + UNIQUE_ID
            + " text not null, " + NAME + " text not null, " + START_DATE + " text not null, " + END_DATE
            + " text not null, " + LOGO_IMAGE_LARGE + " blob, " + LOGO_IMAGE_SMALL + " blob, " + DATA_URL + " text, "
            + AD_IMAGE + " blob, " + FLOOR_PLAN_IMAGE + " blob, " + CONFERENCE_URL + " text, " + MAIL + " text, "
            + FEEDBACK_MAIL + " text, " + FEEDBACK_TEMPLATE_TEXT + " text, " + PROVIDER_NAME + " text, "
            + TWITTER_ACCOUNT + " text, " + HASHTAG + " text, " + DETAILS + " text, " + STREET + " text, " + ZIP_CODE
            + " text, " + CITY + " text, " + LOCATION_NAME + " text, " + LOCATION_LAT + " text, " + LOCATION_LONG
            + " text, " + LOCATION_ALT + " text, " + FEEDBACK_MAIL_SUBJECT + " text, " + FLOOR_PLAN_URL + " text );";

    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String[] ALL_COLUMNS = new String[] { ID, UNIQUE_ID, NAME, START_DATE, END_DATE, LOGO_IMAGE_LARGE,
            LOGO_IMAGE_SMALL, DATA_URL, AD_IMAGE, FLOOR_PLAN_IMAGE, CONFERENCE_URL, MAIL, FEEDBACK_MAIL,
            FEEDBACK_TEMPLATE_TEXT, PROVIDER_NAME, TWITTER_ACCOUNT, HASHTAG, DETAILS, STREET, ZIP_CODE, CITY,
            LOCATION_NAME, LOCATION_LAT, LOCATION_LONG, LOCATION_ALT, FEEDBACK_MAIL_SUBJECT, FLOOR_PLAN_URL };
}
