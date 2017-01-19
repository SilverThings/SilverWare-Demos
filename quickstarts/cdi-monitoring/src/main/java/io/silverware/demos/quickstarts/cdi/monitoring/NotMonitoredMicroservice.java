package io.silverware.demos.quickstarts.cdi.monitoring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.monitoring.annotations.NotMonitored;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

/**
 * @author Tomas Borcin | tborcin@redhat.com | created: 9/25/16.
 */
@Microservice
@Path("/notmonitored_hello_service")
public class NotMonitoredMicroservice {

   private static final Logger log = LogManager.getLogger(NotMonitoredMicroservice.class);

   public NotMonitoredMicroservice() {
      log.info("NotMonitored microservice constructor");
   }

   @PostConstruct
   public void onInit() {
      log.info("NotMonitored microservice PostConstruct " + this.getClass().getName());
   }

   @Path("hello")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   @NotMonitored
   public String hello() {
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      return "NotMonitored microservice hello method\n";
   }

   public void eventObserver(@Observes MicroservicesStartedEvent event) {
      log.info("NotMonitored microservice MicroservicesStartedEvent");
   }
}
