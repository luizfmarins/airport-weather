package com.crossover.trial.weather.rest;

import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.api.WeatherQueryEndpoint;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

	private final static Logger LOGGER = Logger.getLogger("WeatherQuery");

	private WeatherQueryEndpoint delegate = new WeatherQueryEndpointImpl();
	private final Gson gson = new Gson();

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
    	LOGGER.info("WeatherQueryEndpoint.ping");
    	String retVal = delegate.ping();
    	LOGGER.info("WeatherQueryEndpoint.ping Response: " + retVal);
    	return retVal;
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata the iataCode
     * @param radiusString the radius in km
     *
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(String iata, String radiusString) {
    	LOGGER.info("WeatherQueryEndpoint.weather(" + iata + "," + radiusString + ")");
        Response response = delegate.weather(iata, radiusString);
        LOGGER.info("WeatherQueryEndpoint.weather Response: " + gson.toJson(response.getEntity()));
		return response;
    }
}