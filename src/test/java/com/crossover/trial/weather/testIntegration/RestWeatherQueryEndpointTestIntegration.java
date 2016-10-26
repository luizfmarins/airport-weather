package com.crossover.trial.weather.testIntegration;

import static com.crossover.trial.weather.InitialAirports.BOS;
import static com.crossover.trial.weather.testIntegration.HasRadiusFrequency.hasRadiusFreq;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.registerParser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.InitialAirports;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;

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
			.body("radius_freq", hasRadiusFreq(0, 1)); 
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
			.body("radius_freq", hasSize(101))
			.body("radius_freq", hasRadiusFreq(0, 1))
			.body("radius_freq", hasRadiusFreq(100, 1)); 
	}
	
	@Test
	public void ping_oneQueryForBOSradius0_oneQueryForJFKradius100() {
		get("/weather/" + BOS + "/0");
		get("/weather/" + InitialAirports.JFK + "/100");
		
		get("/ping")
			.then().assertThat()
			.statusCode(200)
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(1.0f)) 
			.body("iata_freq.EWR", equalTo(0.0f)) 
			.body("iata_freq.LGA", equalTo(0.0f)) 
			.body("iata_freq.JFK", equalTo(1.0f)) 
			.body("iata_freq.MMU", equalTo(0.0f))
			.body("radius_freq", hasSize(101))
			.body("radius_freq", hasRadiusFreq(0, 1))
			.body("radius_freq", hasRadiusFreq(100, 1)); 
	}
	
	@Test
	public void ping_twoQueryForBOS_radius0_100_100_154_154dot85() {
		get("/weather/" + BOS + "/0");
		get("/weather/" + BOS + "/100");
		get("/weather/" + BOS + "/100");
		get("/weather/" + BOS + "/154");
		get("/weather/" + BOS + "/154.85");
		
		get("/ping")
			.then().assertThat()
			.statusCode(200)
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(5.0f)) 
			.body("iata_freq.EWR", equalTo(0.0f)) 
			.body("iata_freq.LGA", equalTo(0.0f)) 
			.body("iata_freq.JFK", equalTo(0.0f)) 
			.body("iata_freq.MMU", equalTo(0.0f))
			.body("radius_freq", hasSize(155))
			.body("radius_freq", hasRadiusFreq(0, 1))
			.body("radius_freq", hasRadiusFreq(100, 2)) 
			.body("radius_freq", hasRadiusFreq(154, 2)); 
	}
	
	@Test
	public void birl() {
		get("/weather/" + BOS + "/0");
		get("/weather/" + BOS + "/100");
		get("/weather/" + BOS + "/154.48");
		get("/weather/" + BOS + "/154");
		get("/weather/" + BOS + "/154");
		get("/weather/" + BOS + "/154");
		
		get("/ping")
		.then().assertThat()
		.statusCode(200);
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