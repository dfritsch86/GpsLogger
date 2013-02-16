package com.daniel.gpslogger.modules;

public interface ModuleInterface {
	public void startModuleMeasurement();
	public void stopModuleMeasurement();
	public Boolean checkModuleAvailability();
	public String getModuleMeasurement();
	
}
