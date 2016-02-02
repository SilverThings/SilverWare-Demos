package io.silverware.demos.devconf2016.intelligent_home.routes;

import org.apache.camel.builder.RouteBuilder;

import io.silverware.demos.devconf2016.intelligent_home.Configuration;

/**
 * Created by pmacik on 2.2.16.
 */
public abstract class IntelligentHomeRouteBuilder extends RouteBuilder {
   protected Configuration config = Configuration.getInstance();

   public String restBaseUri() {
      return "jetty:http://" + config.getRestHost() + ":" + config.getRestPort();
   }
}
