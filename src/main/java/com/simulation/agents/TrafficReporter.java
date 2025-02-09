package com.simulation.agents;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.simulation.common.Coordinates;
import com.simulation.events.TrafficEvent;
import com.simulation.repository.StationRepo;

public class TrafficReporter extends AbstractLoggingActor {

    private static final int MAX_DISTANCE_TO_STATION = 350;// meters

    private StationRepo stationRepo;

    public TrafficReporter(StationRepo stationRepo) {this.stationRepo = stationRepo;}

    public static Props props(StationRepo stationRepo) {
        return Props.create(TrafficReporter.class, stationRepo);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TrafficEvent.class, this::handleEvent)
                 .build();
    }

    private void handleEvent(TrafficEvent trafficEvent) {
        if (stationInRange(trafficEvent.coordinates))
            log().info(trafficEvent.toString());
    }

    private boolean stationInRange(Coordinates dronePosition) {
        for(Coordinates stationCoordinates : stationRepo.getStationsCoordinates()) {
            double distance = haversineDistance(stationCoordinates.getLatitude(), stationCoordinates.getLongitude(),
                                       dronePosition.getLatitude(), dronePosition.getLongitude());

            if (distance <= MAX_DISTANCE_TO_STATION)
                return true;
        }
        return false;
    }

    /**
     * Return distance in meters.
     */
    public static double haversineDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return dist;
    }

}
