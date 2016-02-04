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

      from("direct:led").setHeader(Exchange.HTTP_METHOD, constant("POST")).to("jetty:http://" + System.getProperty("iot.host", "10.40.3.63:8282") + "/led/batch");

      from("jetty:http://" + System.getProperty("mobile.host", "0.0.0.0:8283") + "/mobile").setBody().simple("${in.header.button}").bean("gatewayMicroservice", "mobileControlButton");

      /*from("timer://foo?period=2000")
         .setBody().simple("Hello from Camel Timer!")
         .bean("camelCdiMicroservice", "sayHello")
         .to("log:test").to("stream:out");*/
   }

}