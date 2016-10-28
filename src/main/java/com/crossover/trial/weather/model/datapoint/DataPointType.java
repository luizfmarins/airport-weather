package com.crossover.trial.weather.model.datapoint;

import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.WeatherException;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
// TODO Tests
public abstract class DataPointType {

	public void update(AtmosphericInformation info, DataPoint data) throws WeatherException {
		validate(data);
		doUpdate(info, data);
		setLastUpdateTime(info);
	}

	private void setLastUpdateTime(AtmosphericInformation info) {
		info.setLastUpdateTime(System.currentTimeMillis());
	}

	protected abstract void doUpdate(AtmosphericInformation info, DataPoint data);

	protected abstract boolean isValid(DataPoint data);
	
	private void validate(DataPoint data) throws WeatherException {
		if (!isValid(data))
			throw new WeatherException("Couldn't update atmospheric data");
			
	}
	
	public static DataPointType valueOf(String pointType) {
		return Type.valueOf(pointType.toUpperCase()).type;
	}
	
	public static enum Type {
		 WIND(new Wind()),
		 TEMPERATURE(new Temperature()),
		 HUMIDTY(new Humidty()),
		 PRESSURE(new Pressure()),
		 CLOUDCOVER(new CloudCover()),
		 PRECIPITATION(new Precipitation());
		 
		 private DataPointType type;
		 
		 private Type(DataPointType type) {
			 this.type = type;
		 }

		 public DataPointType getDataPointType() {
			 return this.type;
		 }
	}
	
}