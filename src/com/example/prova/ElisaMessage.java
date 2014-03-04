package com.example.prova;

public class ElisaMessage {
	String m_body;
	int m_owner;
	int m_x, m_y, m_z; //latitude, longitude, altitude
	
	/*
	 * Creates a good elisa message object setting all params
	 */
	ElisaMessage(String body, int x, int y, int z, int owner)
	{
		m_owner = owner;
		m_x = x;
		m_y = y;
		m_z = z;
		
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
	
	public int getLatitude()
	{
		return m_x;
	}
	
	public int getLongitude()
	{
		return m_y;
	}
	
	public int getAltitude()
	{
		return m_z;
	}

}
