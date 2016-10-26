package com.crossover.trial.weather.testIntegration;

import static com.crossover.trial.weather.InitialAirports.BOS;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.trial.weather.DataPoint;
import com.crossover.trial.weather.DataPointType;
import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;

public class RestWeatherCollectorEndpointTestIntegration extends TestBase {

	private Gson gson = new Gson();
	
	@Test
	public void ping() throws Exception {
		get("/ping")
			.then().assertThat()
				.statusCode(200)
				.body(equalTo("ready"));
	}
	
	@Test
	public void updateWeather() {
		given()
			.contentType(JSON)
			.body(new DataPoint.Builder()
	                .withFirst(0).withLast(10).withMean(6).withMedian(4).withCount(20)
	                .build())
	    .when().post("/weather/" + BOS + "/" + DataPointType.WIND.name())
	    .then().assertThat()
	    .statusCode(200);
	}

	@BeforeClass
	public static void setupBasePath() {
		RestAssured.basePath = "/collect";
	}
}