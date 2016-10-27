package com.crossover.trial.weather.rest;

import static java.util.stream.Collectors.toList;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.api.WeatherQueryEndpoint;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportRepository;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

	// TODO 
	private final static Logger LOGGER = Logger.getLogger("WeatherQuery");
    private static final double MAXIMUN_RADIUS = 1000.0;
    
	private final Gson gson = new Gson();
	private final AirportRepository airportRepository = AirportRepository.getInstance();
	private final RequestFrequency requestFreq = new RequestFrequency();
	
    public static Map<Double, Integer> radiusFreq = new HashMap<Double, Integer>();
    
    static {
        init();
    }

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
        Map<String, Object> retval = new HashMap<>();
        retval.put("datasize", calculateDataSize());
        retval.put("iata_freq", requestFreq.calculateIataFrequency());
        retval.put("radius_freq", calculateRadiusFrequency());

        return gson.toJson(retval);
    }

	private int[] calculateRadiusFrequency() {
		int[] frequencies = new int[radiusFrequencySize()];
		
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
            int frequencyIndex = e.getKey().intValue();
            frequencies[frequencyIndex] += e.getValue();
        }
        
		return frequencies;
	}

	private int radiusFrequencySize() {
		return radiusFreq.keySet().stream()
                .max(Double::compare)
                .orElse(MAXIMUN_RADIUS).intValue() + 1;
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
        double radius = toDouble(radiusString, 0);
        updateRequestFrequency(iata, radius);
        List<AtmosphericInformation> atmosphericInformation = getAtmosphericInformation(iata, radius);
        
		return Response.status(Response.Status.OK).entity(atmosphericInformation).build();
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


    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        requestFreq.notifyWeatherRequestForAirport(iata);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0) + 1);
    }

    
    public static void init() {
        RequestFrequency.clear();
        radiusFreq.clear();
    }
}
