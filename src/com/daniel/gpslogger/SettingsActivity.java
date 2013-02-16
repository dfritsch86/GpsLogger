package com.daniel.gpslogger;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//This is deprecated, but the andorid-support-v4 has not
		//yet compatibility for Preference fragments
		addPreferencesFromResource(R.xml.preferences);
	}
}


/* should be this, when preferenceFramgemnts are supported in the compatibility

package com.daniel.gpslogger;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//settings bit
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new PreferenceHandler())
		.commit();
	}
	
	public static class PreferenceHandler extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}
}

*/