package com.crossover.trial.weather.model.datapoint;

import com.crossover.trial.weather.model.AtmosphericInformation;

class Wind extends DataPointType {

	@Override
	protected void doUpdate(AtmosphericInformation info, DataPoint data) {
		info.setWind(data);
	}

	@Override
	protected boolean isValid(DataPoint data) {
		return data.getMean() >= 0;
	}

}
