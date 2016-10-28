package com.crossover.trial.weather.repository;

import static com.crossover.trial.weather.repository.InitialAirports.bos;
import static com.crossover.trial.weather.repository.InitialAirports.ewr;
import static com.crossover.trial.weather.repository.InitialAirports.jfk;
import static com.crossover.trial.weather.repository.InitialAirports.lga;
import static com.crossover.trial.weather.repository.InitialAirports.mmu;

import java.util.List;

import com.crossover.trial.weather.model.Airport;

public abstract class AirportRepository {

	private static AirportRepository instance;

	public abstract List<Airport> list();
	
	public abstract Airport findByCode(String iataCode);
	
	public abstract void save(Airport airport);
	
	protected abstract void doClear();

	public abstract void delete(Airport airport);
	
	public static AirportRepository getInstance() {
		if (instance == null)
			instance = new MemoryAirportRepository();
		return instance;
	}

	static {
		init();
	}
	
	public static void clear() {
		getInstance().doClear();
		init();
	}
	
	private static void init() {
        AirportRepository airportRepository = AirportRepository.getInstance();
        airportRepository.save(bos());
        airportRepository.save(ewr());
        airportRepository.save(jfk());
        airportRepository.save(lga());
        airportRepository.save(mmu());
	}
}