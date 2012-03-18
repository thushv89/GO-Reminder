package com.gor.dev;

import com.gor.dev.util.IOHandler;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class LocationTabActivity extends Activity {

	Button selectLocB;
	CheckBox enableLocation;
	
	class ButtonHandler implements OnClickListener{

		@Override
		public void onClick(View v) {
			startActivity(new Intent(v.getContext(),MyLocationActivity.class));
		}
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_tab);
		selectLocB=(Button)findViewById(R.id.selectLocB);
		enableLocation=(CheckBox) findViewById(R.id.setLocationCB);
		selectLocB.setOnClickListener(new ButtonHandler());
	}
}
