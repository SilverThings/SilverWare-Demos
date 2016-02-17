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
package io.silverware.demos.devconf.kjar;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class LightCommand extends Command {

   public enum Place {
      JUNK(0),KITCHEN_COOK(1), KITCHEN_SINK(2), BATHROOM_TUB(3), BATHROOM_SINK(4), KITCHEN_GARDEN(5), KITCHEN_TABLE(6),
      CORRIDOR(7), ENTRANCE(8), BEDROOM(9), LIVINGROOM_FIREPLACE(10), LIVINGROOM_LIBRARY(11), BED_WINDOW(12), BED_WARDROBE(13),
      LIVINGROOM_COUCH(14), ALL(0xffff);

      private int led;

      Place(final int led) {
         this.led = led;
      }

      public int getLed() {
         return led;
      }
   }

   private Place place = Place.ALL;
   private LedState state = LedState.OFF;

   public LightCommand(final Place place, final LedState state) {
      this.place = place;
      this.state = state;
   }

   public Place getPlace() {
      return place;
   }

   public LedState getState() {
      return state;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final LightCommand that = (LightCommand) o;

      if (place != that.place) {
         return false;
      }
      return state.equals(that.state);

   }

   @Override
   public int hashCode() {
      int result = place.hashCode();
      result = 31 * result + state.hashCode();
      return result;
   }

   @Override
   public String toString() {
      return "LightCommand{" +
            "place=" + place +
            ", state=" + state +
            '}';
   }
}
