package io.silverware.demos.quickstrarts;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public interface HelloRestServiceProxy {
   @Path("hello")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   String hello();
}
