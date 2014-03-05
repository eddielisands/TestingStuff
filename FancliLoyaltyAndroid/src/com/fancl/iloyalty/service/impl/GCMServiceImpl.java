package com.fancl.iloyalty.service.impl;

import android.content.Context;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.service.GCMService;
import com.fancl.iloyalty.util.LogController;
import com.google.android.gcm.GCMRegistrar;

public class GCMServiceImpl implements GCMService {

	@Override
	public void register(Context context) {
		LogController.log("Call GCM register");
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		GCMRegistrar.register(context, Constants.GCM_PROJECT_ID);
	}

	@Override
	public void unregister(Context context) {
		LogController.log("Call GCM unregister");
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		GCMRegistrar.unregister(context);
	}
}
