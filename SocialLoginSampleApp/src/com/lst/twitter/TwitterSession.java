package com.lst.twitter;

import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TwitterSession {
	private final SharedPreferences sharedPref;
	private final Editor editor;

	private static final String TWEET_AUTH_KEY = "auth_key";
	private static final String TWEET_AUTH_SECRET_KEY = "auth_secret_key";
	private static final String TWEET_USER_NAME = "user_name";
	private static final String TWEET_USER_IMAGE_URL = "image_url";
	private static final String SHARED = "Twitter_Preferences";
	private static final String TWEET_USER_ID = "user_id";

	public TwitterSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		editor = sharedPref.edit();
	}

	public void storeAccessToken(AccessToken accessToken, String username, String imageUrl, long l) {
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, username);
		editor.putString(TWEET_USER_IMAGE_URL, imageUrl);
		editor.putString(TWEET_USER_ID, l+"");
		editor.commit();
	}

	public void resetAccessToken() {
		editor.putString(TWEET_AUTH_KEY, null);
		editor.putString(TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);
		editor.putString(TWEET_USER_IMAGE_URL, null);
		editor.putString(TWEET_USER_ID, null);

		editor.commit();
	}

	public String getUsername() {
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public String getProfileImageUrl() {
		return sharedPref.getString(TWEET_USER_IMAGE_URL, "");
	}

	public String getUserId() {
		return sharedPref.getString(TWEET_USER_ID, "");
	}

	public AccessToken getAccessToken()
	{
		String token = sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);

		if (token != null && tokenSecret != null)
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}
}