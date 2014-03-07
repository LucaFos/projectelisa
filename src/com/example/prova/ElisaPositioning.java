package com.example.prova;

import java.util.Date;

import android.location.Location;

public class ElisaPositioning {
	
	public static double net_latitude = 0;
	public static double net_longitude = 0;
	public static double net_altitude = 0;
	
	public static double gps_latitude = 0;
	public static double gps_longitude = 0;
	public static double gps_altitude = 0;
	
	private static long net_time = 0;
	private static long gps_time = 0;
	
	public static void setNetPos(Location location)
	{
		ElisaPositioning.net_latitude = location.getLatitude();
		ElisaPositioning.net_longitude = location.getLongitude();
		ElisaPositioning.net_altitude = location.getAltitude();
		
		Date date = new Date();
	    ElisaPositioning.net_time = date.getTime();
	}
	
	public static void setGpsPos(Location location)
	{	
		ElisaPositioning.gps_latitude = location.getLatitude();
		ElisaPositioning.gps_longitude = location.getLongitude();
		ElisaPositioning.gps_altitude = location.getAltitude();
		
		Date date = new Date();
	    ElisaPositioning.gps_time = date.getTime();
	}
	
	public static double[] getPos()
	{
		double[] res = new double[2];
		//TODO: interpolate wifi/cell with gps
		
		// yes / yes
		if(gps_time!=0 && net_time!=0){
			if(gps_time<30){
				res[0] = gps_latitude;
				res[1] = gps_longitude;
				res[2] = gps_altitude;
			} else {
				res[0] = net_latitude;
				res[1] = net_longitude;
				res[2] = net_altitude;
			}
		}
		
		// no  / yes
		if(gps_time==0 && net_time!=0){
			res[0] = net_latitude;
			res[1] = net_longitude;
			res[2] = net_altitude;
		}
		
		// yes / no
		if(gps_time!=0 && net_time==0){
			res[0] = gps_latitude;
			res[1] = gps_longitude;
			res[2] = gps_altitude;
		}
		
		// no  / no
		if(gps_time==0 && net_time==0){
			res[0] = 0;
			res[1] = 0;
			res[2] = 0;
		}
		return res;
	}
	
}
