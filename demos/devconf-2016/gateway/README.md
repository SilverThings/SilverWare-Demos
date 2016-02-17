Complex demo to drive Intelligent Home on DevConf 2016
===

Prerequisities:
Install JBoss A-MQ, tested with version 6.2.1.redhat-084.
Start the broker with `bin/standalone.sh`.
Add an admin user mqtt/mqtt:

```
JBossA-MQ:karaf@root> amq:create-admin-user
Please specify a user...
New user name: mqtt
Password for mqtt: 
Verify password for mqtt: 
```

Edit app/src/main/java/io/silverware/demos/devconf/mqtt/MqttRoutes.java to set appropriate IP addresses or ports. Optionally, these can be provided as system properties.

* iot.host - IP address of the RPI based services in the home
* mqtt.host - IP address of the A-MQ broker
* mobile.host - IP address to which the internal REST server will be bind, this is the control interface for the mobile application

Compile with:

`mvn package`

Run the jar file in app/target. Optionally use the following parameters to fix logging of some components:

`java -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -jar app/target/gateway-app-2.0-SNAPSHOT.jar`