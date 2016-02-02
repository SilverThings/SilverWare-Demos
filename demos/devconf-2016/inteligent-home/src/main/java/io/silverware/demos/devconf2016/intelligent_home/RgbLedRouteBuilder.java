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

import org.apache.camel.builder.RouteBuilder;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class RgbLedRouteBuilder extends RouteBuilder {
   private RgbLedConfig rgbLedConfig = new RgbLedConfig();

   @Override
   public void configure() throws Exception {
      final RgbLedProcessor rgbLedProcessor = new RgbLedProcessor();
      final RgbLedBatchProcessor rgbLedBatchProcessor = new RgbLedBatchProcessor(rgbLedConfig);

      //final RgbLedPostSplitProcessor rgbLedPostSplitProcessor = new RgbLedPostSplitProcessor(rgbLedConfig);
      //final AllRgbLedsProcessor allRgbLedsProcessor = new AllRgbLedsProcessor(rgbLedConfig);

      // HTTP query led, r, g, b required
     /* from("jetty:http://0.0.0.0:8282/led/setrgb")
            // set R channel value
            .setHeader("channel", simple("r"))
            .setHeader("value", simple("${header.r}"))
            .process(rgbLedProcessor)
            .to("direct:pca9685-pwm-set")

            // set G channel value
            .setHeader("channel", simple("g"))
            .setHeader("value", simple("${header.g}"))
            .process(rgbLedProcessor)
            .to("direct:pca9685-pwm-set")

            // set B channel value
            .setHeader("channel", simple("b"))
            .setHeader("value", simple("${header.b}"))
            .process(rgbLedProcessor)
            .to("direct:pca9685-pwm-set");*/

      from("jetty:http://0.0.0.0:8282/led/batch")
            .process(rgbLedBatchProcessor)
            .to("direct:pca9685-pwm-set-batch");

      /*from("jetty:http://0.0.0.0:8282/led/setrgb/all")
            .process(allRgbLedsProcessor)
            .split(body().tokenize("\n"))
            .process(rgbLedPostSplitProcessor)
            .process(rgbLedProcessor)
            .to("direct:pca9685-pwm-set");*/
   }
}