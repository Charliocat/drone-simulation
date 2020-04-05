package com.simulation.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.simulation.common.Coordinates;
import com.simulation.reporter.QuadTree;

public class StationRepoImpl implements StationRepo {

    private Set<Coordinates> tubeStations = new HashSet<>();
    private QuadTree quadtree = new QuadTree();

    public StationRepoImpl() {
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
        quadtree.insert(Double.valueOf(tokens[1]), Double.valueOf(tokens[2]), tokens[0].replaceAll("\"", ""));
    }

    @Override
    public Set<Coordinates> getStationsCoordinates() {
        return tubeStations;
    }

    @Override
    public QuadTree getQuadtree() {
        return quadtree;
    }

}
