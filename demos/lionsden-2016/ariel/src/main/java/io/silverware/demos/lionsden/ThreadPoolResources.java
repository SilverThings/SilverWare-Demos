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

import io.silverware.microservices.annotations.Microservice;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class ThreadPoolResources {

   @Produces
   @Named("myCoolPool")
   public ThreadPoolExecutor getPool() {
      return (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
   }

}
