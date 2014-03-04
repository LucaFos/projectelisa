package com.example.prova;


public class ElisaConnector {
	String target;
	
	ElisaGetMessages[] gets;
	
	ElisaConnector(String t)
	{
		target = t;
	}
	
	public void setTarget(String t)
	{
		target = t;
	}
	
	public void getMessages(double x, double y, double z)
	{
		ElisaGetMessages e = new ElisaGetMessages();
		e.setTarget(target);
		e.execute(x, y, z);
	}
	
}
