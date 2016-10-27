package com.crossover.trial.weather.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AtmosphericInformationTest {

	@Mock
	private DataPoint dataPoint;
	private AtmosphericInformation sut = new AtmosphericInformation();
	
	@Test
	public void hasNoInformation() {
		assertHasNoInformation();
	}

	@Test
	public void hasCloudCover() {
		sut.setCloudCover(dataPoint);
		
		assertHasInformation();
	}
	
	@Test
	public void hasHumidity() {
		sut.setHumidity(dataPoint);
		
		assertHasInformation();
	}
	
	@Test
	public void hasPrecipitation() {
		sut.setPrecipitation(dataPoint);
		
		assertHasInformation();
	}
	
	@Test
	public void hasPressure() {
		sut.setPressure(dataPoint);
		
		assertHasInformation();
	}
	
	@Test
	public void hasTemperature() {
		sut.setTemperature(dataPoint);
		
		assertHasInformation();
	}
	
	@Test
	public void hasWind() {
		sut.setWind(dataPoint);
		
		assertHasInformation();
	}
	
	@Test
	public void isUpdatedInTheLastDay() {
		sut.setWind(dataPoint);
		
		assertHasInformation();
	}
	
	private void assertHasNoInformation() {
		assertThat(sut.hasInformation(), is(false));
	}

	private void assertHasInformation() {
		assertThat(sut.hasInformation(), is(true));
	}
	
}
