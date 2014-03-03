package com.example.prova;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
	
	ElisaConnector e = new ElisaConnector(new String("projectelisa.host56.com"));
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void sendMessage(View view) {
        // Do something in response to button
    	LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
    	boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

    	// check if enabled and if not send user to the GSP settings
    	// Better solution would be to display a dialog and suggesting to 
    	// go to the settings
    	if (!enabled) {
    	  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    	  startActivity(intent);
    	}
    	
    	// Define a listener that responds to location updates
    	LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	      makeUseOfNewLocation(location);
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }


	protected void makeUseOfNewLocation(Location location) {
		// TODO Auto-generated method stub
		double latitude = location.getLatitude();
		double altitude = location.getAltitude();
		double longitude = location.getLongitude();
		
		e.spawnGet(latitude, longitude, altitude);
		
	}
    
}
