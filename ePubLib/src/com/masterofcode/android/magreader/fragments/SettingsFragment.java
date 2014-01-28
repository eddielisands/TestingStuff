package com.masterofcode.android.magreader.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class SettingsFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		Log.i("----------", "settings fragment");
		return inflater.inflate(R.layout.settings_view, container);
	}
}
