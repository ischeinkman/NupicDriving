package com.ilanscheinkman.nupicdriving.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilan on 2/8/16.
 */
public class CarReading {
    private long timeStamp;

    private Map<String, Double> attributes;

    public void putReading(String name, double value){
        if (attributes == null) attributes = new HashMap<>();
        attributes.put(name, value);
    }

    public double getReading(String name){
        if (attributes == null) attributes = new HashMap<>();
        return attributes.get(name);
    }

    public Map<String, Double> getReadingMap(){
        if(attributes == null) attributes = new HashMap<>();
        return attributes;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

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
