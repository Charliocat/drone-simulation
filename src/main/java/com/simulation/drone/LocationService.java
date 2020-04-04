package com.simulation.drone;

import com.simulation.common.Movement;

import java.util.Queue;

public interface LocationService {

    Queue<Movement> getLocations(String droneId);

}
