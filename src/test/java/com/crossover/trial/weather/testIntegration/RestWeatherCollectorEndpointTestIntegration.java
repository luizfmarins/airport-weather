package com.crossover.trial.weather.testIntegration;

import static com.crossover.trial.weather.AtmosphericInformation.atmosphericInformationBuilder;
import static com.crossover.trial.weather.DataPointType.CLOUDCOVER;
import static com.crossover.trial.weather.DataPointType.PRESSURE;
import static com.crossover.trial.weather.DataPointType.WIND;
import static com.crossover.trial.weather.InitialAirports.BOS;
import static com.crossover.trial.weather.InitialAirports.EWR;
import static com.crossover.trial.weather.InitialAirports.JFK;
import static com.crossover.trial.weather.InitialAirports.LGA;
import static com.crossover.trial.weather.InitialAirports.MMU;
import static com.crossover.trial.weather.InitialAirports.bos;
import static com.crossover.trial.weather.InitialAirports.mmu;
import static com.crossover.trial.weather.testIntegration.WeatherQueryUtil.queryWeather;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.post;
import static com.jayway.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.crossover.trial.weather.AirportData;
import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.DataPoint;
import com.crossover.trial.weather.DataPointType;
import com.jayway.restassured.RestAssured;

public class RestWeatherCollectorEndpointTestIntegration extends TestBase {

	private static final double FLL_LONGITUDE = -74.168667;
	private static final double FLL_LATITUDE = 40.6925;
	private static final String FLL = "FLL";
	private static final double DISTANCE_BOS_EWR = 201d;
	private static final double DISTANCE_BOS_JFK = 203d;
	private static final DataPoint EMPTY_DATA_POINT = null;

	// TODO According to the javadoc shoud reurn 1
	@Test
	public void ping() throws Exception {
		get("/collect/ping")
			.then().assertThat()
				.statusCode(200)
				.body(equalTo("ready"));
	}
	
	@Test
	public void updateWeather() {
		updateWeather(BOS, WIND, windDatapoint());
	}
	
	@Test
	public void updateWeather_BOS_queryPing_dataSize1() {
		updateWeather(BOS, WIND, windDatapoint());

		assertQueryPingDatasize(1);
	}

	@Test
	public void updateWeather_BOSAndEWR_queryPing_dataSize2() {
		updateWeather(BOS, WIND, windDatapoint());
		updateWeather(EWR, WIND, windDatapoint());
		
		assertQueryPingDatasize(2);
	}
	
	@Test
	public void updateWeather_twoInfoBOS_oneInfoEWR_queryPing_dataSize2() {
		updateWeather(BOS, CLOUDCOVER, cloudOverDatapoint());
		updateWeather(BOS, WIND, windDatapoint());
		updateWeather(EWR, WIND, windDatapoint());
		
		assertQueryPingDatasize(2);
	}
	
	@Test
	public void updateBOSWind_queryWeather() {
		updateWeather(BOS, WIND, windDatapoint());
		
		AtmosphericInformation[] infos = queryWeather(BOS, 0d);
		
		assertThat(infos, arrayWithSize(1));
		assertThat(infos, arrayContaining(
				atmosphericInformationBuilder()
					.withWind(windDatapoint())
					.withCloudCover(EMPTY_DATA_POINT)
					.withHumidity(EMPTY_DATA_POINT)
					.withPressure(EMPTY_DATA_POINT)
					.withTemperature(EMPTY_DATA_POINT)
					.build()));
		
	}
	
	@Test
	public void updateBOSWind_BOSCloudOover_queryWeather() {
		updateWeather(BOS, WIND, windDatapoint());
		updateWeather(BOS, CLOUDCOVER, cloudOverDatapoint());
		
		AtmosphericInformation[] infos = queryWeather(BOS, 0d);
		
		assertThat(infos, arrayWithSize(1));
		assertThat(infos, arrayContaining(
				atmosphericInformationBuilder()
					.withWind(windDatapoint())
					.withCloudCover(cloudOverDatapoint())
					.build()));
		
	}
	
	@Test
	public void updateBOSWind_EWRCloudOover_queryWeather() {
		updateWeather(BOS, WIND, windDatapoint());
		updateWeather(EWR, CLOUDCOVER, cloudOverDatapoint());
		
		AtmosphericInformation[] bosInfos = queryWeather(BOS, 0d);
		
		assertThat(bosInfos, arrayWithSize(1));
		assertThat(bosInfos, arrayContaining(
				atmosphericInformationBuilder()
					.withWind(windDatapoint())
					.withCloudCover(EMPTY_DATA_POINT)
					.build()));
		
		AtmosphericInformation[] ewrInfos = queryWeather(EWR, 0d);
		
		assertThat(ewrInfos, arrayWithSize(1));
		assertThat(ewrInfos, arrayContaining(
				atmosphericInformationBuilder()
					.withWind(EMPTY_DATA_POINT)
					.withCloudCover(cloudOverDatapoint())
					.build()));
	}

