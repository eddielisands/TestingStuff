package com.gt.snssharinglibrary.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Config.SHARING_TYPE;
import com.gt.snssharinglibrary.Constants;
import com.gt.snssharinglibrary.pojo.CusProgressDialog;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.facebook_2_0.FacebookSessionStore;
import com.gt.snssharinglibrary.util.LogController;
import com.gt.snssharinglibrary.util.StringUtil;
import com.gt.snssharinglibrary.util.Util;

public class FacebookService30Impl extends SNSServiceSuperImpl implements
		SNSService {

	private enum STATUS_CALLBACK_TYPE
	{
		RESTORE, LOGIN, LOGOUT, POST
	}

	private enum PendingAction
	{
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private SessionStatusCallback statusRestoreCallback;
	private SessionStatusCallback statusLoginCallback;

	private SNSServiceCallback snsServiceCallback;

	private PendingAction pendingAction = PendingAction.NONE;
	private SNSShareDetail pendingSNSShareDetail;

	private CusProgressDialog cusProgressDialog;

	// TODO Facebook APIs > Graph API > User >
	// https://developers.facebook.com/docs/reference/api/user/
	private List<String> permissionsForRead = Arrays.asList(Config.FACEBOOK_PERMISSIONS);

	private OpenRequest genNewOpenForReadRequest(Activity activity,
			SessionStatusCallback callback) {
		return new OpenRequest(activity)//
		.setPermissions(permissionsForRead)//
		.setCallback(callback)//
		.setDefaultAudience(SessionDefaultAudience.FRIENDS)//
		.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
	}

	@Override
	public void loadConfig() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSNSServiceCallback(SNSServiceCallback snsServiceCallback) {
		this.snsServiceCallback = snsServiceCallback;
	}

	@Override
	public boolean isLogged(Context context) {
		Session session = Session.getActiveSession();

		if (session != null)
		{
			try
			{
				if (session.isOpened())
				{
					return true;
				}
			}
			catch (Exception e)
			{

			}
		}

		return false;
	}

	@Override
	public void login(Activity activity, Handler handler,
			SNS_LOGIN_TYPE snsLoginType) {
		statusLoginCallback = new SessionStatusCallback(activity, STATUS_CALLBACK_TYPE.LOGIN);

		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed())
		{
			session.openForRead(genNewOpenForReadRequest(activity, statusLoginCallback));
		}
		else
		{
			Session.openActiveSession(activity, true, statusLoginCallback);
		}
	}

	@Override
	public void logout(Activity activity, Handler handler) {
		Session session = Session.getActiveSession();

		if (!session.isClosed())
		{
			session.closeAndClearTokenInformation();
		}

		loggoutStatus(activity, true, null);
	}

	@Override
	public void logginStatus(Context context, boolean isSuccessLogin,
			Object errorObject) {

		if (isSuccessLogin)
		{
			Session session = Session.getActiveSession();
			if (session != null)
			{
				FacebookSessionStore.save(session, context);
			}

			getProfile(context);
		}

		if (snsServiceCallback != null)
		{
			snsServiceCallback.logginStatus(Constants.FACEBOOK_CODE, isSuccessLogin, errorObject);
		}
	}

	@Override
	public void loggoutStatus(Context context, boolean isSuccessLogout,
			Object errorObject) {

		if (isSuccessLogout)
		{
			Util.clearCookies(context);
		}

		if (snsServiceCallback != null)
		{
			snsServiceCallback.loggoutStatus(Constants.FACEBOOK_CODE, isSuccessLogout, errorObject);
		}
	}

	@Override
	public void post(Context context, SNSShareDetail snsShareDetail) {
		pendingAction = PendingAction.POST_STATUS_UPDATE;

		Session session = Session.getActiveSession();

		if (session != null)
		{
			pendingSNSShareDetail = snsShareDetail;

			if (hasPublishPermission())
			{
				handlePendingAction(context);
			}
			else
			{
				requestPublishPermission(context, session);
			}
		}
	}

	@Override
	public void postStatus(boolean isSuccessPost, Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.postStatus(Constants.FACEBOOK_CODE, isSuccessPost, errorObject);
		}
	}

	@Override
	public void getProfile(final Context context) {
		Session session = Session.getActiveSession();

		if (session != null)
		{
			if (session.isOpened())
			{
				// make request to the /me API
				Request request = Request.newMeRequest(session, new Request.GraphUserCallback()
				{
					// callback after Graph API response with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null)
						{
							// TextView etName = (TextView)
							// findViewById(R.id.etName);
							// etName.setText(user.getName() + "," +
							// user.getUsername() + "," + user.getId() + "," +
							// user.getLink() + "," + user.getFirstName() +
							// user.asMap().get("email"));
							String username = user.getUsername();
							String facebookId = user.getId();
							String email = null;
							String gender = null;

							try
							{
								email = user.asMap().get("email").toString();
							}
							catch (Exception e)
							{

							}
							try
							{
								gender = user.asMap().get("gender").toString();
							}
							catch (Exception e)
							{

							}

							FacebookSessionStore.saveFacebookInfo(username, facebookId, email, context);
							FacebookSessionStore.saveFacebookGender(context, gender);
							FacebookSessionStore.saveFacebookFirstName(context, user.getFirstName());
							FacebookSessionStore.saveFacebookMiddleName(context, user.getMiddleName());
							FacebookSessionStore.saveFacebookLastName(context, user.getLastName());
							FacebookSessionStore.saveFacebookBirthday(context, user.getBirthday());

							getProfileStatus(context, true, null);
						}
						else
						{
							getProfileStatus(context, false, null);
						}
					}
				});
				Request.executeBatchAsync(request);
			}
		}
	}

	@Override
	public void getProfileStatus(final Context context,
			boolean isSuccessGetProfile, Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.getProfileStatus(Constants.FACEBOOK_CODE, isSuccessGetProfile, null);
		}

		if (Config.REQUEST_NEW_PREMISSION_AFTER_GET_PROFILE)
		{
			Session session = Session.getActiveSession();
			requestPublishPermission(context, session);
		}
	}

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null)
		{
			statusRestoreCallback = new SessionStatusCallback(activity, STATUS_CALLBACK_TYPE.RESTORE);

			if (savedInstanceState != null)
			{
				session = Session.restoreSession(activity, null, statusRestoreCallback, savedInstanceState);
			}
			if (session == null)
			{
				session = new Session(activity);
			}

			Session.setActiveSession(session);

			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED))
			{
				session.openForRead(genNewOpenForReadRequest(activity, statusRestoreCallback));
			}
		}
	}

	@Override
	public void onStart() {
		if (statusRestoreCallback != null)
		{
			Session.getActiveSession().addCallback(statusRestoreCallback);
		}
	}

	@Override
	public void onStop() {
		if (statusRestoreCallback != null)
		{
			Session.getActiveSession().removeCallback(statusRestoreCallback);
		}
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		Session.getActiveSession().onActivityResult(activity, requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private class SessionStatusCallback implements Session.StatusCallback {

		private Context context;
		private STATUS_CALLBACK_TYPE statusCallbackType;
		private boolean deprecated = false;

		public SessionStatusCallback(Context context,
				STATUS_CALLBACK_TYPE statusCallbackType)
		{
			this.context = context;
			this.statusCallbackType = statusCallbackType;
		}

		public void setDeprecated() {
			this.deprecated = true;
		}

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (deprecated)
			{
				return;
			}

			if (statusCallbackType.equals(STATUS_CALLBACK_TYPE.LOGIN))
			{
				if (session != null)
				{
					if (session.isOpened())
					{
						if (session.getAccessToken() != null)
						{
							if (session.getAccessToken().trim().length() > 0)
							{
								logginStatus(context, true, null);
								return;
							}
						}
					}
				}

				logginStatus(context, false, null);
			}
			else
			{
				onSessionStateChange(context, session, state, exception);
			}
		}
	}

	private boolean hasPublishPermission() {
		if (Config.facebook30SharingType.equals(SHARING_TYPE.WEB_DIALOG))
		{
			return true;
		}
		if (Config.facebook30SharingType.equals(SHARING_TYPE.PLAIN_TEXT))
		{
			Session session = Session.getActiveSession();
			return session != null && session.getPermissions().contains("publish_actions");
		}

		return false;
	}

	private void onSessionStateChange(Context context, Session session,
			SessionState state, Exception exception) {
		if (!(exception instanceof FacebookOperationCanceledException) && !(exception instanceof FacebookAuthorizationException) && state == SessionState.OPENED_TOKEN_UPDATED)
		{
			handlePendingAction(context);
		}
		else
		{
			pendingSNSShareDetail = null;
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction(Context context) {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction)
		{
			case POST_PHOTO:
				// postPhoto();
				break;
			case POST_STATUS_UPDATE:
				postStatusUpdate(context, pendingSNSShareDetail);
				break;
		}
	}

	private void postStatusUpdate(Context context, SNSShareDetail snsShareDetail) {
		if (hasPublishPermission() && snsShareDetail != null)
		{
			Session session = Session.getActiveSession();

			if (Config.facebook30SharingType.equals(SHARING_TYPE.WEB_DIALOG))
			{
				WebDialog.FeedDialogBuilder feedDialogBuilder = new WebDialog.FeedDialogBuilder(context, session);

				if (snsShareDetail.getTitle() != null)
				{
					feedDialogBuilder.setName(snsShareDetail.getTitle());
				}

				if (snsShareDetail.getCaption() != null)
				{
					feedDialogBuilder.setCaption(snsShareDetail.getCaption());
				}

				if (snsShareDetail.getDescription() != null)
				{
					feedDialogBuilder.setDescription(snsShareDetail.getDescription());
				}

				if (snsShareDetail.getPictureLink() != null)
				{
					feedDialogBuilder.setPicture(snsShareDetail.getPictureLink());
				}

				if (snsShareDetail.getLink() != null)
				{
					feedDialogBuilder.setLink(snsShareDetail.getLink());
				}

				if (snsShareDetail.getFriendId() != null)
				{
					feedDialogBuilder.setTo(snsShareDetail.getFriendId());
				}

				feedDialogBuilder.setOnCompleteListener(new OnCompleteListener()
				{

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null)
						{
							String postId = values.getString("post_id");

							if (postId != null)
							{
								postStatus(true, null);
							}
							else
							{
								postStatus(false, null);
							}
						}
						else
						{
							// title = getString(R.string.error);
							// alertMessage = error.getErrorMessage();
							postStatus(false, error);
						}

						if (cusProgressDialog != null)
						{
							cusProgressDialog.dismiss();
						}
					}
				});

				feedDialogBuilder.build().show();
			}
			else if (Config.facebook30SharingType.equals(SHARING_TYPE.PLAIN_TEXT))
			{
				String message = snsShareDetail.getDescription();

				if (message != null)
				{
					Request request = Request.newStatusUpdateRequest(session, message, new Request.Callback()
					{
						@Override
						public void onCompleted(Response response) {
							// GraphObject result = response.getGraphObject();
							FacebookRequestError error = response.getError();

							if (error == null)
							{
								postStatus(true, null);
							}
							else
							{
								// title = getString(R.string.error);
								// alertMessage = error.getErrorMessage();
								postStatus(false, error);
							}

							if (cusProgressDialog != null)
							{
								cusProgressDialog.dismiss();
							}
						}
					});
					request.executeAsync();
				}
			}

			/*
			 * cusProgressDialog = new CusProgressDialog(context);
			 * cusProgressDialog
			 * .setMessage(StringMapping.FACEBOOK_POST_LOADING_MESSAGE);
			 * cusProgressDialog.show();
			 * 
			 * Session session = Session.getActiveSession();
			 * 
			 * Bundle bundle = new Bundle();
			 * 
			 * if (snsShareDetail.getTitle() != null) { bundle.putString("name",
			 * snsShareDetail.getTitle()); }
			 * 
			 * if (snsShareDetail.getCaption() != null) {
			 * bundle.putString("caption", snsShareDetail.getCaption()); }
			 * 
			 * if (snsShareDetail.getDescription() != null) {
			 * bundle.putString("description", snsShareDetail.getDescription());
			 * }
			 * 
			 * if (snsShareDetail.getPictureLink() != null) {
			 * bundle.putString("picture", snsShareDetail.getPictureLink()); }
			 * 
			 * if (snsShareDetail.getLink() != null) { bundle.putString("link",
			 * snsShareDetail.getLink()); }
			 * 
			 * if (snsShareDetail.getFriendId() != null) {
			 * bundle.putString("to", snsShareDetail.getFriendId()); }
			 * 
			 * Request request = new Request(Session.getActiveSession(),
			 * "me/feed", bundle, HttpMethod.POST, new Callback() {
			 * 
			 * @Override public void onCompleted(Response response) {
			 * FacebookRequestError error = response.getError();
			 * 
			 * if (error == null) { postStatus(true, null); } else { // title =
			 * getString(R.string.error); // alertMessage =
			 * error.getErrorMessage(); postStatus(false, error); }
			 * 
			 * if(cusProgressDialog != null) { cusProgressDialog.dismiss(); } }
			 * });
			 * 
			 * RequestAsyncTask task = new RequestAsyncTask(request);
			 * task.execute();
			 */
		}
		else
		{
			pendingAction = PendingAction.POST_STATUS_UPDATE;
		}

		pendingSNSShareDetail = null;
	}

	private void requestPublishPermission(Context context, Session session) {
		if (session != null && context instanceof Activity)
		{
			if (statusLoginCallback != null)
			{
				statusLoginCallback.setDeprecated();
			}

			List<String> permissions = new ArrayList<String>();
			permissions.add("publish_stream");

			NewPermissionsRequest newPermissionsRequest = new NewPermissionsRequest((Activity) context, permissions);
			session.requestNewPublishPermissions(newPermissionsRequest);
		}
	}

	@Override
	public void getFriends(int photoWidth, int photoHeight) {
		Session activeSession = Session.getActiveSession();
		if (activeSession.getState().isOpened())
		{
			Request friendRequest = Request.newMyFriendsRequest(activeSession, new GraphUserListCallback()
			{
				@Override
				public void onCompleted(List<GraphUser> users, Response response) {
					LogController.log("INFO >>> " + response.toString());

					if (users != null)
					{
						getFriendsStatus(true, users, null);
					}
					else
					{
						getFriendsStatus(false, null, null);
					}
				}
			});

			Bundle params = new Bundle();
			if (photoWidth <= 0 && photoHeight <= 0)
			{
				params.putString("fields", "id, name, installed, picture");
			}
			else
			{
				params.putString("fields", "id, name, installed, picture.height(" + photoWidth + ").width(" + photoHeight + ")");
			}
			friendRequest.setParameters(params);
			friendRequest.executeAsync();
		}
	}

	@Override
	public void getFriendsStatus(boolean isSuccessGetFriends,
			Object friendsResultObject, Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.getFriendsStatus(isSuccessGetFriends, friendsResultObject, errorObject);
		}
	}

	@Override
	public void getFeeds() {
		Session activeSession = Session.getActiveSession();
		if (activeSession.getState().isOpened())
		{
			Request request = Request.newGraphPathRequest(activeSession, Config.FACEBOOK_FEEDER_ID + "/feed", new Request.Callback()
			{
				@Override
				public void onCompleted(Response response) {
					getFeedsStatus(true, response.getGraphObject().getInnerJSONObject(), null);
				}
			});
			request.executeAsync();
		}
	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.getFeedsStatus(isSuccess, feedsResultObject, errorObject);
		}
	}

	@Override
	public void likeFeed(String feedId) {
		if (!StringUtil.isStringEmpty(feedId))
		{
			Session activeSession = Session.getActiveSession();
			if (activeSession.getState().isOpened())
			{
				Request request = Request.newGraphPathRequest(activeSession, feedId + "/likes", new Request.Callback()
				{
					@Override
					public void onCompleted(Response response) {
						likeFeedStatus(true, response.getGraphObject().getInnerJSONObject(), "");
					}
				});
				request.executeAsync();
			}
		}
		else
		{
			likeFeedStatus(false, "feedId: " + feedId, "");
		}
	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.likeFeedStatus(isSuccess, feedsResultObject, errorObject);
		}
	}

	@Override
	public void openPageByWebView(Activity activity, String link) {
		if (activity != null && !StringUtil.isStringEmpty(link))
		{
			Uri uri = Uri.parse(link);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			activity.startActivity(intent);
		}
	}
}
