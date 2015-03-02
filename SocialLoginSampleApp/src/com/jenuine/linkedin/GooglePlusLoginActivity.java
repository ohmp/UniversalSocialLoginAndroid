package com.jenuine.linkedin;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.closet.beans.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GooglePlusLoginActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener{
private static final int RC_SIGN_IN = 2;
private GoogleApiClient mGoogleApiClient;
private boolean mSignInClicked;
private boolean mIntentInProgress;
private ConnectionResult mConnectionResult;

@Override
protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	mGoogleApiClient = new GoogleApiClient.Builder(this)
	.addConnectionCallbacks(this)
	.addOnConnectionFailedListener(this).addApi(Plus.API, null)
	.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	try {
		Thread.sleep(1000);
		signInWithGplus();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
/**
 * Sign-in into google
 * */
private void signInWithGplus() {
	if (!mGoogleApiClient.isConnecting()) {
		mSignInClicked = true;

		resolveSignInError();

	}
}

/**
 * Sign-out from google
 * */
private void signOutFromGplus() {
	if (mGoogleApiClient.isConnected()) {
		Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
		mGoogleApiClient.disconnect();
		mGoogleApiClient.connect();
		updateUI(false);
	}
}
protected void onStart() {

	mGoogleApiClient.connect();
	super.onStart();
}

protected void onStop() {

	if (mGoogleApiClient.isConnected()) {
		mGoogleApiClient.disconnect();
	}
	super.onStop();
}
/**
 * Method to resolve any signin errors
 * */
private void resolveSignInError() {
	if (mConnectionResult!=null&&mConnectionResult.hasResolution()) {
		try {
			mIntentInProgress = true;
			mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
		} catch (SendIntentException e) {
			mIntentInProgress = false;
			mGoogleApiClient.connect();

		}
	}

}

@Override
public void onConnectionFailed(ConnectionResult result) {

	if (!result.hasResolution()) {
		GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
				0).show();
		return;
	}

	if (!mIntentInProgress) {
		// Store the ConnectionResult for later usage
		mConnectionResult = result;

		if (mSignInClicked) {
			// The user has already clicked 'sign-in' so we attempt to
			// resolve all
			// errors until the user is signed in, or they cancel.
			resolveSignInError();
		}
	}

}

@Override
protected void onActivityResult(int requestCode, int responseCode,
		Intent intent) {

	if (requestCode == RC_SIGN_IN) {
		if (responseCode != RESULT_OK) {
			mSignInClicked = false;
			finish();
		}

		mIntentInProgress = false;

		if (!mGoogleApiClient.isConnecting()) {
			mGoogleApiClient.connect();
		}
	}
}

@Override
public void onConnected(Bundle arg0) {
	mSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG)
				.show();

		// Get user's information
		getProfileInformation();

		// Update the UI after signin
		updateUI(true);

}

/**
 * Updating the UI, showing/hiding buttons and profile layout
 * */
private void updateUI(boolean isSignedIn) {
	finish();
}

/**
 * Fetching user's information name, email, profile pic
 * */
private void getProfileInformation() {
	try {
		
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi
					.getCurrentPerson(mGoogleApiClient);
			
			String personName = currentPerson.getDisplayName();
			String personPhotoUrl = currentPerson.getImage().getUrl();
			String personGooglePlusProfile = currentPerson.getUrl();
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			String bday=currentPerson.getBirthday();
			int sex=currentPerson.getGender();
			String id=currentPerson.getId();
			String gender=sex==0?"MALE":"FEMALE";
			Log.e("google", "Name: " + personName + ", plusProfile: "
					+ personGooglePlusProfile + ", email: " + email
					+ ", Image: " + personPhotoUrl);
            User user=new User();
            user.setBirthday(bday);
            user.setEmail(email);
            user.setName(personName);
            user.setId(id);
            user.setGender(gender);
            user.setProfilepic(personPhotoUrl);
            user.setType("G");
            Intent intent=new Intent();
            intent.putExtra("USER", user);
            setResult(RESULT_OK, intent);
			// txtName.setText(personName);
			// txtEmail.setText(email);

			// by default the profile url gives 50x50 px image only
			// we can replace the value with whatever dimension we want by
			// replacing sz=X
			// personPhotoUrl =
			// personPhotoUrl.substring(0,personPhotoUrl.length() - 2)+
			// PROFILE_PIC_SIZE;

			// new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

		} else {
			Toast.makeText(getApplicationContext(),
					"Person information is null", Toast.LENGTH_LONG).show();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}

@Override
public void onConnectionSuspended(int arg0) {
	mGoogleApiClient.connect();
	updateUI(false);
}

}
