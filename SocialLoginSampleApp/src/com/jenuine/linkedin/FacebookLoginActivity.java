package com.jenuine.linkedin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import com.closet.beans.User;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

public class FacebookLoginActivity extends FragmentActivity implements StatusCallback{
private UiLifecycleHelper uiHelper;

@Override
protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	 uiHelper = new UiLifecycleHelper(this, this);
	    uiHelper.onCreate(savedInstanceState);
//	    onClickLogout();
	    updateView();
}
//facebook call back
private void onClickLogin() {
	Session session = Session.getActiveSession();
	if (!session.isOpened() && !session.isClosed()) {
		session.openForRead(new Session.OpenRequest(this).setPermissions(
				Arrays.asList("email","user_birthday")).setCallback(this));
	} else {
		Session.openActiveSession(this, true, this);
	}
}
@Override
public void onResume() {
    super.onResume();
    uiHelper.onResume();
}

@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    uiHelper.onSaveInstanceState(outState);
}

@Override
public void onPause() {
    super.onPause();
    uiHelper.onPause();
}
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.e("Facebook Activity", String.format("Error: %s", error.toString()));
            Log.e("Facebook Activity", String.format("pendingCall: %s", pendingCall.toString()));
//            finish();
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.i("Activity", "Success!");
            updateView();
        }
    });	
	}
private void updateView() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			makeMeRequest();
			// textInstructionsOrLink.setText(URL_PREFIX_FRIENDS +
			// session.getAccessToken());

			// buttonLoginLogout.setText("Logout");
			// buttonLoginLogout.setOnClickListener(new OnClickListener() {
			// public void onClick(View view) { onClickLogout(); }
			// });
		} else {
			onClickLogin();
			// textInstructionsOrLink.setText("Please login");
			// buttonLoginLogout.setText("Login");
			// buttonLoginLogout.setOnClickListener(new OnClickListener() {
			// public void onClick(View view) { onClickLogin(); }
			// });
		}
		
	}
//facebook call back
	private void onClickLogout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
		}
	}

//facebook call back
	private void makeMeRequest() {

		Request request = Request.newMeRequest(Session.getActiveSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						String email = "";
						String name="";
						String bday="";
						String gender="";
						String id="";
						String type="F";
						String profilepic="";
						
						try {
						
							email = user.asMap().get("email").toString();
						} catch (Exception e) {
							System.out.println("email:" + e.getMessage());
						}
				     try {
							gender = user.asMap().get("gender").toString();
					} catch (Exception e) {
						System.out.println("email:" + e.getMessage());
					}
							name=user.getName();
							bday=user.getBirthday();
							Date date;
							if(!TextUtils.isEmpty(bday)){
							try {
								
								date = new SimpleDateFormat("MM/dd/yyyy").parse(bday);
								 bday = new SimpleDateFormat("yyyy-MM-dd").format(date);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						
							id=user.getId();
							profilepic = String.format("https://graph.facebook.com/%s/picture",id);
						
						User info=new User();
						info.setEmail(email);
						info.setBirthday(bday);
						info.setGender(gender);
						info.setProfilepic(profilepic);
						info.setType(type);
						info.setName(name);
						info.setId(id);
						Intent intent=new Intent();
						intent.putExtra("USER", info);
						setResult(RESULT_OK,intent);
						finish();
						// Utils.show(getApplicationContext(),
						// user.getName()+" "+email+" ");
						// startNewActivity();
						// textInstructionsOrLink.setText(
						// user.getName()+"\n"+email+"\n");
					}

				});
		request.executeAsync();

	}
@Override
public void call(Session session, SessionState state, Exception exception) {
if (exception instanceof FacebookOperationCanceledException) {
	
	finish();
}else{
	updateView();
	
}
	
}
}
