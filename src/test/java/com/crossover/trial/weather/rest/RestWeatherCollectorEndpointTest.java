package com.crossover.trial.weather.rest;

import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.WIND;
import static com.crossover.trial.weather.util.DataPointUtil.windDatapoint;
import static com.crossover.trial.weather.util.InitialAirports.BOS;
import static com.crossover.trial.weather.util.InitialAirports.EWR;
import static com.crossover.trial.weather.util.InitialAirports.JFK;
import static com.crossover.trial.weather.util.InitialAirports.LGA;
import static com.crossover.trial.weather.util.InitialAirports.MMU;
import static com.crossover.trial.weather.util.InitialAirports.bos;
import static com.crossover.trial.weather.util.InitialAirports.mmu;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.getAirport;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.getAirportAssertNotFound;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.updateWeather;
import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.post;
import static com.jayway.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.datapoint.DataPoint;
import com.crossover.trial.weather.model.datapoint.DataPointType;

public class RestWeatherCollectorEndpointTest extends RestTestBase {

	private static final String UNKNOWN_AIRPORT = "UNK";
	private static final int INVALID_WIND_MEAN = -1;
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
	public void getAirport_unknownAirport() {
		getAirportAssertNotFound(UNKNOWN_AIRPORT);
	}
	
	@Test
	public void getAirport_BOS() {
		Airport airport = getAirport(BOS);
		
		assertThat(airport, equalTo(bos()));
	}
	
	@Test
	public void getAirport_doesnSendAtmosphericinformation() {
		String body = get("/collect/airport/" + BOS).asString();
		
		assertThat(body, not(containsString("atmosphericInformation")));
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
	
	@Test
	public void updateWeather_invalidWind() {
		given().contentType(JSON)
			.body(new DataPoint.Builder().withMean(INVALID_WIND_MEAN).build())
			.when().post("/collect/weather/" + BOS + "/" + DataPointType.Type.WIND.name())
			.then().assertThat().statusCode(BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void deleteAirport() {
		Airport airport = getAirport(BOS);
		assertThat(airport, notNullValue());
		
		delete("collect/airport/" + BOS).then()
			.assertThat().statusCode(OK.getStatusCode());
		
		getAirportAssertNotFound(BOS);
		
	}
	
	@Test
	public void deleteAirport_unknownAirport() {
		delete("collect/airport/" + UNKNOWN_AIRPORT).then()
			.assertThat().statusCode(NOT_FOUND.getStatusCode());
	}

	private void addAirport(String iata, double latitude, double longitude) {
		post("/collect/airport/" + iata + "/" + latitude + "/" + longitude)
			.then().assertThat().statusCode(OK.getStatusCode());
		
	}
}