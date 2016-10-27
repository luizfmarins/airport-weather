package com.crossover.trial.weather.rest;

import java.util.HashMap;
import java.util.Map;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.repository.AirportRepository;

/**
 * Internal performance counter to better understand most requested information, this map can be improved but
 * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
 * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
 * performance metrics {@link #ping()}
 */
public class RequestFrequency {

    private static Map<Airport, Integer> requestFrequency = new HashMap<Airport, Integer>();
    private static int weatherQueryCount = 0;
    
    private AirportRepository airportRepository = AirportRepository.getInstance();
    
    public Map<String, Double> calculateIataFrequency() {
		Map<String, Double> freq = new HashMap<>();

		for (Airport data : airportRepository.list()) {
    		double frac = weatherQueryCount == 0 ? 0 : (double) requestFrequency.getOrDefault(data, 0) / weatherQueryCount;
    		freq.put(data.getIata(), frac);
        }
        
		return freq;
	}
    
    public void notifyWeatherRequest(String iata) {
    	Airport airport = airportRepository.findByCode(iata);
    	requestFrequency.put(airport, requestFrequency.getOrDefault(airport, 0) + 1);
    	weatherQueryCount++;
    }
    
    public static void clear() {
    	requestFrequency.clear();
    	weatherQueryCount = 0;
    }
}