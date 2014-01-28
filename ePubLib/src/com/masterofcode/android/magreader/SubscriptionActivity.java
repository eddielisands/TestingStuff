package com.masterofcode.android.magreader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.masterofcode.android.magreader.inapp.util.Base64;
import com.masterofcode.android.magreader.subscription.SubscriptionSignInResult;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class SubscriptionActivity extends Activity {
	public final static int SUBSCRIPTION_ACTION_LOGIN = 1;
	public final static int SUBSCRIPTION_ACTION_LOGOUT = 2;
	public final static int SUBSCRIPTION_ACTION_REQUEST_NAME = 3;
	public final static int SUBSCRIPTION_ACTION_REQUEST_PASSWORD = 4;
	public final static int SUBSCRIPTION_ACTION_REGISTER = 5;
	
	private int 			subscriptionAction = 0;
	private String 			actionUrl;
	private WebView			actionWebView;
	
	private EditText	loginEditText;
	private EditText	passwordEditText;
	private TextView	loginTextView;
	private TextView	passwordTextView;
	private Button		signinButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.subscription_layout);

		if(savedInstanceState==null)
		{
			Intent intent = this.getIntent();
			subscriptionAction = intent.getIntExtra(Constants.BUNDLE_KEY_SUBSCRIPTION_ACTION, 0);
		} else {
			subscriptionAction = savedInstanceState.getInt(Constants.BUNDLE_KEY_SUBSCRIPTION_ACTION, 0);
		}
		
		if(subscriptionAction==0)
		{
			setResult(RESULT_CANCELED);
			finish();
		}

		actionUrl = urlForSubscriptionAction(subscriptionAction);
		
		loginEditText = (EditText) findViewById(R.id.loginEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		loginTextView = (TextView) findViewById(R.id.loginTextView);
		passwordTextView = (TextView) findViewById(R.id.passwordTextView);
		signinButton = (Button) findViewById(R.id.signInButton);
		actionWebView = (WebView) findViewById(R.id.actionWebView);

		if(subscriptionAction==SUBSCRIPTION_ACTION_LOGIN)
		{
			actionWebView.setVisibility(View.GONE);
			loginEditText.requestFocus();
			
			loginEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
			{
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
				{
					passwordEditText.requestFocus();
					return true;
				}
			});
			
			signinButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					signInPressed();
				}
			});
			
		} else {
			loginEditText.setVisibility(View.GONE);
			passwordEditText.setVisibility(View.GONE);
			loginTextView.setVisibility(View.GONE);
			passwordTextView.setVisibility(View.GONE);
			signinButton.setVisibility(View.GONE);
			
			
			actionWebView.getSettings().setJavaScriptEnabled(true);
			actionWebView.loadUrl(actionUrl);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(Constants.BUNDLE_KEY_SUBSCRIPTION_ACTION, subscriptionAction);
	}

	protected void signInPressed()
	{
		String			login = loginEditText.getText().toString();
		String			password = passwordEditText.getText().toString();
		
		if(login.equals(""))
		{
			loginEditText.requestFocus();
			return;
		}
		if(password.equals(""))
		{
			passwordEditText.requestFocus();
			return;
		}
		
		performSignIn(login, password);
	}
	
	protected void performSignIn(String login, String password)
	{
		new SignInTask().execute(actionUrl, login, password);
	}
	
	private String urlForSubscriptionAction(int actionId)
	{
		String		result = null;
		switch (actionId) {
		case SUBSCRIPTION_ACTION_LOGIN:
			result = Constants.BACKEND_AUTH_URL;
			break;

		case SUBSCRIPTION_ACTION_REGISTER:
			result = getString(R.string.subscription_account_action_register_new_account_url);
			break;

		case SUBSCRIPTION_ACTION_REQUEST_NAME:
			result = getString(R.string.subscription_account_action_request_name_url);
			break;

		case SUBSCRIPTION_ACTION_REQUEST_PASSWORD:
			result = getString(R.string.subscription_account_action_request_password_url);
			break;

		default:
			break;
		}
		return result;
	}
	
	public void loginSuccessful(String login, String password)
	{
		ApplicationUtils.setPrefProperty(this, Constants.PREFERENCE_SUBSCRIPTION_ENABLED, true);
		ApplicationUtils.setPrefProperty(this, Constants.PREFERENCE_SUBSCRIPTION_NAME, login);
		ApplicationUtils.setPrefProperty(this, Constants.PREFERENCE_SUBSCRIPTION_PASSWORD, password);
		
		Intent			intent = new Intent();
		intent.putExtra(Constants.BUNDLE_KEY_SUBSCRIPTION_IS_LOGGED_IN, true);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void loginFailed(SubscriptionSignInResult result)
	{
		String			title = null;
		
		if(result.isNetworkError())
		{
			title = getString(R.string.subscription_dialog_title_auth_ioerror);
		} else {
			title = getString(R.string.subscription_dialog_title_auth_failed);
		}
		DialogFragment newFragment = SignInErrorDialogFragment.newInstance(title);
		newFragment.show(getFragmentManager(), "signin_error_dialog");
	}
	
	class SignInTask extends AsyncTask<String, Integer, SubscriptionSignInResult>
	{
		@Override
		protected SubscriptionSignInResult doInBackground(String... params)
		{
			String				urlStr = params[0];
			final String		login = params[1];
			final String		password = params[2];
			HttpURLConnection   http = null;
			
			URL url;
			try
			{
				url = new URL(urlStr);

				/* official android auth callback that failed 
				 * 
	            Authenticator.setDefault (new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication()
	                {
	                    return new PasswordAuthentication(login, password.toCharArray());
	                }
	            });*/

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
	            String 			authPair = login+":"+password;
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
	                
	            	SubscriptionSignInResult		result = new SubscriptionSignInResult(true, false);
	            	result.setLogin(login);
	            	result.setPassword(password);
	            	http.disconnect();
	            	return result;
	            }
            	http.disconnect();
			} catch (IOException e)
			{
				return new SubscriptionSignInResult(false, true);
			}
			return new SubscriptionSignInResult(false, false);
		}

		@Override
		protected void onPostExecute(SubscriptionSignInResult result)
		{
			super.onPostExecute(result);
			
			if(result.isLoginSuccessful())
			{
				loginSuccessful(result.getLogin(), result.getPassword());
			} else {
				loginFailed(result);
			}
		}
		
		final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	                return true;
	        }
		};
		
		private void trustAllHosts() {
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
	

	public static class SignInErrorDialogFragment extends DialogFragment {
		private final static String			TITLE_KEY = "reason_is_ioerror";

        public static SignInErrorDialogFragment newInstance(String title) {
        	SignInErrorDialogFragment frag = new SignInErrorDialogFragment();
            Bundle args = new Bundle();
            args.putString(TITLE_KEY, title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString(TITLE_KEY);
            
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                    )
                    .create();
        }
    }
}
