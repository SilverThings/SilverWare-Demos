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
package io.silverware.demos.quickstarts.cdi;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class CdiHelloWorldMicroservice {

   private static final Logger log = LogManager.getLogger(CdiHelloWorldMicroservice.class);

   public CdiHelloWorldMicroservice() {
      log.info("CdiHelloWorldMicroservice constructor");
   }

   @PostConstruct
   public void onInit() {
      log.info("CdiHelloWorldMicroservice PostConstruct " + this.getClass().getName());
   }

   public void hello() {
      log.info("CdiHelloWorldMicroservice Hello");
   }

   public void eventObserver(@Observes final MicroservicesStartedEvent event) {
      log.info("CdiHelloWorldMicroservice MicroservicesStartedEvent");
   }
}