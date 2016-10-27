package com.crossover.trial.weather.util;

import com.crossover.trial.weather.DataPoint;

public final class DataPointUtil {

	private DataPointUtil() {}
	
	public static DataPoint windDatapoint() {
		return new DataPoint.Builder()
		.withFirst(0)
		.withLast(10)
		.withMean(6)
		.withMedian(4)
		.withCount(20).build();
	}
	
	public static DataPoint cloudOverDatapoint() {
		return new DataPoint.Builder()
				.withFirst(3)
				.withLast(15)
				.withMean(7)
				.withMedian(6)
				.withCount(40).build();
	}
	
	public static DataPoint pressureDatapoint() {
		return new DataPoint.Builder()
				.withFirst(7)
				.withLast(21)
				.withMean(700)
				.withMedian(8)
				.withCount(45).build();
	}
}