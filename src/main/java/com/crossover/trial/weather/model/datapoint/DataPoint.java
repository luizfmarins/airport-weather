package com.crossover.trial.weather.model.datapoint;

import static java.lang.Double.doubleToLongBits;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
public class DataPoint {

    public double mean = 0.0;
    public int first = 0;
    public int second = 0;
    public int third = 0;
    public int count = 0;

    private DataPoint() {}

    protected DataPoint(int first, int second, int mean, int third, int count) {
        this.setFirst(first);
        this.setMean(mean);
        this.setSecond(second);
        this.setThird(third);
        this.setCount(count);
    }

    /** the mean of the observations */
    public double getMean() {
        return mean;
    }

    public void setMean(double mean) { 
    	this.mean = mean; 
    }

    /** 1st quartile -- useful as a lower bound */
    public int getFirst() {
        return first;
    }

    protected void setFirst(int first) {
        this.first = first;
    }

    /** 2nd quartile -- median value */
    public int getSecond() {
        return second;
    }

    protected void setSecond(int second) {
        this.second = second;
    }

    /** 3rd quartile value -- less noisy upper value */
    public int getThird() {
        return third;
    }

    protected void setThird(int third) {
        this.third = third;
    }

    /** the total number of measurements */
    public int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, JSON_STYLE);
    }
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + first;
		long temp;
		temp = Double.doubleToLongBits(mean);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + second;
		result = prime * result + third;
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
		
		DataPoint other = (DataPoint) obj;
		if (count != other.count)
			return false;
		if (first != other.first)
			return false;
		if (doubleToLongBits(mean) != doubleToLongBits(other.mean))
			return false;
		if (second != other.second)
			return false;
		if (third != other.third)
			return false;
		
		return true;
	}

	public static Builder dataPointBuilder() {
		return new Builder();
	}

	public static class Builder {
        private int first;
        private int second;
        private int mean;
        private int median;
        private int last;
        private int count;
		private int third;

        public Builder() { }

        public Builder withFirst(int first) {
            this.first= first;
            return this;
        }

        public Builder withMean(int mean) {
        	this.mean = mean;
            return this;
        }

        public Builder withMedian(int median) {
        	this.median = median;
            return this;
        }

        public Builder withCount(int count) {
        	this.count = count;
            return this;
        }

        public Builder withLast(int last) {
        	this.last = last;
            return this;
        }
        
        public Builder withSecond(int second) {
        	this.second = second;
        	return this;
        }
        
        public Builder withhird(int third) {
			this.third = third;
			return this;
		}

		public DataPoint build() {
            DataPoint dataPoint = new DataPoint(this.first, this.second, this.mean, this.third, this.count);
            dataPoint.setMean(mean);
			return dataPoint;
        }
    }
}
