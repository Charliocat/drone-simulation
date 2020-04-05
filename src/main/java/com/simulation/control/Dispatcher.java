package com.simulation.control;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.simulation.common.Timer;
import com.simulation.drone.Drone;
import com.simulation.repository.LocationRepo;
import com.simulation.events.TimeEvent;

import java.time.Instant;
import java.util.List;

public class Dispatcher extends AbstractLoggingActor {

    public enum Events {
        SHUT_DOWN, NEXT_MOVE
    }

    private Instant endTime = Timer.getInstantFromString("2011-03-22 08:10:00");
    private List<String> registeredDrones;
    private LocationRepo locationRepo;

    public Dispatcher(List<String> registeredDrones, LocationRepo locationRepo) {
        this.registeredDrones = registeredDrones;
        this.locationRepo = locationRepo;
    }

    public static Props props(List<String> registeredDrones, LocationRepo locationRepo) {
        return Props.create(Dispatcher.class, registeredDrones, locationRepo);
    }

    @Override
    public void preStart() {
        for(String droneId: registeredDrones) {
            initDroneHandler(droneId);
        }
    }

    private void initDroneHandler(String droneId) {
        getContext().actorOf(Drone.props(droneId, locationRepo.getLocations(droneId))
                                  .withDispatcher("DroneDispatcher" + droneId)
                                  .withMailbox("bounded-mailbox"), "Drone" + droneId);
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
            return;
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
