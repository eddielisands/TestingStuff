package com.masterofcode.android.magreader.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.inapp.util.Base64;
import com.masterofcode.android.magreader.utils.constants.Constants;

public class RestClient {
	
	private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	public static JSONArray connect(String url)
    {
		String result;
		JSONObject json = null;
 
        HttpClient httpclient = new DefaultHttpClient();
 
        // Prepare a request object
        HttpGet httpget = new HttpGet(url); 
 
        // Execute the request
        HttpResponse response;
        try {
            Log.i("feed_prov",url);
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.d("Praeda",response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
 
            if (entity != null) {
 
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result= convertStreamToString(instream);
                Log.d("Praeda",result);
 
                // A Simple JSONObject Creation
                json=new JSONObject(result);
                 
                // Closing the input stream will trigger connection release
                instream.close();
            }
 
 
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(json!=null)
        {
        	json = json.optJSONObject("value");   // generate exception if no inet
        	JSONArray jsonArray = json.optJSONArray("items");
        	return jsonArray;
    	}
		return null;
    }
	
	public static RequestingIssuesResult sendJson(String url, String searchTermsValue, int amount, int offSetValue) {
		String result;
		
		RequestingIssuesResult		httpResult = basicHttpAuth2(url, searchTermsValue, amount, offSetValue);
		
		if(httpResult==null) return null;
		if(httpResult.isIOError())
		{
			new RequestingIssuesResult(null, httpResult.isAuthValid(), httpResult.isIOError(), null);
		}
        
	    HttpResponse response = httpResult.getHttpResponse();
	    JSONObject json = new JSONObject();
	    	try{
	            HttpEntity entity = response.getEntity(); 
	            if (entity != null) {
	            	// A Simple JSON Response Read
	                InputStream instream = entity.getContent();
	                result= convertStreamToString(instream);
	                //Log.i("Get JSON",result);
	                // A Simple JSONObject Creation
	                json=new JSONObject(result);
	                // Closing the input stream will trigger connection release
	                instream.close();
	            }
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        JSONArray jsonArray = json.optJSONArray("issues");
	        return new RequestingIssuesResult(jsonArray, httpResult.isAuthValid(), httpResult.isIOError(), null);
    }
	
	private static RequestingIssuesResult basicHttpAuth2(String url, String searchTermsValue, int amount, int offSetValue){
		HttpResponse response = null;
		RequestingIssuesResult		validationResult = null;
		try {
			URL			urlObject = new URL(url);
			
			String host = urlObject.getHost();
			int port = urlObject.getPort();
 
			HttpClient client = new DefaultHttpClient();
			client = WebClientDevWrapper.wrapClient(client);
		    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		    
            AuthScope as = new AuthScope(host, port);
            UsernamePasswordCredentials upc;
            
            // use subscription login/password pair if avail
            String 	subscriptionName = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_NAME, null);
            String 	subscriptionPassword = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_PASSWORD, null);
            boolean isLogged = ApplicationUtils.getPrefPropertyBoolean(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);
    		
    		if(isLogged && subscriptionName!=null)
    		{
    			upc = new UsernamePasswordCredentials(
    					subscriptionName, subscriptionPassword);
    		} else {
    			upc = new UsernamePasswordCredentials(
    					Constants.TECH_USER_LOGIN, Constants.TECH_USER_PASSWORD);
    		}
    		
    		if(isLogged)
    		{
        		validationResult = isCredentialsAreValid();
        		
        		if(validationResult.isIOError())
        		{
        			return new RequestingIssuesResult(null, false, true, null);
        		}
        		
        		if(!validationResult.isAuthValid())		// pasword is expired, using technical
        		{
        			upc = new UsernamePasswordCredentials(
        					Constants.TECH_USER_LOGIN, Constants.TECH_USER_PASSWORD);
        		}
    		} else {
    			validationResult = new RequestingIssuesResult(null, true, false, null);
    		}
    		
            ((AbstractHttpClient) client).getCredentialsProvider()
                    .setCredentials(as, upc);
            BasicHttpContext localContext = new BasicHttpContext();
            BasicScheme basicAuth = new BasicScheme();
            localContext.setAttribute("preemptive-auth", basicAuth);
            HttpPost httpget = new HttpPost(url);
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    		JSONObject jsonObject = new JSONObject();
    		if (!TextUtils.isEmpty(searchTermsValue) && amount != -1 && offSetValue != -1) {
    			//	Add a JSON Object
    			JSONObject jsonInternal = new JSONObject();
    			JSONObject paging = new JSONObject();
    			paging.put( "off-set", offSetValue);
    			jsonInternal.put( "Paging", paging);
    			jsonInternal.put("amount", amount);
    			jsonInternal.put("search-terms", searchTermsValue);
    			jsonObject.put("result-parameter", jsonInternal);
    		}
    		nameValuePairs.add(new BasicNameValuePair("json", jsonObject.toString()));
    		httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    		response = client.execute(httpget);
    		Log.d("auth", "Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("auth", "Error: " + e.getMessage());
        }
		return new RequestingIssuesResult(null, validationResult.isAuthValid(), false, response);
	}
	
	protected static RequestingIssuesResult isCredentialsAreValid()
	{
		boolean isLogged = ApplicationUtils.getPrefPropertyBoolean(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);
		
		if(!isLogged) return new RequestingIssuesResult(null, true, false, null);		// default backend password is used
		
        String 	subscriptionName = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_NAME, null);
        String 	subscriptionPassword = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_PASSWORD, null);
        
        HttpURLConnection   http = null;
        
        String		urlStr = Constants.BACKEND_AUTH_URL;
		URL url;
		
		try{
			url = new URL(urlStr);

	        if (url.getProtocol().toLowerCase().equals("https"))
	        {
	            trustAllHosts();
	            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
	            https.setHostnameVerifier(DO_NOT_VERIFY);
	            http = https;
	        } else {
	            http = (HttpURLConnection) url.openConnection();
	        }

	        http.setRequestMethod("GET");
	        http.setDoInput(true);
	        http.setDoOutput(false);
	        http.setConnectTimeout (10000);
	        http.setUseCaches(false);
	        HttpURLConnection.setFollowRedirects(false);
	        String 			authPair = subscriptionName+":"+subscriptionPassword;
	        http.setRequestProperty("Authorization", "Basic " + Base64.encode(authPair.getBytes()));

	        http.connect();
	        
	        if(http.getResponseCode()==200)
	        {
	            // load server response
	            InputStream input = new BufferedInputStream(http.getInputStream());
	            byte data[] = new byte[1024];
	            while (input.read(data) > 0)
	            {
	            }
	            input.close();
	            
	        	http.disconnect();
	        	return new RequestingIssuesResult(null, true, false, null);
	        }
		} catch (IOException e) {
        	return new RequestingIssuesResult(null, false, true, null);
		}
    	http.disconnect();
		return new RequestingIssuesResult(null, false, false, null);
	}
	
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
                return true;
        }
	};
	
	private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                }

                @SuppressWarnings("unused")
				public void checkClientTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                }

                @SuppressWarnings("unused")
				public void checkServerTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                }

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
					
				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
					
				}
        } };

        // Install the all-trusting trust manager
        try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection
                                .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
                e.printStackTrace();
        }
	}

}
