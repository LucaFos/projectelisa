package org.reavsoft.writeitapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.os.AsyncTask;

public class ElisaPostMessage extends AsyncTask<Void, Integer, Integer>
{
	String target = null;
	String response = null;
	String params = null;
	
	public void setTarget(String t)
	{
		target = t;
	}
	
	public void setParameters(String p)
	{
		params = p;
	}
	
	protected void onPostExecute(int res)
	{
		if(res==1){
			System.out.println("[DEBUG]: message successfully inserted");
		} else {
			System.out.println("[DEBUG]: there were errors inserting messages");
		}
	}
	
	@Override
	protected Integer doInBackground(Void... coords) 
	{	
		double x = ElisaPositioning.getLatitude();
		double y = ElisaPositioning.getLongitude();
		double z = ElisaPositioning.getAltitude();
		
	    URL url = null;
		try {
			url = new URL("http://" + target + "/main/post/?x="+String.valueOf(x)+"&y="+String.valueOf(y)+"&z="+String.valueOf(z)+"&owner="+ElisaUser.username);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		try {
		    HttpURLConnection conn = null;
		    conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", 
			   "application/x-www-form-urlencoded");
					
			conn.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
			conn.setUseCaches (false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			//Send request
			DataOutputStream wr = new DataOutputStream (conn.getOutputStream ());
			wr.writeBytes (params);
			wr.flush ();
			wr.close ();
			
			  //Get Response	
			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			
			conn.disconnect();
	        
		} catch (ProtocolException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 1;
	}

}
