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
public class MediaCenterCommand extends Command {

   public enum Media {
      OFF, ROMANCE, NEWS
   }

   private Media media = Media.OFF;

   public MediaCenterCommand(final Media media) {
      this.media = media;
   }

   public Media getMedia() {
      return media;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final MediaCenterCommand that = (MediaCenterCommand) o;

      return media == that.media;

   }

   @Override
   public int hashCode() {
      return media.hashCode();
   }

   @Override
   public String toString() {
      return "MediaCenterCommand{" +
            "media=" + media +
            '}';
   }

   @Override
   public Map<String, String> getCacheUpdate() {
      return Collections.singletonMap(this.getClass().getCanonicalName(), media.toString());
   }

}
