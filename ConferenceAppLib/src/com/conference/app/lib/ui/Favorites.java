package com.conference.app.lib.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.ui.adapter.FavoritesListAdapter;

public class Favorites extends Activity {
    private static final String TAG = Favorites.class.getName();
    private static final boolean DEBUG = false;

    private ListView favoriteList;
    private TextView noFavoritesText;

    private SQLiteDatabase db;
    private Cursor cursor;

    private boolean isFirstInit = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        final DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (db != null && db.isOpen()) {
            db.close();
        }
        isFirstInit = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstInit) {
			return;
		}
		isFirstInit = false;
		
		initViewValues(); //Eddie Li Keep current position after back from background
    }

    private void initViewValues() {
        if (cursor == null || cursor.isClosed()) {
            cursor = db.query(SessionTable.TABLE_NAME, new String[] { SessionTable.ID, SessionTable.TYPE, SessionTable.NAME,
                    SessionTable.SHORT_NAME, SessionTable.UNIQUE_ID, SessionTable.FAVORITE, SessionTable.END_DATE,
                    SessionTable.ROOM_NAME, SessionTable.START_DATE }, SessionTable.FAVORITE + "=1", null, null, null,
                    SessionTable.START_DATE + ", " + SessionTable.NAME + " COLLATE NOCASE");
            startManagingCursor(cursor);
        } else {
            cursor.requery();
        }

        if (cursor.getCount() == 0) {
            showNoFavoritesText();
        } else {
            noFavoritesText.setVisibility(View.GONE);
            favoriteList.setVisibility(View.VISIBLE);

            final FavoritesListAdapter adapter = new FavoritesListAdapter(Favorites.this, cursor, db);
            favoriteList.setAdapter(adapter);
            favoriteList.invalidate();
            adapter.notifyDataSetChanged();
            favoriteList.setItemsCanFocus(true);
            favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                    final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    final String uniqueSessionId = cursor.getString(cursor.getColumnIndex(SessionTable.UNIQUE_ID));

                    final Intent intent = new Intent(Favorites.this, SessionDetails.class);
                    intent.putExtra(SessionDetails.EXTRA_SESSION_ID, uniqueSessionId);
                    startActivity(intent);
                }
            });
        }
    }

    public void showNoFavoritesText() {
        if (noFavoritesText != null && favoriteList != null) {
            noFavoritesText.setVisibility(View.VISIBLE);
            favoriteList.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        noFavoritesText = (TextView) findViewById(R.id.favoriteNoText);
        favoriteList = (ListView) findViewById(R.id.favoritelistview);
    }

    public void onClickActionBarHome(final View view) {
        final Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // overridePendingTransition(R.anim.home_enter, R.anim.home_exit);
    }
}
