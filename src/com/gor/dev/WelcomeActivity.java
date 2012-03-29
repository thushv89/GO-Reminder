package com.gor.dev;


import com.gor.dev.util.CommonUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	TextView inetDetailsTV;
	TextView gpsDetailsTV;
	TextView serviceDetailsTV;
	Button tourB;
	int textRightColor;
	Color textWrongColor;
	/** Called when the activity is first created. */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NetworkHandler nwHandler=new NetworkHandler(this);
		
		textRightColor=Color.rgb(0,42,17);
		//Setup the grid view and the corresponding adapter
		setContentView(R.layout.test2);
		inetDetailsTV=(TextView)findViewById(R.id.inetTV);
		gpsDetailsTV=(TextView)findViewById(R.id.gpsTV);
		serviceDetailsTV=(TextView)findViewById(R.id.serviceTV);
		tourB=(Button)findViewById(R.id.tourB);
		tourB.setOnClickListener(new ButtonHandler());
		
		startService(new Intent(this,NotificationService.class));
		//serviceDetailsTV.setTextColor(textRightColor);
		
		serviceDetailsTV.setText("GO-Reminder is online");
		//simple test to start a new service
		
		if(nwHandler.getIsConnected()){
			//inetDetailsTV.setTextColor(textRightColor);
			inetDetailsTV.setText("You are connected to Internet");
		}else{
			inetDetailsTV.setText("You are not connected to Internet.");
		}
		
		if(CommonUtils.GPS_IS_ENABLED){
			//gpsDetailsTV.setTextColor(textRightColor);
			gpsDetailsTV.setText("GPS is enabled");
		}else{
			gpsDetailsTV.setText("GPS is disabled.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.welcome_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.create_memo){
			startActivity(new Intent(getBaseContext(), CreateMemoActivity.class));
		}else if(item.getItemId()==R.id.view_memos){
			startActivity(new Intent(getBaseContext(), ViewMemoActivity.class));
		}else if(item.getItemId()==R.id.location_org){
			startActivity(new Intent(getBaseContext(), LocationOrgActivity.class));
		}else if(item.getItemId()==R.id.my_location){
			startActivity(new Intent(getBaseContext(),MyLocationActivity.class));
		}
		return true;
	}

	class ButtonHandler implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==tourB.getId()){
				startActivity(new Intent(getBaseContext(),AboutActivity.class));
			}
			
		}
		
	}
}