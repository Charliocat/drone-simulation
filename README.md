
## Requirements
- Java 8+ : [JDK 8]
- Maven 3.6.0+ : [Maven]

## Run and Compile
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

[JDK 8]: https://jdk.java.net/8/
[Maven]: https://maven.apache.org/install.html