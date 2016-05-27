import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.internal.ConnectionProvider;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;
import io.silverware.microservices.util.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;

import java.util.Date;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

@Microservice("activeMQJNDIMicroservice")
public class ActiveMQJNDIMicroservice {

   private Logger log = LogManager.getLogger(ActiveMQJNDIMicroservice.class);

   @Inject
   @MicroserviceReference("jndiConnection")
   private Connection connection;

   @Inject
   @MicroserviceReference("jndiJMSContext")
   private JMSContext jmsContext;

   public void observer(@Observes MicroservicesStartedEvent event) throws Exception {
      log.info("Hello from " + this.getClass().getSimpleName());

      //start embedded broker instance
      EmbeddedJMS jmsServer = new EmbeddedJMS();

      jmsServer.setConfigResourcePath(ClassLoader.getSystemResource("broker/broker.xml").toURI().toString());

      jmsServer.start();
      log.info("Started Embedded JMS Server");

      //JMS 1.1
      //create resources
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      Queue queue11 = session.createQueue("exampleQueue11");

      MessageProducer messageProducer = session.createProducer(queue11);

      MessageConsumer messageConsumer = session.createConsumer(queue11);

      //send & receive message
      messageProducer.send(session.createTextMessage("HELLO FROM JMS 1.1 PRODUCER SENT ON " + new Date()));

      TextMessage textMessage = (TextMessage) messageConsumer.receive(1000);
      System.out.println("RECEIVED MESSAGE: " + textMessage.getText());

      //close created resources
      messageProducer.close();
      messageConsumer.close();
      session.close();

      //JMS 2.0
      //create resources
      Queue queue20 = jmsContext.createQueue("exampleQueue20");

      JMSProducer jmsProducer = jmsContext.createProducer();

      JMSConsumer jmsConsumer = jmsContext.createConsumer(queue20);

      //send & receive
      jmsProducer.send(queue20, "HELLO FROM JMS 2.0 PRODUCER SENT ON " + new Date());

      String message = jmsConsumer.receiveBody(String.class, 1000);
      System.out.println("RECEIVED MESSAGE: " + message);

      //close resources
      jmsConsumer.close();

      //stop the server
      jmsServer.stop();
      log.info("Embedded JMS Server stopped");
   }
}
