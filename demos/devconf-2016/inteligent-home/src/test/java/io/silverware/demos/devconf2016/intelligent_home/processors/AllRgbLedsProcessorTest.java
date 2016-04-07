package io.silverware.demos.devconf2016.intelligent_home.processors;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.silverware.demos.devconf2016.intelligent_home.Configuration;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class AllRgbLedsProcessorTest {
   private AllRgbLedsProcessor processor = new AllRgbLedsProcessor();
   private CamelContext ctx = new DefaultCamelContext();
   private Configuration config = Configuration.getInstance();

   @Test
   public void testAllLeds() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setHeader("r", "50");
      msg.setHeader("g", "50");
      msg.setHeader("b", "50");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0;r;50\n"
            + "0;g;50\n"
            + "0;b;50\n"
            + "1;r;50\n"
            + "1;g;50\n"
            + "1;b;50\n"
            + "2;r;50\n"
            + "2;g;50\n"
            + "2;b;50\n"
            + "3;r;50\n"
            + "3;g;50\n"
            + "3;b;50\n"
            + "4;r;50\n"
            + "4;g;50\n"
            + "4;b;50");
   }

   @Test
   public void testAllLedsSingleChannel() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setHeader("r", "50");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0;r;50\n"
            + "1;r;50\n"
            + "2;r;50\n"
            + "3;r;50\n"
            + "4;r;50");
   }
}
