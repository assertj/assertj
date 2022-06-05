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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.error.classloader.ShouldNotBePrivate.shouldNotBePrivate;

import javax.management.loading.PrivateClassLoader;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ShouldNotBePrivate#shouldNotBePrivate(ClassLoader)}.
 *
 * @author Ashley Scopes
 */
@SuppressWarnings("ConstantConditions")
class ShouldNotBePrivate_Test {

  @Test
  void should_disallow_null_class_loader() {
    // Given
    StubClassLoader classLoader = null;

    // Then
    assertThatCode(() -> shouldNotBePrivate(classLoader))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("classLoader should not be null");
  }

  @Test
  void should_create_error_message_for_class_loader() {
    // Given
    StubClassLoader classLoader = new StubClassLoader();

    // When
    ErrorMessageFactory factory = shouldNotBePrivate(classLoader);

    // Then
    assertThat(factory.create())
      .withRepresentation(StandardRepresentation.STANDARD_REPRESENTATION)
      .isEqualTo(
        String.join(
          "%n",
          "",
          "Expecting class loader",
          "  %s",
          "to not be private, but it implements the %s marker interface"
        ),
        classLoader.getExpectedStandardRepresentation(),
        PrivateClassLoader.class.getName()
      );
  }
}
