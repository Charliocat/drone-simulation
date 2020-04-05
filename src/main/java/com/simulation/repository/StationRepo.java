package com.simulation.repository;

import java.util.Set;

import com.simulation.common.Coordinates;
import com.simulation.reporter.QuadTree;

public interface StationRepo {

    Set<Coordinates> getStationsCoordinates();
    QuadTree getQuadtree();

}
