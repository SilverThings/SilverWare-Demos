package io.silverware.demos.devconf2016.intelligent_home.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pmacik on 1.2.16.
 */
public class RgbLed {
   private Map<String, Channel> channelMap = new HashMap<>();

   public Map<String, Channel> getChannelMap() {
      return channelMap;
   }

   public RgbLed setChannelMap(final Map<String, Channel> channelMap) {
      this.channelMap = channelMap;
      return this;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof RgbLed)) {
         return false;
      }

      final RgbLed rgbLed = (RgbLed) o;

      return getChannelMap() != null ? getChannelMap().equals(rgbLed.getChannelMap()) : rgbLed.getChannelMap() == null;

   }

   @Override
   public int hashCode() {
      return getChannelMap() != null ? getChannelMap().hashCode() : 0;
   }
}
