package com.fancl.iloyalty.asynctask.callback;

public interface DatabaseDownloadAsyncTaskCallback {
	public void noNeedToDownloadNewDatabase();
	
	public void databaseStartDownloading();
	
	public void databaseDownloadFinished();
	
	public void databaseDownloadFailed();
	
	public void databaseDownloadingProgress(int progress);
}
