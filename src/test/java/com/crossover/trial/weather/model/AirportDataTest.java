package com.crossover.trial.weather.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AirportDataTest {

	private AirportData ap1 = new AirportData("AP1", 42.364347, -71.005181);
	private AirportData ap2 = new AirportData("AP2", 40.6925, -74.168667);		
	
	@Test
	public void isWithinRadiusZero_false() {
		assertThat(ap1.isWithinRadius(ap2, 0), is(false));
	}
	
	@Test
	public void isWithinRadius_notIn() {
		assertThat(ap1.isWithinRadius(ap2, 200), is(false));
	}
	
	@Test
	public void isWithinRadius_in() {
		assertThat(ap1.isWithinRadius(ap2, 201), is(true));
	}
	
	@Test
	public void isWithinRadius_equals() {
		assertThat(ap1.isWithinRadius(ap2, 200.87299713488193), is(true));
	}
	
	@Test
	public void isWithinRadius_nullAirport() {
		assertThat(ap1.isWithinRadius(null, 201), is(false));
	}
	
	
}