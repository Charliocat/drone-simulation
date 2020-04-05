package com.simulation.drone;

import java.util.Queue;

import com.simulation.common.Movement;
import com.simulation.control.Dispatcher;
import com.simulation.events.TrafficEvent;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class Drone extends AbstractLoggingActor {

    private String droneId;
    private ActorSelection trafficReporter;
    private Queue<Movement> locations;

    public Drone(String droneId, Queue<Movement> locations) {
        this.droneId = droneId;
        this.locations = locations;
    }

    public static Props props(String droneId, Queue<Movement> locations) {
        return Props.create(Drone.class, droneId, locations);
    }

    @Override
    public void preStart() {
        trafficReporter = getContext().actorSelection("../../trafficReporter");
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .matchEquals(Dispatcher.Events.NEXT_MOVE, this::handleNextPosition)
                .matchEquals(Dispatcher.Events.SHUT_DOWN, this::shutDown)
                .build();
    }

    private void handleNextPosition(Dispatcher.Events event) {
        if (locations.isEmpty()) {
            shutDown(event);
            return;
        }

        trafficReportEvent(locations.poll());
        //drone.tell(locations.poll(), self());
    }

    private void trafficReportEvent(Movement movement) {
        TrafficEvent trafficEvent = new TrafficEvent(droneId, movement.getCoordinates(), movement.getTime());
        trafficReporter.tell(trafficEvent, self());
    }

    private void shutDown(Dispatcher.Events events) {
        log().info("Drone {} is shutting down", droneId);
        context().stop(self());
    }

}
