package com.simulation.drone;

import com.simulation.common.Coordinates;
import com.simulation.common.Movement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class LocationServiceImpl implements LocationService {

    private final List<String> registeredDrones;
    private Map<String, Queue<Movement>> locations = new HashMap<>();

    public LocationServiceImpl(List<String> registeredDrones) {
        this.registeredDrones = registeredDrones;
        for(String droneId: registeredDrones) {
            loadMovementsForDrone(droneId);
        }
    }

    @Override
    public Queue<Movement> getLocations(String droneId) {
        return locations.get(droneId);
    }

    private void loadMovementsForDrone(String droneId){
        locations.put(droneId, readCoordinates(droneId));
    }

    private Queue<Movement> readCoordinates(String droneId) {
        Queue<Movement> droneMovements = new LinkedList<>();
        try (InputStream inputStream =  getClass().getClassLoader().getResourceAsStream(droneId + ".csv")) {
            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    droneMovements.add(parseLine(line));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return droneMovements;
    }

    private Movement parseLine(String line) {
        line = line.replaceAll("\"", "");
        String[] tokens = line.split(",");
        Coordinates coordinates= new Coordinates(tokens[1], tokens[2]);
        return new Movement(tokens[3], coordinates);
    }

}
