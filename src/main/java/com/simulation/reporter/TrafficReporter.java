package com.simulation.reporter;

import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.simulation.common.Coordinates;
import com.simulation.events.TrafficEvent;
import com.simulation.repository.StationRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficReporter extends AbstractActor {

    private static final Logger log = LoggerFactory.getLogger(TrafficReporter.class);
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
            log.info(trafficEvent.toString());
    }

    private boolean stationInRange(Coordinates dronePosition) {
        QuadTree quadtree = stationRepo.getQuadtree();
        List<Coordinates> found = quadtree.query(dronePosition);
        for (Coordinates node: found) {
            double distance2 = haversineDistance(node.getLatitude(),
                                                 node.getLongitude(),
                                                 dronePosition.getLatitude(), dronePosition.getLongitude());

            if (distance2 <= MAX_DISTANCE_TO_STATION)
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
