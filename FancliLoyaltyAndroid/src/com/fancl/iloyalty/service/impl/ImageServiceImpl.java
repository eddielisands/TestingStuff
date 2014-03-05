package com.fancl.iloyalty.service.impl;

import java.io.File;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.service.ImageService;
import com.fancl.iloyalty.util.LogController;

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
		    		success = folder.mkdir();
		    		LogController.log("ImageServiceImpl prepareFileStructure : " + success);
		    	}
			}
		}
	}

}
