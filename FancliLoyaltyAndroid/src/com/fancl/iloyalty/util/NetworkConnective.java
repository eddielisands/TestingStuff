package com.fancl.iloyalty.util;


import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkConnective {

	private static ConnectivityManager connectManager;
	private static android.net.NetworkInfo mobile;
	private static android.net.NetworkInfo wifi;
	
	public static boolean checkNetwork(Context context){
		connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		mobile = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		wifi = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if( wifi.isConnectedOrConnecting() ){
			if( wifi.isConnected() ){
				return true;
			}
		}
		
		if( mobile.isConnectedOrConnecting() ){
			if( mobile.isConnected() ){
				return true;
			}
		}
		
		return false;
	}
}
