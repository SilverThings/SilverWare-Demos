import io.silverware.microservices.annotations.JMS;
import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.MicroservicesStartedEvent;

import org.apache.activemq.artemis.core.config.impl.SecurityConfiguration;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.spi.core.security.ActiveMQJAASSecurityManager;
import org.apache.activemq.artemis.spi.core.security.jaas.InVMLoginModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Microservice("activeMQSecuredMicroservice")
public class ActiveMQSecuredMicroservice {

   private Logger log = LogManager.getLogger(ActiveMQSecuredMicroservice.class);

   @Inject
   @MicroserviceReference("securedConnection")
   @JMS(userName = "admin", password = "admin")
   private Connection connection;

   @Inject
   @MicroserviceReference("securedJMSContext")
   @JMS(userName = "admin", password = "admin")
   private JMSContext jmsContext;

   public void observer(@Observes MicroservicesStartedEvent event) throws Exception {
      log.info("Hello from " + this.getClass().getSimpleName());

      //start embedded broker instance
      EmbeddedJMS jmsServer = new EmbeddedJMS();

      jmsServer.setConfigResourcePath(ClassLoader.getSystemResource("broker/broker.xml").toURI().toString());

      //security configuration
      SecurityConfiguration securityConfig = new SecurityConfiguration();
      securityConfig.addUser("admin", "admin");
      securityConfig.addRole("admin", "admin");
      securityConfig.setDefaultUser("foo");
      ActiveMQJAASSecurityManager securityManager = new ActiveMQJAASSecurityManager(InVMLoginModule.class.getName(), securityConfig);
      jmsServer.setSecurityManager(securityManager);

      jmsServer.start();
      log.info("Started Embedded JMS Server");

      //JMS 1.1
      //create resources
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      //createDurableQueue
      Queue queue = session.createQueue("exampleQueue");

      MessageProducer messageProducer = session.createProducer(queue);

      MessageConsumer messageConsumer = session.createConsumer(queue);

      //send & consume
      messageProducer.send(session.createTextMessage("HELLO FROM JMS 1.1 PRODUCER SENT ON " + new Date()));

      TextMessage textMessage = (TextMessage) messageConsumer.receive(1000);
      System.out.println("RECEIVED MESSAGE: " + textMessage.getText());

      //close created resources
      messageProducer.close();
      messageConsumer.close();
      session.close();

      //JMS 2.0
      JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
      JMSProducer jmsProducer = jmsContext.createProducer();

      //send & receive
      jmsProducer.send(queue, "HELLO FROM JMS 2.0 PRODUCER SENT ON " + new Date());

      System.out.println("RECEIVED MESSAGE: " + jmsConsumer.receiveBody(String.class, 1000));

      //close resources
      jmsConsumer.close();

      //stop the server
      jmsServer.stop();
      log.info("Embedded JMS Server stopped");
   }
}
