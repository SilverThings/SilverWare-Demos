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

import io.silverware.microservices.SilverWareException;
import io.silverware.microservices.annotations.Gateway;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.annotations.ParamName;
import io.silverware.microservices.providers.rest.annotation.JsonService;
import io.silverware.microservices.providers.rest.api.RestService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
@Gateway
public class ServiceA {

   @Inject
   @MicroserviceReference
   @JsonService(endpoint = "http://localhost:8082/rest/ServiceB")
   private RestService serviceB;

   @Inject
   @MicroserviceReference
   @JsonService(endpoint = "http://localhost:8083/rest/ServiceCImpl")
   private ServiceC serviceC;

   @Inject
   @Named("myCoolPool")
   private ThreadPoolExecutor pool;

   @Inject
   @MicroserviceReference
   @JsonService(endpoint = "http://localhost:8084/rest/ServiceG")
   private ServiceG serviceG;

   public String hello(@ParamName("name") final String name) throws SilverWareException {
      return "Hello " + serviceB.call("enrich", "name", name);
   }

   public String helloAsync(@ParamName("name") final String name) throws ExecutionException, InterruptedException, SilverWareException {
      return CompletableFuture.supplyAsync(() -> serviceC.upperCase(name), pool).thenApply(s -> {
         try {
            return serviceB.call("enrich", "name", s);
         } catch (SilverWareException e) {
            e.printStackTrace();
         }
         return "Monsieur Dupont";
      }).thenApply(s -> "Hello " + s).get();
   }

   public String hey() {
      return "Hey " + serviceG.duke();
   }
}
