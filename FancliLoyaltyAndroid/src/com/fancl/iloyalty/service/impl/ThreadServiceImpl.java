package com.fancl.iloyalty.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.service.ThreadService;
import com.fancl.iloyalty.util.LogController;

public class ThreadServiceImpl implements ThreadService{
	
	private ExecutorService imageExecutor;
	
	private static final String SUB_TAG = "ImageUtil";
	
	private static void log(String message)
	{
		LogController.log(SUB_TAG + " >>> " + message);
	}
	
	@Override
	public void startImageExecutor()
	{
		ThreadServiceImpl.log("startImageExecutor " + imageExecutor);
		stopImageExecutor();
		imageExecutor = Executors.newFixedThreadPool(Constants.IMAGE_THREAD_POOL_MAX_SIZE);
	}
	
	@Override
	public void stopImageExecutor()
	{
		if(imageExecutor != null)
		{
			if(!imageExecutor.isShutdown())
			{
				try
				{
					imageExecutor.shutdown();
				}catch( Exception e ){
					
				}
			}
			
			imageExecutor = null;
		}
	}
	
	@Override
	public void executImageRunnable(Runnable runnable)
	{
		ThreadServiceImpl.log("executImageRunnable " + imageExecutor);
		if(runnable != null && imageExecutor != null)
		{	
			try
			{
				imageExecutor.execute(runnable);
			}catch( Exception e ){
				
			}
		}
	}
}
