package com.gor.dev.entities;

import java.io.Serializable;

/**Location represent locations of interest of a user
 * @author Thushan
 *
 */
public class Location implements Serializable{

	private static final long serialVersionUID = -8510489740489175257L;
	private double[] coordinates;	//coordinates of the location
	private String name;	//name of the location
	private String description;	//description of the location
	private String category;	//location category
	
	//getters and setters
	public double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
