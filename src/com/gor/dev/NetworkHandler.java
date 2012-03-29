package com.gor.dev;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//gets the information about the network provider
public class NetworkHandler {

	Context context;
	NetworkInfo activeNetworkInfo;
	ConnectivityManager connectivityManager;
	
	public NetworkHandler(Context context){
		activeNetworkInfo=null;
		this.context=context;
		connectivityManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	//if a network connection is available it will return true
	//but remember in the emulator press F3 to deactivate the network connection
	public boolean isNetworkAvailable() {    
	    activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	//return the information about the network such as the provider's name
	public NetworkInfo getNetworkinfo(){
		activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo;
	}
	
	//check whether we are connected to internet
	public Boolean getIsConnected(){
		activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo.isConnected();
	}
}
