package io.silverware.demos.devconf2016.intelligent_home.model;

/**
 * Created by pmacik on 1.2.16.
 */
public class Channel {
   private int pca9685Id = -1;
   private int pwm = -1;

   public int getPca9685Id() {
      return pca9685Id;
   }

   public Channel setPca9685Id(final int pca9685Id) {
      this.pca9685Id = pca9685Id;
      return this;
   }

   public int getPwm() {
      return pwm;
   }

   public Channel setPwm(final int pwm) {
      this.pwm = pwm;
      return this;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Channel)) {
         return false;
      }

      final Channel channel = (Channel) o;

      if (getPca9685Id() != channel.getPca9685Id()) {
         return false;
      }
      return getPwm() == channel.getPwm();

   }

   @Override
   public int hashCode() {
      int result = getPca9685Id();
      result = 31 * result + getPwm();
      return result;
   }
}
