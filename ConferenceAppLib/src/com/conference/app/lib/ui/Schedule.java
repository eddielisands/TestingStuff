package com.conference.app.lib.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.ConferenceTable;
import com.conference.app.lib.database.tables.SessionTable;
import com.conference.app.lib.ui.adapter.ScheduleListAdapter;
import com.conference.app.lib.ui.adapter.SchedulePagerAdapter;

public class Schedule extends Activity {
	private static final String TAG = Schedule.class.getName();
	private static final boolean DEBUG = false;

	private static final int DAY_TIME_IN_MS = 1000 * 60 * 60 * 24;

	private ViewPager pager;
	private SchedulePagerAdapter pagerAdapter;
	private SQLiteDatabase db;
	private Locale language;
	private int mCurrentItem = 0;

	private boolean isFirstInit = true;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		
		final DatabaseHelper helper = new DatabaseHelper(this);
		db = helper.getReadableDatabase();
		
		initLanguage();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isFirstInit) {
			return;
		}
		isFirstInit = false;

		initViews();
		initViewValues();
		pagerAdapter.showDots(0);

		final Cursor confCursor = db.query(ConferenceTable.TABLE_NAME, ConferenceTable.ALL_COLUMNS, null, null, null, null, null);
		startManagingCursor(confCursor);
		confCursor.moveToFirst();

		final String startDateAsString = confCursor.getString(confCursor.getColumnIndex(ConferenceTable.START_DATE));
		final String endDateAsString = confCursor.getString(confCursor.getColumnIndex(ConferenceTable.END_DATE));

		stopManagingCursor(confCursor);
		confCursor.close();

		final SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
		formater.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		try {
			Calendar now = Calendar.getInstance();
			now.setTimeZone(TimeZone.getTimeZone("GMT+1"));

			final Date startDate = formater.parse(startDateAsString);
			final Date endDate = formater.parse(endDateAsString);

			int tmpPosition = 0;
			while (startDate.getTime() + DAY_TIME_IN_MS <= endDate.getTime()) {
				tmpPosition++;
				startDate.setTime(startDate.getTime() + DAY_TIME_IN_MS);
				Calendar conDate = Calendar.getInstance();
				conDate.setTime(startDate);
				if (now.get(Calendar.YEAR) == conDate.get(Calendar.YEAR) &&
						now.get(Calendar.MONTH) == conDate.get(Calendar.MONTH) &&
						now.get(Calendar.DATE) == conDate.get(Calendar.DATE)) {
					mCurrentItem = tmpPosition;
					break;
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pager.setCurrentItem(mCurrentItem);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (db != null && db.isOpen()) {
			db.close();
		}
		isFirstInit = true;
	}

	private void initLanguage() {
		final String langCode = getString(R.string.lang_code);
		if ("DE".equals(langCode)) {
			language = Locale.GERMAN;
		} else {
			language = Locale.US;
		}
	}

	private void initViews() {
		pager = (ViewPager) findViewById(R.id.schedulePager);
		pagerAdapter = new SchedulePagerAdapter(createPages(), pager);
		pager.setAdapter(pagerAdapter);
	}

	private void initViewValues() {
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(final int pagePosition) {
				mCurrentItem = pagePosition; //Eddie Li Keep current page after back from background
				pagerAdapter.showDots(pagePosition);
			}

			@Override
			public void onPageScrolled(final int currentPosition, final float arg1, final int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(final int arg0) {
			}
		});
	}

	private List<View> createPages() {
		final ArrayList<String> conferenceDays = new ArrayList<String>();
		final ArrayList<String> displayConferenceDays = new ArrayList<String>();

		final Cursor confCursor = db.query(ConferenceTable.TABLE_NAME, ConferenceTable.ALL_COLUMNS, null, null, null, null, null);
		startManagingCursor(confCursor);
		confCursor.moveToFirst();

		final String startDateAsString = confCursor.getString(confCursor.getColumnIndex(ConferenceTable.START_DATE));
		final String endDateAsString = confCursor.getString(confCursor.getColumnIndex(ConferenceTable.END_DATE));
		stopManagingCursor(confCursor);
		confCursor.close();

		final SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
		final SimpleDateFormat formater2 = new SimpleDateFormat("EEEE, dd MMM yyyy", language);
		formater.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		formater2.setTimeZone(TimeZone.getTimeZone("GMT+1"));

		try {
			final Date startDate = formater.parse(startDateAsString);
			final Date endDate = formater.parse(endDateAsString);

			displayConferenceDays.add(formater2.format(startDate));
			conferenceDays.add(formater.format(startDate));

			while (startDate.getTime() + DAY_TIME_IN_MS <= endDate.getTime()) {
				startDate.setTime(startDate.getTime() + DAY_TIME_IN_MS);
				conferenceDays.add(formater.format(startDate));
				displayConferenceDays.add(formater2.format(startDate));
			}
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		final Iterator<String> daysIterator = conferenceDays.iterator();
		final Iterator<String> displayDaysIterator = displayConferenceDays.iterator();
		final List<View> pageViews = new ArrayList<View>();

		for (int count = 1; displayDaysIterator.hasNext(); count++) {
			final String displayDate = displayDaysIterator.next();
			final String day = daysIterator.next();

			final Cursor sessionCursor = db.query(SessionTable.TABLE_NAME, SessionTable.ALL_COLUMNS, SessionTable.START_DATE
					+ " LIKE '%" + day + "%'", null, null, null, SessionTable.START_DATE + " ASC");
			startManagingCursor(sessionCursor);

			final int currentViewNr = count - 1;
			final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View parentView = inflater.inflate(R.layout.schedule_list, null, true);

			final TextView title = (TextView) parentView.findViewById(R.id.scheduleTitle);
			title.setText(displayDate);

			final ImageView leftIndicator = (ImageView) parentView.findViewById(R.id.scheduleLeft);
			if (count == 1) {
				leftIndicator.setVisibility(View.INVISIBLE);
			} else {
				leftIndicator.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						pager.setCurrentItem(currentViewNr - 1);
					}
				});
			}

			final ImageView rightIndicator = (ImageView) parentView.findViewById(R.id.scheduleRight);
			if (displayDaysIterator.hasNext()) {
				rightIndicator.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						pager.setCurrentItem(currentViewNr + 1);
					}
				});
			} else {
				rightIndicator.setVisibility(View.INVISIBLE);
			}

			final ListView scheduleList = (ListView) parentView.findViewById(R.id.scheduleList);
			scheduleList.setAdapter(new ScheduleListAdapter(this, sessionCursor));
			scheduleList.setItemsCanFocus(true);
			scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(final AdapterView<?> adapter, final View view, final int position, final long id) {
					final Cursor cursor = (Cursor) adapter.getItemAtPosition(position);
					final String uniqueSessionId = cursor.getString(cursor.getColumnIndex(SessionTable.UNIQUE_ID));
					pagerAdapter.cancelDots();

					mCurrentItem = pager.getCurrentItem();

					final Intent intent = new Intent(Schedule.this, SessionDetails.class);
					intent.putExtra(SessionDetails.EXTRA_SESSION_ID, uniqueSessionId);
					startActivity(intent);
				}
			});

			pageViews.add(parentView);
		}

		return pageViews;
	}

	public void onClickActionBarHome(final View view) {
		pagerAdapter.cancelDots();
		final Intent intent = new Intent(this, Dashboard.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		// overridePendingTransition(R.anim.home_enter, R.anim.home_exit);
	}
}
