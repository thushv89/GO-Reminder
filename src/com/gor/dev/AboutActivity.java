package com.gor.dev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends Activity implements OnClickListener{

	Button closeB;
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.about);
		closeB=(Button)findViewById(R.id.closeB);
		closeB.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==closeB.getId()){
			startActivity(new Intent(v.getContext(),WelcomeActivity.class));
		}
	}
}
