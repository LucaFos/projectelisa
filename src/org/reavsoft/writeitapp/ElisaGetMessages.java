package org.reavsoft.writeitapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;

public class ElisaGetMessages extends AsyncTask<Void, Integer, String>
{
	String target = null;
	String response = null;
	double factor = 0.00004;
	Handler handler = null;
	
	public void setHandler(Handler h)
	{
		handler = h;
	}
	
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
		if(res != null){
			if(res.equals("false")){
				//do nothing -- empty results
				//TODO: maybe some clear all?
				System.out.println("[DEBUG]: No messages found.");
				ArrayList<ElisaMessage> messages = new ArrayList<ElisaMessage>();
				ElisaConnector.last_messages = messages;
				
				//sending message to MainActivity
				handler.sendEmptyMessage(0);
			} else {
				//TODO: parse json in order to get message list
				try {
					JSONArray jarr = new JSONArray(res);
					
					ArrayList<ElisaMessage> messages = new ArrayList<ElisaMessage>();
					
					for (int i = 0; i < jarr.length(); i++) {
						JSONObject obj = jarr.getJSONObject(i);
						
						String body = obj.getString("body");
						String owner = obj.getString("owner");
						double latitude = Double.parseDouble(obj.getString("x"));
						double longitude = Double.parseDouble(obj.getString("y"));
						double altitude = Double.parseDouble(obj.getString("z"));
						
						messages.add(new ElisaMessage(body, latitude, longitude, altitude, owner));
					}
					
					
					if(messages.size()>0){
						ElisaConnector.last_messages = messages;
						handler.sendEmptyMessage(0);
					} else {
						System.out.println("[DEBUG]: server issue... retrying in a few seconds");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("[DEBUG]: server is probably busy... retrying in a few seconds");
				}
			}
		}
	}
	
	@Override
	protected String doInBackground(Void...coords) 
	{	
		double x = ElisaPositioning.getLatitude();
		double y = ElisaPositioning.getLongitude();
		double z = ElisaPositioning.getAltitude();
		
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
