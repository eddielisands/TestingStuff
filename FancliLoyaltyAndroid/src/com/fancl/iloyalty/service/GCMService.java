package com.fancl.iloyalty.service;

import android.content.Context;

public interface GCMService {

	public void register(Context context);
	
	public void unregister(Context context);
}
