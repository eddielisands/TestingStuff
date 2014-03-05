package com.fancl.iloyalty.asynctask.callback;

public interface TillIdDatabaseDownloadAsyncTaskCallback {
	public void noNeedToDownloadNewTillIdDatabase();
	
	public void tillIdDatabaseStartDownloading();
	
	public void tillIdDatabaseDownloadFinished();
	
	public void tillIdDatabaseDownloadFailed();
	
	public void tillIdDatabaseDownloadingProgress(int progress);
}
