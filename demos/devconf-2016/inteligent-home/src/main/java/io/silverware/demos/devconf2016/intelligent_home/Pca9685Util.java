/*
 * -----------------------------------------------------------------------\
 * SilverWare
 *  
 * Copyright (C) 2010 - 2013 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package io.silverware.demos.devconf2016.intelligent_home;

/**
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public final class Pca9685Util {
   public static int PWM_BASE_ADDRESS = 0x06;

   public static String pwmAddress(final int pwm) {
      return hex(PWM_BASE_ADDRESS + 4 * pwm);
   }

   public static String hex16bit(final int value) {
      final StringBuffer sb = new StringBuffer();
      sb.append(hex(value));
      sb.append(hex(value >> 8));
      return sb.toString();
   }

   public static String hex(final int value) {
      return String.format("%02X", value & 0xFF);
   }

   public static String hexMessage(final int pwm, final int value) {
      final StringBuffer i2cMsg = new StringBuffer();
      i2cMsg.append(Pca9685Util.pwmAddress(Math.max(0, Math.min(15, pwm))));
      i2cMsg.append(Pca9685Util.hex16bit(0)); // HIGH pulse start
      i2cMsg.append(Pca9685Util.hex16bit(Math.max(0, Math.min(4095, value)))); // LOW pulse end
      return i2cMsg.toString();
   }
}
