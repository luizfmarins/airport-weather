package com.crossover.trial.weather.testIntegration;

import static com.jayway.restassured.RestAssured.get;

import com.crossover.trial.weather.AtmosphericInformation;

public final class WeatherQueryUtil {

	private WeatherQueryUtil() {}
	
	public static AtmosphericInformation[] queryWeather(String airport, double radius) {
		AtmosphericInformation[] as = get("query/weather/" + airport + "/" + radius)
				.as(AtmosphericInformation[].class);
		return as;
	}
}
