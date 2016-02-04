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

import io.silverware.demos.devconf.kjar.AirConditioningCommand;
import io.silverware.demos.devconf.kjar.Command;
import io.silverware.demos.devconf.kjar.DoorCommand;
import io.silverware.demos.devconf.kjar.FireplaceCommand;
import io.silverware.demos.devconf.kjar.LightCommand;
import io.silverware.demos.devconf.kjar.MediaCenterCommand;
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
      final List<Command> commands = new ArrayList<>();

      log.info("Processing acutor: {}", command);

      if (session != null) {

         // M - Mood, D - Door, L - Light, C - MediaCenter, F - Fireplace, A - AC, l - all led

         if (command != null) {
            if (command.startsWith("M")) {
               commands.add(new MoodCommand(MoodCommand.Mood.values()[Integer.parseInt(command.substring(1))]));
            }
         }

         processCommands(commands);

      } else {
         log.warn("Cannot obtain KIE session");
      }
   }

   private void processCommands(final List<Command> commands) {
      final List<String> responseCommands = new ArrayList<>();
      final List<String> mobileStatus = new ArrayList<>();
      final EntryPoint entryPoint = session.getEntryPoint("acutors");
      session.setGlobal("commands", responseCommands);
      session.setGlobal("mobileStatus", mobileStatus);

      commands.forEach(entryPoint::insert);

      session.fireAllRules();

      responseCommands.forEach(cmd -> producer.sendBody("direct:command", cmd));
      mobileStatus.forEach(cmd -> producer.sendBody("direct:mobile", cmd));
   }

   public void processCommand(@ParamName("command") final String command) {
      if (command != null) {
         // M - Mood, D - Door, L - Light, C - MediaCenter, F - Fireplace, A - AC, l - all led

         if (command.startsWith("L")) {
            producer.sendBody("direct:led", command.substring(1));
         } else if (command.startsWith("A")) {
            if (command.substring(1).equals("C")) {
               producer.sendBody("direct:acOn", "");
            } else {
               producer.sendBody("direct:acOff", "");
            }
         } else if (command.startsWith("D")) {
            String endpoint = "direct:";
            if (command.substring(1, 2).equals("F")) {
               endpoint += "door";
            } else {
               endpoint += "window";
            }
            if (command.substring(2, 3).equals("0")) {
               endpoint += "Close";
            } else {
               endpoint += "Open";
            }
            producer.sendBody(endpoint, "");
         } else if (command.startsWith("F")) {
            if (command.substring(1, 2).equals("C")) {
               producer.sendBody("direct:fireOff", "");
            } else {
               producer.sendBody("direct:fireOn", "");
            }
         } else if (command.startsWith("C")) {
            String mode = command.substring(1, 2);
            switch (mode) {
               case "O":  // off
                  producer.sendBody("direct:tvOff", "");
                  break;
               case "R":  // romantic
                  producer.sendBody("direct:tvRomantic", "");
                  break;
               case "N":  // news
                  producer.sendBody("direct:tvNews", "");
                  break;
               default:
                  log.warn("Unknown media center state requested.");
            }
         } else if (command.startsWith("l")) {
            String mode = command.substring(1, 2);
            switch (mode) {
               case "0": // off
                  producer.sendBody("direct:ledAllOff", "");
                  break;
               case "R": // romantic
                  producer.sendBody("direct:ledAllRomantic", "");
                  break;
               case "E": // evening
                  producer.sendBody("direct:ledAllEvening", "");
                  break;
               case "1": // evening
                  producer.sendBody("direct:ledAllOn", "");
                  break;
               default:
                  log.warn("Unknown all led state requested.");
            }
         }

      }
   }

   public void mobileControlButton(@ParamName("button") final String button) {
      final int btn = Integer.parseInt(button);
      final List<Command> commands = new ArrayList<>();

      log.info("Mobile button {}", btn);

      switch (btn) {
         case 1:
            commands.add(new MoodCommand(MoodCommand.Mood.DAY));
            break;
         case 2:
            commands.add(new MoodCommand(MoodCommand.Mood.EVENING));
            break;
         case 3:
            commands.add(new MoodCommand(MoodCommand.Mood.SLEEP));
            break;
         case 4:
            commands.add(new MoodCommand(MoodCommand.Mood.ROMANTIC));
            break;
         case 5:
            commands.add(new AirConditioningCommand(AirConditioningCommand.Ac.COOLING));
            break;
         case 6:
            commands.add(new AirConditioningCommand(AirConditioningCommand.Ac.NORMAL));
            break;
         case 7:
            commands.add(new DoorCommand(DoorCommand.Door.FRONT, 100));
            break;
         case 8:
            commands.add(new DoorCommand(DoorCommand.Door.FRONT, 0));
            break;
         case 9:
            commands.add(new DoorCommand(DoorCommand.Door.REAR, 100));
            break;
         case 10:
            commands.add(new DoorCommand(DoorCommand.Door.REAR, 0));
            break;
         case 11:
            commands.add(new LightCommand(LightCommand.Place.BEDROOM, LightCommand.State.ON));
            commands.add(new LightCommand(LightCommand.Place.BED1, LightCommand.State.ON));
            commands.add(new LightCommand(LightCommand.Place.BED2, LightCommand.State.ON));
            break;
         case 12:
            commands.add(new LightCommand(LightCommand.Place.BEDROOM, LightCommand.State.OFF));
            commands.add(new LightCommand(LightCommand.Place.BED1, LightCommand.State.OFF));
            commands.add(new LightCommand(LightCommand.Place.BED2, LightCommand.State.OFF));
            break;
         case 13:
            commands.add(new LightCommand(LightCommand.Place.BED1, LightCommand.State.ON));
            break;
         case 14:
            commands.add(new LightCommand(LightCommand.Place.BED1, LightCommand.State.OFF));
            break;
         case 15:
            commands.add(new LightCommand(LightCommand.Place.BED2, LightCommand.State.ON));
            break;
         case 16:
            commands.add(new LightCommand(LightCommand.Place.BED2, LightCommand.State.OFF));
            break;
         case 17:
            commands.add(new LightCommand(LightCommand.Place.ALL, LightCommand.State.OFF));
            commands.add(new FireplaceCommand(FireplaceCommand.Fire.COLD));
            commands.add(new AirConditioningCommand(AirConditioningCommand.Ac.NORMAL));
            commands.add(new MediaCenterCommand(MediaCenterCommand.Media.OFF));
            commands.add(new DoorCommand(DoorCommand.Door.FRONT, 0));
            commands.add(new DoorCommand(DoorCommand.Door.REAR, 0));
            break;
         case 18:

            break;
         default:
            log.warn("Uknown mobile button.");
      }

      if (commands.size() > 0) {
         processCommands(commands);
      }
   }

   public void processWeather(@ParamName("status") final String status) {
      log.info("Weather status {}", status);

      final String tempField = "\"temperature\" : ";
      String tempStr = status.substring(status.indexOf(tempField) + tempField.length());
      tempStr = tempStr.substring(0, tempStr.indexOf(","));
      int temp = Integer.parseInt(tempStr);

      final String humidityField = "\"humidity\" : ";
      String humidityStr = status.substring(status.indexOf(humidityField) + humidityField.length());
      humidityStr = humidityStr.substring(0, humidityStr.indexOf(","));
      int humidity = Integer.parseInt(humidityStr);

      producer.sendBody("direct:mobile", "action18:" + 0xeeeeee + ":" + temp + "°C / " + humidity + "%");
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