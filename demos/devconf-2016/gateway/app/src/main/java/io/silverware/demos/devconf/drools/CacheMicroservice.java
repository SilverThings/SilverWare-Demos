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
package io.silverware.demos.devconf.drools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.silverware.demos.devconf.kjar.Command;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;
import io.silverware.microservices.providers.cdi.builtin.Storage;

/**
 * Cache is a useful concept in the IoT reference architecture to store the most recent state of individual devices.
 * Our devices do not inform us on their state change. We suppose they execute all the commands successfully and their
 * state is then the same that we expected.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class CacheMicroservice {

   private static final Logger log = LogManager.getLogger(CacheMicroservice.class);

   private static final String CACHE = CacheMicroservice.class.getPackage().getName() + ".CACHE";

   @Inject
   @MicroserviceReference
   private Storage storage;

   @SuppressWarnings("unchecked")
   public Map<String, String> getCache() {
      return Collections.unmodifiableMap((Map<String, String>) storage.get(CACHE));
   }

   @SuppressWarnings("unchecked")
   public Command processCommand(final Command command) {
      log.info("Caching command status {}", command);

      Map<String, String> cache = (Map<String, String>) storage.get(CACHE);
      Map<String, String> update = command.getCacheUpdate();

      if (update != null) {
         cache.putAll(update);
      }

      return command;
   }

   public void eventObserver(@Observes MicroservicesStartedEvent event) {
      if (storage.get(CACHE) == null) {
         storage.put(CACHE, new HashMap<String, String>());
      }
   }


}