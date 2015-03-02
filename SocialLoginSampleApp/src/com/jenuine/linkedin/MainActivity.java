package com.jenuine.linkedin;


import com.closet.beans.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=(Button)findViewById(R.id.button);
        Button fb=(Button)findViewById(R.id.facebook);
        
        Button gp=(Button)findViewById(R.id.google);
        Button tw=(Button)findViewById(R.id.twitter);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 startActivityForResult(new Intent(MainActivity.this,LinkedInLoginActivity.class), 1);
				
			}
		});
 fb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 startActivityForResult(new Intent(MainActivity.this,FacebookLoginActivity.class), 2);
				
			}
		});
 gp.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			 startActivityForResult(new Intent(MainActivity.this,GooglePlusLoginActivity.class), 3);
			
		}
	});
 tw.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			 startActivityForResult(new Intent(MainActivity.this,TwitterLoginActivity.class), 4);
			
		}
	});
       
    }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if(requestCode==1||requestCode==2||requestCode==3||requestCode==4){
		if(resultCode==RESULT_OK&&data!=null){
			User user=data.getParcelableExtra("USER");
			Toast.makeText(getApplicationContext(),user.getName()+" " +user.getBirthday()+" "+user.getEmail(), 500).show();
		}else{
			Toast.makeText(getApplicationContext(),"cancelled", 500).show();
		}
	}
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
