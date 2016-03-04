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

Provide the following system properties either by setting the environment or by using -Dpropert=value parameters.

* iot.host - IP address and port of the RPI based services in the home
* iot.mqtt.host - IP address and port of the RPi based MQTT broker that broadcasts messages on the home state, typically same as iot.host but with a different port number
* mqtt.host - IP address and port of the A-MQ broker
* mobile.host - IP address and port to which the internal REST server will be bind, this is the control interface for the mobile application

Compile with:

`mvn package`

Run the jar file in app/target. Use the following parameters to fix logging of some components and provide system properties:

`java -Diot.host=10.40.1.23:8282 -Diot.mqtt.host=10.40.1.23:1883 -Dmqtt.host=127.0.0.1:1883 -Dmobile.host=0.0.0.0:8283 -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -jar app/target/gateway-app-2.0-SNAPSHOT.jar`