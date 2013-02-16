package com.daniel.gpslogger.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class FileType {
	
	private final int TEMP_MAX;
	private int tempCount = 0;
	private String tempString = "";
	private final String FILENAME;
	private FileOutputStream stream;
	private boolean storageAvailable, storageWritable;
	
	private File file;
	
	public FileType(int tempMax, String fileName, Context con) {
		
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
			storageAvailable = storageWritable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
			storageAvailable = true;
			storageWritable = false;
			Log.w("FileType", "can't access storage");
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
			storageAvailable = storageWritable = false;
			Log.w("FileType", "can't access storage");
		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("_dd-MM-yyyy-HH-mm-ss");
		String currentDateandTime = sdf.format(new Date());
		
		this.TEMP_MAX = tempMax;
		this.FILENAME = fileName + currentDateandTime + ".txt";
		
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File (sdCard.getAbsolutePath() + "/GPSLogger");
		dir.mkdirs();
		file = new File(dir, this.FILENAME);
	}
	
	public void addToFile(String s) {
		this.tempString += s;
		this.tempCount++;
		if (this.tempCount >= this.TEMP_MAX) {
			Boolean done = false;
			while(!done) {
				done = this.writeToFile();
			}
		}
	}
	
	public void finalize() {
		this.writeToFile();
	}
	
	private Boolean writeToFile() {
		
		if(this.storageAvailable && this.storageWritable) {
			byte[] data = this.tempString.getBytes();
			try {
			    stream = new FileOutputStream(file, true);
			    stream.write(data);
			    stream.flush();
			    stream.close();
			    
			    this.tempString = "";
				this.tempCount = 0;
			    
			    Log.i("Writer", "Data succesfully written");
				return true;
			    
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
		}
		return false;
	}
}
