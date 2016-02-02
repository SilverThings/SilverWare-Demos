Demonstrates integration of Drools Knowledge JARs into SilverWare.
Any kjar present on the classpath is recognized by the KIE CDI extension and its sessions are prepared for injection.

In this example, there are two modules:
 * kjar - contains the knowledge module
 * app - the sample application injecting the kjar and firing its rule

Run the following command in the project root (where this file is present). Due to the project structure, the app module cannot be built standalone unless kjar is installed in your local repository.

`> mvn clean package`

Then start SilverWare quickstart by

`> java -jar app/target/drools-helloworld-*.jar`

See the following output showing that the rules were fired:
```2016-02-02 22:12:23,598 INFO  {io.silverware.demos.quickstarts.drools.DroolsHelloWorldMicroservice} DroolsHelloWorldMicroservice constructor
2016-02-02 22:12:24,486 INFO  {io.silverware.demos.quickstarts.drools.DroolsHelloWorldMicroservice} DroolsHelloWorldMicroservice MicroservicesStartedEvent KieSession[0]
Firing:test
2016-02-02 22:12:24,520 INFO  {io.silverware.demos.quickstarts.drools.DroolsHelloWorldMicroservice} Fired!```

