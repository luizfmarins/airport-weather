package com.crossover.trial.weather.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportRepository;

@RunWith(MockitoJUnitRunner.class)
public class WeatherQueryEndpointImplTest {

	@Mock
	private AtmosphericInformation information;
	
	private WeatherQueryEndpointImpl sut = new WeatherQueryEndpointImpl();
	private AirportRepository airportRepository = AirportRepository.getInstance();
	
	@Test
	public void calculateDataSize_noData() throws Exception {
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(0l));
	}
	
	@Test
	public void calculateDataSize_oneAirportWithNoInformation() throws Exception {
		addAirport();
		
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(0l));
	}

	@Test
	public void calculateDataSize_oneAirportWithInformation_notInTheLastDay() {
		when(information.hasInformation()).thenReturn(true);
		
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(0l));
	}
	
	@Test
	public void calculateDataSize_oneAirportWithInformation_updatedInTheLastDay() {
		when(information.hasInformation()).thenReturn(true);
		when(information.isUpdatedInTheLastDay()).thenReturn(true);
		
		long size = sut.calculateDataSize();
		
		assertThat(size, equalTo(1l));
	}

	@Before
	public void before() {
		WeatherQueryEndpointImpl.init();
		AirportRepository.init();
		addAirport();
	}
	
	private void addAirport() {
		Airport airport = new Airport();
		airport.setAtmosphericInformation(information);
		airportRepository.save(airport);
	}
}