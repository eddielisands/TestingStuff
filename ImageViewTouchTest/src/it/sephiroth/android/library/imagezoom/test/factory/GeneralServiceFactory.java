package it.sephiroth.android.library.imagezoom.test.factory;

import it.sephiroth.android.library.imagezoom.test.service.ImageService;
import it.sephiroth.android.library.imagezoom.test.service.ThreadService;
import it.sephiroth.android.library.imagezoom.test.service.impl.ImageServiceImpl;
import it.sephiroth.android.library.imagezoom.test.service.impl.ThreadServiceImpl;

public class GeneralServiceFactory {
	
	/**
	 * Application Service Object
	 */
	private static ImageService imageService;
	private static ThreadService threadService;
	
	public static ImageService getImageService()
	{
		if(imageService == null)
		{
			imageService = new ImageServiceImpl();
		}
		
		return imageService;
	}
	
	public static ThreadService getThreadService()
	{
		if(threadService == null)
		{
			threadService = new ThreadServiceImpl();
		}
		
		return threadService;
	}
}
