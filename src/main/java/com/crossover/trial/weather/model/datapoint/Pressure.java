package com.crossover.trial.weather.model.datapoint;

import com.crossover.trial.weather.model.AtmosphericInformation;

class Pressure extends DataPointType {

	@Override
	protected void doUpdate(AtmosphericInformation info, DataPoint data) {
		info.setPressure(data);
	}

	@Override
	protected boolean isValid(DataPoint data) {
		return data.getMean() >= 650 && data.getMean() < 800;
	}

}
