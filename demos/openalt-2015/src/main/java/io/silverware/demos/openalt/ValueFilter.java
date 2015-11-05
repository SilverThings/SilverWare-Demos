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
package io.silverware.demos.openalt;

import java.util.function.Consumer;
import javax.inject.Inject;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.builtin.Storage;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class ValueFilter {

   private final String STORAGE_PREFIX = ValueFilter.class.getCanonicalName() + ".";

   @Inject
   @MicroserviceReference
   private Storage storage;

   public <T> void change(final String valueId, final T value, final Consumer<T> consumer) {
      Object last = storage.get(STORAGE_PREFIX + valueId);
      if (last == null || !last.equals(value)) {
         consumer.accept(value);
      }

      storage.put(STORAGE_PREFIX + valueId, value);
   }
}
