/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.configuration;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple locator for SPI implementations.
 *
 * @author Filip Hrisafov
 */
class Services {

  private static final Logger logger = Logger.getLogger(Services.class.getCanonicalName());

  private Services() {
  }

  public static <T> T get(Class<T> serviceType, T defaultValue) {

    Iterator<T> services = ServiceLoader.load(serviceType, Services.class.getClassLoader()).iterator();

    T result;
    if (services.hasNext()) {
      result = services.next();
    } else {
      result = defaultValue;
    }
    if (services.hasNext()) {
      result = defaultValue;
      logger
        .log(Level.WARNING, "Found multiple implementations for the service provider {0}. Using the default: {1}",
             new Object[] { serviceType, result.getClass() });
    }
    return result;
  }
}
