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
 * Created by pmacik on 4.3.16.
 */
public class RgbLedBatchProcessorTest {
   private RgbLedBatchProcessor processor = new RgbLedBatchProcessor();
   private CamelContext ctx = new DefaultCamelContext();
   private Configuration config = Configuration.getInstance();

   @Test
   public void testAllLeds() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("65535;r;50");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047");
   }

   @Test
   public void testSingleChannel() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("0;r;0");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;0");

      msg.setBody("0;r;50");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;2047");

      msg.setBody("0;r;100");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;4095");
   }

   @Test
   public void testSingleLed() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("0;r;0\n0;g;50\n0;b;100");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;0\n"
            + "0x00;1;2047\n"
            + "0x00;2;4095");
   }

   @Test
   public void testNonExistingLed() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("5;r;0\n0;g;50\n0;b;100");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;2047\n"
            + "0x00;2;4095");
   }

   @Test
   public void testAllChannelsAllLeds() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("65535;r;0\n65535;g;50\n65535;b;100\n");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;0\n"
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
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("65535;r;50\n0;g;100\n1;g;100");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047\n"
            + "0x00;1;4095\n"
            + "0x00;4;4095");
   }

   @Test
   public void testRgbLedBatchWithAllLedsInTheMiddle() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("0;g;100\n65535;r;50\n1;g;100");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;4095\n"
            + "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047\n"
            + "0x00;4;4095");
   }

   @Test
   public void testRgbLedBatchWithAllLedsAtTheEnd() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("0;g;100\n1;g;100\n65535;r;50");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;4095\n"
            + "0x00;4;4095\n"
            + "0x00;0;2047\n"
            + "0x00;3;2047\n"
            + "0x00;6;2047\n"
            + "0x00;9;2047\n"
            + "0x00;12;2047");
   }

   @Test
   public void testRgbLedBatchSmoothed() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("0;g;10");
      msg.setHeader(RgbLedBatchProcessor.SMOOTH_SET_HEADER, "true");

      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;40\n"
            + "0x00;1;81\n"
            + "0x00;1;122\n"
            + "0x00;1;163\n"
            + "0x00;1;204\n"
            + "0x00;1;245\n"
            + "0x00;1;286\n"
            + "0x00;1;327\n"
            + "0x00;1;368\n"
            + "0x00;1;409");
   }

   @Test
   public void testRgbLedBatchSmoothedWithReset() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("0;g;10");
      msg.setHeader(RgbLedBatchProcessor.SMOOTH_SET_HEADER, "true");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;40\n"
            + "0x00;1;81\n"
            + "0x00;1;122\n"
            + "0x00;1;163\n"
            + "0x00;1;204\n"
            + "0x00;1;245\n"
            + "0x00;1;286\n"
            + "0x00;1;327\n"
            + "0x00;1;368\n"
            + "0x00;1;409");

      msg.setBody("0;g;15");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;450\n"
            + "0x00;1;491\n"
            + "0x00;1;532\n"
            + "0x00;1;573\n"
            + "0x00;1;614");

      config.resetRgbLeds();

      msg.setBody("0;g;10");
      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;40\n"
            + "0x00;1;81\n"
            + "0x00;1;122\n"
            + "0x00;1;163\n"
            + "0x00;1;204\n"
            + "0x00;1;245\n"
            + "0x00;1;286\n"
            + "0x00;1;327\n"
            + "0x00;1;368\n"
            + "0x00;1;409");
   }

   @Test
   public void testRgbLedLongerBatchSmoothed() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("0;g;10\n0;r;10\n0;g;5;\n0;g;15");
      msg.setHeader(RgbLedBatchProcessor.SMOOTH_SET_HEADER, "true");

      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;1;40\n" // G 0 -> 10
            + "0x00;1;81\n"
            + "0x00;1;122\n"
            + "0x00;1;163\n"
            + "0x00;1;204\n"
            + "0x00;1;245\n"
            + "0x00;1;286\n"
            + "0x00;1;327\n"
            + "0x00;1;368\n"
            + "0x00;1;409\n"
            + "0x00;0;40\n" // R 0 -> 10
            + "0x00;0;81\n"
            + "0x00;0;122\n"
            + "0x00;0;163\n"
            + "0x00;0;204\n"
            + "0x00;0;245\n"
            + "0x00;0;286\n"
            + "0x00;0;327\n"
            + "0x00;0;368\n"
            + "0x00;0;409\n"
            + "0x00;1;368\n" // G 10 -> 5
            + "0x00;1;327\n"
            + "0x00;1;286\n"
            + "0x00;1;245\n"
            + "0x00;1;204\n"
            + "0x00;1;245\n" // G 5 -> 15
            + "0x00;1;286\n"
            + "0x00;1;327\n"
            + "0x00;1;368\n"
            + "0x00;1;409\n"
            + "0x00;1;450\n"
            + "0x00;1;491\n"
            + "0x00;1;532\n"
            + "0x00;1;573\n"
            + "0x00;1;614");
   }

   @Test
   public void testAllLedsSmoothed() throws Exception {
      final Exchange ex = new DefaultExchange(ctx);
      final Message msg = ex.getIn();
      config.resetRgbLeds();

      msg.setBody("65535;r;10");
      msg.setHeader(RgbLedBatchProcessor.SMOOTH_SET_HEADER, "true");

      processor.process(ex);
      Assert.assertEquals(msg.getBody(), "0x00;0;40\n" // #0 R 0 -> 10
            + "0x00;0;81\n"
            + "0x00;0;122\n"
            + "0x00;0;163\n"
            + "0x00;0;204\n"
            + "0x00;0;245\n"
            + "0x00;0;286\n"
            + "0x00;0;327\n"
            + "0x00;0;368\n"
            + "0x00;0;409\n"
            + "0x00;3;40\n" // #1 R 0 -> 10
            + "0x00;3;81\n"
            + "0x00;3;122\n"
            + "0x00;3;163\n"
            + "0x00;3;204\n"
            + "0x00;3;245\n"
            + "0x00;3;286\n"
            + "0x00;3;327\n"
            + "0x00;3;368\n"
            + "0x00;3;409\n"
            + "0x00;6;40\n" // #2 R 0 -> 10
            + "0x00;6;81\n"
            + "0x00;6;122\n"
            + "0x00;6;163\n"
            + "0x00;6;204\n"
            + "0x00;6;245\n"
            + "0x00;6;286\n"
            + "0x00;6;327\n"
            + "0x00;6;368\n"
            + "0x00;6;409\n"
            + "0x00;9;40\n" // #3 R 0 -> 10
            + "0x00;9;81\n"
            + "0x00;9;122\n"
            + "0x00;9;163\n"
            + "0x00;9;204\n"
            + "0x00;9;245\n"
            + "0x00;9;286\n"
            + "0x00;9;327\n"
            + "0x00;9;368\n"
            + "0x00;9;409\n"
            + "0x00;12;40\n" // #4 R 0 -> 10
            + "0x00;12;81\n"
            + "0x00;12;122\n"
            + "0x00;12;163\n"
            + "0x00;12;204\n"
            + "0x00;12;245\n"
            + "0x00;12;286\n"
            + "0x00;12;327\n"
            + "0x00;12;368\n"
            + "0x00;12;409");
   }
}
