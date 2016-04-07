/*
 * -----------------------------------------------------------------------\
 * SilverWare
 *  
 * Copyright (C) 2010 - 2013 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package io.silverware.demos.devconf.mqtt;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * These routes wire the individual components together. Actions go to actions topic, then to business rules engine (Drools), resulting commands go to the
 * commands queue. Also, Drools can send status updates to the mobile phone. Weather sensor is periodically checked (pull model) and the corresponding actions
 * are placed in the actions topic.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class IntegrationRoutes extends RouteBuilder {

   @Override
   public void configure() throws Exception {
      final String iotHost = System.getProperty("iot.host", "10.40.2.210:8282");
      final String iotMqttHost = System.getProperty("iot.mqtt.host", "10.40.2.210:1883");
      final String mqttHost = System.getProperty("mqtt.host", "10.40.3.60:1883");
      final String mobileHost = System.getProperty("mobile.host", "0.0.0.0:8283");

      // First, we need to start consumers from "direct" to avoid warnings while Camel processes exchange

      // creates a new action
      from("direct:actions").marshal().serialization().to("mqtt:outActions?publishTopicName=ih/message/actions&userName=mqtt&password=mqtt&host=tcp://" + mqttHost);

      // creates a new command
      from("direct:commands").marshal().serialization().to("mqtt:outCommands?publishTopicName=ih/message/commands&userName=mqtt&password=mqtt&host=tcp://" + mqttHost);

      // sends an update message to the mobile phone
      from("direct:mobile").to("mqtt:outMobile?publishTopicName=ih/message/mobile&userName=mqtt&password=mqtt&host=tcp://" + mqttHost);

      // process weather
      from("direct:weather").bean("weatherMicroservice", "processWeather");

      // process rfid
      from("direct:rfid").bean("weatherMicroservice", "processRfid");

      // expose REST API for the mobile phone to be able to send actions
      from("jetty:http://" + mobileHost + "/mobile").setBody().simple("${in.header.button}").bean("mobileGatewayMicroservice", "mobileAction");

      // periodically check Intelligent Home's REST interface to obtain weather status, process the status as an action
      //from("timer://foo?period=5000").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/sensorData").to("direct:weather");
      // comment out the previous route and enable the following one for debugging purposes
      // from("timer://foo?period=5000").setBody().constant("{ \"temperature\" : 23, \"humidity\" : 42, ").to("direct:weather");

      // Append the following to the previous route to get debug output
      //      .setBody().simple("Weather: ${body}").to("stream:out");

      // process actions in Drools
      from("mqtt:inActions?subscribeTopicName=ih/message/actions&userName=mqtt&password=mqtt&host=tcp://" + mqttHost).unmarshal().serialization().bean("droolsMicroservice", "processAction");

      // read weather from a topic deployed in the home
      from("mqtt:inWeather?subscribeTopicName=ih/message/weather&userName=mqtt&password=mqtt&host=tcp://" + iotMqttHost).to("direct:weather");

      // read RFID tags from a topic deployed in the home
      from("mqtt:inRfid?subscribeTopicName=ih/message/rfidTags&userName=mqtt&password=mqtt&host=tcp://" + iotMqttHost).to("direct:rfid");
   }

}