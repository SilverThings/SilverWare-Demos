/*
 * -----------------------------------------------------------------------\
 * SilverWare
 *  
 * Copyright (C) 2014 - 2016 the original author or authors.
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
package io.silverware.demos.lionsden;

import io.silverware.microservices.annotations.Gateway;
import io.silverware.microservices.annotations.InvocationPolicy;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.annotations.ParamName;
import io.silverware.microservices.silver.services.lookup.LocalLookupStrategy;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
@Gateway
public class ServiceA {

   @Inject
   @MicroserviceReference
   private ServiceB serviceB;

   @Inject
   @MicroserviceReference
   @InvocationPolicy(lookupStrategy = LocalLookupStrategy.class)
   private ServiceC serviceC;

   @Inject
   @Named("myCoolPool")
   private ThreadPoolExecutor pool;

   @Inject
   @MicroserviceReference
   private ServiceG serviceG;

   public String hello(@ParamName("name") final String name) {
      return "Hello " + serviceB.enrich(name);
   }

   public String helloAsync(@ParamName("name") final String name) throws ExecutionException, InterruptedException {
      return CompletableFuture.supplyAsync(() -> serviceC.upperCase(name), pool).thenApply(s -> serviceB.enrich(s)).thenApply(s -> "Hello " + s).get();
   }

   public String hey() {
      return "Hey " + serviceG.duke();
   }
}
