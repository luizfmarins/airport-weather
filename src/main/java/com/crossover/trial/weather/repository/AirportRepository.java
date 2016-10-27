package com.crossover.trial.weather.repository;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.model.Airport;

public class AirportRepository {

	private static AirportRepository instance;

	protected static Map<String, Airport> airports = new HashMap<>();
	
	private AirportRepository() {}
	
	public List<Airport> list() {
		return airports.values().stream().collect(toList());
	}
	
	public Airport findByCode(String iataCode) {
        return airports.get(iataCode);
    }
	
	public void save(Airport airport) {
		airports.put(airport.getIata(), airport);
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