	@Test
	public void updateBOSWind_EWRCloudOver_queryWeather_withEWRInRadius() {
		updateWeather(BOS, WIND, windDatapoint());
		updateWeather(JFK, PRESSURE, pressureDatapoint());
		
		AtmosphericInformation[] infos = queryWeather(BOS, DISTANCE_BOS_JFK);
		
		assertThat(infos, arrayWithSize(2));
		assertThat(infos, Matchers.arrayContainingInAnyOrder(
				atmosphericInformationBuilder()
					.withWind(windDatapoint())
					.withPressure(EMPTY_DATA_POINT)
					.build(),
				atmosphericInformationBuilder()
					.withWind(EMPTY_DATA_POINT)
					.withPressure(pressureDatapoint())
					.build()));
	}
	
	@Test
	public void updateBOSWind_EWRCloudOver_JFKPressure_queryWeather_JFKOutOfRadius() {
		updateWeather(BOS, WIND, windDatapoint());
		updateWeather(EWR, CLOUDCOVER, cloudOverDatapoint());
		updateWeather(JFK, PRESSURE, pressureDatapoint());
		
		AtmosphericInformation[] infos = queryWeather(BOS, DISTANCE_BOS_EWR);
		
		assertThat(infos, arrayWithSize(2));
		assertThat(infos, Matchers.arrayContainingInAnyOrder(
				atmosphericInformationBuilder()
					.withWind(windDatapoint())
					.withCloudCover(EMPTY_DATA_POINT)
					.withPressure(EMPTY_DATA_POINT)
					.build(),
				atmosphericInformationBuilder()
					.withWind(EMPTY_DATA_POINT)
					.withCloudCover(cloudOverDatapoint())
					.withPressure(EMPTY_DATA_POINT)
					.build()));
	}
	
	@Test
	public void testGetAirports() {
		String[] airports = getAirports();
		
		assertThat(airports, arrayContainingInAnyOrder(EWR, MMU, BOS, LGA, JFK));
	}

	private String[] getAirports() {
		return get("/collect/airports").as(String[].class);
	}
	
	@Test
	public void getAirport_BOS() {
		AirportData airport = getAirport(BOS);
		
		assertThat(airport, equalTo(bos()));
	}
	
	@Test
	public void getAirport_MMU() {
		AirportData airport = getAirport(MMU);
		
		assertThat(airport, equalTo(mmu()));
	}
	
	@Test
	public void addAirport_FLL() {
		addAirport(FLL, FLL_LATITUDE, FLL_LONGITUDE);
	}
	
	@Test
	public void addAirport_FLL_getAirport() {
		addAirport(FLL, FLL_LATITUDE, FLL_LONGITUDE);
		
		AirportData airport = getAirport(FLL);
		assertThat(airport.getIata(), equalTo(FLL));
		assertThat(airport.getLatitude(), equalTo(FLL_LATITUDE));
		assertThat(airport.getLongitude(), equalTo(FLL_LONGITUDE));
	}
	
	@Test
	public void addAirport_FLL_getAirports() {
		addAirport(FLL, FLL_LATITUDE, FLL_LONGITUDE);

		String[] airports = getAirports();
		
		assertThat(airports, arrayContainingInAnyOrder(FLL, EWR, MMU, BOS, LGA, JFK));
	}
	
	@Test
	public void addAirportWithSameName() {
		post("/collect/airport/" + BOS + "/" + FLL_LATITUDE + "/" + FLL_LONGITUDE)
			.then().assertThat().statusCode(BAD_REQUEST.getStatusCode());
	}

	private void addAirport(String iata, double latitude, double longitude) {
		post("/collect/airport/" + iata + "/" + latitude + "/" + longitude)
			.then().assertThat().statusCode(OK.getStatusCode());
		
	}

	private AirportData getAirport(String airportName) {
		return get("/collect/airport/" + airportName).as(AirportData.class);
	}
	
	private void assertQueryPingDatasize(int datasize) {
		get("/query/ping")
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.body("datasize", equalTo(datasize));
	}

	private void updateWeather(String airport, DataPointType type, DataPoint datapoint) {
		given().contentType(JSON)
			.body(datapoint)
			.when().post("/collect/weather/" + airport + "/" + type.name())
			.then().assertThat().statusCode(OK.getStatusCode());
	}
	
	private DataPoint windDatapoint() {
		return new DataPoint.Builder()
		.withFirst(0)
		.withLast(10)
		.withMean(6)
		.withMedian(4)
		.withCount(20).build();
	}
	
	private DataPoint cloudOverDatapoint() {
		return new DataPoint.Builder()
				.withFirst(3)
				.withLast(15)
				.withMean(7)
				.withMedian(6)
				.withCount(40).build();
	}
	
	private DataPoint pressureDatapoint() {
		return new DataPoint.Builder()
				.withFirst(7)
				.withLast(21)
				.withMean(700)
				.withMedian(8)
				.withCount(45).build();
	}
}