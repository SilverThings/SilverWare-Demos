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

import io.silverware.demos.devconf.kjar.MoodCommand;
import io.silverware.microservices.annotations.Gateway;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.annotations.ParamName;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
@Gateway
public class GatewayMicroservice {

   private static final Logger log = LogManager.getLogger(GatewayMicroservice.class);

   @Inject
   @KSession
   private KieSession session;

   @Inject
   @MicroserviceReference
   private ProducerTemplate producer;

   public GatewayMicroservice() {
      log.info("GatewayMicroservice constructor");
   }

   public void processAcutor(@ParamName("command") final String command) {
      final List<String> responseCommands = new ArrayList<>();
      final EntryPoint entryPoint = session.getEntryPoint("acutors");
      session.setGlobal("commands", responseCommands);

      log.info("Processing acutor: {}", command);

      if (session != null) {

         if (command != null) {
            if (command.startsWith("M")) {
               entryPoint.insert(new MoodCommand(MoodCommand.Mood.values()[Integer.parseInt(command.substring(1))]));
            }
         }

         session.fireAllRules();
         responseCommands.forEach(cmd -> producer.sendBody("direct:command", cmd));
      } else {
         log.warn("Cannot obtain KIE session");
      }
   }

   public void processCommand(@ParamName("command") final String command) {
      if (command != null) {
         if (command.startsWith("L")) {
            producer.sendBody("direct:led", command.substring(1));
         }
      }
   }

   public void mobileControlButton(@ParamName("button") final String button) {
      log.info("Mobile button {}", button);
   }

   public void directAcutor(@ParamName("command") final String command) {
      producer.sendBody("direct:acutor", command);
   }

   public void eventObserver(@Observes MicroservicesStartedEvent event) {
      log.info("GatewayMicroservice MicroservicesStartedEvent {}", session);
      if (session != null) {
      	 session.insert("test");
          session.fireAllRules();
          log.info("Fired!");
      }
   }
}