package com.conference.app.lib.ui.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseAdapter;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.SpeakerTable;

public class SpeakerAdapter extends CursorAdapter implements SectionIndexer {
    private static final String TAG = SpeakerAdapter.class.getName();
    private static final boolean DEBUG = false;

    public static final Map<String, Bitmap> SPEAKER_IMAGES = new HashMap<String, Bitmap>();

    private final Context ctx;
    private final int resid;
    private final Cursor cursor;
    private String[] sections;
    private Map<String, Integer> alphaIndexer;
    AlphabetIndexer mAlphaIndexer;

    public SpeakerAdapter(final Context ctx, final Cursor cursor) {
        super(ctx, cursor);
        this.ctx = ctx;
        new SpeakerAvatarLoader().execute();

        this.resid = R.layout.speaker_item;
        this.cursor = cursor;

        // createIndexer();
        mAlphaIndexer = new AlphabetIndexer(cursor, cursor.getColumnIndex(SpeakerTable.LAST_NAME), " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    private void createIndexer() {
        alphaIndexer = new HashMap<String, Integer>();

        for (int i = 0; cursor.moveToNext(); i++) {
            final String letter = cursor.getString(cursor.getColumnIndex(SpeakerTable.LAST_NAME)).substring(0, 1);
            if (!alphaIndexer.containsKey(letter)) {
                alphaIndexer.put(letter, i);
            }
        }

        final List<String> keyList = new ArrayList<String>(alphaIndexer.keySet());
        Collections.sort(keyList);

        sections = new String[keyList.size()];
        keyList.toArray(sections);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View rowView;
        if (convertView == null) {
            rowView = newView(ctx, cursor, parent);
        } else {
            rowView = convertView;
        }

        cursor.moveToPosition(position);

        // if (position % 2 == 0) {
        rowView.setBackgroundResource(R.drawable.listitembg1);
        // } else {
        // rowView.setBackgroundResource(R.drawable.listitembg2);
        // }

        bindView(rowView, ctx, cursor);
        return rowView;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        String name = cursor.getString(cursor.getColumnIndex(SpeakerTable.DISPLAY_NAME));
        viewHolder.name.setText(name.trim());
        String comp = cursor.getString(cursor.getColumnIndex(SpeakerTable.COMPANY));
        viewHolder.company.setText(comp.trim());

        final String uniqueUserId = cursor.getString(cursor.getColumnIndex(SpeakerTable.UNIQUE_ID));

        if (SPEAKER_IMAGES.containsKey(uniqueUserId)) {
            final Bitmap bitmap = SPEAKER_IMAGES.get(uniqueUserId);
            viewHolder.image.setImageBitmap(bitmap);
        }
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(resid, null, true);
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.name = (TextView) view.findViewById(R.id.sessionSpeakerName);
        viewHolder.company = (TextView) view.findViewById(R.id.sessionSpeakerCompany);
        viewHolder.image = (ImageView) view.findViewById(R.id.sessionSpeakerImg);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public int getPositionForSection(final int section) {
        // final String letter = sections[section];
        // return alphaIndexer.get(letter);
        return mAlphaIndexer.getPositionForSection(section);
    }

    @Override
    public int getSectionForPosition(final int position) {
        // return 0;
        return mAlphaIndexer.getSectionForPosition(position);
    }

    @Override
    public Object[] getSections() {
        // return sections;
        return mAlphaIndexer.getSections();
    }

    static class ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView company;
    }

    private class SpeakerAvatarLoader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(final Void... params) {
            final DatabaseHelper helper = new DatabaseHelper(ctx);
            final SQLiteDatabase db = helper.getReadableDatabase();
            final Cursor cursor = db.query(SpeakerTable.TABLE_NAME, new String[] { SpeakerTable.ID, SpeakerTable.UNIQUE_ID,
                    SpeakerTable.IMAGE, SpeakerTable.IMAGE_URL }, null, null, null, null, null);

            DatabaseAdapter dbadapter = new DatabaseAdapter(ctx);

            while (cursor.moveToNext()) {
                final String uniqueId = cursor.getString(cursor.getColumnIndex(SpeakerTable.UNIQUE_ID));
                if (!SPEAKER_IMAGES.containsKey(uniqueId)) {
                    if (!cursor.isNull(cursor.getColumnIndex(SpeakerTable.IMAGE))) {
                        final byte[] binaryImage = cursor.getBlob(cursor.getColumnIndex(SpeakerTable.IMAGE));
                        addImageToMap(uniqueId, binaryImage);
                    } else {
                        if (!cursor.isNull(cursor.getColumnIndex(SpeakerTable.IMAGE_URL))) {
                            String imageUrl = cursor.getString(cursor.getColumnIndex(SpeakerTable.IMAGE_URL));
                            byte[] binaryImage;
                            try {
                                binaryImage = downloadImageToByteArray(imageUrl);
                                if (binaryImage != null) {
                                    Cursor c = db.query(SpeakerTable.TABLE_NAME, SpeakerTable.ALL_COLUMNS, SpeakerTable.UNIQUE_ID
                                            + "='" + uniqueId + "'", null, null, null, null);
                                    c.moveToFirst();
                                    ContentValues values = new ContentValues();
                                    DatabaseUtils.cursorRowToContentValues(c, values);
                                    c.close();
                                    dbadapter.createOrUpdateSpeaker(values);

                                    cursor.moveToFirst();
                                    addImageToMap(uniqueId, binaryImage);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            cursor.close();
            if (db.isOpen()) {
                db.close();
            }

            return null;
        }

        private void addImageToMap(final String uniqueId, final byte[] binaryImage) {
            if (binaryImage.length > 0) {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(binaryImage, 0, binaryImage.length);
                SPEAKER_IMAGES.put(uniqueId, bitmap);
            }
        }

        @Override
        protected void onPostExecute(final Void result) {
            SpeakerAdapter.this.notifyDataSetChanged();
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

}
