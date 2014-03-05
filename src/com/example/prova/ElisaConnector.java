
package com.example.prova;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ElisaConnector {
	//internals
	String target;
	
	ElisaConnector(String t)
	{
		target = t;
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
