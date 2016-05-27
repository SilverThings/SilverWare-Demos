import io.silverware.microservices.annotations.JMS;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 * @author <a href="mailto:stefankomartin6@gmail.com">Martin Štefanko</a>
 */
@Microservice("ActiveMQAnnotationMicroservice")
public class ActiveMQAnnotationMicroservice {

   private static final Logger log = LogManager.getLogger(ActiveMQAnnotationMicroservice.class);

   @Inject
   @MicroserviceReference("annotationConnection")
   @JMS(serverUri = "vm://0", initialContextFactory = org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory.class)
   private Connection connection;

   @Inject
   @MicroserviceReference("annotationJMSContext")
   @JMS(serverUri = "vm://0", initialContextFactory = org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory.class)
   private JMSContext jmsContext;

   public void observer(@Observes MicroservicesStartedEvent event) throws Exception {
      log.info("Hello from " + this.getClass().getSimpleName());

      //start embedded broker instance
      EmbeddedJMS jmsServer = new EmbeddedJMS();

      jmsServer.setConfigResourcePath(ClassLoader.getSystemResource("broker/broker.xml").toURI().toString());

      jmsServer.start();
      log.info("Started Embedded JMS Server");

      //JMS 1.1
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      //create topic for sending messages
      Topic topic = session.createTemporaryTopic();

      MessageProducer messageProducer = session.createProducer(topic);

      MessageConsumer messageConsumer = session.createConsumer(topic);
      messageConsumer.setMessageListener(new MessageListener() {
         @Override
         public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            try {
               System.out.println("RECEIVED MESSAGE: " + textMessage.getText());
            } catch (JMSException e) {
               e.printStackTrace();
            }
         }
      });

      //JMS 2.0
      //setup only the producer, messages on topic are consumed by JMS 1.1 consumer
      JMSProducer jmsProducer = jmsContext.createProducer();

      try {
         while (!Thread.currentThread().isInterrupted()) {

            //send messages
            messageProducer.send(session.createTextMessage("HELLO FROM JMS 1.1 PRODUCER SENT ON " + new Date()));
            jmsProducer.send(topic, "HELLO FROM JMS 2.0 PRODUCER SENT ON " + new Date());

            Thread.sleep(1000);
         }
      } catch (InterruptedException ie) {
         System.out.println("Interrupted - shutting down...");
      } finally {
         //close resources
         messageProducer.close();
         messageConsumer.close();
         session.close();

         //stop the server
         jmsServer.stop();
         log.info("Embedded JMS Server stopped");
      }
   }
}
