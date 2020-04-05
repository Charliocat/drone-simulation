package com.simulation.events;

import java.util.concurrent.ThreadLocalRandom;

import com.simulation.common.Coordinates;

public class TrafficEvent {

    public enum TrafficCondition {
        HEAVY, LIGHT, MODERATE;

        public static TrafficCondition getRandom() {
            return values()[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

    public final String id;
    public final Coordinates coordinates;
    public final String time;
    public final int speed = ThreadLocalRandom.current().nextInt(10, 60);
    public final TrafficCondition trafficCondition = TrafficCondition.getRandom();

    public TrafficEvent(String id, Coordinates coordinates, String time) {
        this.id = id;
        this.coordinates = coordinates;
        this.time = time;
    }

    @Override
    public String toString() {
        return "TrafficReport{" +
               ", droneId='" + id + '\'' +
               ", time=" + time +
               ", speed=" + speed +
               ", trafficCondition=" + trafficCondition +
               '}';
    }

}
