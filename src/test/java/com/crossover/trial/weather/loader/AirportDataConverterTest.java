package com.crossover.trial.weather.loader;

import static com.crossover.trial.weather.loader.AirportDataConverter.AIPORT_DATA_CONVERTER;
import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.assertBostonAirport;
import static com.crossover.trial.weather.loader.AirportLoaderTestUtil.bostonAirport;

import org.junit.Test;

import com.crossover.trial.weather.model.Airport;

import jersey.repackaged.com.google.common.base.Function;

public class AirportDataConverterTest {

	private Function<AirportData, Airport> sut = AIPORT_DATA_CONVERTER;
	
	@Test
	public void apply() throws Exception {
		Airport airport = sut.apply(bostonAirport());
		
		assertBostonAirport(airport);
	}
}