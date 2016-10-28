package com.crossover.trial.weather.repository;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.model.Airport;

class MemoryAirportRepository extends AirportRepository {

	private static Map<String, Airport> airports = new HashMap<>();
	
	@Override
	public List<Airport> list() {
		return airports.values().stream().collect(toList());
	}
	
	@Override
	public Airport findByCode(String iataCode) {
        return airports.get(iataCode);
    }
	
	@Override
	public void save(Airport airport) {
		airports.put(airport.getIata(), airport);
	}

	@Override
	public void delete(Airport airport) {
		airports.remove(airport.getIata());
	}
	
	@Override
	protected void doClear() {
		airports.clear();
	}
}