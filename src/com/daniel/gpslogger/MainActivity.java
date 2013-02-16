package com.daniel.gpslogger;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private DataLogger dl;
	private ToggleButton tb1, tb2;
	private ProgressBar pb1;
	private DataDisplayer dd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dl = new DataLogger(this);
		dd = new DataDisplayer(this);
		tb1 = (ToggleButton) this.findViewById(R.id.button1);
		tb2 = (ToggleButton) this.findViewById(R.id.button2);
		pb1 = (ProgressBar) this.findViewById(R.id.progressBar1);
		pb1.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		tb1.setEnabled(this.dl.checkDataModulesAvalability());
		tb2.setEnabled(tb1.isChecked());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.dl.stopLogging();
		this.dl.stopGettingData();
	}
	
	public void toggleMeasuring(View v) {
		if(tb1.isChecked()) {
			this.dl.startGettingData();
			this.tb2.setEnabled(true);
			pb1.setVisibility(View.VISIBLE);
		} else {
			if(tb2.isChecked()) {
				tb1.setChecked(true);
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		        alertDialog.setTitle("Stop Measuring");
		        alertDialog.setMessage("Are you really sure you want to stop Measuring?\nYou will also stop Logging");
		        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	tb1.setChecked(false);
		            	
		            	pb1.setVisibility(View.INVISIBLE);
		    			tb2.setChecked(false);
		    			tb2.setEnabled(false);
		    			if(dl.isLogging()) {
		    				dl.stopLogging();
		    			}
		    			dl.stopGettingData();
		            }
		        });
		        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	dialog.cancel();
		            }
		        });
		        alertDialog.show();
				
			} else {
				pb1.setVisibility(View.INVISIBLE);
				this.tb2.setChecked(false);
				this.tb2.setEnabled(false);
				if(this.dl.isLogging()) {
					this.dl.stopLogging();
				}
				this.dl.stopGettingData();
			}
		}
	}
	
	public void toggleLogging(View v) {
		if(tb2.isChecked()) {
			this.dl.startLogging();
		} else {
			
			tb2.setChecked(true);
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	        alertDialog.setTitle("Stop Logging");
	        alertDialog.setMessage("Are you really sure you want to stop Logging?");
	        alertDialog.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	            	tb2.setChecked(false);
	            	dl.stopLogging();
	            }
	        });
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	dialog.cancel();
	            }
	        });
	        alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Intent intent = new Intent(this, SettingsActivity.class);
	    startActivity(intent);
		return false;
	}
	
	public void updateView(String xml){
		this.dd.updateView(xml);
		
	}

}
