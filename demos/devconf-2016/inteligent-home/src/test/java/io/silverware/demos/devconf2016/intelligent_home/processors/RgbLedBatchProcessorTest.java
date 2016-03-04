package io.silverware.demos.devconf2016.intelligent_home.processors;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by pmacik on 4.3.16.
 */
public class RgbLedBatchProcessorTest {
   private RgbLedBatchProcessor processor = new RgbLedBatchProcessor();
   private CamelContext ctx = new DefaultCamelContext();

   @Test
   public void testAllLeds() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("65535;r;50");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047");
   }

   @Test
   public void testSingleChannel() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("0;r;0");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;0;0");

      ex.getIn().setBody("0;r;50");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;0;2047");

      ex.getIn().setBody("0;r;100");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;0;4095");
   }

   @Test
   public void testSingleLed() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("0;r;0\n0;g;50\n0;b;100");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;0;0\n"
            + "0x00;1;2047\n"
            + "0x00;2;4095");
   }

   @Test
   public void testNonExistingLed() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("5;r;0\n0;g;50\n0;b;100");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;1;2047\n"
            + "0x00;2;4095");
   }

   @Test
   public void testAllChannelsAllLeds() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("65535;r;0\n65535;g;50\n65535;b;100\n");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;0;0\n"
            + "0x00;3;0\n"
            + "0x00;6;0\n"
            + "0x00;9;0\n"
            + "0x00;12;0\n"
            + "0x00;1;2047\n"
            + "0x00;4;2047\n"
            + "0x00;7;2047\n"
            + "0x00;10;2047\n"
            + "0x00;13;2047\n"
            + "0x00;2;4095\n"
            + "0x00;5;4095\n"
            + "0x00;8;4095\n"
            + "0x00;11;4095\n"
            + "0x00;14;4095");
   }

   @Test
   public void testRgbLedBatchWithAllLedsAtBegining() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("65535;r;50\n0;g;100\n1;g;100");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047\n"
            + "0x00;1;4095\n"
            + "0x00;4;4095");
   }

   @Test
   public void testRgbLedBatchWithAllLedsInTheMiddle() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("0;g;100\n65535;r;50\n1;g;100");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;1;4095\n"
            + "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047\n"
            + "0x00;4;4095");
   }

   @Test
   public void testRgbLedBatchWithAllLedsAtTheEnd() throws Exception {
      Exchange ex = new DefaultExchange(ctx);

      ex.getIn().setBody("0;g;100\n1;g;100\n65535;r;50");
      processor.process(ex);
      Assert.assertEquals(ex.getIn().getBody(), "0x00;1;4095\n"
            + "0x00;4;4095\n"
            + "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047");
   }
}
