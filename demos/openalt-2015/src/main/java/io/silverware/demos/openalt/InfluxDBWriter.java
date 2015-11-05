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

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.util.Map;
import javax.inject.Inject;

import io.silverware.microservices.annotations.Microservice;
import io.silverware.microservices.annotations.MicroserviceReference;
import io.silverware.microservices.providers.cdi.builtin.Configuration;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
@Microservice
public class InfluxDBWriter {

   public static final String INFLUXDB_URL = "influxdb.url";
   public static final String INFLUXDB_USER = "influxdb.user";
   public static final String INFLUXDB_PASSWORD = "influxdb.password";

   @Inject
   @MicroserviceReference
   private Configuration configuration;

   public void write(final String dbName, final String measurement, final Map<String, Object> fields) {
      InfluxDB influxDB = InfluxDBFactory.connect((String) configuration.getProperty(INFLUXDB_URL), (String) configuration.getProperty(INFLUXDB_USER), (String) configuration.getProperty(INFLUXDB_PASSWORD));
      Point p = Point.measurement(measurement).fields(fields).build();
      influxDB.write(dbName, "default", p);
   }
}
