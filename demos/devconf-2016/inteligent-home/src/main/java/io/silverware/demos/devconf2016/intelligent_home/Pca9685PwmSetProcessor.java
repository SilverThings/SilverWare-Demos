package io.silverware.demos.devconf2016.intelligent_home;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

/**
 * Created by pmacik on 27.1.16.
 */
public class Pca9685PwmSetProcessor implements Processor {
   private static final Logger log = Logger.getLogger(Pca9685PwmSetProcessor.class);

   @Override
   public void process(final Exchange exchange) throws Exception {
      Message msg = exchange.getIn();
      StringBuffer i2cMsg = new StringBuffer();
      // bound pwm output number to 0-15 range;
      final Object pwmHeader = msg.getHeader(Pca9685RouteBuilder.PWM_HEADER);
      if(pwmHeader == null){
         throw new RuntimeException("'pwm' header not found! Value 0 - 15 expected.");
      }
      final int pwm = Math.max(0, Math.min(15, Integer.valueOf(pwmHeader.toString())));

      // bound value to 0-4095 range
      final Object valueHeader = msg.getHeader(Pca9685RouteBuilder.VALUE_HEADER);
      if(valueHeader == null){
         throw new RuntimeException("'value' header not found! Value 0 - 100 expected.");
      }
      final int value = Math.max(0, Math.min(4095, Integer.valueOf(valueHeader.toString())));
      i2cMsg.append(Pca9685Util.pwmAddress(pwm));
      i2cMsg.append(Pca9685Util.hex16bit(0)); // HIGH pulse start
      i2cMsg.append(Pca9685Util.hex16bit((int) (value))); // LOW pulse end
      if (log.isInfoEnabled()) {
         log.info("Sending I2C message: " + i2cMsg.toString());
      }
      msg.setBody(i2cMsg);
   }
}
