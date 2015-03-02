package com.lst.twitter;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.auth.AccessToken;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class Twitter_Handler {
	public static Twitter twitterObj;
	private final TwitterSession mSession;
	private AccessToken mAccessToken;
	private final CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private final OAuthProvider mHttpOauthprovider;
	private final String mConsumerKey;
	private final String mSecretKey;
	private final ProgressDialog mProgressDlg;
	private TwDialogListener mListener;
	private final Activity context;

	public static final String CALLBACK_URL = "twitterapp://connect";
	private static final String TWITTER_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String TWITTER_AUTHORZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String TWITTER_REQUEST_URL = "https://api.twitter.com/oauth/request_token";

	public Twitter_Handler(Activity context, String consumerKey,
			String secretKey) {
		this.context = context;

		twitterObj = new TwitterFactory().getInstance();

		mSession = new TwitterSession(context);
		mProgressDlg = new ProgressDialog(context);

		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mConsumerKey = consumerKey;
		mSecretKey = secretKey;

		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey,mSecretKey);

		String request_url = TWITTER_REQUEST_URL;
		String access_token_url = TWITTER_ACCESS_TOKEN_URL;
		String authorize_url = TWITTER_AUTHORZE_URL;

		mHttpOauthprovider = new DefaultOAuthProvider(request_url,
				access_token_url, authorize_url);
		mAccessToken = mSession.getAccessToken();

		configureToken();
	}

	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}

	private void configureToken() {
		if (mAccessToken != null) {
			twitterObj.setOAuthConsumer(mConsumerKey, mSecretKey);
			twitterObj.setOAuthAccessToken(mAccessToken);
		}
	}

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();

			mAccessToken = null;
		}
	}

	public String getUsername() {
		return mSession.getUsername();
	}

	public String getProfileImageUrl() {
		return mSession.getProfileImageUrl();
	}

	public String getUserId() {
		return mSession.getUserId();
	}

	public void updateStatus(String status) throws Exception {
		try {
			twitterObj.updateStatus(status);
		} catch (TwitterException e) {
			throw e;
		}
	}

	public void authorize() {
		mProgressDlg.setMessage("Loading ...");
		mProgressDlg.show();

		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;

				try {
					authUrl = mHttpOauthprovider.retrieveRequestToken(
							mHttpOauthConsumer, CALLBACK_URL);
					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler
						.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}

	public void processToken(String callbackUrl) {
		mProgressDlg.setMessage("Finalizing ...");
		mProgressDlg.show();

		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer,
							verifier);

					mAccessToken = new AccessToken(
							mHttpOauthConsumer.getToken(),
							mHttpOauthConsumer.getTokenSecret());

					configureToken();

					User user = twitterObj.verifyCredentials();

					mSession.storeAccessToken(mAccessToken, user.getName(), user.getProfileImageURL(), user.getId());


					//					try {
					//						ResponseList<UserList> lists = twitterObj.getUserLists("lstqa123");
					//						for (UserList list : lists) {
					//
					//							Toast.makeText(context, "id:" + list.getId() + ", name:" + list.getName() + ", description:"+ list.getDescription() + ", slug:" + list.getSlug() + "", Toast.LENGTH_SHORT).show();
					//							Log.e("TWITTER LIST","id:" + list.getId() + ", name:" + list.getName() + ", description:"+ list.getDescription() + ", slug:" + list.getSlug() + "");
					//						}
					//
					//					} catch (Exception e) {
					//						e.printStackTrace();
					//					}
					//					String url = user.getProfileImageURL();
					//					System.out.println("twitter Image URL::"+"https://api.twitter.com/1.1/users/show.json?screen_name=lstqa123&include_entities=false&user_id="+user.getId());

					//					Toast.makeText(context, user.getProfileImageURL(), Toast.LENGTH_SHORT).show();

					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	private String getVerifier(String callbackUrl) {
		String verifier = "";

		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for (String parameter : array) {
				String v[] = parameter.split("=");

				if (URLDecoder.decode(v[0]).equals(
						oauth.signpost.OAuth.OAUTH_VERIFIER)) {
					verifier = URLDecoder.decode(v[1]);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return verifier;
	}

	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {

			@Override
			public void onComplete(String value) {

				processToken(value);

			}

			@Override
			public void onError(String value) {
				mListener.onError("Failed opening authorization page");
			}
		};

		TwitterDialog twitter = new TwitterDialog(context, url, listener);
		twitter.setCancelable(false);
		twitter.show();
		
	}

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();

			if (msg.what == 1) {
				if (msg.arg1 == 1)
					mListener.onError("Error getting request token");
				else
					mListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};

	public interface TwDialogListener {
		public void onComplete(String value);

		public void onError(String value);
	}

}