package com.crossover.trial.weather.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.crossover.trial.weather.model.datapoint.DataPoint;

@RunWith(MockitoJUnitRunner.class)
public class AtmosphericInformationTest {

	private static final int LAST_UPDATE_TIME = 5;
	@Mock
	private DataPoint dataPoint;
	@Mock
	private Clock clock;
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
		sut.setLastUpdateTime(LAST_UPDATE_TIME);
		when(clock.isBeforeLastDay(LAST_UPDATE_TIME))
			.thenReturn(true);
		Clock.setInstance(clock);
		
		assertThat(sut.isUpdatedInTheLastDay(), is(true));
	}
	
	@Test
	public void isNotUpdatedInTheLastDay() {
		sut.setLastUpdateTime(LAST_UPDATE_TIME);
		when(clock.isBeforeLastDay(LAST_UPDATE_TIME))
			.thenReturn(false);
		Clock.setInstance(clock);
		
		assertThat(sut.isUpdatedInTheLastDay(), is(false));
	}

	@After
	public void tearDown() {
		Clock.setInstance(null);
	}
	
	private void assertHasNoInformation() {
		assertThat(sut.hasInformation(), is(false));
	}

	private void assertHasInformation() {
		assertThat(sut.hasInformation(), is(true));
	}
	
}
