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

import javax.inject.Inject;

import io.silverware.microservices.annotations.Gateway;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.annotations.ParamName;
import io.silverware.microservices.providers.rest.annotation.JsonService;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
@Gateway
public class ServiceB {

   @Inject
   @MicroserviceReference
   @JsonService(endpoint = "http://localhost:8083/rest/ServiceCImpl")
   private ServiceC upper;

   public String enrich(@ParamName("name") final String name) {
      return "Mr. " + upper.upperCase(name);
   }
}
