package com.crossover.trial.weather.rest;

import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.WIND;
import static com.crossover.trial.weather.repository.InitialAirports.BOS;
import static com.crossover.trial.weather.repository.InitialAirports.EWR;
import static com.crossover.trial.weather.repository.InitialAirports.JFK;
import static com.crossover.trial.weather.repository.InitialAirports.LGA;
import static com.crossover.trial.weather.repository.InitialAirports.MMU;
import static com.crossover.trial.weather.repository.InitialAirports.bos;
import static com.crossover.trial.weather.repository.InitialAirports.mmu;
import static com.crossover.trial.weather.util.DataPointUtil.windDatapoint;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.updateWeather;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.post;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.crossover.trial.weather.model.Airport;

public class RestWeatherCollectorEndpointTest extends RestTestBase {

	private static final double FLL_LONGITUDE = -74.168667;
	private static final double FLL_LATITUDE = 40.6925;
	private static final String FLL = "FLL";

	@Test
	public void ping() throws Exception {
		get("/collect/ping")
			.then().assertThat()
				.statusCode(200)
				.body(equalTo("1"));
	}
	
	@Test
	public void testUpdateWeather() {
		updateWeather(BOS, WIND, windDatapoint());
	}
	
	@Test
	public void testGetAirports() {
		String[] airports = getAirports();
		
		assertThat(airports, arrayContainingInAnyOrder(EWR, MMU, BOS, LGA, JFK));
	}

	private String[] getAirports() {
		return get("/collect/airports").as(String[].class);
	}
	
	@Test
	public void getAirport_BOS() {
		Airport airport = getAirport(BOS);
		
		assertThat(airport, equalTo(bos()));
	}
	
	@Test
	public void getAirport_MMU() {
		Airport airport = getAirport(MMU);
		
		assertThat(airport, equalTo(mmu()));
	}
	
	@Test
	public void addAirport_FLL() {
		addAirport(FLL, FLL_LATITUDE, FLL_LONGITUDE);
	}
	
	@Test
	public void addAirport_FLL_getAirport() {
		addAirport(FLL, FLL_LATITUDE, FLL_LONGITUDE);
		
		Airport airport = getAirport(FLL);
		assertThat(airport.getIata(), equalTo(FLL));
		assertThat(airport.getLatitude(), equalTo(FLL_LATITUDE));
		assertThat(airport.getLongitude(), equalTo(FLL_LONGITUDE));
	}
	
	@Test
	public void addAirport_FLL_getAirports() {
		addAirport(FLL, FLL_LATITUDE, FLL_LONGITUDE);

		String[] airports = getAirports();
		
		assertThat(airports, arrayContainingInAnyOrder(FLL, EWR, MMU, BOS, LGA, JFK));
	}
	
	@Test
	public void addAirportWithSameName() {
		post("/collect/airport/" + BOS + "/" + FLL_LATITUDE + "/" + FLL_LONGITUDE)
			.then().assertThat().statusCode(BAD_REQUEST.getStatusCode());
	}

	private void addAirport(String iata, double latitude, double longitude) {
		post("/collect/airport/" + iata + "/" + latitude + "/" + longitude)
			.then().assertThat().statusCode(OK.getStatusCode());
		
	}

	private Airport getAirport(String airportName) {
		return get("/collect/airport/" + airportName).as(Airport.class);
	}
}