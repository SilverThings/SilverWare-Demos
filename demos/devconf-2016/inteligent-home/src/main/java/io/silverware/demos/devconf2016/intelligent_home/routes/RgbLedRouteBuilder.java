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
package io.silverware.demos.devconf2016.intelligent_home.routes;

import io.silverware.demos.devconf2016.intelligent_home.processors.RgbLedBatchProcessor;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class RgbLedRouteBuilder extends IntelligentHomeRouteBuilder {
   @Override
   public void configure() throws Exception {
      final RgbLedBatchProcessor rgbLedBatchProcessor = new RgbLedBatchProcessor();

      // direct routes
      from("direct:led-set-batch")
            .process(rgbLedBatchProcessor)
            .to("direct:pca9685-pwm-set-batch");

      // REST API routes
      from(restBaseUri() + "/led/batch?httpMethodRestrict=POST")
            .to("direct:led-set-batch");

      from(restBaseUri() + "/led/setrgb?httpMethodRestrict=GET")
            .setBody(simple("${header.led};r;${header.r}\n"
                  + "${header.led};g;${header.g}\n"
                  + "${header.led};b;${header.b}\n"
            ))
            .to("direct:led-set-batch");

      final String allLed = Integer.toString(0xFFFF);
      from(restBaseUri() + "/led/setrgb/all?httpMethodRestrict=GET")
            .setBody(simple(allLed + ";r;${header.r}\n"
                  + allLed + ";g;${header.g}\n"
                  + allLed + ";b;${header.b}\n"
            ))
            .to("direct:led-set-batch");
   }
}