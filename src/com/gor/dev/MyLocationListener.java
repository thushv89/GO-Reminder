package com.gor.dev;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.gor.dev.util.CommonUtils;

/**MyLocationListener will send updates on user's current locations to 
 * those who are interested
 * @author Thushan
 *
 */
public class MyLocationListener implements LocationListener {

	Context context;	//context required to display certain messages
	MapView mView;	//mapView required to change zoom & pan of it when a location update comes
	MapController mControl;
	public MyLocationListener(Context context,MapView mView){
		this.context=context;
		this.mView=mView;
		mControl=mView.getController();
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		if(location != null){
			GeoPoint gp=new GeoPoint((int) (location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
			//Update the static global field my current location for every location change
			CommonUtils.MY_CURRENT_LOCATION[0]=location.getLatitude();
			CommonUtils.MY_CURRENT_LOCATION[1]=location.getLongitude();

			Toast.makeText(context, "Location changed: lat:"+location.getLatitude()+" longi:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
			mControl.animateTo(gp);//go to user's location on the map
			mControl.setZoom(13);//zoom to level 13
			mView.invalidate();
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(context, 
				"Sorry Provider is not available at the momonet", Toast.LENGTH_LONG);
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}

}
