package com.crossover.trial.weather;

import static com.crossover.trial.weather.loader.AirportDataConverter.AIPORT_DATA_CONVERTER;
import static jersey.repackaged.com.google.common.collect.Collections2.transform;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.crossover.trial.weather.client.WeatherCollectClient;
import com.crossover.trial.weather.loader.AirportData;
import com.crossover.trial.weather.loader.AirportFileLoader;
import com.crossover.trial.weather.model.Airport;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 *
 * @author code test administrator
 */
public class AirportLoader {

	private final AirportFileLoader fileLoader;
	private final WeatherCollectClient collect;

    public AirportLoader(File file, String serviceUrl) {
        fileLoader = new AirportFileLoader(file);
        collect = new WeatherCollectClient(serviceUrl);
    }

	public void upload() throws IOException {
		List<AirportData> data = fileLoader.load();
		Collection<Airport> airports = transform(data, AIPORT_DATA_CONVERTER);
		upload(airports);
	}
	
    private void upload(Collection<Airport> airports) {
    	for (Airport a : airports) 
    		collect.addAirport(a);
	}

	public static void main(String args[]) throws IOException{
        File airportDataFile = new File(args[0]);
        if (!airportDataFile.exists() || airportDataFile.length() == 0) {
            System.err.println(airportDataFile + " is not a valid input");
            System.exit(1);
        }

        AirportLoader al = new AirportLoader(airportDataFile, "http://localhost:9090");
        al.upload();
        System.exit(0);
    }
}