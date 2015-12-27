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
package io.silverware.demos.quickstarts.deltaspike;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class DeltaSpikeMicroservice {

   private static final Logger log = LogManager.getLogger(DeltaSpikeMicroservice.class);

   @Inject
   @ConfigProperty(name = "db.port", defaultValue = "3306", parameterizedBy = "db.vendor")
   private Integer dbPort;

   public DeltaSpikeMicroservice() {
      log.info("DeltaSpikeMicroservice constructor");
   }

   @PostConstruct
   public void onInit() {
      log.info("DeltaSpikeMicroservice PostConstruct " + this.getClass().getName());
      Integer apiDbPort = ConfigResolver.resolve("db.port").as(Integer.class).parameterizedBy("db.vendor").withDefault(3306).getValue();
      log.info("dbPort inject: {}, api: {}", dbPort, apiDbPort);
   }

   public void hello() {
      log.info("DeltaSpikeMicroservice Hello");
   }

   public void eventObserver(@Observes MicroservicesStartedEvent event) {
      log.info("DeltaSpikeMicroservice MicroservicesStartedEvent");
   }
}