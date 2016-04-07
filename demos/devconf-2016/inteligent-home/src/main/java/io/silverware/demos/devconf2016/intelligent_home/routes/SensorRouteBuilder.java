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
package io.silverware.demos.devconf2016.intelligent_home.routes;

import org.apache.camel.model.dataformat.JsonLibrary;

import io.silverware.demos.devconf2016.intelligent_home.processors.SensorDataProcessor;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class SensorRouteBuilder extends IntelligentHomeRouteBuilder {

   @Override
   public void configure() throws Exception {
      final SensorDataProcessor sensorDataProcessor = new SensorDataProcessor();

      from(restBaseUri() + "/sensorData?httpMethodRestrict=GET")
            .setHeader("address", simple(config.getSensorAddress()))
            .setBody(simple(""))
            .to("bulldog:i2c?readLength=2")
            .process(sensorDataProcessor)
            .marshal().json(JsonLibrary.Jackson, true);

      from("timer:sensorBroadcast?period=5000")
            .setHeader("address", simple(config.getSensorAddress()))
            .to("bulldog:i2c?readLength=2")
            .process(sensorDataProcessor)
            .marshal().json(JsonLibrary.Jackson, true)
            .to("mqtt:status?publishTopicName=ih/message/weather&userName=mqtt&password=mqtt&host=tcp://" + mqttHost() + "/");
   }
}
