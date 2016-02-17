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

import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class DoorCommand extends Command {

   public enum Door {
      FRONT, REAR
   }

   private Door door = Door.FRONT;
   private int openPercentage = 0;

   public DoorCommand(final Door door, final int openPercentage) {
      this.door = door;
      this.openPercentage = openPercentage;
   }

   public Door getDoor() {
      return door;
   }

   public int getOpenPercentage() {
      return openPercentage;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final DoorCommand that = (DoorCommand) o;

      if (openPercentage != that.openPercentage) {
         return false;
      }
      return door == that.door;

   }

   @Override
   public int hashCode() {
      int result = door.hashCode();
      result = 31 * result + openPercentage;
      return result;
   }

   @Override
   public String toString() {
      return "DoorCommand{" +
            "door=" + door +
            ", openPercentage=" + openPercentage +
            '}';
   }

   @Override
   public Map<String, String> getCacheUpdate() {
      return Collections.singletonMap(this.getClass().getCanonicalName() + "." + door.toString(), String.valueOf(openPercentage));
   }
}
