package com.gor.dev;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DateTimeTabActivity extends Activity {

	class CheckListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			if(isChecked){
				memoDate.setEnabled(true);
				memoTime.setEnabled(true);
			}else{
				memoDate.setEnabled(false);
				memoTime.setEnabled(false);
			}
		}		
	}
	
	CheckBox enableDateTime;
	EditText memoDate;
	EditText memoTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.datetime_tab);
		enableDateTime=(CheckBox) findViewById(R.id.setDateTimeCB);
		enableDateTime.setOnCheckedChangeListener(new CheckListener());
		memoDate=(EditText) findViewById(R.id.dateET);
		memoTime=(EditText) findViewById(R.id.timeEt);
		memoDate.setEnabled(false);
		memoTime.setEnabled(false);
		//selectLocB=(Button)findViewById(R.id.selectLocB);
	}
}
