package com.jenuine.linkedin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.closet.beans.User;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.DateOfBirth;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.VisibilityType;
import com.jenuine.linkedin.LinkedinDialog.OnVerifyListener;

public class LinkedInLoginActivity extends Activity {
	Button login;
	Button share;
	EditText et;
	TextView name;
	ImageView photo;
	protected VisibilityType visibility = VisibilityType.CONNECTIONS_ONLY;

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(
                    Config.LINKEDIN_CONSUMER_KEY,Config.LINKEDIN_CONSUMER_SECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(Config.LINKEDIN_CONSUMER_KEY,
					Config.LINKEDIN_CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	LinkedInApiClient client;
	LinkedInAccessToken accessToken = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); 
		}
	
		linkedInLogin();
		
	}

	private void linkedInLogin() {
		ProgressDialog progressDialog = new ProgressDialog(
				LinkedInLoginActivity.this);

		final LinkedinDialog d = new LinkedinDialog(LinkedInLoginActivity.this,
				progressDialog);
		d.show();

		// set call back listener to get oauth_verifier value
		d.setVerifierListener(new OnVerifyListener() {
			@Override
			public void onVerify(String verifier) {
				
				try {
					Log.i("LinkedinSample", "verifier: " + verifier);

					accessToken = LinkedinDialog.oAuthService
							.getOAuthAccessToken(LinkedinDialog.liToken,
									verifier);
					
					
					
					//LinkedinDialog.factory.createLinkedInApiClient(accessToken);
					//client = factory.createLinkedInApiClient(accessToken);
					
					
					client = factory.createLinkedInApiClient(accessToken);
//					client.postNetworkUpdate("LinkedIn Android app test");
					// Person profile = client.getProfileForCurrentUser();
					Person profile = null;
					try {
						profile = client.getProfileForCurrentUser(EnumSet.of(
								ProfileField.ID, ProfileField.FIRST_NAME,
								ProfileField.EMAIL_ADDRESS, ProfileField.LAST_NAME,
								ProfileField.HEADLINE, ProfileField.INDUSTRY,
								ProfileField.PICTURE_URL, ProfileField.DATE_OF_BIRTH,
								ProfileField.LOCATION_NAME, ProfileField.MAIN_ADDRESS,
									ProfileField.LOCATION_COUNTRY));
						Log.e("create access token secret", client.getAccessToken()
								.getTokenSecret());
					} catch (NullPointerException e) {
						// TODO: handle exception
					}
					
					
					
					
					try{
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getToken());
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getTokenSecret());
					}catch(Exception e){
						
					}
					//Person p = client.getProfileForCurrentUser();
//					name.setText("Welcome " + p.getFirstName() + " "
//							+ p.getLastName());
					User user=new User();
					try{
					user.setId(profile.getId());
				}catch(Exception e){
					
				}try{
					user.setEmail(profile.getEmailAddress());
				}catch(Exception e){
					
				}try{
					user.setName(profile.getFirstName()+" "+profile.getLastName());
				}catch(Exception e){
					
				}try{
					if(profile.getDateOfBirth()!=null){
					DateOfBirth date = profile.getDateOfBirth();
				String str=	date.getDay()+"/"+date.getMonth()+"/"+date.getYear();
					user.setBirthday(str);
					}
				}catch(Exception e){
					
				}
					try{
					user.setProfilepic(profile.getPictureUrl());
				}catch(Exception e){
					
				}
					user.setType("L");
					Intent intent=new Intent();
					intent.putExtra("USER", user);
					setResult(RESULT_OK, intent);
					
					d.dismiss();
					finish();

				} catch (Exception e) {
					finish();
				}
			}

			@Override
			public void onCancel() {
				finish();
				
			}
		});
//		d.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//			
//				finish();
//			}
//		});
		// set progress dialog
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
}

