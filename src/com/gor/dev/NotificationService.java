package com.gor.dev;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.gor.dev.entities.Memo;
import com.gor.dev.util.CommonUtils;
import com.gor.dev.util.IOHandler;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class NotificationService extends Service {

	NotificationManager nm;
	Calendar time=Calendar.getInstance();
	Timer timer=new Timer();
	String workingDir="";
	ArrayList<Integer> memoIDs=new ArrayList<Integer>();
	LocationListener lListener;
	LocationManager lManager;
	Context context;
	int startID;
	Thread locUpdateThread;
	int locationNotificationID=1000;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		lListener=new MyLocationListener(getBaseContext()); //subscribe to myLocationListener

		lManager=(LocationManager) getSystemService(LOCATION_SERVICE);	//Get GPS service
		lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
				CommonUtils.TIME_TO_UPDATE_LOCATION, 
				CommonUtils.DISTANCE_TO_UPDATE_LOCATION, lListener);	//Get the GPS provider
		workingDir=getBaseContext().getFilesDir()+"/Memos";
		nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//Toast.makeText(this,"Service created at "+time.getTime(),Toast.LENGTH_LONG).show();
		
		if(!lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			CommonUtils.GPS_IS_ENABLED=false;
		}else{
			CommonUtils.GPS_IS_ENABLED=true;
		}

	}

	
	/**
	 * This method runs periodically doing whatever specified inside
	 */
	private void runUpdate() {
		timer.scheduleAtFixedRate(new TimerTask(){ 
			public void run() {
				//nm.cancel(id);
				String[] files=IOHandler.listItemsInDir(workingDir);
				if(files.length!=CommonUtils.ALL_MEMOS.size()){
					CommonUtils.ALL_MEMOS.clear();
					startID=20000;
					for(String fName:files){
						Memo m=(Memo)IOHandler.ReadObject(workingDir, fName);
						CommonUtils.ALL_MEMOS.add(m);
						
					}
				}
				//Toast.makeText(context,CommonUtils.MY_CURRENT_LOCATION[0]+" "+CommonUtils.MY_CURRENT_LOCATION[1],Toast.LENGTH_LONG).show();
				
				for(Memo memo:CommonUtils.ALL_MEMOS){
					if(memo.isDateTimeEnabled()){
						//isMatchDateTime(memo.getDate(), memo.getTime());
						setAlarm(memo.getDate(), memo.getTime());
					}
				}
				
			}}, 100, CommonUtils.QUERY_INTERVAL);
	}
	
	/** This method sets the alarm to whatever time speficied
	 * @param dateStr date in string format
	 * @param time time in string format
	 */
	private void setAlarm(String dateStr,String time){
		Calendar cal = Calendar.getInstance();
		
		String[] dateTokens=dateStr.split("/");
		
		String[] timeTokens=time.split(":");
		String ampm=timeTokens[1].substring(2);
		ampm=ampm.toUpperCase();
		int hrOfDay=Integer.parseInt(timeTokens[0]);
		if("PM".equals(ampm)){
			hrOfDay+=12;
		}
		
		timeTokens[1]=timeTokens[1].substring(0, 2);
		
		cal.set(Integer.parseInt(dateTokens[2]),Integer.parseInt(dateTokens[0]), Integer.parseInt(dateTokens[1]), 
				hrOfDay,Integer.parseInt(timeTokens[1]));
		
		Intent activate = new Intent(this, ViewMemoActivity.class);
		AlarmManager alarams ;
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, activate, 0);
		alarams = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarams.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);

	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy(){
		Toast.makeText(getApplicationContext(), "Service Terminated", Toast.LENGTH_LONG).show();
		nm.cancelAll();
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		showAppOnlineNotification();		
		runUpdate();		
		return 0;
	}

	private void showAppOnlineNotification(){
		CharSequence text="Service Started";
		Notification nf=new Notification(R.drawable.gor_app, text, System.currentTimeMillis());
		PendingIntent contentIntn=PendingIntent.getActivity(this,0,new Intent(this,ViewMemoActivity.class), 0);		
		nf.setLatestEventInfo(this, "GO-Reminder is online",text, contentIntn);
		nm.notify(254, nf);
		
	}
	
	
	private boolean isMatchDateTime(String dateStr,String time){
		Calendar c=Calendar.getInstance();
		
		String[] dateTokens=dateStr.split("/");
		int month=Integer.parseInt(dateTokens[0]);
		int date = Integer.parseInt(dateTokens[1]);
		int year=Integer.parseInt(dateTokens[2]);
		
		String[] timeTokens=time.split(":");
		String ampm=timeTokens[1].substring(2);
		int ampmVal;
		ampm=ampm.toUpperCase();
		if("AM".equals(ampm)){
			ampmVal=0;
		}else{
			ampmVal=1;
		}
		timeTokens[1]=timeTokens[1].substring(0, 2);
		
		int hours=Integer.parseInt(timeTokens[0]);
		int minutes=Integer.parseInt(timeTokens[1]);
		if(c.MONTH==month && c.DATE==date && c.YEAR==year &&
				c.HOUR==hours && c.MINUTE==minutes && c.AM_PM==ampmVal){
			return true;
		}
		return false;
	}
}
