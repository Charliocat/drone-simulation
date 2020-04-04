package com.simulation.tube;

import com.simulation.common.Coordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class StationRepository {

    private Set<Coordinates> tubeStations = new HashSet<>();

    public StationRepository() {
        init();
    }

    private void init() {
        try (InputStream inputStream =  getClass().getClassLoader().getResourceAsStream("tube.csv")) {
            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    parseLine(line);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) {
        String[] tokens = line.split(",");
        tubeStations.add(new Coordinates(tokens[1], tokens[2]));
    }

}
