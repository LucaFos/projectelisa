
package com.example.prova;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ElisaConnector {
	//internals
	String target;
	double factor;
	
	public static ArrayList<ElisaMessage> last_messages = new ArrayList<ElisaMessage>();
	
	ElisaConnector(String t)
	{
		target = t;
	}
	
	public void setFactor(double f)
	{
		factor = f;
	}
	
	public void setTarget(String t)
	{
		target = t;
	}
	
	public void getMessages()
	{
		ElisaGetMessages e = new ElisaGetMessages();
		//executing
		e.setTarget(target);
		e.setFactor(factor);
		e.execute();
	}
	
	public void postMessage(String message)
	{
		try {
			ElisaPostMessage e = new ElisaPostMessage();
			//executing
			e.setTarget(target);
			e.setParameters("body="+URLEncoder.encode(message, "UTF-8"));
			e.execute();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
