package com.fancl.iloyalty.service.callback;

public interface ActivityMonitorServiceCallback {
	
	public void applicationGoingToBackground();
	
	public void applicationGoingToForeground();
}
