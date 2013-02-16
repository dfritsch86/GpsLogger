package com.daniel.gpslogger.modules;

public class SensorType {
	public String featureSensor;
	public String description;
	public int sensorType;
	public boolean isAvailable = false;
	public float[] sensorValues = {0, 0, 0};
	
	public SensorType(String s, int i, String d) {
		this.featureSensor = s;
		this.sensorType = i;
		this.description = d;
	}
}
