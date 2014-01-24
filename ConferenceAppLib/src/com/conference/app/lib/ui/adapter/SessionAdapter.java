package com.conference.app.lib.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseAdapter;
import com.conference.app.lib.database.tables.SessionTable;

public class SessionAdapter extends CursorAdapter implements SectionIndexer {
    private static final String TAG = SessionAdapter.class.getName();
    private static final boolean DEBUG = false;

    private static final int MAX_NAME_LENGTH = 40;

    private final Context ctx;
    private final Cursor cursor;
    private final DatabaseAdapter dbAdapter;

    private String[] sections;
    private Map<String, Integer> alphaIndexer;
    private final LayoutInflater mInflater;

    private static Bitmap cacheSessionImg;
    private static Bitmap cacheWorkshopImg;
    private static Bitmap cacheKeyNoteImg;

    public SessionAdapter(final Context ctx, final Cursor cursor) {
        super(ctx, cursor);
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
        // new TrackColorLoader().execute();

        this.cursor = cursor;
        this.dbAdapter = new DatabaseAdapter(ctx);

        createIndexer();
    }

    private void createIndexer() {
        alphaIndexer = new HashMap<String, Integer>();

        for (int i = 0; cursor.moveToNext(); i++) {
            final String letter = cursor.getString(cursor.getColumnIndex(SessionTable.SEARCH_NAME)).substring(0, 1).toUpperCase();
            if (!alphaIndexer.containsKey(letter)) {
                alphaIndexer.put(letter, i);
            }
        }

        List<String> keyList = new ArrayList<String>(alphaIndexer.keySet());
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

        if (position % 2 == 0) {
            rowView.setBackgroundResource(R.drawable.listitembg1);
        } else {
            rowView.setBackgroundResource(R.drawable.listitembg2);
        }

        bindView(rowView, ctx, cursor);
        return rowView;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String uniqueSessionId = cursor.getString(cursor.getColumnIndex(SessionTable.UNIQUE_ID));
        final String sessionName = cursor.getString(cursor.getColumnIndex(SessionTable.SHORT_NAME));

        if (sessionName != null && !"null".equals(sessionName) && sessionName.length() > 0) {
            viewHolder.title.setText(sessionName.trim());
        } else {
            final String title = cursor.getString(cursor.getColumnIndex(SessionTable.NAME)).trim();
            if (title.length() > MAX_NAME_LENGTH) {
                viewHolder.title.setText(title.substring(0, MAX_NAME_LENGTH).concat("..."));
            } else {
                viewHolder.title.setText(title.trim());
            }
        }

        viewHolder.star.setChecked(cursor.getInt(cursor.getColumnIndex(SessionTable.FAVORITE)) != 0);
        viewHolder.star.setTag(uniqueSessionId);
        viewHolder.star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (buttonView.isPressed()) {
                    final String uniqueSessionId = (String) buttonView.getTag();
                    dbAdapter.setFavorite(uniqueSessionId, isChecked);
                }
            }
        });

        String session = "SESSION".toLowerCase();
        String workshop = "WORKSHOP".toLowerCase();
        String keynote = "KEYNOTE".toLowerCase();
        String sessionType = cursor.getString(cursor.getColumnIndex(SessionTable.TYPE)).toLowerCase();
        if (session.equals(sessionType)) {
            if (cacheSessionImg == null) {
                cacheSessionImg = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.session_icon);
            }
            viewHolder.image.setImageBitmap(cacheSessionImg);
        } else if (workshop.equals(sessionType)) {
            if (cacheWorkshopImg == null) {
                cacheWorkshopImg = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.workshop_icon);
            }
            viewHolder.image.setImageBitmap(cacheWorkshopImg);
        } else if (keynote.equals(sessionType)) {
            if (cacheKeyNoteImg == null) {
                cacheKeyNoteImg = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.keynote_icon);
            }
            viewHolder.image.setImageBitmap(cacheKeyNoteImg);
        }

        String s = getDateRowText(cursor);
        viewHolder.row2.setText(s);

        // viewHolder.trackColor.setBackgroundColor(Color.parseColor(dbAdapter.getTrackColorByUniqueSessionId(cursor.getString(cursor
        // .getColumnIndex(SessionTable.UNIQUE_ID)))));
        // viewHolder.trackColor.setVisibility(View.VISIBLE);
    }

    private String getDateRowText(final Cursor cursor) {
        String s = "";
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            String startdate = cursor.getString(cursor.getColumnIndex(SessionTable.START_DATE));
            String enddate = cursor.getString(cursor.getColumnIndex(SessionTable.END_DATE));
            String dateOnly = dateFormat.format(dateTimeFormat.parse(startdate));
            String starttime = timeFormat.format(dateTimeFormat.parse(startdate));
            String endtime = timeFormat.format(dateTimeFormat.parse(enddate));

            s = dateOnly + " / " + starttime + " - " + endtime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.schedule_item, null, false);
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.title = (TextView) view.findViewById(R.id.scheduleItemSessionTitle);
        // viewHolder.trackColor = view.findViewById(R.id.scheduleItemTrackColor);
        viewHolder.star = (ToggleButton) view.findViewById(R.id.scheduleItemFav);
        viewHolder.image = (ImageView) view.findViewById(R.id.scheduleItemSessionImg);
        viewHolder.row2 = (TextView) view.findViewById(R.id.row2text);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public int getPositionForSection(final int section) {
        final String letter = sections[section];
        return alphaIndexer.get(letter);
    }

    @Override
    public int getSectionForPosition(final int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    static class ViewHolder {
        public ToggleButton star;
        public ImageView image;
        public TextView title;
        public TextView row2;
        // public View trackColor;
    }

    // private class TrackColorLoader extends AsyncTask<Void, Void, Void> {
    // @Override
    // protected Void doInBackground(final Void... params) {
    // final DatabaseHelper helper = new DatabaseHelper(ctx);
    // final SQLiteDatabase db = helper.getReadableDatabase();
    // final Cursor cursor = db.query(SessionTable.TABLE_NAME, new String[] { SessionTable.ID,
    // SessionTable.UNIQUE_ID }, null, null, null, null, null);
    // final DatabaseAdapter dbAdapter = new DatabaseAdapter(ctx);
    //
    // while (cursor.moveToNext()) {
    // dbAdapter
    // .getTrackColorByUniqueSessionId(cursor.getString(cursor.getColumnIndex(SessionTable.UNIQUE_ID)));
    // }
    //
    // cursor.close();
    // if (db.isOpen()) {
    // db.close();
    // }
    //
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(final Void result) {
    // SessionAdapter.this.notifyDataSetChanged();
    // }
    // }
}
