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
public class ServoProcessor implements Processor {
   private Configuration config = Configuration.getInstance();

   // 100Hz
   private static final int[] PWM_MIN = new int[] { 780, 670 }; // 0%
   private static final int[] PWM_MAX = new int[] { 1040, 1070 }; // 100%

   // input headers servo = servo number (0-1); value = set's the servo's position (0-100%);
   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message in = exchange.getIn();
      final int servo = Integer.valueOf(in.getHeader("servo").toString()); // 0-1
      if (servo < 0 || servo > 1) {
         throw new IllegalArgumentException("Invalid servo number (" + servo + "), (0-1) expected.");
      }
      final String value = in.getHeader("value").toString(); // 0-100 [%]

      final String pca9685Address = config.getServoPca9685Address(servo);
      if (pca9685Address == null) {
         throw new IllegalArgumentException("The address of PCA9685 for servo #" + servo + " is invalid or not defined in " + Configuration.CONFIG_FILE);
      }
      in.setHeader("address", pca9685Address);
      in.setHeader(Pca9685RouteBuilder.PWM_HEADER, config.getServoPwm(servo));
      in.setHeader(Pca9685RouteBuilder.VALUE_HEADER, (int) map(Integer.valueOf(value), 0, 100, PWM_MIN[servo], PWM_MAX[servo]));
   }

   private static double map(final double value, final double imin, final double imax, final double omin, final double omax) {
      if (value <= imin) {
         return omin;
      } else if (value >= imax) {
         return omax;
      } else {
         final double t = (value - imin) / (imax - imin);
         return omin + (t * (omax - omin));
      }
   }
}
