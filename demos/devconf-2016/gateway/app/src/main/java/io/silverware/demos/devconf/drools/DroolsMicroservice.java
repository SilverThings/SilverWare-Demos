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

import io.silverware.demos.devconf.kjar.Action;
import io.silverware.demos.devconf.kjar.Command;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;

import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.inject.Inject;

/**
 * Processes actions from the action message topic and publishes the resulting commands to the commands message topic. The decision is based
 * on business rules implemented in Drools.
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class DroolsMicroservice {

   private static final Logger log = LogManager.getLogger(DroolsMicroservice.class);

   // KieSession is not thread safe, we need to synchronize calls
   private static Semaphore sync = new Semaphore(1);

   @Inject
   @KSession
   private KieSession session;

   @Inject
   @MicroserviceReference
   private ProducerTemplate producer;

   public void processActions(final List<Action> actions) throws InterruptedException {
      log.info("Firing rules for action {}", actions);

      sync.acquire();

      try {
         final List<Command> commands = new ArrayList<>();
         final EntryPoint entryPoint = session.getEntryPoint("actions");
         session.setGlobal("producer", producer);
         session.setGlobal("commands", commands);

         actions.forEach(entryPoint::insert);

         session.fireAllRules();

         commands.forEach(cmd -> producer.asyncSendBody("direct:commands", cmd));
      } finally {
         sync.release();
      }
   }

   public void processAction(final Action action) throws InterruptedException {
      processActions(Collections.singletonList(action));
   }

}
