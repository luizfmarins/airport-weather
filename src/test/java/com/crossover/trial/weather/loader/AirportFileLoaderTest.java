package com.crossover.trial.weather.loader;

import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.bostonAirport;
import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.file;
import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.newarkAirport;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

public class AirportFileLoaderTest {

	private AirportFileLoader sut;
	
	@Test
	public void readEmptyFile() throws Exception {
		sut = newFileLoader("/no_airport.dat");
		
		List<AirportData> data = sut.load();
		
		assertThat(data, hasSize(0));
	}
	
	@Test
	public void readOneAirport() throws Exception {
		sut = newFileLoader("/one_airport.dat");
		
		List<AirportData> data = sut.load();
		
		assertThat(data, hasSize(1));
		assertThat(data, contains(bostonAirport()));
	}
	
	@Test
	public void readTwoAirport() throws Exception {
		sut = newFileLoader("/two_airports.dat");
		
		List<AirportData> data = sut.load();
		
		assertThat(data, hasSize(2));
		assertThat(data, contains(bostonAirport(), newarkAirport()));
	}
	
	public AirportFileLoader newFileLoader(String file) throws URISyntaxException {
		File resource = file(file);
		return new AirportFileLoader(resource);
	}
}