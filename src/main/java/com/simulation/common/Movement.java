package com.simulation.common;

public class Movement {

    private Coordinates coordinates;
    private String time;

    public Movement(String time, Coordinates coordinates) {
        this.time = time;
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
