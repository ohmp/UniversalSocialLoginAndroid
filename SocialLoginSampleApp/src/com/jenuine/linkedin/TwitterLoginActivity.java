package com.jenuine.linkedin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.closet.beans.User;
import com.lst.twitter.Twitt_Sharing;
import com.lst.twitter.Twitt_Sharing.TwitterSuccess;


public class TwitterLoginActivity extends FragmentActivity implements TwitterSuccess{

	@Override
	public void fail(String error) {
		finish();
		
	}

	@Override
	public void success(User info) {
		Intent intent=new Intent();
		intent.putExtra("USER", info);
		setResult(RESULT_OK,intent);
		finish();
	}
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Twitt_Sharing ts=new Twitt_Sharing(this, "dgDOdxCZW3Zc5Ca6A2p7oWkzp","gWKz4X9t5TjFADiRJmFcITB4j0H54am67EOUOkG7aeCFVeJlYn");
		ts.shareToTwitter(getApplicationContext(), "", null, "login");
		ts.setListner(this);
	}

}
