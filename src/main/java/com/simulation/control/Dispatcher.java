package com.simulation.control;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.simulation.common.Timer;
import com.simulation.drone.Drone;
import com.simulation.drone.DroneHandler;
import com.simulation.drone.LocationService;
import com.simulation.events.TimeEvent;

import java.time.Instant;
import java.util.List;

public class Dispatcher extends AbstractLoggingActor {

    public enum Events {
        SHUT_DOWN, NEXT_MOVE
    }

    private Instant endTime = Timer.getInstantFromString("2011-03-22 08:10:00");
    private List<String> registeredDrones;
    private LocationService locationService;

    public Dispatcher(List<String> registeredDrones, LocationService locationService) {
        this.registeredDrones = registeredDrones;
        this.locationService = locationService;
    }

    public static Props props(List<String> registeredDrones, LocationService locationService) {
        return Props.create(Dispatcher.class, registeredDrones, locationService);
    }

    @Override
    public void preStart() {
        for(String droneId: registeredDrones) {
            initDroneHandler(droneId);
        }
    }

    private void initDroneHandler(String droneId) {
        getContext().actorOf(DroneHandler.props(droneId, locationService.getLocations(droneId)), "Dispatcher" + droneId);
        getContext().actorOf(Drone.props(droneId).withDispatcher("DroneDispatcher" + droneId), "Drone" + droneId);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TimeEvent.class, this::handleEvent)
                .matchEquals(Events.SHUT_DOWN, s -> sendTerminate())
                .build();
    }

    private void handleEvent(TimeEvent event) {
        if (event.getTime().isAfter(endTime)) {
            log().info("Time to shut down: {}", endTime);
            sendTerminate();
        }

        getContext().getChildren().forEach(
                handler -> handler.tell(Events.NEXT_MOVE, self())
        );
    }

    private void sendTerminate() {
        getContext().getChildren().forEach(
                handler -> handler.tell(Events.SHUT_DOWN, self())
        );
        context().stop(self());
    }

}
