/*
 * -----------------------------------------------------------------------\
 * Silverware
 *  
 * Copyright (C) 2010 - 2016 the original author or authors.
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
package io.silverware.demos.quickstarts.openshift.cluster.api;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

/**
 * Object which is parameter of method.
 * Notice that it implements Serializable interface
 *
 * @author Slavomír Krupa (slavomir.krupa@gmail.com)
 */
public class CustomObject implements Serializable {

   private static final Random random = new Random();

   public CustomObject(final int number, final String name) {
      this.number = number;
      this.name = name;
   }

   private int number;
   private String name;

   public int getNumber() {
      return number;
   }

   public void setNumber(final int number) {
      this.number = number;
   }

   public String getName() {
      return name;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public static CustomObject randomObject() {
      return new CustomObject(random.nextInt(10), UUID.randomUUID().toString());
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final CustomObject object = (CustomObject) o;

      if (getNumber() != object.getNumber()) {
         return false;
      }
      return getName() != null ? getName().equals(object.getName()) : object.getName() == null;
   }

   @Override
   public int hashCode() {
      int result = getNumber();
      result = 31 * result + (getName() != null ? getName().hashCode() : 0);
      return result;
   }

   @Override
   public String toString() {
      return "CustomObject{" +
            "number=" + number +
            ", name='" + name + '\'' +
            '}';
   }
}
