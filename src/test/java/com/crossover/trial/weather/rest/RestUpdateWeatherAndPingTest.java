package com.crossover.trial.weather.rest;

import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.CLOUDCOVER;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.WIND;
import static com.crossover.trial.weather.repository.InitialAirports.BOS;
import static com.crossover.trial.weather.repository.InitialAirports.EWR;
import static com.crossover.trial.weather.util.DataPointUtil.cloudOverDatapoint;
import static com.crossover.trial.weather.util.DataPointUtil.windDatapoint;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.updateWeather;
import static com.jayway.restassured.RestAssured.get;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class RestUpdateWeatherAndPingTest extends RestTestBase {

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
	
	private void assertQueryPingDatasize(int datasize) {
		get("/query/ping")
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.body("datasize", equalTo(datasize));
	}
}
