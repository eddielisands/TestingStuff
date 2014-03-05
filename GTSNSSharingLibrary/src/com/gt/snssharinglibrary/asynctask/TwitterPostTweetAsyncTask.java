package com.gt.snssharinglibrary.asynctask;

import java.net.URLEncoder;

import org.apache.http.client.utils.URLEncodedUtils;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.os.AsyncTask;

import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.StringMapping;
import com.gt.snssharinglibrary.pojo.CusProgressDialog;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.util.LogController;
import com.sugree.twitter.Twitter;

public class TwitterPostTweetAsyncTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private SNSService snsService;
	private Twitter twitter;
	private SNSShareDetail snsShareDetail;

	private CusProgressDialog cusProgressDialog;

	public TwitterPostTweetAsyncTask(Context context, SNSService snsService,
			Twitter twitter, SNSShareDetail snsShareDetail)
	{
		this.context = context;
		this.snsService = snsService;
		this.twitter = twitter;
		this.snsShareDetail = snsShareDetail;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// process of thread before start(UI Thread)

		cusProgressDialog = new CusProgressDialog(context);
		cusProgressDialog.setMessage(StringMapping.FACEBOOK_LOGIN_LOADING_MESSAGE);
		cusProgressDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (twitter != null && snsShareDetail != null)
		{
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true);
			cb.setOAuthConsumerKey(Config.TWITTER_CONSUMER_KEY);
			cb.setOAuthConsumerSecret(Config.TWITTER_CONSUMER_SECRET);
			cb.setOAuthAccessToken(twitter.getAccessToken());
			cb.setOAuthAccessTokenSecret(twitter.getSecretToken());

			TwitterFactory tf = new TwitterFactory(cb.build());
			twitter4j.Twitter twitter4j = tf.getInstance();

			try
			{
				String message = snsShareDetail.getDescription();
//				try
//				{
//					message = URLEncoder.encode(message);
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}

				twitter4j.updateStatus(message);

				return Boolean.valueOf(true);
			}
			catch (TwitterException e)
			{
				LogController.log("TwitterException on Post >>> " + e.getMessage());
				e.printStackTrace();
				return Boolean.valueOf(false);
			}
		}

		return Boolean.valueOf(false);
	}

	@Override
	protected void onPostExecute(Boolean isSuccess) {
		super.onPostExecute(isSuccess);
		// process of thread ended(UI Thread)

		if (snsService != null)
		{
			snsService.postStatus(isSuccess, null);
		}

		if (cusProgressDialog != null)
		{
			cusProgressDialog.dismiss();
		}
	}
}
