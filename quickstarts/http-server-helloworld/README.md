# Hello world http-server-microservice-provider
A simple application demonstrating the use of **http-server-microservice-provider**.

The application contains only one class, the `HelloworldRestService` microservice. This microservice is annotated with `javax.ws.rs` annotations, thus it will be deployed into SilverWare http server and exposed via REST.

Build the application
```
mvn clean package
```

##Exposing microservice without SSL
Run the application
```
java -jar target/rest-client-helloworld-*.jar
```

Go to http://localhost:8080/silverware/rest/hello_service/hello

##Exposing microservice with SSL
To protect exposed microservice via SSL, you must pass a set of SSL properties to **http-server-microservice-provider**. We will set these properties using Maven command line properties



Run the application
```
java -jar http-server-helloworld-2.1-SNAPSHOT.jar -Dsilverware.http.server.keystore=quickstart-server.keystore -Dsilverware.http.server.truststore=quickstart-server.truststore -Dsilverware.http.server.keystore.password=silverware -Dsilverware.http.server.truststore.password=silverware -Dsilverware.http.server.ssl.enabled=true
```

You can see SSL was configured in the application logs:
```
2016-11-12 17:27:00,947 INFO  {io.silverware.microservices.providers.http.HttpServerMicroserviceProvider} Property 'silverware.http.server.ssl.enable' set to 'true', enabling SSL.
2016-11-12 17:27:00,950 INFO  {io.silverware.microservices.providers.http.HttpServerMicroserviceProvider} SSL configuration provided by user:
2016-11-12 17:27:00,950 INFO  {io.silverware.microservices.providers.http.HttpServerMicroserviceProvider} Keystore: quickstart-server.keystore
2016-11-12 17:27:00,950 INFO  {io.silverware.microservices.providers.http.HttpServerMicroserviceProvider} Truststore: quickstart-server.truststore
```

Go to http://localhost:8080/silverware/rest/hello_service/hello, you will be redirected to https://localhost:10443/silverware/rest/hello_service/hello, or you can go directly to https://localhost:10443/silverware/rest/hello_service/hello