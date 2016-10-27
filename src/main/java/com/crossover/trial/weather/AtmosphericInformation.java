package com.crossover.trial.weather;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericInformation {

    /** temperature in degrees celsius */
    private DataPoint temperature;

    /** wind speed in km/h */
    private DataPoint wind;

    /** humidity in percent */
    private DataPoint humidity;

    /** precipitation in cm */
    private DataPoint precipitation;

    /** pressure in mmHg */
    private DataPoint pressure;

    /** cloud cover percent from 0 - 100 (integer) */
    private DataPoint cloudCover;

    /** the last time this data was updated, in milliseconds since UTC epoch */
    private long lastUpdateTime;

    public AtmosphericInformation() {}

    protected AtmosphericInformation(DataPoint temperature, DataPoint wind, DataPoint humidity, DataPoint percipitation, DataPoint pressure, DataPoint cloudCover) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.precipitation = percipitation;
        this.pressure = pressure;
        this.cloudCover = cloudCover;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public DataPoint getTemperature() {
        return temperature;
    }
    public void setTemperature(DataPoint temperature) {
        this.temperature = temperature;
    }
    public DataPoint getWind() {
        return wind;
    }
    public void setWind(DataPoint wind) {
        this.wind = wind;
    }
    public DataPoint getHumidity() {
        return humidity;
    }
    public void setHumidity(DataPoint humidity) {
        this.humidity = humidity;
    }
    public DataPoint getPrecipitation() {
        return precipitation;
    }
    public void setPrecipitation(DataPoint precipitation) {
        this.precipitation = precipitation;
    }
    public DataPoint getPressure() {
        return pressure;
    }
    public void setPressure(DataPoint pressure) {
        this.pressure = pressure;
    }
    public DataPoint getCloudCover() {
        return cloudCover;
    }
    public void setCloudCover(DataPoint cloudCover) {
        this.cloudCover = cloudCover;
    }
    protected long getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    protected void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    @Override
    public String toString() {
    	return ReflectionToStringBuilder.toString(this, JSON_STYLE);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cloudCover == null) ? 0 : cloudCover.hashCode());
		result = prime * result + ((humidity == null) ? 0 : humidity.hashCode());
		result = prime * result + ((precipitation == null) ? 0 : precipitation.hashCode());
		result = prime * result + ((pressure == null) ? 0 : pressure.hashCode());
		result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
		result = prime * result + ((wind == null) ? 0 : wind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
		if (obj == null) 
			return false;
		if (getClass() != obj.getClass()) 
			return false;
		AtmosphericInformation other = (AtmosphericInformation) obj;
		if (!Objects.equals(cloudCover, other.cloudCover))
			return false;
		if (!Objects.equals(humidity, other.humidity))
			return false;
		if (!Objects.equals(precipitation, other.precipitation))
			return false;
		if (!Objects.equals(pressure, other.pressure))
			return false;
		if (!Objects.equals(temperature, other.temperature))
			return false;
		if (!Objects.equals(wind, other.wind))
			return false;
		
		return true;
	}
	
	public static Builder atmosphericInformationBuilder() {
		return new Builder();
	}
	
	public static class Builder {
	
		private DataPoint temperature; 
        private DataPoint wind;
        private DataPoint humidity;
        private DataPoint precipitation; 
        private DataPoint pressure;
        private DataPoint cloudCover;
         
        private Builder() {}
        
		public Builder withTemperature(DataPoint temperature) {
			this.temperature = temperature;
			return this;
		}

		public Builder withWind(DataPoint wind) {
			this.wind = wind;
			return this;
		}

		public Builder withHumidity(DataPoint humidity) {
			this.humidity = humidity;
			return this;
		}

		public Builder setPrecipitation(DataPoint precipitation) {
			this.precipitation = precipitation;
			return this;
		}

		public Builder withPressure(DataPoint pressure) {
			this.pressure = pressure;
			return this;
		}

		public Builder withCloudCover(DataPoint cloudCover) {
			this.cloudCover = cloudCover;
			return this;
		}

		public AtmosphericInformation build() {
			return new AtmosphericInformation(temperature, wind, humidity, precipitation, pressure, cloudCover);
		}
	}
}