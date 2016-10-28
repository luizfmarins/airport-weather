package com.crossover.trial.weather.util;

import static com.crossover.trial.weather.util.InitialAirports.bos;
import static com.crossover.trial.weather.util.InitialAirports.ewr;
import static com.crossover.trial.weather.util.InitialAirports.jfk;
import static com.crossover.trial.weather.util.InitialAirports.lga;
import static com.crossover.trial.weather.util.InitialAirports.mmu;

import com.crossover.trial.weather.repository.AirportRepository;

public class Fixture {

	private Fixture() {}

	public static void setup() {
		tearDown();
		AirportRepository airportRepository = AirportRepository.getInstance();
		airportRepository.save(bos());
		airportRepository.save(ewr());
		airportRepository.save(jfk());
		airportRepository.save(lga());
		airportRepository.save(mmu());

	}

	public static void tearDown() {
		AirportRepository.clear();
		AirportRepository.setInitized(true);
	}
}