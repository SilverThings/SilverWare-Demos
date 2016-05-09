package groovy

import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.groovy.GroovyVerticle

public class MyGroovyVerticle extends GroovyVerticle {

   def logger = LoggerFactory.getLogger(this.class.getName())

   public void start() {
      logger.info("Starting " + this.class.getName())

      vertx.setPeriodic(1000, { id ->
         vertx.eventBus().send("silverware.topic", "Hello from " + this.class.getName())
      })
   }

   public void stop() {
      logger.info("Stopping" + this.class.getName())
   }

}