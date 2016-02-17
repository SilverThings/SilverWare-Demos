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

import io.silverware.demos.devconf.kjar.AirConditioningCommand;
import io.silverware.demos.devconf.kjar.DoorCommand;
import io.silverware.demos.devconf.kjar.FireplaceCommand;
import io.silverware.demos.devconf.kjar.LightCommand;
import io.silverware.demos.devconf.kjar.MediaCenterCommand;

/**
 * These routes are listening on the commands queue and executing the needed workflow to fulfill the commands.
 * The workflow is simulated in Camel routes but here we could execute any business process or Node-Red flow.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class WorkflowRoutes extends RouteBuilder {

   @Override
   public void configure() throws Exception {
      final String iotHost = System.getProperty("iot.host", "10.40.2.210:8282");
      final String mqttHost = System.getProperty("mqtt.host", "10.40.3.60:1883");

      // where does the command belong to?
      from("mqtt:inCommands?subscribeTopicName=ih/message/commands&userName=mqtt&password=mqtt&host=tcp://" + mqttHost).unmarshal().serialization()
            .choice()
            .when().simple("${body} is 'io.silverware.demos.devconf.kjar.AirConditioningCommand'").to("direct:ac")
            .when().simple("${body} is 'io.silverware.demos.devconf.kjar.DoorCommand'").to("direct:door")
            .when().simple("${body} is 'io.silverware.demos.devconf.kjar.FireplaceCommand'").to("direct:fire")
            .when().simple("${body} is 'io.silverware.demos.devconf.kjar.LightCommand'").to("direct:led")
            .when().simple("${body} is 'io.silverware.demos.devconf.kjar.BatchLightCommand'").to("direct:ledBatch")
            .when().simple("${body} is 'io.silverware.demos.devconf.kjar.MediaCenterCommand'").to("direct:media")
            .when().simple("${body} is 'io.silverware.demos.devconf.kjar.UpdateStatusCommand'").setBody().simple("${body.updateMessage}").to("direct:mobile");

      // led lights
      from("direct:led").choice()
                        .when().simple("${body.place} == '" + LightCommand.Place.ALL + "'").to("direct:ledAll")
                        .otherwise().to("direct:ledSingle");
      from("direct:ledSingle").log("Unimplemented ledSingle route");
      from("direct:ledAll").setHeader(Exchange.HTTP_METHOD, constant("GET"))
                           .setHeader("r", simple("${body.state.r}"))
                           .setHeader("g", simple("${body.state.g}"))
                           .setHeader("b", simple("${body.state.b}"))
                           .setBody().constant("").to("jetty:http://" + iotHost + "/led/setrgb/all");
      from("direct:ledBatch").setHeader(Exchange.HTTP_METHOD, constant("POST")).setBody().simple("${body.batch}").to("jetty:http://" + iotHost + "/led/batch");

      /*from("direct:led").setHeader(Exchange.HTTP_METHOD, constant("POST")).to("jetty:http://" + iotHost + "/led/batch");
      from("direct:ledAllOff").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/led/setrgb/all?r=0&g=0&b=0");
      from("direct:ledAllRomantic").setHeader(Exchange.HTTP_METHOD, constant("GET")).setHeader("r", constant("50")).setHeader("g", constant("10")).setHeader("b", constant("10")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/led/setrgb/all");
      from("direct:ledAllOn").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/led/setrgb/all?r=100&g=100&b=100");
      from("direct:ledAllEvening").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/led/setrgb/all?r=80&g=80&b=50");*/

      // air conditioning
      from("direct:ac").choice()
                       .when().simple("${body.ac} == '" + AirConditioningCommand.Ac.NORMAL + "'").to("direct:acOn")
                       .otherwise().to("direct:acOff");
      from("direct:acOn").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/ac/on");
      from("direct:acOff").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/ac/off");

      // fireplace
      from("direct:fire").choice()
                         .when().simple("${body.fire} == '" + FireplaceCommand.Fire.HEAT + "'").to("direct:fireOn")
                         .otherwise().to("direct:fireOff");
      from("direct:fireOn").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/fireplace/on");
      from("direct:fireOff").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/fireplace/off");

      // media center
      from("direct:media").choice()
            .when().simple("${body.media} == '" + MediaCenterCommand.Media.OFF + "'").to("direct:tvOff")
            .when().simple("${body.media} == '" + MediaCenterCommand.Media.NEWS + "'").to("direct:tvNews")
            .otherwise().to("direct:tvRomantic");
      from("direct:tvRomantic").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/tv/romantic");
      from("direct:tvNews").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/tv/news");
      from("direct:tvOff").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/tv/off");

      // door, aka front door, aka main entrance
      from("direct:door").choice()
            .when().simple("${body.door} == '" + DoorCommand.Door.FRONT + "'").to("direct:doorFront")
            .otherwise().to("direct:doorRear");
      from("direct:doorFront").choice()
            .when().simple("${body.openPercentage} == 0").to("direct:doorClose")
            .otherwise().to("direct:doorOpen");
      from("direct:doorOpen").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/door/open");
      from("direct:doorClose").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/door/close");

      // window, aka rear door
      from("direct:doorRear").choice()
            .when().simple("${body.openPercentage} == 0").to("direct:windowClose")
            .otherwise().to("direct:windowOpen");
      from("direct:windowOpen").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/window/open");
      from("direct:windowClose").setBody().constant("").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + iotHost + "/window/close");

      // mock routes - uncomment when the intelligent home device is not present
      // from("jetty:http://" + iotHost + "?matchOnUriPrefix=true").log("Called house on url").to("stream:out");
   }

}
