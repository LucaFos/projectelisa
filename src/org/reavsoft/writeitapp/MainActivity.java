package org.reavsoft.writeitapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import org.reavsoft.writeitapp.R;

import com.facebook.*;
import com.facebook.model.*;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TabHost;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	
	TabHost tabHost; 
	
	ElisaConnector e = new ElisaConnector(new String("projectelisa.host56.com"));
	
	public static Handler mainHandler;
	
	class RefreshMessagesTask extends TimerTask {
		public void run() {
			//stuff executed every 5 seconds
			System.out.println("[DEBUG]: executing task...");
			e.getMessages();
			
			/*
			//this has to be done because only the original thread that
			//created a view hierarchy can touch its views.
			runOnUiThread(new Runnable(){
	              @Override
	              public void run(){
	            	  //refreshMessages();
	              }
	           });
	        */
			
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	
        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        
        TabSpec spec1=tabHost.newTabSpec("TAB 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("TAB 1");
        
        TabSpec spec2=tabHost.newTabSpec("INFO");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("INFO");
        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        
        mainHandler = new Handler() {
            @Override
            public void handleMessage (Message msg) {
            	//TODO: differentiate between messages
                refreshMessages();
            }
        };
        
        e.setHandler(mainHandler);
        
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
				//System.out.println("factor is: "+String.valueOf(factor));
				
				e.setFactor(ElisaPositioning.getFactor(s.getProgress()));
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
		
		RefreshMessagesTask myTask = new RefreshMessagesTask();
        Timer myTimer = new Timer();
 
        myTimer.schedule(myTask, 1000, 5000);
        
        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {
        
        // callback when session changes state
        @SuppressWarnings("deprecation")
		@Override
          public void call(Session session, SessionState state, Exception exception) {
        	  if (session.isOpened()) {
        		  
        		  // make request to the /me API
	    		  Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
	
	    		    // callback after Graph API response with user object
	    		    @Override
	    		    public void onCompleted(GraphUser user, Response response) {
	    		    	if (user != null) {
	    		    		TextView welcome = (TextView) findViewById(R.id.welcome);
	    		    		welcome.setText("Hello " + user.getName() + "!");
	    		    		
	    		    		final String userFacebookId = user.getId();
	    		    		
	    		    		//loading user data in specific Elisa class
	    		    		ElisaUser.name = user.getName();
	    		    		ElisaUser.username = user.getUsername();
	    		    		ElisaUser.id = user.getId();
	    		    		
	    		    		
		    				new AsyncTask<Void, Void, Bitmap>()
		    				{
	    		    			@Override
	    		    			protected Bitmap doInBackground(Void... params)
	    		    			{
	    		    				if (userFacebookId == null){ return null; }

	    		    				String url = String.format("https://graph.facebook.com/%s/picture", userFacebookId);

	    		    				InputStream inputStream = null;
									try {
										inputStream = new URL(url).openStream();
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	    		    				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

	    		    				return bitmap;
	    		    			}

	    		    			@Override
	    		    			protected void onPostExecute(Bitmap bitmap)
	    		    			{
		    				      // safety check
	    		    				if (bitmap != null && !isFinishing())
	    		    				{
	    		    					  // do what you need to do with the bitmap :)
	    		    					ImageView profile_picture = (ImageView) findViewById(R.id.profile_picture);
	    		    					profile_picture.setImageBitmap(bitmap);
    		    			  		}
	    		    			}
	    		    			
		    				}.execute();
	    		    	}}
	    		  });
        	  }
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
                
                TextView author = (TextView)getLayoutInflater().inflate(R.layout.author_layout, null);
                author.setText("   written by "+ElisaConnector.last_messages.get(i).getAuthor());
                
                //Button button = (Button) findViewById(R.layout.buttons_layout);
                LinearLayout buttons = (LinearLayout)getLayoutInflater().inflate(R.layout.buttons_layout, null);
                
                TextView divisor = (TextView)getLayoutInflater().inflate(R.layout.divisor_layout, null);
                
                ll.addView(t);
                ll.addView(author);
                ll.addView(buttons);
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
    
    //facebook integration specific parts
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
    
}
