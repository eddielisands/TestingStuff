package com.conference.app.lib.ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.conference.app.lib.R;
import com.conference.app.lib.database.DatabaseHelper;
import com.conference.app.lib.database.tables.ConferenceTable;
import com.conference.app.lib.network.Downloader;
import com.conference.app.lib.util.ImageCache;
import com.conference.app.lib.util.Preferences;
import com.conference.app.lib.util.TimeUtil;

public class Dashboard extends Activity {
    private static final int AD_ROTATE_TIMEOUT = 4000;
    private static final String TAG = Dashboard.class.getName();
    private static final boolean DEBUG = false;

    private static final int DIALOG_DOWNLOAD_PROGRESS = 1;
    private static final int DIALOG_NOT_CONNECTED = 2;
    private static final int DIALOG_DOWNLOAD_NO_WLAN = 3;

    private static final int COUNTER_UPDATE_INTERVALL_IN_MS = 1000;
    private static final int TIME_10 = 10;

    private TextView countdown;
    // private ImageView banner;
    // private ImageView advertisment;
    private View dashboardRoot;

    private Preferences pref;
    private SQLiteDatabase db;
    private Handler handler1;
    private Handler handler2;
    private Handler handler3;
    private Runnable r1;
    private Runnable r2;
    private Runnable r3;
    private ImageView ad1;
    private ImageView ad2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        final DatabaseHelper helper = new DatabaseHelper(Dashboard.this);
        db = helper.getReadableDatabase();

        pref = new Preferences(this);

