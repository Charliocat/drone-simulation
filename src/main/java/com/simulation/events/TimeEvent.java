package com.simulation.events;

import java.time.Instant;

public class TimeEvent {

    public TimeEvent(Instant time) {
        this.time = time;
    }

    private Instant time;

    public Instant getTime() {
        return time;
    }
}
