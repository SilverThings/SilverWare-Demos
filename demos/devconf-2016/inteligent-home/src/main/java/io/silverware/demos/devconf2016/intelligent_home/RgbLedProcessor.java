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
package io.silverware.demos.devconf2016.intelligent_home;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.Properties;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class RgbLedProcessor implements Processor {
   private static final Properties rgbLedProperties = new Properties();
   private static final String LED_PCA9685_PREFIX = "pca9685";
   private static final String LED_PREFIX = "led";

   public RgbLedProcessor() throws Exception {
      super();
      rgbLedProperties.load(RgbLedProcessor.class.getResourceAsStream("/rgbLed.properties"));
   }

   // input headers led=led number; channel=r, g or b; r=red value; g=green value; b=blue value;
   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message in = exchange.getIn();
      final String led = in.getHeader("led").toString();
      final String channel = in.getHeader("channel").toString(); // one of 'r', 'g' or 'b'

      // [0] = pca9685 module, [1] = pwm output
      final String[] ledCoordinates = rgbLedProperties.getProperty(LED_PREFIX + "." + led + "." + channel).split(";");

      final String pca9685Address = rgbLedProperties.getProperty(LED_PCA9685_PREFIX + "." + ledCoordinates[0]);
      final String pwmOutput = ledCoordinates[1];

      in.setHeader("address", pca9685Address);
      in.setHeader(Pca9685RouteBuilder.PWM_HEADER, pwmOutput);
      in.setHeader(Pca9685RouteBuilder.VALUE_HEADER, in.getHeader(channel));
   }
}
