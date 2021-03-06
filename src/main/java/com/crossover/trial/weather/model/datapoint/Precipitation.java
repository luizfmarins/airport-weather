package com.crossover.trial.weather.model.datapoint;

import com.crossover.trial.weather.model.AtmosphericInformation;

class Precipitation extends DataPointType {

	@Override
	protected void doUpdate(AtmosphericInformation info, DataPoint data) {
		info.setPrecipitation(data);
	}

	@Override
	protected boolean isValid(DataPoint data) {
		return data.getMean() >=0 && data.getMean() < 100;
	}
}