package com.crossover.trial.weather.rest;

import static java.util.stream.Collectors.toList;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.crossover.trial.weather.api.WeatherQueryEndpoint;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportRepository;
import com.google.gson.Gson;

public class WeatherQueryEndpointImpl implements WeatherQueryEndpoint {

	private final Gson gson = new Gson();
	private final AirportRepository airportRepository = AirportRepository.getInstance();
	private final RequestFrequency requestFreq = new RequestFrequency();
	private final RadiusFrequency radiusFreq = new RadiusFrequency();
	
	 static {
	        init();
	    }
	
	@Override
	public String ping() {
		Map<String, Object> retval = new HashMap<>();
        retval.put("datasize", calculateDataSize());
        retval.put("iata_freq", requestFreq.calculateIataFrequency());
        retval.put("radius_freq", radiusFreq.calculateRadiusFrequency());

        return gson.toJson(retval);
	}

	long calculateDataSize() {
		List<AtmosphericInformation> atmosphericInformation = airportRepository.list().stream()
			.map(ap -> ap.getAtmosphericInformation())
			.collect(toList());
		
		long datasize = atmosphericInformation.stream()
			.filter(ai -> ai.hasInformation() && ai.isUpdatedInTheLastDay())
			.count();
		return datasize;
	}
	
	private List<AtmosphericInformation> getAtmosphericInformation(String iata, double radius) {
        Airport airport = airportRepository.findByCode(iata);

        if (radius == 0) 
            return newArrayList(airport.getAtmosphericInformation());
        
        List<AtmosphericInformation> retval = airportRepository.list().stream()
        	.filter(other -> airport.isWithinRadius(other, radius) && other.getAtmosphericInformation().hasInformation())
        	.map(ap -> ap.getAtmosphericInformation())
        	.collect(toList());
        
		return retval;
	}

    private void updateRequestFrequency(String iata, Double radius) {
        requestFreq.notifyWeatherRequest(iata);
        radiusFreq.notifyWeatherRequest(radius);
    }
	
	@Override
	public Response weather(String iata, String radiusString) {
		double radius = toDouble(radiusString, 0);
        
        updateRequestFrequency(iata, radius);
        List<AtmosphericInformation> atmosphericInformation = getAtmosphericInformation(iata, radius);
        
		return Response.status(Response.Status.OK).entity(atmosphericInformation).build();
	}
	
	public static void init() {
        RequestFrequency.clear();
        RadiusFrequency.clear();
    }
}
