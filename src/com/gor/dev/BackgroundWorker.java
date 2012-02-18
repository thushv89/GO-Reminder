package com.gor.dev;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.gor.dev.entities.Memo;
import com.gor.dev.util.CommonUtils;
import com.gor.dev.util.IOHandler;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/**
 * @author Thushan
 *
 */
public class BackgroundWorker extends Service {

	/** This is loaded with all the memos reside in the device initially
	 * 
	 */
	private ArrayList<Memo> memos=new ArrayList<Memo>();
	Timer myTimer;
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		myTimer=new Timer();
		
		myTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				ctsTask();	
			}
		}, 0, 30000);
		
		String[] fileNames=IOHandler.listItemsInDir("");
		for(String fn:fileNames){
			memos.add((Memo)IOHandler.ReadObject("", fn));
		}
	}
	
	
	
	/**	This method checks for memos related to user's current location
	 * @return List of memos related to user's current location
	 */
	public ArrayList<Memo> checkIsMemoNearLocation(){
		ArrayList<Memo> nearMemos=new ArrayList<Memo>();
		for(Memo m:memos){
			if(Math.abs(m.getCoordinates()[0]-CommonUtils.MY_CURRENT_LOCATION[0])<0.0000 && Math.abs(m.getCoordinates()[1]-CommonUtils.MY_CURRENT_LOCATION[1])<0.0000){
				nearMemos.add(m);
			}
		}
		return nearMemos;
	}
	
	
	@Override
	public void onDestroy() {
		CommonUtils.started=false;
		CommonUtils.ended=true;
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		CommonUtils.started=true;
		CommonUtils.ended=false;
	}
	
	/**
	 * This method will be run continuously as a service
	 */
	public void ctsTask(){
	
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
