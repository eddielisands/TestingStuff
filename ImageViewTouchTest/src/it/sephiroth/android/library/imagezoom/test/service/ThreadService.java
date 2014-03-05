package it.sephiroth.android.library.imagezoom.test.service;

public interface ThreadService {
	public void startImageExecutor();
	public void stopImageExecutor();
	public void executImageRunnable(Runnable runnable);
}
