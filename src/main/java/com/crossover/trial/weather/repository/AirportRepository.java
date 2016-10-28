package com.crossover.trial.weather.repository;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.crossover.trial.weather.AirportLoader;
import com.crossover.trial.weather.model.Airport;

public abstract class AirportRepository {

	private static final String AIRPORTS_DAT = "airports.dat";
	private static AirportRepository instance;
	private static boolean isInitialized = false;

	public abstract List<Airport> list();
	
	public abstract Airport findByCode(String iataCode);
	
	public abstract void save(Airport airport);
	
	protected abstract void doClear();

	public abstract void delete(Airport airport);
	
	public static AirportRepository getInstance() {
		if (instance == null) {
			instance = new MemoryAirportRepository();
			init();
		}
		return instance;
	}

	private static void init() {
		try {
			doInit();
		} catch (URISyntaxException | IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static void clear() {
		getInstance().doClear();
	}
	
	public static void setInitized(boolean isInitialized) {
		AirportRepository.isInitialized = isInitialized;
	}
	
	private static void doInit() throws URISyntaxException, IOException {
		if (isInitialized) 
			return;
		isInitialized = true;
		
		File file = new File(AirportRepository.class.getResource(AIRPORTS_DAT).toURI().getPath());
		AirportLoader loader = new AirportLoader(file, "http://localhost:9090");
		loader.upload();
	}

}