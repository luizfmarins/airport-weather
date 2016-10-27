package com.crossover.trial.weather.rest;

import com.crossover.trial.weather.model.Airport;

public final class InitialAirports {

	public static String BOS = "BOS";
	public static String EWR = "EWR";
	public static String JFK = "JFK";
	public static String LGA = "LGA";
	public static String MMU = "MMU";
	
	private InitialAirports() {}
	
	public static Airport bos() {
		return new Airport(BOS, 42.364347, -71.005181);
	}
	
	public static Airport ewr() {
		return new Airport(EWR, 40.6925, -74.168667);
	}
	
	public static Airport jfk() {
		return new Airport(JFK, 40.639751, -73.778925);
	}
	
	public static Airport lga() {
		return new Airport(LGA, 40.777245, -73.872608);
	}
	
	public static Airport mmu() {
		return new Airport(MMU, 40.79935, -74.4148747);
	}
}