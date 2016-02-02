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

   private static final String LED_PCA9685_PREFIX = "pca9685";
   private static final String LED_PREFIX = "led";

   public static final int RGB_LED_COUNT = 16;
   public static final int PCA9685_COUNT = 3;

   public static final String CONFIG_FILE = "/home.conf";

   private final String[] pca9685s = new String[PCA9685_COUNT];
   private final RgbLed[] rgbLeds = new RgbLed[RGB_LED_COUNT];

   private final Properties homeConfig = new Properties();

   private Configuration() {
      try {
         homeConfig.load(RgbLedProcessor.class.getResourceAsStream(CONFIG_FILE));
         final String[] channelNames = new String[] { "r", "g", "b" };

         // for all leds in config
         for (int led = 0; led < RGB_LED_COUNT; led++) {

            for (int ch = 0; ch < 3; ch++) {
               final String channel = channelNames[ch];
               final String rgbLedProp = homeConfig.getProperty(LED_PREFIX + "." + led + "." + channel);

               if (rgbLedProp != null) {
                  final String[] ledCoordinates = rgbLedProp.split(";");
                  final int pca9685Id = Integer.valueOf(ledCoordinates[0]);
                  final String pca9685Address = homeConfig.getProperty(LED_PCA9685_PREFIX + "." + pca9685Id);

                  if (pca9685Address != null) {
                     if (pca9685s[pca9685Id] == null) {
                        pca9685s[pca9685Id] = pca9685Address;
                     }
                     if (rgbLeds[led] == null) {
                        rgbLeds[led] = new RgbLed();
                     }
                     if (!rgbLeds[led].getChannelMap().containsKey(channel)) {
                        rgbLeds[led].getChannelMap().put(channel, new Channel());
                     }
                     rgbLeds[led].getChannelMap().get(channel).setPca9685Id(pca9685Id).setPwm(Integer.valueOf(ledCoordinates[1]));
                  }
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

   public String getPca9685Address(int led, String channel) {
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

   public RgbLed getRgbLed(int led) {
      return rgbLeds[led];
   }

   public String getSensorAddress() {
      return homeConfig.getProperty("dht11.address");
   }

   public String getFanGpioPin() {
      return homeConfig.getProperty("fan.gpio");
   }

   public String getFireplaceGpioPin() {
      return homeConfig.getProperty("fireplace.gpio");
   }

   public String getRestHost() {
      return homeConfig.getProperty("rest.host", "0.0.0.0");
   }

   public String getRestPort() {
      return homeConfig.getProperty("rest.port", "8282");
   }
}
