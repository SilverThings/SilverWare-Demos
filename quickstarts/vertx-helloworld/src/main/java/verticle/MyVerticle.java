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

package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author <a href="mailto:stefankomartin6@gmail.com">Martin Štefanko</a>
 */
public class MyVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(MyVerticle.class);

    @Override
    public void start() throws Exception {
        log.info(this.getClass().getName() + " is starting...");
        vertx.setPeriodic(1000, id -> {
           vertx.eventBus().send("silverware.topic", "Hello from " + this.getClass().getSimpleName());
        });
    }

    @Override
    public void stop() throws Exception {
        log.info(this.getClass().getSimpleName() + " is stopping");
    }
}
