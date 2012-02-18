package com.gor.dev.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;

import com.gor.dev.entities.Location;

/**Maintains the operations related to location organizing
 * @author Thushan
 *
 */
public class LocationOrganizer {

	private static String workingDir="";
	private static boolean initialize(Context context){
		workingDir=context.getFilesDir()+"/Locations";
		File file= new File(workingDir);
		if(!file.exists()){
			file.mkdir();
			return true;
		}
		return false;
	}

	/**Write a location to persistent storage
	 * @param context	context of the directory system
	 * @param l	Location to be written
	 */
	public static void writeLocation(Context context,Location l){
		initialize(context);
		IOHandler.WriteObject(l,workingDir+"/"+l.getName()+".loc");
	}

	/**Read a location from persistent storage
	 * @param context	context of the directory system
	 * @return The location read
	 */
	public static Location readLocation(Context context,String name){
		initialize(context);
		Location lread;
		lread=(Location)IOHandler.ReadObject(workingDir, name);
		return lread;
	}

	/**Get a list of all the locations in storage
	 * @param context	context of the directory system
	 * @return	List of locations
	 */
	public static ArrayList<Location> getAllLocations(Context context){
		initialize(context);
		String[] locationNames=IOHandler.listItemsInDir(workingDir);
		ArrayList<Location> locs=new ArrayList<Location>();
		for(int i=0;i<locationNames.length;i++){
			locs.add((Location)IOHandler.ReadObject(workingDir, locationNames[i]));
		}
		return locs;
	}

}
