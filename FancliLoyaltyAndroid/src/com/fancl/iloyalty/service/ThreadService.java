package com.fancl.iloyalty.service;

public interface ThreadService {
	public void startImageExecutor();
	public void stopImageExecutor();
	public void executImageRunnable(Runnable runnable);
}
