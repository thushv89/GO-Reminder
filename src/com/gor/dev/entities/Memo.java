package com.gor.dev.entities;

import java.io.Serializable;
import java.util.Date;
//import android.text.format.Time;

/**Memo represent a reminder user put
 * @author Thushan
 *
 */
public class Memo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1631091788599926614L;
	private  String subject;	//subject of the reminder
	private String date;	//date to be reminded
	private String time;	//time to be reminded
	private double[] coordinates;	//location coordinates
	private boolean dateTimeEnabled;
	private int notificationID;	
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNotificationID() {
		return notificationID;
	}
	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}
	
	public Memo(){}
	public Memo(String id,String subj,String d,String t,Boolean dateTimeCB,double[] p){
		this.id=id;
		subject=subj;
		date=d;
		time=t;
		this.dateTimeEnabled=dateTimeCB;
		coordinates=p;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}
	public double[] getCoordinates() {
		return coordinates;
	}
	public boolean isDateTimeEnabled() {
		return dateTimeEnabled;
	}
	public void setDateTimeEnabled(boolean dateTimeEnabled) {
		this.dateTimeEnabled = dateTimeEnabled;
	}
}
