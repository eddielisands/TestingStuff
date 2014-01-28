package com.masterofcode.android.magreader.utils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import com.masterofcode.android.magreader.MainActivity;
import com.masterofcode.android.magreader.utils.constants.Constants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

public class ApplicationUtils {
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	
	public static String formatDateForDb(String date) {
		String newDateStr = null;
		try {
			Date dateObj;
			SimpleDateFormat curFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
			dateObj = (Date) curFormater.parse(date);
			SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"); 			 
			newDateStr = postFormater.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newDateStr;
	}
	
	/*Little fix for Acer Iconia*/
	public static String getPublicationDate(String date){
		String newDateStr = null;
		String [] dateobj = date.split(" ");
		newDateStr = getMonth(dateobj[2].trim()) + "." + dateobj[3].trim();
		return newDateStr;
	}
	
	private static String getMonth(String month){
		final String[] monthsAbbr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		String monthNum = "01";
		for (int i = 0; i< monthsAbbr.length; i++){
			if (month.equalsIgnoreCase(monthsAbbr[i])){
				monthNum = String.valueOf(i + 1).length() > 1 ? String.valueOf(i + 1) : "0" + String.valueOf(i + 1);
				break;
			}
				
		}
		return monthNum;
	}
	
	public static String getPublicationDateIssueDetails(String date){
		String newDateStr = null;
		String [] dateobj = date.split(" ");
		newDateStr = getFullMonth(dateobj[2].trim()) + " " + dateobj[1].trim() + ", " + dateobj[3].trim();
		return newDateStr;
	}
	
	private static String getFullMonth(String month){
		final String[] monthsAbbr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		final String[] monthsNames = {"January", "February",
	            "March", "April", "May", "June", "July",
	            "August", "September", "October", "November",
	            "December"};
		String monthNum = "January";
		for (int i = 0; i< monthsAbbr.length; i++){
			if (month.equalsIgnoreCase(monthsAbbr[i])){
				monthNum = monthsNames[i];
				break;
			}
				
		}
		return monthNum;
	}
	/*End of Little fix for Acer Iconia*/
	
	//date for issue detail
	public static String formatDateForIssueDetail(String date) {
		String newDateStr = null;
		try {
			Date dateObj;
			
			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
			dateObj = (Date) curFormater.parse(date);
			SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy"); 			 
			newDateStr = postFormater.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newDateStr;
	}
	
	//date for issue description
		public static String formatDateForIssueDescr(String date) {
			String newDateStr = null;
			try {
				Date dateObj;
				
				SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
				dateObj = (Date) curFormater.parse(date);
				SimpleDateFormat postFormater = new SimpleDateFormat("MM.yyyy"); 			 
				newDateStr = postFormater.format(dateObj);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return newDateStr;
		}
	
	public static String formatDateForView(String date) {
		String newDateStr = null;
		try {
			Date dateObj;
			
			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
			dateObj = (Date) curFormater.parse(date);
			SimpleDateFormat postFormater = new SimpleDateFormat("MMMMMMMMMMMMM dd, HH:mm"); 			 
			newDateStr = postFormater.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newDateStr;
	}
	
	public static String formatDateForItemView(String date) {
		String newDateStr = null;
		try {
			Date dateObj;
			
			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
			dateObj = (Date) curFormater.parse(date);
			SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMMMMMMMMMMM yyyy"); 			 
			newDateStr = postFormater.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newDateStr;
	}
	
	public static String formatDateForBoomarkingItemView(long timestamp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		Date date = cal.getTime();
		
		SimpleDateFormat postFormater = new SimpleDateFormat("MMMMMMMMMMMMM dd, HH:mm"); 			 
		return postFormater.format(date);
	}
	
	public static String formatDateForActionBar(String date) {
		String newDateStr = null;
		try {
			Date dateObj;
			
			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
			dateObj = (Date) curFormater.parse(date);
			SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMMMMMMMMMMM yyyy, HH:mm"); 			 
			newDateStr = postFormater.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newDateStr;
	}
	
	public static String formatDateRefreshForActionBar(Date date) {
		String newDateStr = null;
			SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMMMMMMMMMMM yyyy, HH:mm"); 			 
			newDateStr = postFormater.format(date);
		return newDateStr;
	}
	
	public static String formatDateUTCForDb(String date) {
		String newDateStr = null;
		try {
			Date dateObj;
			SimpleDateFormat curFormater = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
			dateObj = (Date) curFormater.parse(date);
			SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"); 			 
			newDateStr = postFormater.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newDateStr;
	}
	
	public static final int getCurrentScreenWidth(final Context context) {
        return context.getResources().getDisplayMetrics().widthPixels - 14;
    }
	
	public static SharedPreferences getPreferences(final Context context) {
		return getPreferences(context, Constants.KEY_PREF);
	}
	
	public static SharedPreferences getPreferences(final Context context,final String prefsName) {
		return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
	}
	
	public static void setPrefProperty(final Context context, final String propKey, String propValue) {
		final SharedPreferences sharedPreferences = getPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(propKey, propValue);
		editor.commit();
	}
	public static void setPrefProperty(final Context context, final String propKey, boolean propValue) {
		final SharedPreferences sharedPreferences = getPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(propKey, propValue);
		editor.commit();
	}
	
	public static void setPrefProperty(final Context context, final String propKey, int propValue)
	{
		final SharedPreferences sharedPreferences = getPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(propKey, propValue);
		editor.commit();
	}
	
	public static boolean getPrefProperty(final Context context, final String propKey) {
		final SharedPreferences sharedPreferences = getPreferences(context);
		return sharedPreferences.getBoolean(propKey, false);		
	}
	
	public static boolean getPrefPropertyBoolean(final Context context, final String propKey, final boolean defaultvalue) {
		final SharedPreferences sharedPreferences = getPreferences(context);
		return sharedPreferences.getBoolean(propKey, defaultvalue);		
	}
	
	public static int getPrefPropertyInt(final Context context, final String propKey)
	{
		final SharedPreferences sharedPreferences = getPreferences(context);
		return sharedPreferences.getInt(propKey, 0);		
	}

	public static String getPrefPropertyString(final Context context, final String propKey, final String defaultvalue) {
		final SharedPreferences sharedPreferences = getPreferences(context);
		return sharedPreferences.getString(propKey, defaultvalue);		
	}
	
	public static boolean isLandscape(Context context){
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	public static boolean checkIssueIsAvail(){
       	JSONArray jsonArray = RestClient.connect(Constants.CHECK_FEEDS_AVAILABLE_URL);
		return jsonArray != null ? true : false;
	}
	
	public static String getHostName(String url){
		String pattern = "^(?:[^/]+://)?([^/:]+)";

		Matcher matcher = Pattern.compile(pattern).matcher(url);
		if (matcher.find()) {
			int start = matcher.start(1);
			int end = matcher.end(1);
			return url.substring(start, end);
		}
		return null;
	}
	
	public static Dialog createNoInternetDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("No Internet Connection")
            .setIcon(android.R.drawable.stat_sys_warning)
            .setMessage("You need to have Internet connection")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
        return builder.create();
    }
	
	public static String getEpubFileNameFromUrl(URL epubUrl){
		String filename = epubUrl.getFile().substring(epubUrl.getFile().lastIndexOf("/") + 1, epubUrl.getFile().length());
		if (!filename.contains(".epub"))
			filename += ".epub";
		
		return filename;
	}
	
	public static String getStringWithoutHTMLTags(String text){
		Pattern mPattern = Pattern.compile("<[^>]+>");
		Matcher mMatcher = mPattern.matcher(text);
		if (mMatcher.find()){
			return mMatcher.replaceAll("");
		} else
			return text;
	}
	
	public static void rmDir(String path)
	{
		File		current = new File( path );
		    
	    if(current.isDirectory())
	    {
			File[] content = current.listFiles();

			// dirs first
			for (int i = 0; i < content.length; i++)
			{
				File currentFile = content[i];
				if (currentFile.isDirectory())
				{
					rmDir(currentFile.getPath());
				}
			}
			
		    // rm files
			for (int i = 0; i < content.length; i++)
			{
				File currentFile = content[i];
				if (currentFile.isFile())
				{
				    currentFile.delete();
				}
			}
			
			current.delete();
	    }
	}
	
	public static String[] removeItemFromStringsArray(String[] input, String deleteMe)
	{
		/*
	    LinkedList<String> result = new LinkedList<String>();

	    for(String item : input)
	        if(!deleteMe.equals(item))
	            result.add(item);

	    return (String[]) result.toArray(input);*/
		List<String> list = new ArrayList<String>(Arrays.asList(input));
		list.removeAll(Arrays.asList(deleteMe));
		return list.toArray(EMPTY_STRING_ARRAY);
	}

}
