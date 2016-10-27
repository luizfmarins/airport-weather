package com.crossover.trial.weather.repository;

import java.util.List;

import com.crossover.trial.weather.model.Airport;

public abstract class AirportRepository {

	private static AirportRepository instance;

	public abstract List<Airport> list();
	
	public abstract Airport findByCode(String iataCode);
	
	public abstract void save(Airport airport);
	
	protected abstract void doClear();

	public static AirportRepository getInstance() {
		if (instance == null)
			instance = new MemoryAirportRepository();
		return instance;
	}
	
	public static void clear() {
		instance.doClear();
	}
}