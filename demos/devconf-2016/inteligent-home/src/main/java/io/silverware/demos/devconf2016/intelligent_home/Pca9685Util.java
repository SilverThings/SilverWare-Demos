package io.silverware.demos.devconf2016.intelligent_home;

/**
 * Created by pmacik on 27.1.16.
 */
public final class Pca9685Util {
   public static int PWM_BASE_ADDRESS = 0x06;

   public static String pwmAddress(final int pwm) {
      return hex(PWM_BASE_ADDRESS + 4 * pwm);
   }

   public static String hex16bit(final int value) {
      StringBuffer sb = new StringBuffer();
      sb.append(hex(value));
      sb.append(hex(value >> 8));
      return sb.toString();
   }

   public static String hex(final int value) {
      return String.format("%02X", value & 0xFF);
   }
}
