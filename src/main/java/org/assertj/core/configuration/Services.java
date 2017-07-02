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

import static java.lang.String.format;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * A simple locator for SPI implementations.
 *
 * @author Filip Hrisafov
 */
class Services {

  private Services() {}

  public static <SERVICE> SERVICE get(Class<SERVICE> serviceType, SERVICE defaultValue) {

    Iterator<SERVICE> services = ServiceLoader.load(serviceType, Services.class.getClassLoader()).iterator();

    SERVICE result = services.hasNext() ? services.next() : defaultValue;
    if (services.hasNext()) {
      result = defaultValue;
      System.err.println(format("Found multiple implementations for the service provider %s. Using the default: %s",
                                serviceType, result.getClass()));
    }
    return result;
  }
}
