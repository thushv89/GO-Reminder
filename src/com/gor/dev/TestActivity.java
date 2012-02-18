package com.gor.dev;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

public class TestActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	
		GridView gView= (GridView)findViewById(R.id.gridView1);
		ButtonAdapter bAdapter = new ButtonAdapter(this,getLayoutInflater());
		gView.setAdapter(bAdapter);
	}

}
