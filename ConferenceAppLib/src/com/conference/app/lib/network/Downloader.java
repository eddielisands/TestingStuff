package com.conference.app.lib.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseAdapter;
import com.conference.app.lib.database.tables.ConferenceTable;
import com.conference.app.lib.database.tables.SessionSpeakerTable;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.database.tables.SessionTrackTable;
import com.conference.app.lib.database.tables.SpeakerTable;
import com.conference.app.lib.database.tables.TrackTable;
import com.conference.app.lib.network.jsonkeys.ConferenceKeys;
import com.conference.app.lib.network.jsonkeys.ConfigKeys;
import com.conference.app.lib.network.jsonkeys.SessionKeys;
import com.conference.app.lib.network.jsonkeys.SpeakerKeys;
import com.conference.app.lib.network.jsonkeys.TrackKeys;
import com.conference.app.lib.util.Preferences;

public class Downloader {
    private static final String TAG = Downloader.class.getName();
    private static final boolean DEBUG = false;

    private static final int MAX_SQL_DATA_SIZE_IN_BYTES = 256 * 1024;

    private final Context ctx;
    private final Preferences pref;
    private final DatabaseAdapter dbAdapter;

    public Downloader(final Context ctx) {
        this.ctx = ctx;
        this.pref = new Preferences(ctx);
        this.dbAdapter = new DatabaseAdapter(ctx);
    }

