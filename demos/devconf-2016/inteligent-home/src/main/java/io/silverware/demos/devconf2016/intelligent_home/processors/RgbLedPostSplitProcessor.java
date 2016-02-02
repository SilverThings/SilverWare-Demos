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
public class RgbLedPostSplitProcessor implements Processor {
   private RgbLedConfig rgbLedConfig;

   public RgbLedPostSplitProcessor(RgbLedConfig rgbLedConfig) {
      this.rgbLedConfig = rgbLedConfig;
   }

   @Override
   public void process(final Exchange exchange) throws Exception {
      final Message in = exchange.getIn();
      final String[] msg = in.getBody().toString().split(";");
      in.setHeader("led", msg[0]);
      in.setHeader("channel", msg[1]);
      in.setHeader("value", msg[2]);
   }
}
