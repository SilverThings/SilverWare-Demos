package io.silverware.demos.devconf2016.intelligent_home;

import java.io.IOException;
import java.util.Properties;

import io.silverware.demos.devconf2016.intelligent_home.model.Channel;
import io.silverware.demos.devconf2016.intelligent_home.model.RgbLed;

/**
 * Created by pmacik on 1.2.16.
 */
public class RgbLedConfig {

   private static final String LED_PCA9685_PREFIX = "pca9685";
   private static final String LED_PREFIX = "led";

   public static final int RGB_LED_COUNT = 16;
   public static final int PCA9685_COUNT = 3;

   public static final String RGB_LED_CONFIG_FILE = "/rgbLed.properties";

   private final String[] pca9685s = new String[PCA9685_COUNT];
   private final RgbLed[] rgbLeds = new RgbLed[RGB_LED_COUNT];

   private final Properties rgbLedProperties = new Properties();

   public RgbLedConfig() {
      try {
         rgbLedProperties.load(RgbLedProcessor.class.getResourceAsStream(RGB_LED_CONFIG_FILE));
         final String[] channelNames = new String[] { "r", "g", "b" };

         // for all leds in config
         for (int led = 0; led < RGB_LED_COUNT; led++) {

            for (int ch = 0; ch < 3; ch++) {
               final String channel = channelNames[ch];
               final String rgbLedProp = rgbLedProperties.getProperty(LED_PREFIX + "." + led + "." + channel);

               if (rgbLedProp != null) {
                  final String[] ledCoordinates = rgbLedProp.split(";");
                  final int pca9685Id = Integer.valueOf(ledCoordinates[0]);
                  final String pca9685Address = rgbLedProperties.getProperty(LED_PCA9685_PREFIX + "." + pca9685Id);

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
         return rgbLed.getChannelMap().get(channel).getPwm();
      }
   }

   public RgbLed getRgbLed(int led) {
      return rgbLeds[led];
   }
}
