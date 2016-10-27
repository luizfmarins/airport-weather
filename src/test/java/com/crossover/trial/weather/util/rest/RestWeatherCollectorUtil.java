package com.crossover.trial.weather.util.rest;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.OK;

import com.crossover.trial.weather.model.datapoint.DataPoint;
import com.crossover.trial.weather.model.datapoint.DataPointType;

public final class RestWeatherCollectorUtil {

	private RestWeatherCollectorUtil () {}
	
	public static void updateWeather(String airport, DataPointType.Type type, DataPoint datapoint) {
		given().contentType(JSON)
			.body(datapoint)
			.when().post("/collect/weather/" + airport + "/" + type.name())
			.then().assertThat().statusCode(OK.getStatusCode());
	}
	
}
