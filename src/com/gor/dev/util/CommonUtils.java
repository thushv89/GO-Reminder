package com.gor.dev.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.gor.dev.entities.Memo;

import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;

public class CommonUtils {
	
	public static ArrayList<Memo> ALL_MEMOS=new ArrayList<Memo>();	//all memos saved in the device
	public static Double[] MY_CURRENT_LOCATION=new Double[2];	//my current location
	public static boolean started=false;
	public static boolean ended=true;
	public static int QUERY_INTERVAL=90000;
	public static int DISTANCE_TO_UPDATE_LOCATION=5;
	public static int TIME_TO_UPDATE_LOCATION=5000;
	public static Boolean test=false;
	/**Takes the current date and time and convert to a string 
	 * @return String that is converted from calendar instance
	 */
	public static String getCurrentDateTimeString(){
		Calendar cal=Calendar.getInstance();
		String dateTime;
		dateTime="Memo-"+cal.get(Calendar.DAY_OF_MONTH)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.HOUR)+"-"+cal.get(Calendar.MINUTE)+"-"+cal.get(Calendar.SECOND);
		return dateTime;
	}
	
	public static String getFormattedLocationString(double[] coordinates,String address){
		return address+" "+"Lat: "+coordinates[0]+" Lon: "+coordinates[1];
	}
	
}
