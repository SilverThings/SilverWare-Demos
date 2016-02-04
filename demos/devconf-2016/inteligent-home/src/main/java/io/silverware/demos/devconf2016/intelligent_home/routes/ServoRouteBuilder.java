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

import io.silverware.demos.devconf2016.intelligent_home.processors.ServoOpenProcessor;
import io.silverware.demos.devconf2016.intelligent_home.processors.ServoProcessor;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class ServoRouteBuilder extends IntelligentHomeRouteBuilder {
   @Override
   public void configure() throws Exception {
      final ServoProcessor servoProcessor = new ServoProcessor();
      final ServoOpenProcessor servoOpenProcessor = new ServoOpenProcessor();

      from(restBaseUri() + "/servo/set?httpMethodRestrict=GET")
            .to("direct:servo-set");

      from(restBaseUri() + "/door/open")
            .setHeader("servo", simple("0"))
            .process(servoOpenProcessor)
            .to("direct:servo-set");

      from(restBaseUri() + "/door/close")
            .setHeader("servo", simple("0"))
            .setHeader("value", simple("0"))
            .to("direct:servo-set");

      from(restBaseUri() + "/window/open")
            .setHeader("servo", simple("1"))
            .process(servoOpenProcessor)
            .to("direct:servo-set");

      from(restBaseUri() + "/window/close")
            .setHeader("servo", simple("1"))
            .setHeader("value", simple("0"))
            .to("direct:servo-set");

      from("direct:servo-set")
            .process(servoProcessor)
            .to("direct:pca9685-pwm-set");
   }
}