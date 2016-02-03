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
package io.silverware.demos.quickstarts.drools;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class DroolsHelloWorldMicroservice {

   private static final Logger log = LogManager.getLogger(DroolsHelloWorldMicroservice.class);

   @Inject
   @KSession
   private KieSession session;

   public DroolsHelloWorldMicroservice() {
      log.info("DroolsHelloWorldMicroservice constructor");
   }

   public void eventObserver(@Observes MicroservicesStartedEvent event) {
      log.info("DroolsHelloWorldMicroservice MicroservicesStartedEvent {}", session);
      if (session != null) {
      	 session.insert("test");
          session.fireAllRules();
          log.info("Fired!");
      }
   }
}