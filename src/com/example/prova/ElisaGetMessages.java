package com.example.prova;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ElisaGetMessages extends AsyncTask<Void, Integer, String>
{
	String target = null;
	String response = null;
	double factor = 0.00004;
	
	public void setFactor(double f)
	{
		factor = f;
	}
	
	public void setTarget(String t)
	{
		target = t;
	}
	
	protected void onPostExecute(String res)
	{
		if(res.equals("false")){
			//do nothing -- empty results
			//TODO: maybe some clear all?
			System.out.println("[DEBUG]: No messages found.");
		} else {
			//TODO: parse json in order to get message list
			try {
				JSONArray jarr = new JSONArray(res);
				
				//TODO: remove me asap!!
				System.out.println(jarr.toString());
				
				List<ElisaMessage> messages = new ArrayList<ElisaMessage>();
				
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject obj = jarr.getJSONObject(i);
					
					String body = obj.getString("body");
					double latitude = Double.parseDouble(obj.getString("x"));
					double longitude = Double.parseDouble(obj.getString("y"));
					double altitude = Double.parseDouble(obj.getString("z"));
					
					messages.add(new ElisaMessage(body, latitude, longitude, altitude, -1));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String doInBackground(Void...coords) 
	{
		double x = ElisaPositioning.latitude;
		double y = ElisaPositioning.longitude;
		double z = ElisaPositioning.altitude;
		
	    URL url = null;
		try {
			url = new URL("http://" + target + "/main/get/?x="+String.valueOf(x)+"&y="+String.valueOf(y)+"&z="+String.valueOf(z)+"&f="+String.valueOf(factor));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	    HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

	    	InputStream in = new BufferedInputStream(conn.getInputStream());
	        response = ElisaUtils.convertStreamToString(in);
	        
	        conn.disconnect();
	        
		} catch (ProtocolException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return response;
	}

}
