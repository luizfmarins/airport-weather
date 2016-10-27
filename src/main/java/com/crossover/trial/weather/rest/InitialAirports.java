package com.crossover.trial.weather.rest;

import com.crossover.trial.weather.model.AirportData;

public final class InitialAirports {

	public static String BOS = "BOS";
	public static String EWR = "EWR";
	public static String JFK = "JFK";
	public static String LGA = "LGA";
	public static String MMU = "MMU";
	
	private InitialAirports() {}
	
	public static AirportData bos() {
		return new AirportData(BOS, 42.364347, -71.005181);
	}
	
	public static AirportData ewr() {
		return new AirportData(EWR, 40.6925, -74.168667);
	}
	
	public static AirportData jfk() {
		return new AirportData(JFK, 40.639751, -73.778925);
	}
	
	public static AirportData lga() {
		return new AirportData(LGA, 40.777245, -73.872608);
	}
	
	public static AirportData mmu() {
		return new AirportData(MMU, 40.79935, -74.4148747);
	}
}