package com.simulation.drone;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.simulation.control.Dispatcher;
import com.simulation.common.Movement;

import java.time.Instant;
import java.util.Queue;

public class DroneHandler extends AbstractActor {

    private String droneId;
    private ActorSelection drone;
    private Queue<Movement> locations;

    public DroneHandler(String droneId, Queue<Movement> locations) {
        this.droneId = droneId;
        this.locations = locations;
    }

    public static Props props(String droneId, Queue<Movement> locations) {
        return Props.create(DroneHandler.class, droneId, locations);
    }

    @Override
    public void preStart() {
        drone = getContext().actorSelection("../Drone" + droneId);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(String.class, s -> {
                        drone.tell(new Drone.SayHello(s), ActorRef.noSender());
                        })
                .matchEquals(Dispatcher.Events.NEXT_MOVE, this::sendNextPosition)
                .match(Instant.class, instant -> {
                    drone.tell(instant, self());
                })
                .matchEquals(Dispatcher.Events.SHUT_DOWN, s -> context().stop(self()))
                .build();
    }

    private void sendNextPosition(Dispatcher.Events event) {
        if (locations.isEmpty()) {
            drone.tell(Dispatcher.Events.SHUT_DOWN, self());
            return;
        }

        Movement nextMovement = locations.poll();
        drone.tell(nextMovement, self());
    }

}
