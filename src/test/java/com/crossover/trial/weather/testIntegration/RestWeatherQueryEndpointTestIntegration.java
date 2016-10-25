package com.crossover.trial.weather.testIntegration;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.registerParser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;

public class RestWeatherQueryEndpointTestIntegration extends TestBase {

	@Test
	public void ping() {
		get("/ping")
		.then().assertThat()
			.statusCode(200)
			.body("datasize", equalTo(0))
			.body("iata_freq.EWR", equalTo(0.0f))
			.body("iata_freq.BOS", equalTo(0.0f))
			.body("iata_freq.LGA", equalTo(0.0f))
			.body("iata_freq.JFK", equalTo(0.0f))
			.body("iata_freq.MMU", equalTo(0.0f))
			.body("radius_freq", hasSize(1001));
	}
	
	@BeforeClass
	public static void setupBasePath() {
		registerParser("text/plain", Parser.JSON);
		RestAssured.basePath = "/query";
	}
}