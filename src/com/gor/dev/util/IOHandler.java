package com.gor.dev.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;
/**IOHandler handles all the IO related tasks
 * @author Thushan
 *
 */
public class IOHandler {




	/**Takes in an object and store it in a persistent storage
	 * @param obj Object to store
	 * @param filePath	File path to store the object
	 */
	public static void WriteObject(Object obj,String filePath){	
		ObjectOutputStream myOutput;
		try {
			myOutput = new ObjectOutputStream(new FileOutputStream(filePath,true));
			myOutput.writeObject(obj);
			myOutput.flush();
			myOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**If the specified file exists delete the file
	 * @param dir	The directory file is in
	 * @param fileName	Name of the file to delete
	 * @return	The deleted object
	 */
	public static Object ifExistDelete(String dir,String fileName){
		File file=new File(dir+"/"+fileName);
		if(file.exists()){
			Object m=ReadObject(dir, fileName);
			file.delete();
			return m;
		}
		return null;
	}

	/**Reads an object from the persistent storage
	 * @param dir	Directory to read from
	 * @param fileName	Name of the file to be read
	 * @return	The object read
	 */
	public static Object ReadObject(String dir,String fileName){
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(dir,fileName)));
			Object obj = ois.readObject();
			ois.close();
			return obj;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**Lists the files in a directory
	 * @param dir	Dir to search for
	 * @return	Array of strings of filenames in the directory
	 */
	public static String[] listItemsInDir(String dir){
		File file=new File(dir);
		return file.list();
	}

	/**Creates a directory if the directory does not exist
	 * @param base	bese directory the directory is created
	 * @param dir	directory name to create
	 * @return	Whether the directory is created or not
	 */
	public static boolean createDirIfNotExist(File base,String dir){
		try{
			File file=new File(base,dir);
			if(!file.exists()){
				file.mkdir();
			}
			return true;
		}catch(Exception e){
			return false;
		}

	}
}