    public boolean downloadData() throws IOException, JSONException {
        final HttpClient client = new DefaultHttpClient();
        final HttpGet get = new HttpGet(ctx.getString(R.string.server_url));
        get.setHeader(ServerValues.ACCEPT_HEADER, ServerValues.JSON_CONTENT_VALUE);

        final HttpResponse response = client.execute(get);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            final HttpEntity entity = response.getEntity();
            final String stringEntity = EntityUtils.toString(entity, "UTF-8");

            try {
                final JSONObject fullJsonObj = new JSONObject(stringEntity);

                if (pref.getVersion() == -1 || pref.getVersion() < fullJsonObj.getInt(ConfigKeys.VERSION_CODE) || DEBUG) {
                    final int version = fullJsonObj.getInt(ConfigKeys.VERSION_CODE);

                    downloadConference(fullJsonObj.getJSONObject(ConferenceKeys.PARENT_KEY));
                    downloadSpeakers(fullJsonObj.getJSONArray(SpeakerKeys.PARENT_KEY));
                    downloadSessions(fullJsonObj.getJSONArray(SessionKeys.PARENT_KEY));
                    downloadTracks(fullJsonObj.getJSONArray(TrackKeys.PARENT_KEY));

                    pref.setVersion(version);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            return true;
        } else {
            return false;
        }
    }

    private void downloadConference(final JSONObject conferenceJsonObj) throws JSONException, IOException {
        if (conferenceJsonObj != null) {
            final ContentValues confValues = new ContentValues();
            confValues.put(ConferenceTable.DATA_URL, conferenceJsonObj.getString(ConferenceKeys.DATA_URL));
            confValues.put(ConferenceTable.END_DATE, conferenceJsonObj.getString(ConferenceKeys.END_DATE));
            confValues.put(ConferenceTable.FEEDBACK_MAIL, conferenceJsonObj.getString(ConferenceKeys.FEEDBACK_MAIL));
            confValues.put(ConferenceTable.FEEDBACK_TEMPLATE_TEXT, conferenceJsonObj.getString(ConferenceKeys.FEEDBACK_TEMPLATE_TEXT));
            confValues.put(ConferenceTable.FEEDBACK_MAIL_SUBJECT, conferenceJsonObj.getString(ConferenceKeys.FEEDBACK_MAIL_SUBJECT));
            confValues.put(ConferenceTable.NAME, conferenceJsonObj.getString(ConferenceKeys.NAME));
            confValues.put(ConferenceTable.PROVIDER_NAME, conferenceJsonObj.getString(ConferenceKeys.PROVIDER_NAME));
            confValues.put(ConferenceTable.START_DATE, conferenceJsonObj.getString(ConferenceKeys.START_DATE));
            confValues.put(ConferenceTable.TWITTER_ACCOUNT, conferenceJsonObj.getString(ConferenceKeys.TWITTER_ACCOUNT));
            confValues.put(ConferenceTable.UNIQUE_ID, conferenceJsonObj.getString(ConferenceKeys.UNIQUE_ID));
            confValues.put(ConferenceTable.CONFERENCE_URL, conferenceJsonObj.getString(ConferenceKeys.WEBSITE_URL));
            confValues.put(ConferenceTable.MAIL, conferenceJsonObj.getString(ConferenceKeys.MAIL));

            confValues.put(ConferenceTable.DETAILS, conferenceJsonObj.getString(ConferenceKeys.DETAILS));
            confValues.put(ConferenceTable.STREET, conferenceJsonObj.getString(ConferenceKeys.STREET));
            confValues.put(ConferenceTable.ZIP_CODE, conferenceJsonObj.getString(ConferenceKeys.ZIP_CODE));
            confValues.put(ConferenceTable.CITY, conferenceJsonObj.getString(ConferenceKeys.CITY));
            confValues.put(ConferenceTable.LOCATION_NAME, conferenceJsonObj.getString(ConferenceKeys.LOCATION_NAME));
            confValues.put(ConferenceTable.LOCATION_LAT, conferenceJsonObj.getString(ConferenceKeys.LOCATION_LAT));
            confValues.put(ConferenceTable.LOCATION_LONG, conferenceJsonObj.getString(ConferenceKeys.LOCATION_LONG));
            confValues.put(ConferenceTable.LOCATION_ALT, conferenceJsonObj.getString(ConferenceKeys.LOCATION_ALT));

            final JSONArray hashtags = conferenceJsonObj.getJSONArray(ConferenceKeys.HASHTAG);
            confValues.put(ConferenceTable.HASHTAG, getHashTagString(hashtags));

            // final String smallLogoUrl = conferenceJsonObj.getString(ConferenceKeys.LOGO_IMAGE_SMALL);
            // confValues.put(ConferenceTable.LOGO_IMAGE_SMALL, downloadImageToByteArray(smallLogoUrl));
            // final String largeLogoUrl = conferenceJsonObj.getString(ConferenceKeys.LOGO_IMAGE_LARGE);
            // confValues.put(ConferenceTable.LOGO_IMAGE_LARGE, downloadImageToByteArray(largeLogoUrl));
            // final String adUrl = conferenceJsonObj.getString(ConferenceKeys.AD_IMAGE_URL);
            // confValues.put(ConferenceTable.AD_IMAGE, downloadImageToByteArray(adUrl));
            final String floorPlanUrl = conferenceJsonObj.getString(ConferenceKeys.FLOORPLAN_IMAGE_URL);
            confValues.put(ConferenceTable.FLOOR_PLAN_IMAGE, downloadImageToByteArray(floorPlanUrl));
            confValues.put(ConferenceTable.FLOOR_PLAN_URL, floorPlanUrl);

            dbAdapter.createOrUpdateConference(confValues);
        }
    }

    private void downloadSpeakers(final JSONArray speakersJsonObj) throws JSONException, IOException {
        if (speakersJsonObj != null) {
            for (int i = 0; i < speakersJsonObj.length(); i++) {
                downloadSingleSpeaker(speakersJsonObj.getJSONObject(i));
            }
        }
    }

    private void downloadSingleSpeaker(final JSONObject speakerJsonObj) throws JSONException, IOException {
        if (speakerJsonObj != null) {
            final ContentValues speakerValues = new ContentValues();
            speakerValues.put(SpeakerTable.COMPANY, getStringNullSafe(speakerJsonObj, SpeakerKeys.COMPANY));
            speakerValues.put(SpeakerTable.DETAILS, getStringNullSafe(speakerJsonObj, SpeakerKeys.DETAILS));
            speakerValues.put(SpeakerTable.DISPLAY_NAME, getStringNullSafe(speakerJsonObj, SpeakerKeys.DISPLAY_NAME));
            speakerValues.put(SpeakerTable.EMAIL, getStringNullSafe(speakerJsonObj, SpeakerKeys.EMAIL));
            speakerValues.put(SpeakerTable.FIRST_NAME, getStringNullSafe(speakerJsonObj, SpeakerKeys.FIRST_NAME));
            speakerValues.put(SpeakerTable.LAST_NAME, getStringNullSafe(speakerJsonObj, SpeakerKeys.LAST_NAME));
            // speakerValues.put(SpeakerTable.TWITTER_ACCOUNT, speakerJsonObj.getString(SpeakerKeys.TWITTER_ACCOUNT));
            speakerValues.put(SpeakerTable.URL, getStringNullSafe(speakerJsonObj, SpeakerKeys.URL));

            final String uniqueId = speakerJsonObj.getString(SpeakerKeys.UNIQUE_ID);
            speakerValues.put(SpeakerTable.UNIQUE_ID, uniqueId);

            speakerValues.put(SpeakerTable.IMAGE_URL, speakerJsonObj.getString(SpeakerKeys.IMAGE));

            dbAdapter.createOrUpdateSpeaker(speakerValues);
        }
    }

    private static String getStringNullSafe(final JSONObject speakerJsonObj, String field) throws JSONException {
        if (speakerJsonObj.isNull(field)) {
            return "";
        }
        return speakerJsonObj.getString(field);
    }

    private void downloadSessions(final JSONArray sessionsJsonObj) throws JSONException {
        if (sessionsJsonObj != null) {
            for (int i = 0; i < sessionsJsonObj.length(); i++) {
                downloadSingleSession(sessionsJsonObj.getJSONObject(i));
            }
        }
    }

    private void downloadSingleSession(final JSONObject sessionJsonObj) throws JSONException {
        if (sessionJsonObj != null) {
            final ContentValues sessionValues = new ContentValues();
            sessionValues.put(SessionTable.DETAILS, sessionJsonObj.getString(SessionKeys.DETAILS));
            sessionValues.put(SessionTable.END_DATE, sessionJsonObj.getString(SessionKeys.END_DATE));
            // sessionValues.put(SessionTable.KEY_WORDS, sessionJsonObj.getString(SessionKeys.KEY_WORDS));
            sessionValues.put(SessionTable.LEVEL, sessionJsonObj.getString(SessionKeys.LEVEL));
            sessionValues.put(SessionTable.REQUIREMENTS, sessionJsonObj.getString(SessionKeys.REQUIREMENTS));
            sessionValues.put(SessionTable.ROOM_FLOOR, sessionJsonObj.getString(SessionKeys.ROOM_FLOOR));
            sessionValues.put(SessionTable.ROOM_NAME, sessionJsonObj.getString(SessionKeys.ROOM_NAME));
            sessionValues.put(SessionTable.SHORT_NAME, sessionJsonObj.getString(SessionKeys.SHORT_NAME));
            sessionValues.put(SessionTable.START_DATE, sessionJsonObj.getString(SessionKeys.START_DATE));
            sessionValues.put(SessionTable.TYPE, sessionJsonObj.getString(SessionKeys.TYPE));
            sessionValues.put(SessionTable.UNIQUE_ID, sessionJsonObj.getString(SessionKeys.UNIQUE_ID));
            sessionValues.put(SessionTable.URL, sessionJsonObj.getString(SessionKeys.URL));
            final String sessionName = sessionJsonObj.getString(SessionKeys.NAME);
            sessionValues.put(SessionTable.NAME, sessionName);
            sessionValues.put(SessionTable.SEARCH_NAME, sessionName.replaceAll("[^a-zA-Z0-9]+", ""));

            // final JSONArray hashtags = sessionJsonObj.getJSONArray(SessionKeys.HASHTAG);
            // sessionValues.put(SessionTable.HASH_TAG, getHashTagString(hashtags));

            // sessionValues.put(SessionTable.STATUS, sessionJsonObj.getJSONArray(SessionKeys.STATUS).toString());

            dbAdapter.createOrUpdateSession(sessionValues);

            final String uniqueSessionId = sessionJsonObj.getString(SessionKeys.UNIQUE_ID);

            final JSONArray speakerIds = sessionJsonObj.getJSONArray(SessionKeys.SPEAKERS_UNIQUE_IDS);
            for (int i = 0; i < speakerIds.length(); i++) {
                final String uniqueSpeakerId = speakerIds.getString(i);
                final ContentValues values = new ContentValues();
                values.put(SessionSpeakerTable.SESSION_ID, uniqueSessionId);
                values.put(SessionSpeakerTable.SPEAKER_ID, uniqueSpeakerId);
                dbAdapter.createSessionSpeaker(values);
            }

            final JSONArray trackIds = sessionJsonObj.getJSONArray(SessionKeys.TRACKS_UNIQUE_IDS);
            for (int i = 0; i < trackIds.length(); i++) {
                final String uniqueTrackId = trackIds.getString(i);
                final ContentValues values = new ContentValues();
                values.put(SessionTrackTable.SESSION_ID, uniqueSessionId);
                values.put(SessionTrackTable.TRACK_ID, uniqueTrackId);
                dbAdapter.createSessionTrack(values);
            }
        }
    }

    private void downloadTracks(final JSONArray tracksJsonObj) throws JSONException {
        if (tracksJsonObj != null) {
            for (int i = 0; i < tracksJsonObj.length(); i++) {
                downloadSingleTrack(tracksJsonObj.getJSONObject(i));
            }
        }
    }

    private void downloadSingleTrack(final JSONObject trackJsonObj) throws JSONException {
        if (trackJsonObj != null) {
            final ContentValues trackValues = new ContentValues();
            trackValues.put(TrackTable.COLOR, trackJsonObj.getString(TrackKeys.COLOR));
            trackValues.put(TrackTable.DETAILS, trackJsonObj.getString(TrackKeys.DETAILS));
            trackValues.put(TrackTable.NAME, trackJsonObj.getString(TrackKeys.NAME));
            trackValues.put(TrackTable.ORDER, trackJsonObj.getInt(TrackKeys.ORDER));
            trackValues.put(TrackTable.UNIQUE_ID, trackJsonObj.getString(TrackKeys.UNIQUE_ID));

            dbAdapter.createOrUpdateTracks(trackValues);
        }
    }

    private boolean fitImageInDB(final String imageUrl) throws IOException {
        if (imageUrl != null && !"null".equals(imageUrl)) {
            final HttpClient client = new DefaultHttpClient();
            final HttpGet get = new HttpGet(imageUrl);
            final HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (response.containsHeader(ServerValues.CONTENT_LENGTH)) {
                    final Header[] headers = response.getHeaders(ServerValues.CONTENT_LENGTH);
                    if (headers.length > 0 && headers[0] != null) {
                        final int imageSize = Integer.parseInt(headers[0].getValue());
                        return imageSize < MAX_SQL_DATA_SIZE_IN_BYTES;
                    }
                } else {
                    return true;
                }
            } else {
                throw new IOException("Statuscode: " + response.getStatusLine().getStatusCode());
            }
        }

        return false;
    }

