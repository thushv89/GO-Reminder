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
	
	public static void setButtonStyle(Button b,String text,int id){
    	b.setText(text);
    	b.setId(id);
    }
	
	/**Takes the current date and time and convert to a string 
	 * @return String that is converted from calendar instance
	 */
	public static String getCurrentDateTimeString(){
		Calendar cal=Calendar.getInstance();
		String dateTime;
		dateTime="Memo-"+cal.get(Calendar.DAY_OF_MONTH)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.HOUR)+"-"+cal.get(Calendar.MINUTE)+"-"+cal.get(Calendar.SECOND);
		return dateTime;
	}
	
	
	/**Takes a Time object and convert to a string
	 * @param time Time to be converted
	 * @return	String of time converted from Time object
	 */
	public static String timeToString(Time time){
		return time.hour+":"+time.minute;
	}
	
	
	/**Takes a string and convert to a time object
	 * @param time String of time
	 * @return Time object created from the time string
	 */
	public static Time stringToTime(String time){
		Time t=new Time();
		String[] tokens=time.split(":");
		t.hour=Integer.parseInt(tokens[0]);
		t.minute=Integer.parseInt(tokens[1]);
		
		return t;
	}
}
