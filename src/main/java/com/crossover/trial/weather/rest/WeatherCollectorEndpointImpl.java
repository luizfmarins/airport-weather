package com.crossover.trial.weather.rest;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.api.WeatherCollectorEndpoint;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.WeatherException;
import com.crossover.trial.weather.model.datapoint.DataPoint;
import com.crossover.trial.weather.model.datapoint.DataPointType;
import com.crossover.trial.weather.repository.AirportRepository;
import com.google.gson.Gson;

public class WeatherCollectorEndpointImpl implements WeatherCollectorEndpoint {

	private final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());
	private static final int OK = 1;
	private final AirportRepository airportRepository = AirportRepository.getInstance();
	private final Gson gson = new Gson();
	
	@Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity(OK).build();
    }

    @Override
    public Response updateWeather(String iataCode, String pointType, String datapointJson) {
        try {
            addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException ex) {
        	LOGGER.warning(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response getAirports() {
    	Set<String> retval = airportRepository.list().stream()
    			.map(a -> a.getIata())
    			.collect(toSet());

        return Response.status(Response.Status.OK).entity(retval).build();
    }


    @Override
    public Response getAirport(String iata) {
        Airport ad = airportRepository.findByCode(iata);
        if (ad == null)
        	return Response.status(Response.Status.NOT_FOUND).build();
        
        return Response.status(Response.Status.OK).entity(ad).build();
    }


    @Override
    public Response addAirport(String iata, String latString, String longString) {
    	Airport airport = airportRepository.findByCode(iata);
    	if (airport != null)
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	
        addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response deleteAirport(@PathParam("iata") String iata) {
    	Airport airport = airportRepository.findByCode(iata);
    	if (airport == null)
        	return Response.status(Response.Status.NOT_FOUND).build();
    	
    	airportRepository.delete(airport);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
    
    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link DataPointType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws WeatherException if the update can not be completed
     */
    private void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {
        Airport airport = airportRepository.findByCode(iataCode);
        updateAtmosphericInformation(airport, pointType, dp);
    }

    /**
     * update atmospheric information with the given data point for the given point type
     *
     * @param ai the atmospheric information object to update
     * @param pointType the data point type as a string
     * @param dp the actual data point
     */
    private void updateAtmosphericInformation(Airport airport, String pointType, DataPoint dp) throws WeatherException {
        final DataPointType dptype = DataPointType.valueOf(pointType.toUpperCase());
        AtmosphericInformation ai = airport.getAtmosphericInformation();
        dptype.update(ai, dp);
    }

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode 3 letter code
     * @param latitude in degrees
     * @param longitude in degrees
     *
     * @return the added airport
     */
    private Airport addAirport(String iataCode, double latitude, double longitude) {
        Airport ad = new Airport();
        ad.setIata(iataCode);
        ad.setLatitude(latitude);
        ad.setLongitude(longitude);
        airportRepository.save(ad);
        return ad;
    }
}