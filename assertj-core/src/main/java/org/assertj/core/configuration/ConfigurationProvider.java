/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.configuration;

import static java.lang.String.format;
import static org.assertj.core.configuration.Configuration.DEFAULT_CONFIGURATION;

import java.util.List;
import java.util.ServiceLoader;

import org.assertj.core.presentation.CompositeRepresentation;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;

/**
 * Provider for all the configuration settings / parameters within AssertJ.
 * <p>
 * All the configuration possibilities are registered via an SPI.
 *
 * @author Filip Hrisafov
 * @since 2.9.0 / 3.9.0
 */
public final class ConfigurationProvider {

  public static final ConfigurationProvider CONFIGURATION_PROVIDER = new ConfigurationProvider();
  private final Configuration configuration;
  private CompositeRepresentation compositeRepresentation;

  private ConfigurationProvider() {
    configuration = Services.get(Configuration.class, DEFAULT_CONFIGURATION);
    if (configuration != DEFAULT_CONFIGURATION) {
      configuration.applyAndDisplay();
    }
    List<Representation> representations = Services.getAll(Representation.class);
    compositeRepresentation = new CompositeRepresentation(representations);
    if (!configuration.hasCustomRepresentation()) {
      // registered representations are only used if the configuration representation
      if (representations.size() == 1) {
        System.out.println(format("AssertJ has found one registered representation: %s, AssertJ will use it first and then fall back to standard representation if it returned a null representation of the value to display.",
                                  representations.get(0)));
      } else if (representations.size() > 1) {
        System.out.println(format("AssertJ has found %s registered representations, AssertJ will use them first and then fall back to standard representation if they returned a null representation of the value to display, the order (by highest priority first) of use will be: %s",
                                  representations.size(), compositeRepresentation.getAllRepresentationsOrderedByPriority()));
      }
    } else if (!representations.isEmpty()) {
      System.out.println(format("AssertJ has found these representations %s in the classpath but they won't be used as the loaded configuration has specified a custom representation which takes precedence over representations loaded with the java ServiceLoader: %s",
                                representations, representation()));
    }
  }

  /**
   * Returns the {@link Representation} that AssertJ will use, which is taken first from:
   * <ul>
   * <li>the representation returned by a custom {@link Configuration} through {@link Configuration#representation()} but only if it is different from the {@link StandardRepresentation}</li>
   * <li>the {@link Representation} with highest priority loaded from the classpath by the {@link ServiceLoader}</li>
   * </ul>
   * If no custom representation was registered or overridden in a specific {@link Configuration}, the {@link StandardRepresentation} is used.
   * <p>
   * @return the default {@link Representation} that needs to be used within AssertJ
   * @since 2.9.0 / 3.9.0
   * @since 3.22.0 support for registered multiple {@link Representation}s with priority. 
   */
  public Representation representation() {
    return configuration.hasCustomRepresentation() ? configuration.representation() : compositeRepresentation;
  }

  /**
   * Returns the configuration used in for all tests.
   *
   * @return the configuration applied for all tests.
   * @since 3.13.0
   */
  public Configuration configuration() {
    return configuration;
  }

  /**
   * Triggers loading any registered {@link Configuration}.
   * <p>
   * This method should be called before any user configuration changes to make sure these are not overridden by a registered {@link Configuration} later on.
   */
  public static void loadRegisteredConfiguration() {
    // does nothing but results in loading any registered Configuration as CONFIGURATION_PROVIDER is initialized
  }
}
