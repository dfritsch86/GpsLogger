package com.daniel.gpslogger.util;


import android.content.Context;

public class Writer {
	
	private FileType nmea, gps;
	
	public Writer(String nmeaFileName, String gpsFileName, Context context) {
		this.nmea = new FileType(100, nmeaFileName, context);
		this.gps = new FileType(50, gpsFileName, context);
	}
	
	public void addToNmea(String s) {
		this.nmea.addToFile(s);
	}
	
	public void addToGps(String s) {
		this.gps.addToFile(s);
	}
	
	public void finalize() {
		this.nmea.finalize();
		this.gps.finalize();
	}
}
