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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import io.silverware.microservices.annotations.Microservice;

/**
 * @author Slavomír Krupa (slavomir.krupa@gmail.com)
 */
@Microservice
public class ClusteredHelloWorldService {
   private static final Logger LOG = LogManager.getLogger(ClusteredHelloWorldService.class);

   public void hello() {
      LOG.info("Hello there!");
   }

   public int magicCount(int a, int b) {
      LOG.info("Computing a addition of {} and {} ", a, b);
      return a + b;
   }

   public int magicCount(int a, Integer b) {
      LOG.info("Computing multiplication of {} and {} ", a, b);
      return a * b;
   }

   public CustomObject customSerialization(CustomObject object) {
      LOG.info("Doubling a {} ", object);
      if (object == null) {
         return null;
      }
      object.setInnerClass(new CustomObject.BrokenInnerClass(UUID.randomUUID()));
      object.setmFloat(object.getmFloat() * 2);
      object.setmInteger(object.getmInteger() * 2);
      object.setmString(object.getmString() + object.getmString());
      return object;
   }
}
