package io.silverware.demos.quickstrarts;

import io.silverware.microservices.annotations.Microservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
@Microservice
@Path("/hello_service")
public class HelloRestService {
   @Path("hello")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public Response hello() {
      return Response.ok("Hello world from REST service!").build();
   }
}
