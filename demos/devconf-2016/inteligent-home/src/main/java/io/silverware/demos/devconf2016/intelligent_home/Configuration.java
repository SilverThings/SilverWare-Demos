package io.silverware.demos.devconf2016.intelligent_home;

import java.io.IOException;
import java.util.Properties;

import io.silverware.demos.devconf2016.intelligent_home.model.Channel;
import io.silverware.demos.devconf2016.intelligent_home.model.RgbLed;
import io.silverware.demos.devconf2016.intelligent_home.processors.RgbLedProcessor;

/**
 * Created by pmacik on 1.2.16.
 */
public final class Configuration {

   private static final Configuration INSTANCE = new Configuration();

   private static final String PCA9685_PREFIX = "pca9685";
   private static final String LED_PREFIX = "led";
   private static final String SERVO_PREFIX = "servo";

   public static final int RGB_LED_COUNT = 15;
   public static final int PCA9685_COUNT = 3;
   public static final int SERVO_COUNT = 2;

   public static final String CONFIG_FILE = "/home.conf";

   private final String[] pca9685s = new String[PCA9685_COUNT];
   private final RgbLed[] rgbLeds = new RgbLed[RGB_LED_COUNT];
   private final Channel[] servos = new Channel[SERVO_COUNT];

   private final Properties homeConfig = new Properties();
   public final String[] channelNames = new String[] { "r", "g", "b" };

   private Configuration() {
      try {
         homeConfig.load(RgbLedProcessor.class.getResourceAsStream(CONFIG_FILE));

         // for all leds in config
         for (int led = 0; led < RGB_LED_COUNT; led++) {

            for (String channel : channelNames) {
               final String rgbLedProp = homeConfig.getProperty(LED_PREFIX + "." + led + "." + channel);

               if (rgbLedProp != null) {
                  final String[] ledCoordinates = rgbLedProp.split(";");
                  final int pca9685Id = Integer.valueOf(ledCoordinates[0]);
                  final String pca9685Address = homeConfig.getProperty(PCA9685_PREFIX + "." + pca9685Id);

                  if (pca9685Address != null) {
                     if (pca9685s[pca9685Id] == null) {
                        pca9685s[pca9685Id] = pca9685Address;
                     }
                     if (rgbLeds[led] == null) {
                        rgbLeds[led] = new RgbLed();
                     }
                     if (!rgbLeds[led].getChannelMap().containsKey(channel)) {
                        final Channel newChannel = new Channel();
                        newChannel.setValue(0);
                        rgbLeds[led].getChannelMap().put(channel, newChannel);
                     }
                     rgbLeds[led].getChannelMap().get(channel).setPca9685Id(pca9685Id).setPwm(Integer.valueOf(ledCoordinates[1]));
                  }
               }
            }
         }

         for (int servo = 0; servo < SERVO_COUNT; servo++) {
            final String servoProp = homeConfig.getProperty(SERVO_PREFIX + "." + servo);

            if (servoProp != null) {
               final String[] servoCoordinates = servoProp.split(";");
               final int pca9685Id = Integer.valueOf(servoCoordinates[0]);
               final String pca9685Address = homeConfig.getProperty(PCA9685_PREFIX + "." + pca9685Id);

               if (pca9685Address != null) {
                  if (pca9685s[pca9685Id] == null) {
                     pca9685s[pca9685Id] = pca9685Address;
                  }
                  if (servos[servo] == null) {
                     servos[servo] = new Channel();
                  }
                  servos[servo].setPca9685Id(pca9685Id).setPwm(Integer.valueOf(servoCoordinates[1]));
               }
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static Configuration getInstance() {
      return INSTANCE;
   }

   public String getRgbLedPca9685Address(int led, String channel) {
      final RgbLed rgbLed = getRgbLed(led);
      if (rgbLed == null) {
         return null;
      } else {
         return pca9685s[getRgbLed(led).getChannelMap().get(channel).getPca9685Id()];
      }
   }

   public int getRgbLedPwm(int led, String channel) {
      final RgbLed rgbLed = getRgbLed(led);
      if (rgbLed == null) {
         return -1;
      } else {
         Channel ch = rgbLed.getChannelMap().get(channel);
         if (ch == null) {
            return -1;
         } else {
            return ch.getPwm();
         }
      }
   }

   public int getRgbLedValue(int led, String channel) {
      final RgbLed rgbLed = getRgbLed(led);
      if (rgbLed == null) {
         return -1;
      } else {
         Channel ch = rgbLed.getChannelMap().get(channel);
         if (ch == null) {
            return -1;
         } else {
            return ch.getValue();
         }
      }
   }

   public void setRgbLedValue(int led, String channel, int value) {
      final RgbLed rgbLed = getRgbLed(led);
      if (rgbLed == null) {
         return;
      } else {
         Channel ch = rgbLed.getChannelMap().get(channel);
         if (ch == null) {
            return;
         } else {
            ch.setValue(value);
         }
      }
   }

   public RgbLed getRgbLed(int led) {
      return rgbLeds[led];
   }

   public String getServoPca9685Address(int servo) {
      if (servos[servo] == null) {
         return null;
      } else {
         return pca9685s[servos[servo].getPca9685Id()];
      }
   }

   public int getServoPwm(int servo) {
      if (servos[servo] == null) {
         return -1;
      } else {
         return servos[servo].getPwm();
      }
   }

   public void resetRgbLeds() {
      Channel channel = null;
      for (RgbLed led : rgbLeds) {
         for (String channelName : channelNames) {
            if (led != null) {
               channel = led.getChannelMap().get(channelName);
               if (channel != null) {
                  channel.setValue(0);
               }
            }
         }
      }
   }

   public String getSensorAddress() {
      return homeConfig.getProperty("dht11.address");
   }

   public String getRfidAddress() {
      return homeConfig.getProperty("rfid.address");
   }

   public String getFanGpioPin() {
      return homeConfig.getProperty("fan.gpio");
   }

   public String getFireplaceGpioPin() {
      return homeConfig.getProperty("fireplace.gpio");
   }

   public String getRestHost() {
      return homeConfig.getProperty("iot.host", "0.0.0.0:8282");
   }

   public String getMqttHost() {
      return homeConfig.getProperty("mqtt.host", "localhost:1883");
   }
}
