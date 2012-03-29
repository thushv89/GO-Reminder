package com.gor.dev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener{

	Button nextB;
	Button closeB;
	TextView titleTV;
	TextView desc1TV;
	TextView desc2TV;
	String[] titles;
	String[] desc1;
	String[] desc2;
	int currentIdx=0;
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.about_page);
		nextB=(Button)findViewById(R.id.nextB);
		closeB=(Button)findViewById(R.id.closeB);
		titleTV=(TextView)findViewById(R.id.titleTV);
		desc1TV=(TextView)findViewById(R.id.desc1TV);
		desc2TV=(TextView)findViewById(R.id.desc2TV);
		
		nextB.setOnClickListener(this);
		closeB.setOnClickListener(this);
		titles=initTitleList();
		desc1=initDesc1List();
		desc2=initDesc2List();
		
		setTextonTextViews(0);
	}
	
	private void setTextonTextViews(int i){
		titleTV.setText(titles[i]);
		desc1TV.setText(desc1[i]);
		desc2TV.setText(desc2[i]);
	}
	//Titles for the set of about pages
	public String[] initTitleList(){
		String[] titleList={"GO-Reminder","Welcome Page","Create Memo","View Memos",
				"Location Organizer","My Current Location"};
		return titleList;
	}
	//descriptions just below the tile page
	public String[] initDesc1List(){
		String[] desc1List={"GO-Reminder is a geo-location based task reminder. In other words it reminds you of tasks to be done based on location rather than time. Cool isn't it?",
				"Welcome page will show you the current status of internet, GPS and whether the application is online or offline. It also allows you to go to new windows by pressing menu button",
				"Create memo window lets you to save a memo. Memo must include a subject and optionally it can include time & date and a location.",
				"View Memos window shows you all them memos saved. They are sorted in the order they were created. Also you can search among the memos for a specific word in a subject of a memo.",
				"Location organizer does what it does best! It organizes locatinos in a neat way, for you to access it with convenience later.",
				"My Current location shows your current location. You can mark a location there. Then a small popup window will appear which allows you to save the location or tag your friends on that location."
				};
		return desc1List;
		}
	public String[] initDesc2List(){
		String[] desc2List={"GO-Reminder also provide capability to maintain a neat organizer of locations that are of interest to you. It does this by classifying locations to categories. And the user also can tell about these locations to his friends",
				"Create Memo, View memo, Location Organizer, My current location can be accessed via the welcome window",
				"After you clicking the save button, application will take care of reminding you of the task accordingly (based on time & date or location or both!)",
				"Click on a memo to see its subject. Long click lets you to edit or remove individual memos",
				"Location organizer categorizes the locations to Family, Shopping, Friends, Work and Other. Long clicking allows you to delete locations saved",
				""
				};
		return desc2List;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==nextB.getId()){
			currentIdx++;
			if(currentIdx>4){
				nextB.setEnabled(false);
			}
			setTextonTextViews(currentIdx);
		}
		if(v.getId()==closeB.getId()){
			finish();
		}
	}
}
