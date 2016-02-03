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
package io.silverware.demos.devconf2016.intelligent_home.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.silverware.demos.devconf2016.intelligent_home.Configuration;
import io.silverware.demos.devconf2016.intelligent_home.model.SensorData;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class SensorDataProcessor implements Processor {
   private Configuration config = Configuration.getInstance();
   private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private static final String SENSOR_NAME = "LivingRoom";

   // input headers led=led number; channel=r, g or b; r=red value; g=green value; b=blue value;
   @Override
   public void process(final Exchange exchange) throws Exception {

      final Message in = exchange.getIn();
      final String body = (String) in.getBody();
      final SensorData sensorData = new SensorData();
      sensorData
            .sensorName(SENSOR_NAME)
            .temperature(Byte.decode("0x" + body.substring(0, 2)))
            .humidity(Byte.decode("0x" + body.substring(2, 4)))
            .timestamp(TIMESTAMP_FORMAT.format(new Date()));
      in.setBody(sensorData);
   }
}
