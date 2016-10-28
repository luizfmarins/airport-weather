package com.crossover.trial.weather.loader;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URISyntaxException;

import com.crossover.trial.weather.model.Airport;

public final class AirportLoaderTestUtil {

	private AirportLoaderTestUtil() {}
	
	public static AirportData newarkAirport() {
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

	public static AirportData bostonAirport() {
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
	
	public static File file(String file) throws URISyntaxException {
		String path = AirportFileLoaderTest.class.getResource(file).toURI().getPath();
		File resource = new File(path);
		return resource;
	}
	
	public static void assertBostonAirport(Airport airport) {
		assertThat(airport.getIata(), equalTo("BOS"));
		assertThat(airport.getLatitude(), equalTo(42.364347));
		assertThat(airport.getLongitude(), equalTo(-71.005181));
	}
	
	public static void assertNewarkAirport(Airport airport) {
		assertThat(airport.getIata(), equalTo("EWR"));
		assertThat(airport.getLatitude(), equalTo(40.6925));
		assertThat(airport.getLongitude(), equalTo(-74.168667));
	}
	
}
