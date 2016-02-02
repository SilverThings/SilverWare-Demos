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

import io.silverware.demos.devconf2016.intelligent_home.RgbLedConfig;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class AllRgbLedsProcessor implements Processor {
   private RgbLedConfig rgbLedConfig;

   public AllRgbLedsProcessor(RgbLedConfig rgbLedConfig) {
      this.rgbLedConfig = rgbLedConfig;
   }

   // input headers led=led number; channel=r, g or b; r=red value; g=green value; b=blue value;
   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message in = exchange.getIn();
      final StringBuffer allRgbLeds = new StringBuffer();

      for (int led = 0; led < RgbLedConfig.RGB_LED_COUNT; led++) {
         if (rgbLedConfig.getRgbLed(led) != null) {
            allRgbLeds.append(led);
            allRgbLeds.append(";");
            allRgbLeds.append("r");
            allRgbLeds.append(";");
            allRgbLeds.append(in.getHeader("r"));
            allRgbLeds.append("\n");

            allRgbLeds.append(led);
            allRgbLeds.append(";");
            allRgbLeds.append("g");
            allRgbLeds.append(";");
            allRgbLeds.append(in.getHeader("g"));
            allRgbLeds.append("\n");

            allRgbLeds.append(led);
            allRgbLeds.append(";");
            allRgbLeds.append("b");
            allRgbLeds.append(";");
            allRgbLeds.append(in.getHeader("b"));
            allRgbLeds.append("\n");
         }
      }
      in.setBody(allRgbLeds.toString());
   }
}
