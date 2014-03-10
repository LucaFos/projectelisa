package org.reavsoft.writeitapp;

public class ElisaMessage {
	String m_body;
	String m_owner;
	double m_x, m_y, m_z; //latitude, longitude, altitude
	
	/*
	 * Creates a good elisa message object setting all params
	 */
	ElisaMessage(String body, double latitude, double longitude, double altitude, String owner)
	{
		m_owner = owner;
		m_x = latitude;
		m_y = longitude;
		m_z = altitude;
		
		m_body = body;
	}
	
	/*
	 * Empty overloaded constructor for an empty versatile message object
	 */
	ElisaMessage()
	{
	}
	
	/*
	 * All getter/setter-s
	 */
	public void setBody(String body)
	{
		m_body = body;
	}
	
	public String getBody()
	{
		return m_body;
	}
	
	public void setLatitude(int x)
	{
		m_x = x;
	}
	
	public void setLongitude(int y)
	{
		m_y = y;
	}
	
	public void setAltitude(int z)
	{
		m_z = z;
	}
	
	public double getLatitude()
	{
		return m_x;
	}
	
	public double getLongitude()
	{
		return m_y;
	}
	
	public double getAltitude()
	{
		return m_z;
	}

	public void setAuthor(String s) {
		m_owner = s;
	}
	
	public String getAuthor() {
		return m_owner;
	}

}
