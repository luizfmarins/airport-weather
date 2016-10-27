package com.crossover.trial.weather.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

	private static final double EARTH_RADIUS_KM = 6372.8;
	
    /** the three letter IATA code */
    private String iata;

    /** latitude value in degrees */
    private double latitude;

    /** longitude value in degrees */
    private double longitude;

    public AirportData() {}
    
    public AirportData(String iata, double latitude, double longitude) {
		this.iata = iata;
		this.latitude = latitude;
		this.longitude = longitude;}

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public boolean equals(Object other) {
        if (other instanceof AirportData) 
            return ((AirportData)other).getIata().equals(this.getIata());

        return false;
    }

	public boolean isWithinRadius(AirportData other, double radius) {
		if (other == null)
			return false;
		
		return calculateDistance(other) <= radius; 
	}
	
	private double calculateDistance(AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - getLongitude());
        double a =  Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c;
    }
}
