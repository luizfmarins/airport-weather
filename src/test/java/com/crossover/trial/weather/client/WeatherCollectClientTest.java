package com.crossover.trial.weather.client;

import static com.crossover.trial.weather.repository.InitialAirports.BOS;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.repository.AirportRepository;
import com.crossover.trial.weather.rest.RestTestBase;

public class WeatherCollectClientTest extends RestTestBase {

	private WeatherCollectClient sut = new WeatherCollectClient("http://localhost:9090");
	
	@Test
	public void addAirport() {
		Response response = sut.addAirport(new Airport(BOS, 55, -75));
		
		assertThat(response.getStatus(), equalTo(OK.getStatusCode()));
	}
	
	@Before
	public void before() {
		AirportRepository.rellyClear();
	}	
}