        initViews();
        initAppStart();
    }

    private void initViews() {
        countdown = (TextView) findViewById(R.id.countdown);
        countdown.setText("");
        ad1 = (ImageView) findViewById(R.id.ad1);
        ad2 = (ImageView) findViewById(R.id.ad2);
        // banner = (ImageView) findViewById(R.id.logo);
        // advertisment = (ImageView) findViewById(R.id.DashboardAdvertisment);
        dashboardRoot = findViewById(R.id.dashboardRoot);

        handler1 = new Handler();
        handler2 = new Handler();
        handler3 = new Handler();

        r1 = new Runnable() {
            @Override
            public void run() {
                // TODO animate view opacity before setting to gone/visible
                countdown.setVisibility(View.GONE);
                ad1.setVisibility(View.VISIBLE);
                handler2.postDelayed(r2, AD_ROTATE_TIMEOUT);
            }
        };
        r2 = new Runnable() {
            @Override
            public void run() {
                // TODO animate view opacity before setting to gone/visible
                ad1.setVisibility(View.GONE);
                ad2.setVisibility(View.VISIBLE);
                handler3.postDelayed(r3, AD_ROTATE_TIMEOUT);
            }
        };
        r3 = new Runnable() {
            @Override
            public void run() {
                // TODO animate view opacity before setting to gone/visible
                ad2.setVisibility(View.GONE);
                countdown.setVisibility(View.VISIBLE);
                handler1.postDelayed(r1, AD_ROTATE_TIMEOUT);
            }
        };
        handler1.postDelayed(r1, AD_ROTATE_TIMEOUT);

    }

    private void initAppStart() {
        if (!pref.isFirstStart() && pref.isDownloadFinished()) {
            dashboardRoot.setVisibility(View.VISIBLE);
            new DownloaderTask().execute(isOnline());
        } else {
            if (isOnline()) {
                if (isWifiOn()) {
                    new DownloaderTask().execute();
                } else {
                    showDialog(DIALOG_DOWNLOAD_NO_WLAN);
                }
            } else {
                showDialog(DIALOG_NOT_CONNECTED);
            }
        }
    }

    public void onClickSchedule(final View view) {
        startActivity(new Intent(this, Schedule.class));
    }

    public void onClickSessions(final View view) {
        startActivity(new Intent(this, Session.class));
    }

    public void onClickSpeakers(final View view) {
        startActivity(new Intent(this, Speaker.class));
    }

    public void onClickTwitter(final View view) {
        if (isOnline()) {
            startActivity(new Intent(this, TweetReader.class));
        } else {
            Toast.makeText(this, R.string.dashboard_toast_no_inet, Toast.LENGTH_LONG).show();
        }
    }

    public void onClickFav(final View view) {
        startActivity(new Intent(this, Favorites.class));
    }

    public void onClickMap(final View view) {
        startActivity(new Intent(this, LocationAndFeedback.class));
    }

    public void onClickAdvertisment(final View view) {
        // final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jaxenter.de"));
        // startActivity(browserIntent);
    }

    public void onClickCountdown(final View view) {
        // can be implemented if needed
    }

    public void onClickAd1(final View view) {
        String url = "http://entwickler.de/mobile-technology";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onClickAd2(final View view) {
        String url = "http://www.arconsis.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new ImageCache().clearCache();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        switch (id) {
        case DIALOG_DOWNLOAD_PROGRESS:
            final ProgressDialog dia = new ProgressDialog(this);
            dia.setCancelable(false);
            dia.setMessage(getString(R.string.dashboard_dialog_download));
            return dia;
        case DIALOG_NOT_CONNECTED:
            final AlertDialog.Builder notConnectedDia = new AlertDialog.Builder(this);
            notConnectedDia.setMessage(R.string.dashboard_dia_no_net_msg).setTitle(R.string.dashboard_dia_no_net_title)
                    .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            Dashboard.this.finish();
                        }
                    });
            return notConnectedDia.create();
        case DIALOG_DOWNLOAD_NO_WLAN:
            final AlertDialog.Builder notWlanDia = new AlertDialog.Builder(this);
            notWlanDia.setMessage(R.string.dashboard_dia_no_wlan_msg).setTitle(R.string.dashboard_dia_no_wlan_title)
                    .setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            new DownloaderTask().execute();
                        }
                    }).setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            Dashboard.this.finish();
                        }
                    });
            return notWlanDia.create();
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    private void setCountdown(final Calendar calendar) {
        final long datetime = calendar.getTimeInMillis() - System.currentTimeMillis();

        new CountDownTimer(datetime, COUNTER_UPDATE_INTERVALL_IN_MS) {
            @Override
            public void onTick(final long millisUntilFinished) {
                final long sec = (millisUntilFinished / TimeUtil.SECOND_IN_MS) % TimeUtil.SECONDS_PER_MINUTE;
                final long min = (millisUntilFinished / TimeUtil.MINUTE_IN_MS) % TimeUtil.MINUTES_PER_HOUR;
                final long hours = (millisUntilFinished / TimeUtil.HOUR_IN_MS) % TimeUtil.HOURS_PER_DAY;
                final long days = (millisUntilFinished / TimeUtil.DAY_IN_MS) % TimeUtil.DAYS_PER_WEEK;
                final long weeks = (millisUntilFinished / TimeUtil.WEEK_IN_MS);

                final StringBuilder sb = new StringBuilder();
                if (weeks > 0) {
                    sb.append(weeks);
                    sb.append(" ");
                    if (weeks == 1) {
                        sb.append(getString(R.string.dashboard_counter_week));
                    } else {
                        sb.append(getString(R.string.dashboard_counter_weeks));
                    }
                    sb.append(" ");
                }
                if (days > 0) {
                    sb.append(days);
                    sb.append(" ");
                    if (days == 1) {
                        sb.append(getString(R.string.dashboard_counter_day));
                    } else {
                        sb.append(getString(R.string.dashboard_counter_days));
                    }
                    sb.append(" ");
                }

                if (days > 0 || weeks > 0) {
                    sb.append(getString(R.string.dashboard_counter_and));
                    sb.append(" ");
                }

                if (hours < TIME_10) {
                    sb.append("0");
                }
                sb.append(hours + " ");
                sb.append(getString(R.string.dashboard_counter_hours));
                sb.append(" ");
                // sb.append(":");
                // if (min < TIME_10) {
                // sb.append("0");
                // }
                // sb.append(min);
                // sb.append(":");
                // if (sec < TIME_10) {
                // sb.append("0");
                // }
                // sb.append(sec);
                sb.append(getString(R.string.dashboard_counter_until));

                String when = getString(R.string.dashboard_date);
                SpannableString spannablecontent = new SpannableString(when + "\n" + sb.toString());
                spannablecontent.setSpan(new ForegroundColorSpan(Color.RED), 0, when.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannablecontent.setSpan(new AbsoluteSizeSpan(31), 0, when.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                countdown.setText(spannablecontent);
            }

            @Override
            public void onFinish() {
                countdown.setText(getString(R.string.dashboard_counter_ready));
            }
        }.start();
    }

    private boolean isOnline() {
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    private boolean isWifiOn() {
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info != null && info.isConnected();
    }

    private class DownloaderTask extends AsyncTask<Boolean, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pref.isFirstStart() || !pref.isDownloadFinished()) {
                showDialog(DIALOG_DOWNLOAD_PROGRESS);
            } else {
                if (pref.isDownloadFinished()) {
                    loadBannerAndCounter();
                    dashboardRoot.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        protected Boolean doInBackground(final Boolean... params) {
            boolean result = false;
            try {
                if (params != null && params.length > 0 && params[0] != null) {
                    if (params[0]) {
                        result = new Downloader(Dashboard.this).downloadData();
                    }
                } else {
                    result = new Downloader(Dashboard.this).downloadData();
                }
            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getMessage(), e);
                result = false;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
                result = false;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                result = false;
            }

            return result;
        }

        private void loadBannerAndCounter() {
            final Cursor cursor = db.query(ConferenceTable.TABLE_NAME, new String[] { ConferenceTable.LOGO_IMAGE_LARGE,
                    ConferenceTable.START_DATE, ConferenceTable.AD_IMAGE }, null, null, null, null, null);
            if (cursor == null) {
				return;
			}
            cursor.moveToFirst();

            // final int columnIndexImageLarge = cursor.getColumnIndex(ConferenceTable.LOGO_IMAGE_LARGE);
            // if (!cursor.isNull(columnIndexImageLarge)) {
            // byte[] binaryImage = cursor.getBlob(columnIndexImageLarge);
            // banner.setImageBitmap(BitmapFactory.decodeByteArray(binaryImage, 0, binaryImage.length));
            // }

            // if (cursor.isNull(cursor.getColumnIndex(ConferenceTable.AD_IMAGE))) {
            // advertisment.setVisibility(View.GONE);
            // } else {
            // advertisment.setVisibility(View.VISIBLE);
            // byte[] bb2 = cursor.getBlob(cursor.getColumnIndex(ConferenceTable.AD_IMAGE));
            // advertisment.setImageBitmap(BitmapFactory.decodeByteArray(bb2, 0, bb2.length));
            // }

            try {
                final SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN);
                final Date date = formater.parse(cursor.getString(cursor.getColumnIndex(ConferenceTable.START_DATE)));
                final Calendar c = new GregorianCalendar(Locale.GERMAN);
                c.setTime(date);
                setCountdown(c);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            cursor.close();
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            if (pref.isFirstStart() || !pref.isDownloadFinished()) {
                removeDialog(DIALOG_DOWNLOAD_PROGRESS);
                pref.setFirstStart(false);
                if (result) {
                    loadBannerAndCounter();
                }
            }

            if (result || pref.isDownloadFinished()) {
                dashboardRoot.setVisibility(View.VISIBLE);
            }

            if (!pref.isDownloadFinished()) {
                pref.setDownloadFinished(result);
            }
        }
    }
}
