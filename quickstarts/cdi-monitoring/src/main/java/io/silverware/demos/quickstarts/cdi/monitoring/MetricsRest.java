package io.silverware.demos.quickstarts.cdi.monitoring;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.monitoring.MetricsAggregator;
import io.vertx.core.json.JsonObject;

/**
 * @author Tomas Borcin | tborcin@redhat.com | created: 9/25/16.
 */
@Microservice
@Path("/monitoring")
public class MetricsRest {

   @Inject
   @MicroserviceReference
   private MonitoredMicroservice monitoredMicroservice;

   @Inject
   @MicroserviceReference
   private NotMonitoredMicroservice notMonitoredMicroservice;

   @Path("monitored_service")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String callMonitoredService() {
      return this.monitoredMicroservice.hello();
   }

   @Path("notmonitored_service")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String callNotMonitoredService() {
      return this.notMonitoredMicroservice.hello();
   }

   @Path("show_metrics")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public JsonObject showMetrics() {
      return new JsonObject(MetricsAggregator.getMetrics());
   }

   @Path("show_values")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String showValues() {
      return MetricsAggregator.getValues();
   }

   @Path("show_values_size")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String showValuesSize() {
      return MetricsAggregator.getValuesSize();
   }
}
