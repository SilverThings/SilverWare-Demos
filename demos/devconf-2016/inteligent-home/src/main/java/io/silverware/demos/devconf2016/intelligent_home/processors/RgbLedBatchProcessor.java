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

import io.silverware.demos.devconf2016.intelligent_home.Configuration;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class RgbLedBatchProcessor implements Processor {

   private static final Logger log = Logger.getLogger(RgbLedBatchProcessor.class);

   private Configuration config = Configuration.getInstance();
   private boolean batchAppendNewLine;

   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message msg = exchange.getIn();
      final StringBuffer pwmBatch = new StringBuffer();

      final String[] batchLines = msg.getBody(String.class).split("\n");

      batchAppendNewLine = false;
      for (String batchLine : batchLines) {
         // input message batch line "<led>;<channel(r,g,b)>;<value(0-100)>"
         //TODO: add batch line format validation
         if (log.isDebugEnabled()) {
            log.debug("Batch line: " + batchLine);
         }
         final String[] parts = batchLine.split(";");
         final int led = Integer.valueOf(parts[0]);
         final String channel = parts[1];
         final int value = Integer.valueOf(parts[2]);
         if (log.isTraceEnabled()) {
            log.trace("LED #: " + led);
            log.trace("LED channel: " + channel);
            log.trace("Value: " + value);
         }
         // output message batch line "<i2c address>;<pwm output(0-15)>;<value(0-4095)>"
         if (led == 0xFFFF) {
            for (int i = 0; i < Configuration.RGB_LED_COUNT; i++) {
               batchAppend(pwmBatch, i, channel, value);
            }
         } else {
            batchAppend(pwmBatch, led, channel, value);
         }
      }
      msg.setBody(pwmBatch.toString());
   }

   private void batchAppend(StringBuffer pwmBatch, int led, String channel, int value) {
      final String i2cAddress = config.getRgbLedPca9685Address(led, channel);
      if (i2cAddress == null) {
         if (log.isWarnEnabled()) {
            log.warn("I2C address of PCA9685 driver for LED '" + led + "' and channel '" + channel + "' not found in configuration file (" + Configuration.CONFIG_FILE + "), skipping.");
         }
         return;
      }
      final int rgbLedPwm = config.getRgbLedPwm(led, channel);
      if (rgbLedPwm < 0) {
         if (log.isWarnEnabled()) {
            log.warn("PWM output for LED '" + led + "' and channel '" + channel + "' not assigned in configuration file (" + Configuration.CONFIG_FILE + "), skipping.");
         }
         return;
      }
      final StringBuffer batch = new StringBuffer();
      batch.append(i2cAddress); // I2C address
      batch.append(";");
      batch.append(rgbLedPwm); // pwm output
      batch.append(";");
      batch.append(Integer.valueOf((int) (40.95 * value))); // pwm value

      if (log.isDebugEnabled()) {
         log.debug("Appending to PWM batch: [" + batch.toString() + "]");
      }

      if (batchAppendNewLine) {
         pwmBatch.append("\n");
      } else {
         batchAppendNewLine = true;
      }
      pwmBatch.append(batch);
   }
}
