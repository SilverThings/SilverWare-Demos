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
public class ResetRouteBuilder extends RouteBuilder {
   @Override
   public void configure() throws Exception {
      final Pca9685PwmSetProcessor pca9685PwmSetProcessor = new Pca9685PwmSetProcessor();

      from("jetty:http://0.0.0.0:8282/reset?httpMethodRestrict=GET")
            .setHeader("address", simple("0x70"))
            .to("direct:pca9685-reset");
   }
}