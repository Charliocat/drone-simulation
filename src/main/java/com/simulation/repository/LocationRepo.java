package com.simulation.repository;

import com.simulation.common.Movement;

import java.util.Queue;

public interface LocationRepo {
    Queue<Movement> getLocations(String droneId);
}
