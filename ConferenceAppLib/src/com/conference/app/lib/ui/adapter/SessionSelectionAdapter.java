package com.conference.app.lib.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.tables.TrackTable;

public class SessionSelectionAdapter extends CursorAdapter {
    private static final String TAG = SessionSelectionAdapter.class.getName();
    private static final boolean DEBUG = false;

    private final Context ctx;
    private final int resid;
    private final Cursor cursor;

    public SessionSelectionAdapter(final Context ctx, final Cursor cursor) {
        super(ctx, cursor);
        this.ctx = ctx;
        this.cursor = cursor;
        this.resid = R.layout.track_item;
    }

    @Override
    public int getCount() {
        return cursor.getCount() + 1;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View rowView;
        if (convertView == null) {
            rowView = newView(ctx, cursor, parent);
        } else {
            rowView = convertView;
        }

        if (position == 0) {
            final ViewHolder viewHolder = (ViewHolder) rowView.getTag();
            viewHolder.title.setText(ctx.getString(R.string.sessions_all_sessions));
            // viewHolder.color.setVisibility(View.GONE);
        } else {
            cursor.moveToPosition(position - 1);
            bindView(rowView, ctx, cursor);
        }

        if (position % 2 == 0) {
            rowView.setBackgroundResource(R.drawable.listitembg1);
        } else {
            rowView.setBackgroundResource(R.drawable.listitembg2);
        }

        return rowView;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.title.setText(cursor.getString(cursor.getColumnIndex(TrackTable.NAME)));

        // final String color = cursor.getString(cursor.getColumnIndex(TrackTable.COLOR));

        // if (color == null) {
        // viewHolder.color.setVisibility(View.GONE);
        // } else {
        // viewHolder.color.setVisibility(View.VISIBLE);
        // viewHolder.color.setBackgroundColor(Color.parseColor(color));
        // }
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(resid, null, true);
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.title = (TextView) view.findViewById(R.id.sessionTitle);
        // viewHolder.color = view.findViewById(R.id.trackColor);

        view.setTag(viewHolder);

        return view;
    }

    static class ViewHolder {
        public TextView title;
        // public View color;
    }
}
