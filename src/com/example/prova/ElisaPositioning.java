package com.example.prova;

public class ElisaPositioning {
	
	public static double latitude = 0;
	public static double longitude = 0;
	public static double altitude = 0;
	
	public static void setPos(double x, double y, double z)
	{
		ElisaPositioning.latitude = x;
		ElisaPositioning.longitude = y;
		ElisaPositioning.altitude = z;
	}

}