    private String downloadImageToCacheDir(final String uniqueId, final String imageUrl) throws IOException {
        if (imageUrl != null && !"null".equals(imageUrl)) {
            final HttpClient client = new DefaultHttpClient();
            final HttpGet get = new HttpGet(imageUrl);
            final HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                final HttpEntity entity = response.getEntity();
                final BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
                final InputStream is = bufferedEntity.getContent();

                final File cacheDir = ctx.getCacheDir();
                final File imageFile = new File(cacheDir, uniqueId);

                if (imageFile.exists()) {
                    imageFile.delete();
                    imageFile.createNewFile();
                } else {
                    imageFile.createNewFile();
                }

                final FileOutputStream fos = new FileOutputStream(imageFile);
                final BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];

                while (bis.read(buffer) != -1) {
                    fos.write(buffer);
                }

                fos.close();
                bis.close();

                return imageFile.getAbsolutePath();
            } else {
                throw new IOException("Statuscode: " + response.getStatusLine().getStatusCode());
            }
        } else {
            return null;
        }
    }

    private byte[] downloadImageToByteArray(final String imageUrl) throws IOException {
        if (imageUrl != null && !"null".equals(imageUrl)) {
            final HttpClient client = new DefaultHttpClient();
            final HttpGet get = new HttpGet(imageUrl);
            final HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                final HttpEntity entity = response.getEntity();
                final BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
                return EntityUtils.toByteArray(bufferedEntity);
            } else {
                throw new IOException("Statuscode: " + response.getStatusLine().getStatusCode());
            }
        } else {
            return null;
        }
    }

    private String getHashTagString(final JSONArray hashtags) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashtags.length(); i++) {
            sb.append(hashtags.get(i));

            if (i + 1 < hashtags.length()) {
                sb.append(" OR ");
            }
        }
        return sb.toString();
    }
}
