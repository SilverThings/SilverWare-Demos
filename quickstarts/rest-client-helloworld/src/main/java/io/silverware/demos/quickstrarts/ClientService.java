package io.silverware.demos.quickstrarts;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.rest.annotation.ServiceConfiguration;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
@Microservice
@Path("/client_service")
public class ClientService {
   @Inject
   @MicroserviceReference
   @ServiceConfiguration(endpoint = "http://localhost:8080/silverware/rest/hello_service")
   HelloRestServiceProxy restService;

   @Path("call_rest_service")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public Response callRestService() {
      return Response.ok(this.restService.hello()).build();
   }
}
