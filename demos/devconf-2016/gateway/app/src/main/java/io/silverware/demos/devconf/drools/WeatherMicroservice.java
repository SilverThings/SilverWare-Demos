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
package io.silverware.demos.devconf.drools;

import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import io.silverware.demos.devconf.kjar.WeatherAction;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;

/**
 * Extracts weather information from the Intelligent Home's JSON object and puts them on the actions message topic.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class WeatherMicroservice {

   private static final Logger log = LogManager.getLogger(WeatherMicroservice.class);

   @Inject
   @MicroserviceReference
   private ProducerTemplate producer;

   public void processWeather(final String status) {
      log.info("Weather status {}", status);

      final String tempField = "\"temperature\" : ";
      String tempStr = status.substring(status.indexOf(tempField) + tempField.length());
      tempStr = tempStr.substring(0, tempStr.indexOf(","));
      int temp = Integer.parseInt(tempStr);

      final String humidityField = "\"humidity\" : ";
      String humidityStr = status.substring(status.indexOf(humidityField) + humidityField.length());
      humidityStr = humidityStr.substring(0, humidityStr.indexOf(","));
      int humidity = Integer.parseInt(humidityStr);

      producer.sendBody("direct:actions", new WeatherAction(temp, humidity));
   }

}
