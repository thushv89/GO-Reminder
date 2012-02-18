package com.gor.dev;


import com.gor.dev.util.CommonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	/** Called when the activity is first created. */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Setup the grid view and the corresponding adapter
		setContentView(R.layout.welcome);
		//Button b=(Button) findViewById(R.id.button1);
		GridView gView= (GridView)findViewById(R.id.gridView1);
		ButtonAdapter bAdapter = new ButtonAdapter(this,getLayoutInflater());
		gView.setAdapter(bAdapter);
		
		//simple test to start a new service
		if(CommonUtils.started && !CommonUtils.ended){
			//b.setEnabled(true);
		}else if(!CommonUtils.started && CommonUtils.ended){
			//b.setEnabled(false);
		}
	}


}