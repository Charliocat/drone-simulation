# Drone Simulator


## Requirements
- Java 8+ : [JDK 8]
- Maven 3.6.0+ : [Maven]

## Running Application
- Run with Maven:
```
mvn exec:java -Dexec.mainClass=com.simulation.Main
```

- Compile to generate jar with all dependencies:
```
mvn clean compile assembly:single
```

- Run jar with:
```
java -jar target/drone-simulation-1.0-SNAPSHOT-jar-with-dependencies.jar
```
## Applicatoin Details
### Problem description
#### Scenario
There are two automatic drones that fly around London and report on traffic conditions. When a drone flies over a tube station, it assesses what the traffic condition is like in the area, and reports on it.
#### Task
Write a simulation that has one dispatcher and two drones. Each drone should "move" independently on different processes. The dispatcher should send the coordinates to each drone detailing where the drone's next position should be. The dispatcher should also be responsible for terminating the program. 
When the drone receives a new coordinate, it moves, checks if there is a tube station in the area, and if so, reports on the traffic conditions there. 
#### Notes
- The simulation should finish @ 08:10, where the drones will receive a "SHUTDOWN" signal.
- The two drones have IDs 6043 and 5937. There is a file containing their lat/lon points for their routes. The csv file layout is drone id,latitude,longitude,time
- There is also a file with the lat/lon points for London tube stations. station,lat,lon
- Traffic reports should have the following format:
    - Drone ID
    - Time
    - Speed
    - Conditions of Traffic (HEAVY, LIGHT, MODERATE). This can be chosen randomly.
###Remarks
- Assume that the drones follow a straight line between each point, travelling at constant speed.
- Disregard the fact that the start time is not in synch. The dispatcher can start pumping data as soon as it has read the files.
- A nearby station should be less than 350 meters from the drone's position.
- Bonus point: Put a constraint on each drone to have limited memory, so they can only consume ten points at a time.

### Application Design
The application has been implemented using the [Akka] framework. 

Thanks to [Akka] send message from one proces to another is easy. In this case is used to simulate the event communication between all the application agents.

The parts of the application are: 
- `Main`: Handles the ticker, Dispatcher and TrafficReporter, sends simulated time events to the Dispatcher. 
- `Dispatcher`: Handles data related to drones. Also in charge to stop the simulation when is time.
- `Dron`: Handles data and sends their position to the TrafficReporter
- `TrafficReporter`: Handles data from drones and checks their distance to the stations. Prints Report if corresponds.


[JDK 8]: https://jdk.java.net/8/
[Maven]: https://maven.apache.org/install.html
[Akka]: https://akka.io/