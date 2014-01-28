package com.masterofcode.android.magreader.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtils {
	public static void copyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	public static String getThumbnailsLink(String text){
		Pattern imgUrl = Pattern.compile("(http?|ftp)://[a-z0-9-]+(\\.[a-z0-9-]+)+(/[\\w-]+)*/[\\w-]+\\.(gif|png|jpg|PNG|JPG)");
		Matcher imageMatcher = imgUrl.matcher(text);
		if (imageMatcher.find()){
			return imageMatcher.group();
		} else
			return "";
	}
}
