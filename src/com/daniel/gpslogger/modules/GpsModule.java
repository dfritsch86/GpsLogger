package com.daniel.gpslogger.modules;

import com.daniel.gpslogger.DataLogger;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


public class GpsModule extends Service implements ModuleInterface, LocationListener, GpsStatus.Listener, NmeaListener {
	
	//*****************************************
	//*               Fields                  *
	//*****************************************
	private Context context;
	private LocationManager lm;
	private String lp;
	private Boolean canLocate = false;
	private GpsStatus gpsStatus;
	private Location location;
	private DataLogger dl;
	
	
	public GpsModule(Context c, DataLogger dl) {
		this.context = c;
		this.lm = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
		this.lp = LocationManager.GPS_PROVIDER;
		this.dl = dl;
	}
	
	@Override
	public void startModuleMeasurement() {
		
		if(lm == null) {
			this.lm = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
		}
		Log.i("GPSModule", "Start Locating");
		lm.requestLocationUpdates(lp, 0, 0, this);
		lm.addNmeaListener(this);
		lm.addGpsStatusListener(this);
	}
	
	@Override
	public void stopModuleMeasurement() {
		Log.i("GPSModule", "Stop Locating");
		if(lm != null){
            lm.removeUpdates(this);
            lm.removeGpsStatusListener(this);
            lm.removeNmeaListener(this);
            lm = null;
        }
	}
	
	@Override
	public Boolean checkModuleAvailability() {
		this.checkProviderAvailability();
		return this.canLocate;
	}
	
	@Override
	public String getModuleMeasurement() {
		
		String resultString = "{";
		String separator = "##";
		String separator2 = "**";
		String separator3 = "--";
		
		if(this.location != null) {
			resultString += 
					Double.toString(this.location.getLatitude()) + separator2 +
					Double.toString(this.location.getLongitude()) +  separator2 +
					Double.toString(this.location.getAltitude()) +  separator2 +
					Double.toString(this.location.getAccuracy()) + separator2 + 
					"{";
			
			int numOfSats = 0;
			if(this.gpsStatus.getSatellites() != null) {
				for(GpsSatellite sat : this.gpsStatus.getSatellites()) {
					numOfSats++;
					resultString +=
							"{" +
									sat.getPrn() + separator +
									sat.getSnr() + separator +
									sat.getAzimuth() + separator +
									sat.getElevation() + separator +
									sat.hasAlmanac() + separator +
									sat.hasEphemeris() + separator +
									sat.usedInFix() + "}" + separator3;
				}
				//Removing last "##" for the last satellite
				if(numOfSats > 0){
					resultString = resultString.substring(0, resultString.length()-separator3.length());
				}
			}
			resultString += "}";
		}
		resultString += "}";
		return resultString;
		
	}
	
	
	private void checkProviderAvailability() {
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.w("GPSModule", "gps not available");
			this.canLocate = false;
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
	        alertDialog.setTitle("GPS settings");
	        alertDialog.setMessage("GPS is not enabled. Do you want to go to the settings menu?");
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                context.startActivity(intent);
	            }
	        });
	 
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	        
	        alertDialog.show();
			
		} else {
			this.canLocate = true;
		}
	}
	
	
	//*****************************************
	//*    Interface and/or Listener Methods   *
	//*****************************************
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i("GPSModule", "Location Changed");
		this.location = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onGpsStatusChanged(int event) {
		switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i("GPSModule", "GPS event EVENT STARTED");
				break;
	        case GpsStatus.GPS_EVENT_STOPPED:
	        	Log.i("GPSModule", "GPS event EVENT STOPPED");
	        	break;
	        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	        	//Log.i("GPSModule", "GPS event SATELLITE STATUS");
	        	this.gpsStatus = lm.getGpsStatus(null);
	        	this.dl.collectData();
	            break;
	        case GpsStatus.GPS_EVENT_FIRST_FIX:
	        	Log.i("GPSModule", "GPS event FIRST FIX");
	        	this.showMessage("GPS Fix received");
	        	break;
		}	
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmeaSentence) {
		//Log.i("GPSModule", " NMEA sentence received");
		this.dl.collectNmea(timestamp, nmeaSentence);
	}
	
	
	//*****************************************
	//*    Helper Methods, Getters, Setter    *
	//*****************************************
	private void showMessage(String m) {
		Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
	}
	
}
