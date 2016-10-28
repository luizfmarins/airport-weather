package com.crossover.trial.weather.rest;

import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.api.WeatherCollectorEndpoint;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {

	private final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());
    
	private final WeatherCollectorEndpoint delegate = new WeatherCollectorEndpointImpl();
	private final Gson gson = new Gson();

    @Override
    public Response ping() {
    	LOGGER.info("WeatherCollectorEndpoint.ping");
        Response response = delegate.ping();
        LOGGER.info("WeatherCollectorEndpoint.ping OK");
        return response;
    }

    @Override
    public Response updateWeather(String iataCode, String pointType, String datapointJson) {
    	LOGGER.info("WeatherCollectorEndpoint.updateWeather("+ iataCode +", " + pointType + ", " + datapointJson + ")");
        Response response = delegate.updateWeather(iataCode, pointType, datapointJson);
        LOGGER.info("WeatherCollectorEndpoint.updateWeather Response: " + gson.toJson(response.getEntity()));
		return response;
    }

    @Override
    public Response getAirports() {
    	LOGGER.info("WeatherCollectorEndpoint.getAirports");
    	Response response = delegate.getAirports();
    	LOGGER.info("WeatherCollectorEndpoint.getAirports Response: " + gson.toJson(response.getEntity()));
		return response;
    }


    @Override
    public Response getAirport(String iata) {
    	LOGGER.info("WeatherCollectorEndpoint.getAirport(" + iata + ")");
    	Response response = delegate.getAirport(iata);
    	LOGGER.info("WeatherCollectorEndpoint.getAirport Response: " + gson.toJson(response.getEntity()));
		return response;
    }


    @Override
    public Response addAirport(String iata, String latString, String longString) {
    	LOGGER.info("WeatherCollectorEndpoint.addAirport("+ iata +", " + latString + ", " + longString + ")");
    	Response response = delegate.addAirport(iata, latString, longString);
    	LOGGER.info("WeatherCollectorEndpoint.addAirport Response: " + gson.toJson(response.getEntity()));
		return response;
    }


    @Override
    public Response deleteAirport(String iata) {
    	LOGGER.info("WeatherCollectorEndpoint.deleteAirport("+ iata + ")");
        Response response = delegate.deleteAirport(iata);
        LOGGER.info("WeatherCollectorEndpoint.deleteAirport Response: " + gson.toJson(response.getEntity()));
		return response;
    }

    @Override
    public Response exit() {
    	LOGGER.info("WeatherCollectorEndpoint.exit");
        return delegate.exit();
    }
}