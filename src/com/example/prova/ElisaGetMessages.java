package com.example.prova;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

public class ElisaGetMessages extends AsyncTask<Double, Integer, String>
{
	String target = null;
	String response = null;
	
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
				System.out.println(jarr.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String doInBackground(Double... coords) 
	{
	    URL url = null;
		try {
			url = new URL("http://" + target + "/main/get/?x="+
		                   String.valueOf(coords[0])+"&y="+
					       String.valueOf(coords[1])+
					       "&z="+String.valueOf(coords[2]));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	    HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

	    	InputStream in = new BufferedInputStream(conn.getInputStream());
	        response = ElisaUtils.convertStreamToString(in);
	        
		} catch (ProtocolException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return response;
	}
}
