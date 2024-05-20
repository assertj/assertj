/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.testkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.assertj.core.configuration.ConfigurationProvider;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.Isolated;

/**
 * An annotation for any test class that mutates the global configuration.
 *
 * <p>By using this annotation, any tests that mutate this configuration will have the configuration
 * reset to the default values after each test case runs.
 *
 * @author Ashley Scopes
 */
@ExtendWith(MutatesGlobalConfiguration.AssumptionMutatingExtension.class)
@Isolated("Mutates global state")
@Target(ElementType.TYPE)
public @interface MutatesGlobalConfiguration {

  final class AssumptionMutatingExtension implements AfterEachCallback, AfterAllCallback {

    @Override
    public void afterEach(ExtensionContext context) {
      resetAll();
    }

    @Override
    public void afterAll(ExtensionContext context) {
      resetAll();
    }

    private void resetAll() {
      ConfigurationProvider.CONFIGURATION_PROVIDER.configuration().setDefaults();
    }
  }
}
