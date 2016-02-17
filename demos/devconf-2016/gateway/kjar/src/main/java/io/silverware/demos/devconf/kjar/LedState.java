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

import java.io.Serializable;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class LedState implements Serializable {

   public static final LedState OFF = new LedState(0, 0, 0);
   public static final LedState ON = new LedState(100, 100, 100);

   private int r, g, b;

   public LedState(final int r, final int g, final int b) {
      this.r = r;
      this.g = g;
      this.b = b;
   }

   public int getR() {
      return r;
   }

   public int getG() {
      return g;
   }

   public int getB() {
      return b;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final LedState ledState = (LedState) o;

      if (r != ledState.r) {
         return false;
      }
      if (g != ledState.g) {
         return false;
      }
      return b == ledState.b;

   }

   @Override
   public int hashCode() {
      int result = r;
      result = 31 * result + g;
      result = 31 * result + b;
      return result;
   }

   @Override
   public String toString() {
      return "LedState{" +
            "r=" + r +
            ", g=" + g +
            ", b=" + b +
            '}';
   }
}
