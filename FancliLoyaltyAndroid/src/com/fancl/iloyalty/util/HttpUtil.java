package com.fancl.iloyalty.util;

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
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.service.callback.HttpDownloadFileCallback;
import com.google.gson.JsonObject;

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

	public static boolean uploadFile(String url, String fileKey, File file) throws Exception
	{
		DefaultHttpClient mHttpClient = new DefaultHttpClient();

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		try {

			HttpUtil.log("strart upload file");

			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntity.addPart(fileKey, new FileBody(file));
			httppost.setEntity(multipartEntity);

			httppost.setParams(params);

			HttpUtil.log("strart...");

			HttpResponse httpResponse = mHttpClient.execute(httppost);

			String result = null;

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			HttpUtil.log("file upload statusCode >>> " + statusCode);

			if(statusCode == HttpURLConnection.HTTP_OK)
			{
				result = EntityUtils.toString(httpResponse.getEntity());
			}

			HttpUtil.log("file upload result >>> " + result);

			if(result != null)
			{
				if(result.indexOf("error_response") >= 0)
				{
					throw new Exception("File upload failed.");
				}
			}
			else
			{
				throw new Exception("Cannot get connection response");
			}

			HttpUtil.log("file uploaded");

			return true;

		} catch (Exception e) {
			HttpUtil.log("upload file exception");
			e.printStackTrace();
			throw e;
		}
	}

	public static String uploadFileWithParams(String url, List<String> keys,
			List<String> values, List<String> fileKeys, List<File> files) throws Exception {
		DefaultHttpClient mHttpClient = new DefaultHttpClient();

		Charset charSet = java.nio.charset.Charset.forName("UTF-8");

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		try
		{

			HttpUtil.log("strart upload file");

			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			if (keys != null && values != null)
			{
				for (int i = 0; i < keys.size(); i++)
				{
					multipartEntity.addPart(keys.get(i), new StringBody(values.get(i), "text/plain", charSet));
				}
			}

			if (fileKeys != null && files != null)
			{
				for (int i = 0; i < fileKeys.size(); i++)
				{
					multipartEntity.addPart(fileKeys.get(i), new FileBody(files.get(i)));
				}
			}

			StringBuilder urlParametersBuilder = new StringBuilder();
			JsonObject jsonParams = null;

			if (keys != null && values != null)
			{
				if (keys.size() != values.size())
				{
					throw new Exception("keys length and values length not equal.");
				}

				jsonParams = new JsonObject();

				int i;
				for (i = 0; i < keys.size(); i++)
				{
					// LogController.log("keys.get(i) >>  " + keys.get(i));

					if (i == 0)
					{
						urlParametersBuilder.append(keys.get(i));
						urlParametersBuilder.append("=");
						urlParametersBuilder.append(URLEncoder.encode(values.get(i), "utf-8"));
					}
					else
					{
						urlParametersBuilder.append("&");
						urlParametersBuilder.append(keys.get(i));
						urlParametersBuilder.append("=");
						urlParametersBuilder.append(URLEncoder.encode(values.get(i), "utf-8"));
					}

					jsonParams.addProperty(keys.get(i), values.get(i));
				}
			}

			httppost.setEntity(multipartEntity);

			httppost.setParams(params);

			HttpUtil.log("strart...");

			LogController.log("httpPOst>>>>>>" + httppost);

			HttpResponse httpResponse = mHttpClient.execute(httppost);

			LogController.log("httpPost>>>" + httppost);

			String result = null;

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			HttpUtil.log("file upload statusCode >>> " + statusCode);

			if (statusCode == HttpURLConnection.HTTP_OK)
			{
				result = EntityUtils.toString(httpResponse.getEntity());
			}
			else
			{
				throw new Exception("Http Status Code not 200.");
			}

			HttpUtil.log("file upload result >>> " + result);

			HttpUtil.log("file upload call finished");

			return result;
		}
		catch (Exception e)
		{
			HttpUtil.log("upload file exception");
			e.printStackTrace();
			throw e;
		}
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
