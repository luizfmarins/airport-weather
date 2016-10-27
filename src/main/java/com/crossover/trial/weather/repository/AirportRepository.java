package com.crossover.trial.weather.repository;

import java.util.ArrayList;
import java.util.List;

import com.crossover.trial.weather.model.Airport;

public class AirportRepository {

	private static AirportRepository instance;

	protected static List<Airport> airports = new ArrayList<>();
	
	private AirportRepository() {}
	
	public List<Airport> list() {
		return airports;
	}
	
	public Airport findByCode(String iataCode) {
        return airports.stream()
            .filter(ap -> ap.getIata().equals(iataCode))
            .findFirst().orElse(null);
    }
	
	public void save(Airport airport) {
		airports.add(airport);
	}

	public static AirportRepository getInstance() {
		if (instance == null)
			instance = new AirportRepository();
		return instance;
	}
	
	public static void clear() {
		airports.clear();
	}
}