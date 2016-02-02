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
import org.apache.log4j.Logger;

import io.silverware.demos.devconf2016.intelligent_home.RgbLedConfig;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class AllRgbLedsProcessor implements Processor {
   private static final Logger log = Logger.getLogger(AllRgbLedsProcessor.class);

   private RgbLedConfig rgbLedConfig;

   public AllRgbLedsProcessor(RgbLedConfig rgbLedConfig) {
      this.rgbLedConfig = rgbLedConfig;
   }

   // input headers r=red value; g=green value; b=blue value;
   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message in = exchange.getIn();
      final StringBuffer allRgbLeds = new StringBuffer();
      final String[] channels = new String[] { "r", "g", "b" };

      boolean first = true;
      for (int led = 0; led < RgbLedConfig.RGB_LED_COUNT; led++) {
         if (rgbLedConfig.getRgbLed(led) != null) {
            for (String channel : channels) {
               if (rgbLedConfig.getRgbLedPwm(led, channel) >= 0) {
                  if (first) {
                     first = false;
                  } else {
                     allRgbLeds.append("\n");
                  }
                  String value = (String) in.getHeader(channel);
                  if (log.isTraceEnabled()) {
                     log.trace("Adding to batch for all LEDs: led=" + led + ", channel=" + channel + ", value=" + value);
                  }
                  allRgbLeds.append(led);
                  allRgbLeds.append(";");
                  allRgbLeds.append(channel);
                  allRgbLeds.append(";");
                  allRgbLeds.append(value);
               }
            }
         }
      }
      in.setBody(allRgbLeds.toString());
   }
}
