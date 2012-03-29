package com.gor.dev;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.gor.dev.entities.Memo;
import com.gor.dev.util.CommonUtils;
import com.gor.dev.util.IOHandler;

/**MyLocationListener will send updates on user's current locations to 
 * those who are interested
 * @author Thushan
 *
 */
public class MyLocationListener implements LocationListener {

	int startID;
	Context context;	//context required to display certain messages
	MapView mView;	//mapView required to change zoom & pan of it when a location update comes
	MapController mControl;
	String workingDir;
	NotificationManager nm;
	Notification memoNotif;
	boolean ranInitial=false;	//maintain whether AllMemos were updated with notification id initially
	
	public MyLocationListener(Context context,MapView mView){
		this.context=context;
		workingDir=context.getFilesDir()+"/Memos";
		this.mView=mView;
		mControl=mView.getController();
		nm=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		

	}
	public MyLocationListener(Context context){
		this.context=context;
		workingDir=context.getFilesDir()+"/Memos";
		nm=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		
		memoNotif=new Notification();
		memoNotif.icon=R.drawable.at_location;
		memoNotif.iconLevel=1;
		memoNotif.flags |= Notification.DEFAULT_VIBRATE;
		memoNotif.flags |= Notification.DEFAULT_SOUND;
	}
	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		if(location != null){
			GeoPoint gp=new GeoPoint((int) (location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
			//Update the static global field my current location for every location change
			CommonUtils.MY_CURRENT_LOCATION[0]=location.getLatitude()*1E6;
			CommonUtils.MY_CURRENT_LOCATION[1]=location.getLongitude()*1E6;

			Toast.makeText(context, "Your location: "+CommonUtils.getFormattedLocationString(CommonUtils.MY_CURRENT_LOCATION,""), Toast.LENGTH_SHORT).show();
			checkNearLocation();
			if(mView!=null && mControl!=null){
				mControl.animateTo(gp);//go to user's location on the map
				mControl.setZoom(13);//zoom to level 13
				mView.invalidate();
			}
		}
	}

	private void checkNearLocation(){
		String[] files=IOHandler.listItemsInDir(workingDir);
		if(!ranInitial || files.length!=CommonUtils.ALL_MEMOS.size()){
			ranInitial=true;
			CommonUtils.ALL_MEMOS.clear();
			startID=10000;
			for(String fName:files){
				Memo m=(Memo)IOHandler.ReadObject(workingDir, fName);
				m.setNotificationID(startID);
				startID++;
				CommonUtils.ALL_MEMOS.add(m);
			}
		}
		//Toast.makeText(context,CommonUtils.MY_CURRENT_LOCATION[0]+" "+CommonUtils.MY_CURRENT_LOCATION[1],Toast.LENGTH_LONG).show();
		
		for(Memo memo:CommonUtils.ALL_MEMOS){
			if(memo.getCoordinates()!=null){
			Double diff1=Math.abs(CommonUtils.MY_CURRENT_LOCATION[0]-memo.getCoordinates()[0]);
			Double diff2=Math.abs(CommonUtils.MY_CURRENT_LOCATION[1]-memo.getCoordinates()[1]);
			//Created to check value of my location
			Toast.makeText(context, diff1+" "+diff2, Toast.LENGTH_SHORT).show();
			if(Math.abs(CommonUtils.MY_CURRENT_LOCATION[0]-memo.getCoordinates()[0])<1000 &&
					Math.abs(CommonUtils.MY_CURRENT_LOCATION[1]-memo.getCoordinates()[1])<1000){
				//memosToRemind.add(memo);
				showAtLocationNotification(memo);
			}
			}
		}
	
	}
	
	private void showAtLocationNotification(Memo m){		
		CharSequence text=m.getSubject();

		PendingIntent contentIntn=PendingIntent.getActivity(context,0,new Intent(context,ViewMemoActivity.class), 0);		
		memoNotif.setLatestEventInfo(context, "Reminder",text, contentIntn);
		nm.notify(m.getNotificationID(), memoNotif);
		
	}
	
	@Override
	public void onProviderDisabled(String arg0) {
		CommonUtils.GPS_IS_ENABLED=false;
	}

	@Override
	public void onProviderEnabled(String arg0) {
		CommonUtils.GPS_IS_ENABLED=true;
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}

}
