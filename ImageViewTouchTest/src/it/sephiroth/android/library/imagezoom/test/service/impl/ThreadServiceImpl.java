package it.sephiroth.android.library.imagezoom.test.service.impl;


import it.sephiroth.android.library.imagezoom.test.Constants;
import it.sephiroth.android.library.imagezoom.test.service.ThreadService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadServiceImpl implements ThreadService{
	
	private ExecutorService imageExecutor;
	
	private static final String SUB_TAG = "ImageUtil";
	
	private static void log(String message)
	{
//		LogController.log(SUB_TAG + " >>> " + message);
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
