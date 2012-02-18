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
	private Date date;	//date to be reminded
	private String time;	//time to be reminded
	private double[] coordinates;	//location coordinates
	
	public Memo(){}
	public Memo(String subj,Date d,String t,double[] p){
		subject=subj;
		date=d;
		time=t;
		coordinates=p;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
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
}
