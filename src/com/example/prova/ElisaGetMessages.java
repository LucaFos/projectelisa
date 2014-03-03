package com.example.prova;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
		if(res=="false"){
			//do nothing -- empty results
			//TODO: maybe some clear all?
			System.out.println("[DEBUG]: No messages found.");
		} else {
			System.out.println(res);
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	    try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

	    try {
	    	InputStream in = new BufferedInputStream(conn.getInputStream());
	        response = ElisaUtils.convertStreamToString(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return response;
	}
}
