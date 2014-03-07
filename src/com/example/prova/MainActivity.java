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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TabHost;

public class MainActivity extends Activity {
	
	TabHost tabHost;
	
	ElisaConnector e = new ElisaConnector(new String("projectelisa.host56.com"));
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	
        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        
        TabSpec spec1=tabHost.newTabSpec("TAB 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("TAB 1");
        
        TabSpec spec2=tabHost.newTabSpec("TAB 2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("TAB 2");
        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        
        startPositioning();
        
    	Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edittext = (EditText) findViewById(R.id.editText1);
				String message = edittext.getText().toString();
				if (message.length() != 0) {
					System.out.println("sending message: "+message);
					e.postMessage(message);
					edittext.setText("");
				}
			}
		});
		
		SeekBar skb = (SeekBar) findViewById(R.id.seekBar1);
		skb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch (SeekBar s) {
				double factor = (s.getProgress()+1)/1000000.0;
				e.setFactor(factor);
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void cleanMessages()
    {
    	LinearLayout ll = (LinearLayout) findViewById(R.id.messageLinearLayout);
    	
    	if(((LinearLayout) ll).getChildCount() > 0){ 
    		((LinearLayout) ll).removeAllViews();
    	}
    }
    
    public void refreshMessages()
    {	
    	LinearLayout ll = (LinearLayout) findViewById(R.id.messageLinearLayout);
    	
    	cleanMessages();
    	
    	for(int i=0; i<ElisaConnector.last_messages.size();i++){
    		TextView t = (TextView)getLayoutInflater().inflate(R.layout.message_layout, null);
        	t.setText(ElisaConnector.last_messages.get(i).getBody());
        	
        	TextView divisor = (TextView)getLayoutInflater().inflate(R.layout.divisor_layout, null);
        	
        	ll.addView(t);
        	ll.addView(divisor);
    	}
    }
    
    public void startPositioning() {
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
    	LocationListener networkListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	    	ElisaPositioning.setNetPos(location);
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };
    	
    	// Define a listener that responds to location updates
      	LocationListener gpsListener = new LocationListener() {
      	    public void onLocationChanged(Location location) {
      	      // Called when a new location is found by the network location provider.
      	    	ElisaPositioning.setGpsPos(location);
      	    }

      	    public void onStatusChanged(String provider, int status, Bundle extras) {}

      	    public void onProviderEnabled(String provider) {}

      	    public void onProviderDisabled(String provider) {}
      	  };
    	  
    	// Register the listener with the Location Manager to receive location updates
    	service.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
    	service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
    }
    
}
