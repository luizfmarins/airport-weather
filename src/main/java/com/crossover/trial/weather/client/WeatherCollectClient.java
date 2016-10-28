package com.crossover.trial.weather.client;

import static java.lang.String.format;
import static javax.ws.rs.client.Entity.json;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.model.Airport;

public class WeatherCollectClient {

	private static final String ADD_AIRPORT_PATH = "/airport/%s/%s/%s";
	
	private WebTarget collect;
	
	public WeatherCollectClient(String target) {
		Client client = ClientBuilder.newClient();
		collect = client.target(target + "/collect/");
	}

	public Response addAirport(Airport airport) {
		WebTarget path = collect.path(addAirportPath(airport));
		return path.request().post(json(null));
	}

	private String addAirportPath(Airport airport) {
		return format(ADD_AIRPORT_PATH, airport.getIata(), airport.getLatitude(), airport.getLongitude());
	}
}