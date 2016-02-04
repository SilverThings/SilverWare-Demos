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
public class MoodCommand {

   public enum Mood {
      SLEEP, ROMANTIC, EVENING, DAY
   }

   private Mood mood;

   public MoodCommand(final Mood mood) {
      this.mood = mood;
   }

   public Mood getMood() {
      return mood;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final MoodCommand that = (MoodCommand) o;

      return mood == that.mood;

   }

   @Override
   public int hashCode() {
      return mood.hashCode();
   }

   @Override
   public String toString() {
      return "M" + mood.ordinal();
   }
}
