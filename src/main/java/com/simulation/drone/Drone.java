package com.simulation.drone;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;
import akka.japi.pf.ReceiveBuilder;
import com.simulation.control.Dispatcher;
import com.simulation.common.Movement;

import java.time.Instant;

public class Drone extends AbstractLoggingActor implements RequiresMessageQueue<BoundedMessageQueueSemantics> {

    interface Message { }

    static class SayHello implements Message {
        public final String message;

        SayHello(String message) {
            this.message = message;
        }
    }

    private String id;


    public Drone(String id) {
        this.id = id;
    }

    public static Props props(String id) {
        return Props.create(Drone.class, id);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(SayHello.class, this::parseMessage)
                .matchEquals(Dispatcher.Events.SHUT_DOWN, this::shutDown)
                .match(Movement.class, nextPosition -> {
                    log().info("Drone {} moving to coordinates: {}", id, nextPosition.getCoordinates());
                })
                .build();
    }

    private void shutDown(Dispatcher.Events events) {
        log().info("Drone {} is shutting down", id);
        context().stop(self());
    }

    private void handleEvent(Instant instant) {
    }

    private void parseMessage(SayHello message) {
        log().info(message.message + " from drone: " + id);
    }

}
