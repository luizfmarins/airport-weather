package com.crossover.trial.weather.rest;

import java.util.HashMap;
import java.util.Map;

public class RadiusFrequency {

	private static final double MAXIMUN_RADIUS = 1000.0;
	
	private static Map<Double, Integer> radiusFreq = new HashMap<Double, Integer>();
	
	public int[] calculateRadiusFrequency() {
		int[] frequencies = new int[radiusFrequencySize()];
		
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
            int frequencyIndex = e.getKey().intValue();
            frequencies[frequencyIndex] += e.getValue();
        }
        
		return frequencies;
	}
	
	public void notifyWeatherRequest(Double radius) {
		radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0) + 1);
	}
	
	private int radiusFrequencySize() {
		return radiusFreq.keySet().stream()
                .max(Double::compare)
                .orElse(MAXIMUN_RADIUS).intValue() + 1;
	}

	public static void clear() {
		radiusFreq.clear();
	}
}