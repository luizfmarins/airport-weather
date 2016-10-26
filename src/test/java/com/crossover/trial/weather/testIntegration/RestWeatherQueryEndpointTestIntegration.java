package com.crossover.trial.weather.testIntegration;

import static com.crossover.trial.weather.InitialAirports.BOS;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.registerParser;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.trial.weather.AtmosphericInformation;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;

import jersey.repackaged.com.google.common.collect.Lists;

public class RestWeatherQueryEndpointTestIntegration extends TestBase {

	private static final AtmosphericInformation EMPTY_ATMOSPHERIC_INFORMATION = new AtmosphericInformation();

	// TODO Rurring the whole class breaks tests
	@Test
	public void ping_noPreviousWeatherQueries() {
		get("/ping")
			.then().assertThat()
			.statusCode(200)
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(0.0f)) 
			.body("iata_freq.EWR", equalTo(0.0f)) 
			.body("iata_freq.LGA", equalTo(0.0f)) 
			.body("iata_freq.JFK", equalTo(0.0f)) 
			.body("iata_freq.MMU", equalTo(0.0f)) 
			.body("radius_freq", hasSize(1001));
	}
	
	@Test
	public void ping_oneQueryForBOS_radius0() {
		get("/weather/" + BOS + "/0");
		
		get("/ping")
			.then().assertThat()
			.statusCode(200)
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(1.0f)) 
			.body("iata_freq.EWR", equalTo(0.0f)) 
			.body("iata_freq.LGA", equalTo(0.0f)) 
			.body("iata_freq.JFK", equalTo(0.0f)) 
			.body("iata_freq.MMU", equalTo(0.0f))
			.body("radius_freq", hasSize(1))
			.body("radius_freq", containsInAnyOrder(0)); // TODO is correct?
	}
	
	@Test
	public void ping_twoQueryForBOS_radius0And100() {
		get("/weather/" + BOS + "/0");
		get("/weather/" + BOS + "/100");
		
		get("/ping")
			.then().assertThat()
			.statusCode(200)
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(2.0f)) 
			.body("iata_freq.EWR", equalTo(0.0f)) 
			.body("iata_freq.LGA", equalTo(0.0f)) 
			.body("iata_freq.JFK", equalTo(0.0f)) 
			.body("iata_freq.MMU", equalTo(0.0f))
			.body("radius_freq", hasSize(101)); // TODO is correct?
	}
	
	@Test
	public void weather_bos_radius0() {
		AtmosphericInformation[] as = get("/weather/" + BOS + "/0")
				.as(AtmosphericInformation[].class);
		
		assertThat(as.length, equalTo(1));
		assertThat(as[0], equalTo(EMPTY_ATMOSPHERIC_INFORMATION));
	}
	
	@Test
	public void weather_bos_noOtherAirportInRadius_bosWithNoInformation() {
		AtmosphericInformation[] as = get("/weather/" + BOS + "/100")
				.as(AtmosphericInformation[].class);
		
		assertThat(as.length, equalTo(0));
	}
	
	@Test
	public void weather_bos_noOtherAirportWithInformationInRadius_bosWithNoInformation() {
		AtmosphericInformation[] as = get("/weather/" + BOS + "/309")
				.as(AtmosphericInformation[].class);
		
		assertThat(as.length, equalTo(0));
	}
	
	@BeforeClass
	public static void setupBasePath() {
		registerParser("text/plain", Parser.JSON);
		RestAssured.basePath = "/query";
	}
}