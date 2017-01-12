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
package io.silverware.demos.quickstarts.version;

import java.io.Serializable;
import java.util.UUID;

/**
 * Simple class demonstrating different possibilities
 *
 * @author Slavomír Krupa (slavomir.krupa@gmail.com)
 */
public class CustomObject implements Serializable {
   private int mInteger;
   private String mString;
   private float mFloat;
   private BrokenInnerClass innerClass;

   public CustomObject(int mInteger, String mString, float mFloat, BrokenInnerClass innerClass) {
      this.mInteger = mInteger;
      this.mString = mString;
      this.mFloat = mFloat;
      this.innerClass = innerClass;
   }

   public int getmInteger() {
      return mInteger;
   }

   public void setmInteger(int mInteger) {
      this.mInteger = mInteger;
   }

   public String getmString() {
      return mString;
   }

   public void setmString(String mString) {
      this.mString = mString;
   }

   public float getmFloat() {
      return mFloat;
   }

   public void setmFloat(float mFloat) {
      this.mFloat = mFloat;
   }

   public BrokenInnerClass getInnerClass() {
      return innerClass;
   }

   public void setInnerClass(BrokenInnerClass innerClass) {
      this.innerClass = innerClass;
   }

   @Override
   public String toString() {
      return "CustomObject{" +
            "mInteger=" + mInteger +
            ", mString='" + mString + '\'' +
            ", mFloat=" + mFloat +
            ", innerClass=" + innerClass +
            '}';
   }

   public static class BrokenInnerClass implements Serializable {
      private UUID id;

      public BrokenInnerClass(UUID id) {
         this.id = id;
      }

      public UUID getId() {
         return id;
      }

      public void setId(UUID id) {
         this.id = id;
      }

      @Override
      public String toString() {
         return "BrokenInnerClass{" +
               "id=" + id +
               '}';
      }
   }
}
