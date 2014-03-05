package it.sephiroth.android.library.imagezoom.test.service.impl;


import it.sephiroth.android.library.imagezoom.test.Constants;
import it.sephiroth.android.library.imagezoom.test.service.ImageService;
import it.sephiroth.android.library.imagezoom.test.util.LogController;

import java.io.File;

import android.os.Environment;


public class ImageServiceImpl implements ImageService {

	private boolean flieStructurePrepared = false;
	
	@Override
	public void prepareFileStructure() {
		if(!flieStructurePrepared)
		{
			flieStructurePrepared = true;
			
			boolean success = false;
			for(int i = 0 ; i < Constants.FILE_STRUCTURE_ARRAY.length ; i++)
			{
				File folder = new File(Constants.FILE_STRUCTURE_ARRAY[i]);
		    	if(!folder.exists())
		    	{
		    		success = folder.mkdirs();
		    		LogController.log("ImageServiceImpl prepareFileStructure : " + success + "path: " + folder.getAbsolutePath());
		    	}
			}
		}
	}

}
