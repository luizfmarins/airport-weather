package com.crossover.trial.weather.loader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

public class AirportFileLoaderTest {

	private AirportFileLoader sut;
	private File resource;
	
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
		assertThat(data, contains(bostonAirport(),newarkAirport()));
	}

	private AirportData newarkAirport() {
		return new AirportData.Builder()
			.withCity("Newark")
			.withCountry("United States")
			.withIata("EWR")
			.withIcao("KEWR")
			.withLatitude("40.6925")
			.withLongitude("-74.168667")
			.withAltitude("18")
			.withTimezone("-5")
			.withDst("A")
			.build();
	}

	private AirportData bostonAirport() {
		return new AirportData.Builder()
			.withCity("Boston")
			.withCountry("United States")
			.withIata("BOS")
			.withIcao("KBOS")
			.withLatitude("42.364347")
			.withLongitude("-71.005181")
			.withAltitude("19")
			.withTimezone("-5")
			.withDst("A")
			.build();
	}
	
	private AirportFileLoader newFileLoader(String file) throws URISyntaxException {
		String path = getClass().getResource(file).toURI().getPath();
		resource = new File(path);
		return new AirportFileLoader(resource);
	}
}