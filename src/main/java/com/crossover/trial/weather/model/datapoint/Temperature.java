package com.crossover.trial.weather.model.datapoint;

import com.crossover.trial.weather.model.AtmosphericInformation;

class Temperature extends DataPointType {

	@Override
	protected void doUpdate(AtmosphericInformation info, DataPoint data) {
		info.setTemperature(data);
	}

	@Override
	protected boolean isValid(DataPoint data) {
		return data.getMean() >= -50 && data.getMean() < 100;
	}
}