package com.crossover.trial.weather.model;

import static java.lang.System.currentTimeMillis;

public class Clock {

	private static final int DAY_MILISECONDS = 86400000;
	private static Clock instance;
	
	private Clock() {}
	
	public static Clock newInstance() {
		if (instance != null)
			return instance;
		return new Clock();
	}
	
	public static void setInstance(Clock clock) {
		Clock.instance = clock;
	}

	public boolean isBeforeLastDay(long time) {
		return time > currentTimeMillis() - DAY_MILISECONDS;
	}
}