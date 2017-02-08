Sample quickstart for cdi-microservice monitoring. Keep in mind that after minute expires every recorded value, but metrics calculated from this values persist.

First you need to build a quickstart:
```
mvn clean package
```

Then run quickstart:
```
java -jar target/cdi-monitoring-*.jar
```

By default there are two microservices in this example. One of them is being monitored and second one is not.

Monitored microservice can be invoked by: 
```
curl http://localhost:8080/silverware/rest/monitoring/monitored_service
```

Not monitored microservice:
```
curl http://localhost:8080/silverware/rest/monitoring/notmonitored_service
```

Recorded metrics:
```
curl http://localhost:8080/silverware/rest/monitoring/show_metrics
```

All recorded values:
```
curl http://localhost:8080/silverware/rest/monitoring/show_values
```

Number of values recorded for every microservice:
```
curl http://localhost:8080/silverware/rest/monitoring/show_values_size
```

Quickstart also exposes measured metrics into JMX by default. All you have to do is connect to this process with JMX console and find method getMetrics of JMXPublisherBean. Notice that you still have to invoke monitored service in order to get some measured metrics.