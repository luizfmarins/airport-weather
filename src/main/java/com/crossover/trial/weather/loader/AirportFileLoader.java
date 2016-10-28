package com.crossover.trial.weather.loader;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.readLines;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class AirportFileLoader {

	private static final String SEPARATOR = ",";
	private File resource;

	public AirportFileLoader(File resource) {
		this.resource = resource;
	}

	public List<AirportData> load() throws IOException {
		List<String> lines = readLines(resource, Charset.forName("UTF-8"));
		return lines.stream().map(l -> toAirportData(l)).collect(toList());
	}
	
	private AirportData toAirportData(String line) {
		String[] columns = line.split(SEPARATOR);
		return new AirportData.Builder()
			.withCity(columnValue(columns, 2))
			.withCountry(columnValue(columns, 3))
			.withIata(columnValue(columns, 4))
			.withIcao(columnValue(columns, 5))
			.withLatitude(columnValue(columns, 6))
			.withLongitude(columnValue(columns, 7))
			.withAltitude(columnValue(columns, 8))
			.withTimezone(columnValue(columns, 9))
			.withDst(columnValue(columns, 10))
			.build();
	}

	private String columnValue(String[] columns, int index) {
		return columns[index].replaceAll("\"", "");
	}
}