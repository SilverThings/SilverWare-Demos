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
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class MqttRoutes extends RouteBuilder {

   @Override
   public void configure() throws Exception {

/*
    <route id="priority-choice-route" streamCache="true">
        <from uri="timer://foo?period=2000"/>

        <transform>
            <method beanType="org.apache.commons.lang3.RandomStringUtils" method="randomNumeric(3)"/>
        </transform>

        <choice>
            <when>
                <simple>${body} &gt; 500</simple>
                <log message="sending to high priority queue: ${body}" logName="choice"/>
                <to uri="mqtt:outHi?publishTopicName=high/message/basic&amp;userName=mqtt&amp;password=mqtt&amp;host=tcp://mqtt.example.com:1883"/>
            </when>
            <otherwise>
                <log message="sending to low priority queue: ${body}" logName="choice"/>
                <to uri="mqtt:outLo?publishTopicName=low/message/basic&amp;userName=mqtt&amp;password=mqtt&amp;host=tcp://mqtt.example.com:1883"/>
            </otherwise>
        </choice>
    </route>

    <route id="high-priority-route" streamCache="true">
        <from uri="mqtt:inHi?subscribeTopicName=high/message/basic&amp;userName=mqtt&amp;password=mqtt&amp;host=tcp://mqtt.example.com:1883"/>
        <log message="received in high priority queue: ${body}" logName="high.priority"/>
    </route>

    <route id="low-priority-route" streamCache="true">
        <from uri="mqtt:inLo?subscribeTopicName=low/message/basic&amp;userName=mqtt&amp;password=mqtt&amp;host=tcp://mqtt.example.com:1883"/>
        <log message="received in low priority queue: ${body}" logName="low.priority"/>
    </route>
 */
      from("direct:acutor").to("mqtt:outAcutor?publishTopicName=ih/message/acutors&userName=mqtt&password=mqtt&host=tcp://" + System.getProperty("mqtt.host", "10.40.3.60:1883"));

      from("direct:command").to("mqtt:outCommand?publishTopicName=ih/message/commands&userName=mqtt&password=mqtt&host=tcp://" + System.getProperty("mqtt.host", "10.40.3.60:1883"));

      from("direct:mobile").to("mqtt:outMobile?publishTopicName=ih/message/mobile&userName=mqtt&password=mqtt&host=tcp://" + System.getProperty("mqtt.host", "10.40.3.60:1883"));

      from("mqtt:inAcutor?subscribeTopicName=ih/message/acutors&userName=mqtt&password=mqtt&host=tcp://" + System.getProperty("mqtt.host", "10.40.3.60:1883")).bean("gatewayMicroservice", "processAcutor");

      from("mqtt:inCommand?subscribeTopicName=ih/message/commands&userName=mqtt&password=mqtt&host=tcp://" + System.getProperty("mqtt.host", "10.40.3.60:1883")).bean("gatewayMicroservice", "processCommand");

      from("mqtt:inStatus?subscribeTopicName=ih/message/status&userName=mqtt&password=mqtt&host=tcp://" + System.getProperty("mqtt.host", "10.40.3.60:1883")).bean("gatewayMicroservice", "processWeather");

      from("direct:led").setHeader(Exchange.HTTP_METHOD, constant("POST")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/led/batch");
      from("direct:ledAllOff").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/led/setrgb/all?r=0&g=0&b=0");
      from("direct:ledAllRomantic").setHeader(Exchange.HTTP_METHOD, constant("GET")).setHeader("r", constant("50")).setHeader("g", constant("10")).setHeader("b", constant("10")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/led/setrgb/all");
      from("direct:ledAllOn").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/led/setrgb/all?r=100&g=100&b=100");
      from("direct:ledAllEvening").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/led/setrgb/all?r=80&g=80&b=50");

      from("direct:acOn").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/ac/on");
      from("direct:acOff").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/ac/off");

      from("direct:fireOn").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/fireplace/on");
      from("direct:fireOff").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/fireplace/off");

      from("direct:tvRomantic").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/tv/romantic");
      from("direct:tvNews").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/tv/news");
      from("direct:tvOff").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/tv/off");

      from("direct:doorOpen").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/door/open");
      from("direct:doorClose").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/door/close");

      from("direct:windowOpen").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/window/open");
      from("direct:windowClose").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/window/close");

      from("jetty:http://" + System.getProperty("mobile.host", "0.0.0.0:8283") + "/mobile").setBody().simple("${in.header.button}").bean("gatewayMicroservice", "mobileControlButton");

      from("timer://foo?period=5000").setHeader(Exchange.HTTP_METHOD, constant("GET")).to("jetty:http://" + System.getProperty("iot.host", "10.40.2.210:8282") + "/sensorData").bean("gatewayMicroservice", "processWeather");

      /*from("timer://foo?period=2000")
         .setBody().simple("Hello from Camel Timer!")
         .bean("camelCdiMicroservice", "sayHello")
         .to("log:test").to("stream:out");*/
   }

}