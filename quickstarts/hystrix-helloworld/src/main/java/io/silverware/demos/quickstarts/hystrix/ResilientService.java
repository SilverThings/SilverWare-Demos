/*
 * -----------------------------------------------------------------------\
 * SilverWare
 *  
 * Copyright (C) 2016 - 2017 the original author or authors.
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
package io.silverware.demos.quickstarts.hystrix;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.annotations.hystrix.basic.CircuitBreaker;
import io.silverware.microservices.annotations.hystrix.basic.Timeout;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Microservice
public class ResilientService {

   @Inject
   @CircuitBreaker
   @MicroserviceReference
   private UnstableMicroservice mostStable;

   @Inject
   @CircuitBreaker
   @MicroserviceReference
   private UnstableMicroservice moreStable;

   @Inject
   @CircuitBreaker
   @MicroserviceReference
   private UnstableMicroservice lessStable;

   @Inject
   @CircuitBreaker
   @MicroserviceReference
   private UnstableMicroservice leastStable;

   @Inject
   @Timeout
   @MicroserviceReference
   private SlowMicroservice slowest;

   @Inject
   @Timeout
   @MicroserviceReference
   private SlowMicroservice slower;

   @Inject
   @Timeout
   @MicroserviceReference
   private SlowMicroservice faster;

   @Inject
   @Timeout
   @MicroserviceReference
   private SlowMicroservice fastest;

   public void execute(@Observes MicroservicesStartedEvent event) throws InterruptedException {
      ExecutorService executorService = Executors.newFixedThreadPool(8);
      executorService.execute(new SafeRunnable(() -> leastStable.failAlways()));
      executorService.execute(new SafeRunnable(() -> lessStable.failHalf()));
      executorService.execute(new SafeRunnable(() -> moreStable.failThird()));
      executorService.execute(new SafeRunnable(() -> mostStable.failZero()));
      executorService.execute(new SafeRunnable(() -> slowest.timeoutAlways()));
      executorService.execute(new SafeRunnable(() -> slower.timeoutHalf()));
      executorService.execute(new SafeRunnable(() -> faster.timeoutThird()));
      executorService.execute(new SafeRunnable(() -> fastest.timeoutZero()));
      executorService.shutdown();
   }

   private static class SafeRunnable implements Runnable {

      private final Runnable runnable;

      public SafeRunnable(Runnable runnable) {
         this.runnable = runnable;
      }

      @Override
      public void run() {
         for (int i = 0; i < 600; i++) {
            try {
               runnable.run();
            } catch (Exception ex) {
               // failed calls are shown in Hystrix Dashboard
            }
            try {
               Thread.sleep(500);
            } catch (InterruptedException ex) {
               // TODO change
            }
         }
      }
   }

}
