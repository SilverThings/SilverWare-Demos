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
      CORRIDOR, JUNK, BED1, BED2, BEDROOM, BATHROOM, KITCHEN, DININGROOM, LIVINGROOM
   }

   public enum State {
      OFF, DIM, ON
   }

   private Place place = Place.CORRIDOR;
   private State state = State.OFF;

   public LightCommand(final Place place, final State state) {
      this.place = place;
      this.state = state;
   }

   public Place getPlace() {
      return place;
   }

   public State getState() {
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
      return state == that.state;

   }

   @Override
   public int hashCode() {
      int result = place.hashCode();
      result = 31 * result + state.hashCode();
      return result;
   }

   @Override
   public String toString() {
      return "L" + place.ordinal() + state.ordinal();
   }
}
