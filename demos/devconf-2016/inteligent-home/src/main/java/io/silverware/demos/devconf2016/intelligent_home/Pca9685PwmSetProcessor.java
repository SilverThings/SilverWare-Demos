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
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class Pca9685PwmSetProcessor implements Processor {
   private static final Logger log = Logger.getLogger(Pca9685PwmSetProcessor.class);

   @Override
   public void process(final Exchange exchange) throws Exception {
      Message msg = exchange.getIn();
      StringBuffer i2cMsg = new StringBuffer();
      // bound pwm output number to 0-15 range;
      final Object pwmHeader = msg.getHeader(Pca9685RouteBuilder.PWM_HEADER);
      if(pwmHeader == null){
         throw new RuntimeException("'pwm' header not found! Value 0 - 15 expected.");
      }
      final int pwm = Math.max(0, Math.min(15, Integer.valueOf(pwmHeader.toString())));

      // bound value to 0-4095 range
      final Object valueHeader = msg.getHeader(Pca9685RouteBuilder.VALUE_HEADER);
      if(valueHeader == null){
         throw new RuntimeException("'value' header not found! Value 0 - 100 expected.");
      }
      final int value = Math.max(0, Math.min(4095, Integer.valueOf(valueHeader.toString())));
      i2cMsg.append(Pca9685Util.pwmAddress(pwm));
      i2cMsg.append(Pca9685Util.hex16bit(0)); // HIGH pulse start
      i2cMsg.append(Pca9685Util.hex16bit((int) (value))); // LOW pulse end
      if (log.isDebugEnabled()) {
         log.debug("Sending I2C message: " + i2cMsg.toString());
      }
      msg.setBody(i2cMsg);
   }
}
