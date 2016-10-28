package com.crossover.trial.weather.loader;

import com.crossover.trial.weather.model.Airport;

import jersey.repackaged.com.google.common.base.Function;

public class AirportDataConverter implements Function<AirportData, Airport> {

	public static Function<AirportData, Airport> AIPORT_DATA_CONVERTER = new AirportDataConverter();
	
	private AirportDataConverter() {}
	
	@Override
	public Airport apply(AirportData data) {
		return new Airport(data.getIata(), Double.valueOf(data.getLatitude()), Double.valueOf(data.getLongitude()));
	}
}