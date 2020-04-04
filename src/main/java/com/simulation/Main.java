package com.simulation;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.simulation.common.Timer;
import com.simulation.control.Dispatcher;
import com.simulation.drone.LocationService;
import com.simulation.drone.LocationServiceImpl;
import com.simulation.events.TimeEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        List<String> registeredDrones = new ArrayList<>();
        registeredDrones.add("5937");
        //registeredDrones.add("5937");

        LocationService locationService = new LocationServiceImpl(registeredDrones);

        ActorSystem system = ActorSystem.create("simulation");
        ActorRef dispatcher = system.actorOf(Dispatcher.props(registeredDrones, locationService), "mainDispatcher");

        Timer timer = new Timer("2011-03-22 08:05:00", "2011-03-22 08:30:00");
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Instant event = timer.getTime();
                if (event.isAfter(timer.getEndTime()))
                    initShutdown();

                dispatcher.tell(new TimeEvent(timer.getTime()), ActorRef.noSender());
            }

            private void initShutdown() {
                system.terminate();
                exec.shutdown();
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

}
