package com.example.prova;

import java.util.Date;

import android.location.Location;

public class ElisaPositioning {
	
	public static double opt_latitude = 0;
	public static double opt_longitude = 0;
	public static double opt_altitude = 0;
	
	public static double net_latitude = 0;
	public static double net_longitude = 0;
	public static double net_altitude = 0;
	
	public static double gps_latitude = 0;
	public static double gps_longitude = 0;
	public static double gps_altitude = 0;
	
	private static long net_time = 0;
	private static long gps_time = 0;
	
	private static double factor = 0.00004;
	
	/*
	 * 1: NETWORK
	 * 2: GPS
	 */
	public static int status = 1;
	
	public static enum Connection {GPS,NET};
	
	public static void setNetPos(Location location)
	{
		System.out.println("updated net pos...");
		ElisaPositioning.net_latitude = location.getLatitude();
		ElisaPositioning.net_longitude = location.getLongitude();
		ElisaPositioning.net_altitude = location.getAltitude();
		
		Date date = new Date();
	    ElisaPositioning.net_time = date.getTime();
	    
	    updatePos();
	    updateFac();
	}
	
	public static void setGpsPos(Location location)
	{	
		System.out.println("updated gps pos...");
		ElisaPositioning.gps_latitude = location.getLatitude();
		ElisaPositioning.gps_longitude = location.getLongitude();
		ElisaPositioning.gps_altitude = 0;
		
		//TODO: altitude needs to be fixed with gps active, really critical
		//		keeping  for compatibility with NETWORK_PROVIDER
		
		Date date = new Date();
	    ElisaPositioning.gps_time = date.getTime();
	    
	    updatePos();
	    updateFac();
	}
	
	public static void updateFac()
	{
		if(ElisaPositioning.status==2){
			ElisaPositioning.factor = 0.000001;
		} else {
			ElisaPositioning.factor = 0.00001;
		}
	}
	
	public static double getFactor(int value)
	{
		return (value+1)*ElisaPositioning.factor;
	}
	
	public static void printGpsPos()
	{
		System.out.println("("+String.valueOf(ElisaPositioning.gps_latitude)+","+String.valueOf(ElisaPositioning.gps_longitude)+","+String.valueOf(ElisaPositioning.gps_altitude)+")");
	}
	
	public static void printNetPos()
	{
		System.out.println("("+String.valueOf(ElisaPositioning.net_latitude)+","+String.valueOf(ElisaPositioning.net_longitude)+","+String.valueOf(ElisaPositioning.net_altitude)+")");
	}
	
	public static void updatePos()
	{
		Date date = new Date();
	    long time = date.getTime();
		
		//TODO: interpolate wifi/cell with gps
		
		// yes / yes
		if(gps_time!=0 && net_time!=0){
			if(time-gps_time<15){
				ElisaPositioning.opt_latitude = gps_latitude;
				ElisaPositioning.opt_longitude = gps_longitude;
				ElisaPositioning.opt_altitude = gps_altitude;
				
				ElisaPositioning.status = 2;
			} else {
				ElisaPositioning.opt_latitude = net_latitude;
				ElisaPositioning.opt_longitude = net_longitude;
				ElisaPositioning.opt_altitude = net_altitude;
				
				ElisaPositioning.status = 1;
			}
		}
		
		// no  / yes
		if(gps_time==0 && net_time!=0){
			ElisaPositioning.opt_latitude = net_latitude;
			ElisaPositioning.opt_longitude = net_longitude;
			ElisaPositioning.opt_altitude = net_altitude;
			
			ElisaPositioning.status = 1;
		}
		
		// yes / no
		if(gps_time!=0 && net_time==0){
			ElisaPositioning.opt_latitude = gps_latitude;
			ElisaPositioning.opt_longitude = gps_longitude;
			ElisaPositioning.opt_altitude = gps_altitude;
			
			ElisaPositioning.status = 2;
		}
		
		// no  / no
		if(gps_time==0 && net_time==0){
			ElisaPositioning.opt_latitude = 0;
			ElisaPositioning.opt_longitude = 0;
			ElisaPositioning.opt_altitude = 0;
			
			ElisaPositioning.status = 1;
		}
	}
	
	public static double getLatitude()
	{
		return ElisaPositioning.opt_latitude;
	}
	
	public static double getLongitude()
	{
		return ElisaPositioning.opt_longitude;
	}
	
	public static double getAltitude()
	{
		return ElisaPositioning.opt_altitude;
	}
	
}
