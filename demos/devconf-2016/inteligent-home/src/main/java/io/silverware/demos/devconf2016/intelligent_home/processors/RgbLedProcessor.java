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

import io.silverware.demos.devconf2016.intelligent_home.Configuration;
import io.silverware.demos.devconf2016.intelligent_home.routes.Pca9685RouteBuilder;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class RgbLedProcessor implements Processor {
   private Configuration config = Configuration.getInstance();

   // input headers led=led number; channel=r, g or b; r=red value; g=green value; b=blue value;
   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message in = exchange.getIn();
      final int led = Integer.valueOf(in.getHeader("led").toString()); // 0-15
      final String channel = in.getHeader("channel").toString(); // one of 'r', 'g' or 'b'
      final String value = in.getHeader("value").toString(); // 0-100 [%]

      final String pca9685Address = config.getPca9685Address(led, channel);
      if (pca9685Address == null) {
         throw new IllegalArgumentException("The address of PCA9685 for LED #" + led + " is invalid or not defined in " + Configuration.CONFIG_FILE);
      }
      in.setHeader("address", pca9685Address);
      in.setHeader(Pca9685RouteBuilder.PWM_HEADER, config.getRgbLedPwm(led, channel));
      in.setHeader(Pca9685RouteBuilder.VALUE_HEADER, (int) (40.95 * Integer.valueOf(value)));
   }
}
