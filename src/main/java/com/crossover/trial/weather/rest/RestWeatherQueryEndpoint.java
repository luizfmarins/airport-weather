package com.crossover.trial.weather.rest;

import static com.crossover.trial.weather.rest.InitialAirports.bos;
import static com.crossover.trial.weather.rest.InitialAirports.ewr;
import static com.crossover.trial.weather.rest.InitialAirports.jfk;
import static com.crossover.trial.weather.rest.InitialAirports.lga;
import static com.crossover.trial.weather.rest.InitialAirports.mmu;
import static com.crossover.trial.weather.rest.RestWeatherCollectorEndpoint.addAirport;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.math.NumberUtils;

import com.crossover.trial.weather.api.WeatherQueryEndpoint;
import com.crossover.trial.weather.model.AirportData;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    private static final double MAXIMUN_RADIUS = 1000.0;
	private final static Logger LOGGER = Logger.getLogger("WeatherQuery");
    private static final double EARTH_RADIUS_KM = 6372.8;
    private static final Gson gson = new Gson();

    /** all known airports */
    protected static List<AirportData> airportData = new ArrayList<>();

    /** atmospheric information for each airport, idx corresponds with airportData */
    protected static List<AtmosphericInformation> atmosphericInformation = new LinkedList<>();

    /**
     * Internal performance counter to better understand most requested information, this map can be improved but
     * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
     * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
     * performance metrics {@link #ping()}
     */
    public static Map<AirportData, Integer> requestFrequency = new HashMap<AirportData, Integer>();
    public static Map<Double, Integer> radiusFreq = new HashMap<Double, Integer>();
    private static int weatherQueryCount = 0;
    
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
        retval.put("iata_freq", calculateIataFrequency());
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

	private Map<String, Double> calculateIataFrequency() {
		Map<String, Double> freq = new HashMap<>();

		for (AirportData data : airportData) {
    		double frac = weatherQueryCount == 0 ? 0 : requestFrequency.getOrDefault(data, 0) / weatherQueryCount;
    		freq.put(data.getIata(), frac);
        }
        
		return freq;
	}

	long calculateDataSize() {
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
        double radius = NumberUtils.toDouble(radiusString, 0);
        updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = getAtmosphericInformation(iata, radius);
        return Response.status(Response.Status.OK).entity(retval).build();
    }

	private List<AtmosphericInformation> getAtmosphericInformation(String iata, double radius) {
        if (radius == 0) {
            int idx = getAirportDataIdx(iata);
            return newArrayList(atmosphericInformation.get(idx));
        } 
        
        List<AtmosphericInformation> retval = new ArrayList<>();
        AirportData ad = findAirportData(iata);
        for (int i=0; i< airportData.size(); i++){
            if (calculateDistance(ad, airportData.get(i)) <= radius){
                AtmosphericInformation ai = atmosphericInformation.get(i);
                if (ai.hasInformation())
                    retval.add(ai);
            }
        }
		return retval;
	}


    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, 0) + 1);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0) + 1);
        weatherQueryCount++;
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public static AirportData findAirportData(String iataCode) {
        return airportData.stream()
            .filter(ap -> ap.getIata().equals(iataCode))
            .findFirst().orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public static int getAirportDataIdx(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        return airportData.indexOf(ad);
    }

    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    public double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a =  Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c;
    }

    /**
     * A dummy init method that loads hard coded data
     */
    // TODO
    public static void init() {
        airportData.clear();
        atmosphericInformation.clear();
        requestFrequency.clear();
        radiusFreq.clear();
        weatherQueryCount = 0;

        addAirport(bos());
        addAirport(ewr());
        addAirport(jfk());
        addAirport(lga());
        addAirport(mmu());
    }
}
