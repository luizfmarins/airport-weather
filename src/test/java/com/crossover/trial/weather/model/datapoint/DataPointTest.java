package com.crossover.trial.weather.model.datapoint;

import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.CLOUDCOVER;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.HUMIDTY;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.PRECIPITATION;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.PRESSURE;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.TEMPERATURE;
import static com.crossover.trial.weather.model.datapoint.DataPointType.Type.WIND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.WeatherException;
import com.crossover.trial.weather.model.datapoint.DataPoint.Builder;
import com.crossover.trial.weather.model.datapoint.DataPointType.Type;

@RunWith(MockitoJUnitRunner.class)
public class DataPointTest {

	private static final boolean VALID = true;
	private static final boolean INVALID = false;
	
	@Mock
	private AtmosphericInformation info;
	@Rule
	public ExpectedException ex = ExpectedException.none();
	
	@Test
	public void wind_negativeMean_invalid() {
		assertMean(WIND, -1, INVALID);
	}
	
	@Test
	public void wind_zeroMean_valid() {
		assertMean(WIND, 0, VALID);
	}
	
	@Test
	public void wind_positiveMean_valid() {
		assertMean(WIND, 1, VALID);
	}
	
	@Test
	public void wind_update() throws WeatherException {
		DataPointType wind = WIND.getDataPointType();
		wind.update(info, defaultDataPoint());
		
		verify(info).setWind(defaultDataPoint());
	}
	
	@Test
	public void update_invalidDataPoint_throwsWeatherException() throws WeatherException {
		DataPointType wind = WIND.getDataPointType();
		
		ex.expect(WeatherException.class);
		wind.update(info, dataPoint().withMean(-1).build());
	}
	
	@Test
	public void temperature_meanLesserThen100_valid() throws Exception {
		assertMeanBetween(TEMPERATURE, -50, 99);
	}
	
	@Test
	public void temperature_update_setTemperature() throws Exception {
		DataPointType temperature = TEMPERATURE.getDataPointType();
		temperature.update(info, defaultDataPoint());
		
		verify(info).setTemperature(defaultDataPoint());
	}

	@Test
	public void humidty_meanBewtween0_99() {
		assertMeanBetween(HUMIDTY, 0, 99);
	}
	
	@Test
	public void humidty_update_setHumidty() throws Exception {
		DataPointType humidty = HUMIDTY.getDataPointType();
		humidty.update(info, defaultDataPoint());
		
		verify(info).setHumidity(defaultDataPoint());
	}
	
	@Test
	public void pressure_meanBetween650_799() throws Exception {
		assertMeanBetween(PRESSURE, 650, 799);
	}

	@Test
	public void pressure_update_setPressure() throws Exception {
		DataPointType pressure = PRESSURE.getDataPointType();
		pressure.update(info, pressureDataPoint());
		
		verify(info).setPressure(pressureDataPoint());
	}
	
	@Test
	public void cloudCover_meanBetween0_99() throws Exception {
		assertMeanBetween(CLOUDCOVER, 0, 99);
	}
	
	@Test
	public void cloudCover_update_setPressure() throws Exception {
		DataPointType cloudCover = CLOUDCOVER.getDataPointType();
		cloudCover.update(info, defaultDataPoint());
		
		verify(info).setCloudCover(defaultDataPoint());
	}
	
	@Test
	public void precipitation_meanBetween0_99() throws Exception {
		assertMeanBetween(PRECIPITATION, 0, 99);
	}
	
	@Test
	public void precipitation_update_setPrecipitation() throws Exception {
		DataPointType precipitation = PRECIPITATION.getDataPointType();
		precipitation.update(info, defaultDataPoint());
		
		verify(info).setPrecipitation(defaultDataPoint());
	}
	
	private void assertMeanBetween(Type type, int min, int max) {
		assertMean(type, min -1, INVALID);
		assertMean(type, min, VALID);
		assertMean(type, min +1, VALID);
		
		assertMean(type, max-1, VALID);
		assertMean(type, max, VALID);
		assertMean(type, max+1, INVALID);
	}
	
	private DataPoint defaultDataPoint() {
		return dataPoint().withMean(1).build();
	}

	private DataPoint pressureDataPoint() {
		return dataPoint().withMean(651).build();
	}
	
	private void assertMean(Type type, int mean, boolean isValid) {
		DataPointType dataPointType = type.getDataPointType();
		assertThat(dataPointType.isValid(dataPoint().withMean(mean).build()), equalTo(isValid));
	}
	
	private Builder dataPoint() {
		return new DataPoint.Builder();
	}
	
	
}
