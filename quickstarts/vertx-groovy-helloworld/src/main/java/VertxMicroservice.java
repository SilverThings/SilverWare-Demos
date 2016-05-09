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

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
@Microservice("vertxMicroservice")
public class VertxMicroservice {

   private Logger log = LoggerFactory.getLogger(VertxMicroservice.class);

   @Inject
   @MicroserviceReference
   private Vertx vertx;


   public void testVertx(@Observes MicroservicesStartedEvent event) {
log.info("observer started");
      vertx.eventBus().consumer("silverware.topic", message -> {
         log.info("Received message: " + message.body());
      });
   }
}
