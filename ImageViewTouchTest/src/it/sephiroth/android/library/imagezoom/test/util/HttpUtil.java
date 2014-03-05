package it.sephiroth.android.library.imagezoom.test.util;


import it.sephiroth.android.library.imagezoom.test.Constants;
import it.sephiroth.android.library.imagezoom.test.service.callback.HttpDownloadFileCallback;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;


public class HttpUtil {
	
	private static final String SUB_TAG = "HttpUtil";
	
	private static void log(String message)
	{
		LogController.log(SUB_TAG + " >>> " + message);
	}
	
	public static byte[] downloadXMLFromGet(String url, String[] keys, String[] values) throws ClientProtocolException, ConnectTimeoutException, Exception{
		return HttpUtil.downloadXMLFromGet(url, keys, values, Constants.GENERAL_HTTP_REQUEST_TIMEOUT);
	}
	
	public static byte[] downloadXMLFromGet(String url, String[] keys, String[] values, int timeout) throws ClientProtocolException, ConnectTimeoutException, Exception {

		String getURL = url;

		if (keys != null && values != null)
		{
			if (keys.length != values.length)
			{
				throw new Exception("keys length and values length not equal.");
			}

			HttpUtil.log("url : " + url);

			int i;
			for (i = 0; i < keys.length; i++)
			{
				if (i == 0)
				{
					getURL += "?" + keys[i] + "=" + URLEncoder.encode(values[i], "utf-8");
				}
				else
				{
					getURL += "&" + keys[i] + "=" + URLEncoder.encode(values[i], "utf-8");
				}
			}
		}
		
		URL getUrl = new URL(getURL);

		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		try
		{
			connection.connect();
			
			if (connection.getResponseCode() == 200)
			{			
				InputStream inputStream = connection.getInputStream();
				int size = 0;
				byte[] buf = new byte[1024];
				while ((size = inputStream.read(buf)) > 0)
				{
					byteArrayOutputStream.write(buf, 0, size);
				}
			}
			
			connection.disconnect();
			
			return byteArrayOutputStream.toByteArray();
		}
		catch (ClientProtocolException e) {
			if(connection != null)
			{
				connection.disconnect();
			}
			throw e;
		}
		catch (ConnectTimeoutException e) {
			if(connection != null)
			{
				connection.disconnect();
			}
			throw e;	
		}
		catch (Exception e) {
			if(connection != null)
			{
				connection.disconnect();
			}
			throw e;	
		}
	}
	
	public static byte[] downloadXMLFromPost(String url, String[] keys, String[] values) throws ClientProtocolException, ConnectTimeoutException, Exception{
		return HttpUtil.downloadXMLFromPost(url, keys, values, Constants.GENERAL_HTTP_REQUEST_TIMEOUT);
	}
	
	public static byte[] downloadXMLFromPost(String url, String[] keys, String[] values, int timeout) throws ClientProtocolException, ConnectTimeoutException, Exception {

		String urlParameters = "";

		if (keys != null && values != null)
		{
			if (keys.length != values.length)
			{
				throw new Exception("keys length and values length not equal.");
			}

			HttpUtil.log("url : " + url);

			int i;
			for (i = 0; i < keys.length; i++)
			{
				if (i == 0)
				{
					urlParameters += keys[i] + "=" + URLEncoder.encode(values[i], "utf-8");
				}
				else
				{
					urlParameters += "&" + keys[i] + "=" + URLEncoder.encode(values[i], "utf-8");
				}
			}
		}

		HttpUtil.log("urlParameters >>> " + urlParameters);
		
		URL getUrl = new URL(url);

		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches (false);
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		try
		{
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			if (connection.getResponseCode() == 200)
			{			
				InputStream inputStream = connection.getInputStream();
				int size = 0;
				byte[] buf = new byte[1024];
				while ((size = inputStream.read(buf)) > 0)
				{
					byteArrayOutputStream.write(buf, 0, size);
				}
			}
			
			connection.disconnect();
			
			return byteArrayOutputStream.toByteArray();
		}
		catch (ClientProtocolException e) {
			if(connection != null)
			{
				connection.disconnect();
			}
			
			throw e;
		}
		catch (ConnectTimeoutException e) {
			if(connection != null)
			{
				connection.disconnect();
			}
			throw e;	
		}
		catch (Exception e) {
			if(connection != null)
			{
				connection.disconnect();
			}
			throw e;	
		}
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			sb.append(line + "\n");
		}
		is.close();
		
		return sb.toString();
	}
	
	public static boolean downloadFile(String url, String filePath, HttpDownloadFileCallback callback) {
		
		HttpURLConnection c = null;
		InputStream input = null;
		OutputStream output = null;
		
		try
		{
			URL u = new URL(url);
			
			HttpUtil.log("downloadFile : " + url);
			
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setReadTimeout(Constants.GENERAL_HTTP_REQUEST_TIMEOUT);
			c.setConnectTimeout(Constants.GENERAL_HTTP_REQUEST_TIMEOUT);
			c.connect();
			
			int fileLength = c.getContentLength();
			
			HttpUtil.log("downloadFile fileLength : " + fileLength);
			
			input = new BufferedInputStream(u.openStream());
			output = new FileOutputStream(filePath);
			
			byte data[] = new byte[1024];
			long total = 0;
			int count;
			
			while ((count = input.read(data)) != -1)
			{
				total += count;
				
				if(callback != null)
				{
					callback.currentProgress((int) (total * 100 / fileLength));
				}
				
				output.write(data, 0, count);
			}
			
			output.flush();
			output.close();
			input.close();
			c.disconnect();
			
			File originalFile = new File(filePath);
			File file = new File(filePath + ".tmp");
			if (file.exists())
			{
				file.renameTo(originalFile);
			}
		}
		catch (Exception e)
		{
			try
			{
				if (c != null)
				{
					c.disconnect();
				}
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
			
			try
			{
				if (input != null)
				{
					input.close();
				}
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
			
			try
			{
				if (output != null)
				{
					output.close();
				}
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
			
			return false;
		}
		
		return true;
	}
}
