package com.daniel.gpslogger;

import android.content.Context;
import android.widget.TextView;

public class DataDisplayer {
	private Context context;
	private TextView lat, lon, prec, alt, sats, sensors;
	
	public DataDisplayer(Context c) {
		this.context = c;
		this.lat = (TextView)((MainActivity) context).findViewById(R.id.textView3);
		this.lon = (TextView)((MainActivity) context).findViewById(R.id.textView4);
		this.prec = (TextView)((MainActivity) context).findViewById(R.id.textView7);
		this.alt = (TextView)((MainActivity) context).findViewById(R.id.textView8);
		this.sats = (TextView)((MainActivity) context).findViewById(R.id.textView9);
		this.sensors = (TextView)((MainActivity) context).findViewById(R.id.textView20);
	}
	
	public void updateView(String s) {
		
		this.sats.setText(s);
		
		String[] results = s.split(", ");
		
		//Display Sensor Data
		String sensorTemp = results[6].substring(1, results[6].length() - 2);
		String sensorText ="\n";
		String[] sensorValues = sensorTemp.split("\\*\\*");
		for(int i = 0; i < sensorValues.length; i++) {
			if(!sensorValues[i].equals("{}")) {
				sensorText += sensorValues[i].substring(1, sensorValues[i].length() - 1).replace("##", "\t") + "\n";
			}
		}
		this.sensors.setText(sensorText);
		
		
		//Display Location Data
		if(!results[5].equals("{}")) {
			String locTemp = results[5].substring(1, results[5].length() - 1);
			this.sats.setText(locTemp);
			
			String[] locValues = locTemp.split("\\*\\*");
			this.lat.setText(locValues[0]);
			this.lon.setText(locValues[1]);
			this.alt.setText(locValues[2]);
			this.prec.setText(locValues[3]);
			
			if(!locValues[4].equals("{}")) {
				String satString = "";
				String[] sats = locValues[4].substring(1, locValues[4].length() - 1).split("--");
				for(int i = 0; i < sats.length ; i++) {
					String[] satellite = sats[i].substring(1, sats[i].length() - 1).split("##");
					satString += "PRN: " + satellite[0];
					satString += "\tSNR: " + satellite[1] + "\n";
					satString += "Azimuth: " + satellite[2];
					satString += "\tElevation: " + satellite[3] + "\n";
					satString += "hasAl: " + satellite[4];
					satString += "\thasEph: " + satellite[5];
					satString += "\tinFix: " + satellite[6] + "\n\n";
				}
				this.sats.setText(satString);
			}
		}
	}
}
