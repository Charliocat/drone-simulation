package com.simulation;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.simulation.control.Timer;
import com.simulation.control.Dispatcher;
import com.simulation.events.TimeEvent;
import com.simulation.agents.TrafficReporter;
import com.simulation.repository.LocationRepo;
import com.simulation.repository.LocationRepoImpl;
import com.simulation.repository.StationRepo;
import com.simulation.repository.StationRepoImpl;

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
        registeredDrones.add("6043");

        LocationRepo locationRepo = new LocationRepoImpl(registeredDrones);
        StationRepo stationRepo = new StationRepoImpl();

        ActorSystem system = ActorSystem.create("simulation");
        ActorRef dispatcher = system.actorOf(Dispatcher.props(registeredDrones, locationRepo), "mainDispatcher");
        system.actorOf(TrafficReporter.props(stationRepo).withDispatcher("TrafficReporter"), "trafficReporter");

        Timer timer = new Timer("2011-03-22 08:00:00", "2011-03-22 08:30:00");
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Instant event = timer.getTime();
                if (event.isAfter(timer.getEndTime()))
                    initShutdown();

                if (dispatcher.isTerminated())
                    initShutdown();
                else
                    dispatcher.tell(new TimeEvent(timer.getTime()), ActorRef.noSender());
            }

            private void initShutdown() {
                system.terminate();
                exec.shutdown();
            }
        }, 0, 40, TimeUnit.MILLISECONDS);
    }

}
