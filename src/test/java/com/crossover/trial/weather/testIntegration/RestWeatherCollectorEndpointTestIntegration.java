package com.crossover.trial.weather.testIntegration;

import static com.crossover.trial.weather.DataPointType.CLOUDCOVER;
import static com.crossover.trial.weather.DataPointType.WIND;
import static com.crossover.trial.weather.InitialAirports.BOS;
import static com.crossover.trial.weather.InitialAirports.EWR;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.crossover.trial.weather.DataPoint;
import com.crossover.trial.weather.DataPointType;

public class RestWeatherCollectorEndpointTestIntegration extends TestBase {

	@Test
	public void ping() throws Exception {
		get("/collect/ping")
			.then().assertThat()
				.statusCode(200)
				.body(equalTo("ready"));
	}
	
	@Test
	public void updateWeather() {
		updateWeather(BOS, WIND, datapoint());
	}
	
	@Test
	public void updateWeather_BOS_queryPing_dataSize1() {
		updateWeather(BOS, WIND, datapoint());

		assertQueryPingDatasize(1);
	}

	@Test
	public void updateWeather_BOSAndEWR_queryPing_dataSize2() {
		updateWeather(BOS, WIND, datapoint());
		updateWeather(EWR, WIND, datapoint());
		
		assertQueryPingDatasize(2);
	}
	
	@Test
	public void updateWeather_twoInfoBOS_oneInfoEWR_queryPing_dataSize2() {
		updateWeather(BOS, CLOUDCOVER, datapoint());
		updateWeather(BOS, WIND, datapoint());
		updateWeather(EWR, WIND, datapoint());
		
		assertQueryPingDatasize(2);
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
	
	private DataPoint datapoint() {
		return new DataPoint.Builder()
			.withFirst(0).withLast(10).withMean(6).withMedian(4).withCount(20)
			.build();
	}
}