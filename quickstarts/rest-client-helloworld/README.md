# Hello world rest-client-microservice-provider
A simple application demonstrating the use of **rest-client-microservice-provider**.

The application consists of the following classes:

* `ClientService` - a REST based microservice that will be used to access the application
* `HelloRestService` - this is an arbitrary REST service which can be implemented in any language and located anywhere in the Internet, in this quickstart, the REST service is implemented in Java and is deployed within SilverWare using **http-server-microservice-provider**.
* `HelloRestServiceProxy` - an interface that describes an arbitrary REST service (the `HelloRestService`) using the `javax.ws.rs` annotations, SilverWare will create a proxy that is injected into the `ClientService` microservice using **cdi-microservice-provider**, this proxy translates the interface method calls to the REST calls via HTTP

Build and run the application
```
mvn clean package
java -jar target/rest-client-helloworld-*.jar
```

Go to http://localhost:8080/silverware/rest/client_service/call_rest_service