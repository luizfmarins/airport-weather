package com.crossover.trial.weather.rest;

import static com.crossover.trial.weather.model.AtmosphericInformation.atmosphericInformationBuilder;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.CLOUDCOVER;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.PRESSURE;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.WIND;
import static com.crossover.trial.weather.util.DataPointUtil.cloudOverDatapoint;
import static com.crossover.trial.weather.util.DataPointUtil.pressureDatapoint;
import static com.crossover.trial.weather.util.DataPointUtil.windDatapoint;
import static com.crossover.trial.weather.util.InitialAirports.BOS;
import static com.crossover.trial.weather.util.InitialAirports.EWR;
import static com.crossover.trial.weather.util.InitialAirports.JFK;
import static com.crossover.trial.weather.util.rest.RestWeatherCollectorUtil.updateWeather;
import static com.crossover.trial.weather.util.rest.RestWeatherQueryUtil.queryWeather;
import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.datapoint.DataPoint;

public class RestUpdateAndQueryWeatherTest extends RestTestBase {
	
	private static final double DISTANCE_BOS_EWR = 201d;
	private static final double DISTANCE_BOS_JFK = 203d;
	private static final DataPoint EMPTY_DATA_POINT = null;
	
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
		assertThat(infos, arrayContainingInAnyOrder(
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
		assertThat(infos, arrayContainingInAnyOrder(
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
	public void atmosphericInformation_doesntContains_updatedInTheLastDay() {
		updateWeather(BOS, WIND, windDatapoint());
		
		String json = get("query/weather/" + BOS + "/" + 0).asString();
		
		assertThat(json, not(containsString("updatedInTheLastDay")));
	}

}
