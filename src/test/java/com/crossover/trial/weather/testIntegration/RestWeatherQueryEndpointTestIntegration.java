package com.crossover.trial.weather.testIntegration;

import static com.crossover.trial.weather.InitialAirports.BOS;
import static com.crossover.trial.weather.InitialAirports.JFK;
import static com.crossover.trial.weather.testIntegration.HasRadiusFrequency.hasRadiusFreq;
import static com.crossover.trial.weather.testIntegration.WeatherQueryUtil.queryWeather;
import static com.jayway.restassured.RestAssured.get;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.InitialAirports;
import com.jayway.restassured.RestAssured;

public class RestWeatherQueryEndpointTestIntegration extends TestBase {

	private static final String PATH_QUERY_PING = "query/ping";
	private static final AtmosphericInformation EMPTY_ATMOSPHERIC_INFORMATION = new AtmosphericInformation();

	@Test
	public void ping_noPreviousWeatherQueries() {
		get(PATH_QUERY_PING)
			.then().assertThat()
			.statusCode(OK.getStatusCode())
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
		queryWeather(BOS, 0);
		
		get(PATH_QUERY_PING)
			.then().assertThat()
			.statusCode(OK.getStatusCode())
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
		queryWeather(BOS, 0);
		queryWeather(BOS, 100);
		
		get(PATH_QUERY_PING)
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(1.0f)) 
			.body("radius_freq", hasSize(101))
			.body("radius_freq", hasRadiusFreq(0, 1))
			.body("radius_freq", hasRadiusFreq(100, 1)); 
	}
	
	@Test
	public void ping_oneQueryForBOSradius0_oneQueryForJFKradius100() {
		queryWeather(BOS, 0);
		queryWeather(JFK, 100);
		
		get(PATH_QUERY_PING)
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(0.5f)) 
			.body("iata_freq.JFK", equalTo(0.5f)) 
			.body("radius_freq", hasSize(101))
			.body("radius_freq", hasRadiusFreq(0, 1))
			.body("radius_freq", hasRadiusFreq(100, 1)); 
	}
	
	@Test
	public void ping_twoQueryForBOS_radius0_100_100_154_154dot85() {
		queryWeather(BOS, 0);
		queryWeather(BOS, 100);
		queryWeather(BOS, 100);
		queryWeather(BOS, 154);
		queryWeather(BOS, 154.85);
		
		get(PATH_QUERY_PING)
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.body("datasize", equalTo(0))
			.body("iata_freq.BOS", equalTo(1.0f)) 
			.body("radius_freq", hasSize(155))
			.body("radius_freq", hasRadiusFreq(0, 1))
			.body("radius_freq", hasRadiusFreq(100, 2)) 
			.body("radius_freq", hasRadiusFreq(154, 2)); 
	}
	
	@Test
	public void ping_atmosphericInformationUpdatedInMoreThenADay_doesnCount() {
		fail("Not implemented!");
	}
	
	@Test
	public void weather_bos_radius0() {
		AtmosphericInformation[] infos = queryWeather(BOS, 0);
		
		assertThat(infos, arrayWithSize(1));
		assertThat(infos, arrayContaining(EMPTY_ATMOSPHERIC_INFORMATION));
	}
	
	@Test
	public void weather_bos_noOtherAirportInRadius_bosWithNoInformation() {
		AtmosphericInformation[] infos = queryWeather(BOS, 100);
		
		assertThat(infos, arrayWithSize(0));
	}
	
	@Test
	public void weather_bos_noOtherAirportWithInformationInRadius_bosWithNoInformation() {
		AtmosphericInformation[] infos = queryWeather(BOS, 309);
		
		assertThat(infos, arrayWithSize(0));
	}
}