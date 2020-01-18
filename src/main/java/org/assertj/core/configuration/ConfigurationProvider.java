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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.configuration;

import static java.lang.String.format;
import static org.assertj.core.configuration.Configuration.DEFAULT_CONFIGURATION;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

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
  private final Representation representation;
  private final Configuration configuration;

  private ConfigurationProvider() {
    representation = Services.get(Representation.class, STANDARD_REPRESENTATION);
    if (representation != STANDARD_REPRESENTATION) {
      System.err.println(format("Although it still works, registering a Representation through a file named 'org.assertj.core.presentation.Representation' in the META-INF/services directory is deprecated.%n"
                                + "The proper way of providing a Representation is to register a Configuration as described in the documentation (a Configuration allowing to provide a Representation and other AssertJ configuration elements)"));
    }
    configuration = Services.get(Configuration.class, DEFAULT_CONFIGURATION);
    if (configuration != DEFAULT_CONFIGURATION) {
      configuration.applyAndDisplay();
    }
  }

  /**
   * Returns the default {@link Representation} that needs to be used within AssertJ, which is taken first from:
   * <ul>
   * <li>a registered {@link Configuration#representation()} if any </li>
   * <li>a registered {@link Representation}</li>
   * </ul>
   * If no custom representation was registered, the {@link StandardRepresentation} will be used.
   *
   * @return the default {@link Representation} that needs to be used within AssertJ
   * @since 2.9.0 / 3.9.0
   */
  public Representation representation() {
    return configuration.hasCustomRepresentation() ? configuration.representation() : representation;
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
