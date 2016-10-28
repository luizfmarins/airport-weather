package com.crossover.trial.weather.loader;

import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.assertBostonAirport;
import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.assertNewarkAirport;
import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.file;
import static com.crossover.trial.weather.repository.InitialAirports.BOS;
import static com.crossover.trial.weather.repository.InitialAirports.EWR;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.getAirport;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.getAirportAssertNotFound;

import org.junit.Before;
import org.junit.Test;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.repository.AirportRepository;
import com.crossover.trial.weather.rest.RestTestBase;

public class AirportLoaderTest extends RestTestBase {

	private AirportLoader loader;
	
	@Test
	public void loadOneAirport() throws Exception {
		getAirportAssertNotFound(BOS);

		loader = new AirportLoader(file("/one_airport.dat"), "http://localhost:9090");
		loader.upload();
		
		Airport airport = getAirport(BOS);
		
		assertBostonAirport(airport);
	}
	
	@Test
	public void loadTwoAirport() throws Exception {
		getAirportAssertNotFound(BOS);
		
		loader = new AirportLoader(file("/two_airports.dat"), "http://localhost:9090");
		loader.upload();
		
		Airport bos = getAirport(BOS);
		Airport ewr = getAirport(EWR);
		
		assertBostonAirport(bos);
		assertNewarkAirport(ewr);
	}
	
	@Before
	public void before() {
		AirportRepository.rellyClear();
	}
	
}
