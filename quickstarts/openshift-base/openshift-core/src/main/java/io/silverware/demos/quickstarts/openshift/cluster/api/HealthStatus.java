/*
 * -----------------------------------------------------------------------\
 * Silverware
 *  
 * Copyright (C) 2010 - 2016 the original author or authors.
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
package io.silverware.demos.quickstarts.openshift.cluster.api;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.builtin.CurrentContext;
import io.silverware.microservices.silver.CdiSilverService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This service expose health status of SilverWare
 *
 * @author Slavomír Krupa (slavomir.krupa@gmail.com)
 */
@Microservice
@Path("health")
public class HealthStatus {

   @Inject
   @MicroserviceReference
   private CurrentContext context;

   @GET
   @Path("status")
   @Produces(MediaType.TEXT_PLAIN)
   public Response status() {
      final boolean isDeployed = ((CdiSilverService) this.context.getContext().getProvider(CdiSilverService.class)).isDeployed();
      return Response.status(isDeployed ? Response.Status.OK : Response.Status.FORBIDDEN).entity(String.valueOf(isDeployed)).build();
   }
}