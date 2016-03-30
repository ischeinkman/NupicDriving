package com.ilanscheinkman.nupicdriving.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for storing car data.
 * Created by ilan on 2/8/16.
 */
public class CarReading {
    private long timeStamp;

    private Map<String, Double> attributes;

    /**
     * Put a new datapoint into this reading.
     * @param name the name of the field
     * @param value the value of the field at this current time
     */
    public void putReading(String name, double value){
        if (attributes == null) attributes = new HashMap<>();
        attributes.put(name, value);
    }

    /**
     * Read a datapoint in this reading.
     * @param name the name of the field
     * @return the value of the field currently stored
     */
    public double getReading(String name){
        if (attributes == null) attributes = new HashMap<>();
        return attributes.get(name);
    }

    /**
     * Get the full map of current datapoints. Does not include this reading's timestamp.
     * @return the current field-value map
     */
    public Map<String, Double> getReadingMap(){
        if(attributes == null) attributes = new HashMap<>();
        return attributes;
    }

    /**
     * Get the timestamp for this reading, measured in milliseconds since the unix epoch.
     * @return the timestamp currently stored
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Get the timestamp for this reading, measured in milliseconds since the unix epoch.
     * @param timeStamp the timestamp to set
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && o instanceof CarReading
                && ((CarReading) o).getTimeStamp() == this.getTimeStamp()
                && ((CarReading) o).getReadingMap().equals(this.getReadingMap()));
    }
}
