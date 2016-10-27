package com.crossover.trial.weather.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.crossover.trial.weather.model.AtmosphericInformation;

@RunWith(MockitoJUnitRunner.class)
public class RestWeatherQueryEndpointUnitTest {

	@Mock
	private AtmosphericInformation information;
	
	private RestWeatherQueryEndpoint sut = new RestWeatherQueryEndpoint();
	
	@Test
	public void calculateDataSize_noData() throws Exception {
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(0l));
	}
	
	@Test
	public void calculateDataSize_oneAirportWithNoInformation() throws Exception {
		RestWeatherQueryEndpoint.atmosphericInformation.add(information);
		
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(0l));
	}
	
	@Test
	public void calculateDataSize_oneAirportWithInformation_notInTheLastDay() {
		RestWeatherQueryEndpoint.atmosphericInformation.add(information);
		when(information.hasInformation()).thenReturn(true);
		
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(0l));
	}
	
	@Test
	public void calculateDataSize_oneAirportWithInformation_updatedInTheLastDay() {
		RestWeatherQueryEndpoint.atmosphericInformation.add(information);
		when(information.hasInformation()).thenReturn(true);
		when(information.isUpdatedInTheLastDay()).thenReturn(true);
		
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(1l));
	}
	
	@Before
	public void before() {
		RestWeatherQueryEndpoint.init();
	}
}
