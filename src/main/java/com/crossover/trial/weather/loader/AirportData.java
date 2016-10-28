package com.crossover.trial.weather.loader;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import jersey.repackaged.com.google.common.base.Objects;

public class AirportData {

	private String city;
	private String country;
	private String iata;
	private String icao;
	private String latitude;
	private String longitude;
	private String altitude;
	private String timezone;
	private String dst;
	
	private AirportData () {}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public void setIata(String iata) {
		this.iata = iata;
	}
	
	public String getIata() {
		return iata;
	}

	public String getIcao() {
		return icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altitude == null) ? 0 : altitude.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((dst == null) ? 0 : dst.hashCode());
		result = prime * result + ((iata == null) ? 0 : iata.hashCode());
		result = prime * result + ((icao == null) ? 0 : icao.hashCode());
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((timezone == null) ? 0 : timezone.hashCode());
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
		
		AirportData other = (AirportData) obj;
		if (!Objects.equal(altitude , other.altitude)) 
			return false;
		if (!Objects.equal(city, other.city)) 
			return false;
		if (!Objects.equal(country, other.country))
			return false;
		if (!Objects.equal(dst , other.dst))
			return false;
		if (!Objects.equal(iata, other.iata))
			return false;
		if (!Objects.equal(icao, other.icao))
			return false;
		if (!Objects.equal(icao, other.icao))
			return false;
		if (!Objects.equal(latitude, other.latitude))
			return false;
		if (!Objects.equal(longitude, other.longitude))
			return false;
		if (!Objects.equal(timezone, other.timezone))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, JSON_STYLE);
	}

	public static class Builder {

		private String city;
		private String country;
		private String iata;
		private String icao;
		private String latitude;
		private String longitude;
		private String altitude;
		private String timezone;
		private String dst;

		public Builder withCity(String city) {
			this.city = city;
			return this;
		}

		public Builder withCountry(String country) {
			this.country = country;
			return this;
		}

		public Builder withIata(String iata) {
			this.iata = iata;
			return this;
		}

		public Builder withIcao(String icao) {
			this.icao = icao;
			return this;
		}

		public Builder withLatitude(String latitude) {
			this.latitude = latitude;
			return this;
		}

		public Builder withLongitude(String longitude) {
			this.longitude = longitude;
			return this;
		}

		public Builder withAltitude(String altitude) {
			this.altitude = altitude;
			return this;
		}

		public Builder withTimezone(String timezone) {
			this.timezone = timezone;
			return this;
		}

		public Builder withDst(String dst) {
			this.dst = dst;
			return this;
		}

		public AirportData build() {
			AirportData data = new AirportData();
			data.setCity(city);
			data.setCountry(country);
			data.setIata(iata);
			data.setIcao(icao);
			data.setLatitude(latitude);
			data.setLongitude(longitude);
			data.setAltitude(altitude);
			data.setTimezone(timezone);
			data.setDst(dst);
			return data;
		}
	}
}