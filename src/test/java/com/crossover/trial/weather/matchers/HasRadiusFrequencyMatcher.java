package com.crossover.trial.weather.matchers;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class HasRadiusFrequencyMatcher extends BaseMatcher<List<Integer>> {

	private final int radius;
	private final int frequency;

	private HasRadiusFrequencyMatcher(int radius, int frequency) {
		this.radius = radius;
		this.frequency = frequency;}
	
	@Override
	public boolean matches(Object item) {
		@SuppressWarnings("unchecked")
		List<Integer> frequencies = (List<Integer>) item;
		return frequencies.get(radius) == frequency;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Expected frequency for radius '" + radius + "': <" + frequency + ">");
	}
	
	public static HasRadiusFrequencyMatcher hasRadiusFreq(int radius, int frequency) {
		return new HasRadiusFrequencyMatcher(radius, frequency);
	}
}