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
import static org.assertj.core.error.classloader.ShouldNotLoadClassSuccessfully.shouldNotLoadClassSuccessfully;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * {@link ShouldNotLoadClassSuccessfully#shouldNotLoadClassSuccessfully(ClassLoader, String)}.
 *
 * @author Ashley Scopes
 */
@SuppressWarnings("ConstantConditions")
class ShouldNotLoadClassSuccessfully_Test {

  @Test
  void should_disallow_null_class_loader() {
    // Given
    StubClassLoader classLoader = null;
    String binaryName = "foo.bar.Baz";

    // Then
    assertThatCode(() -> shouldNotLoadClassSuccessfully(classLoader, binaryName))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("classLoader should not be null");
  }

  @Test
  void should_disallow_null_binary_name() {
    // Given
    StubClassLoader classLoader = new StubClassLoader();
    String binaryName = null;

    // Then
    assertThatCode(() -> shouldNotLoadClassSuccessfully(classLoader, binaryName))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("binaryName should not be null");
  }

  @Test
  void should_create_error_message_for_class_loader() {
    // Given
    StubClassLoader classLoader = new StubClassLoader();
    String binaryName = "bork.qux.Quxx";

    // When
    ErrorMessageFactory factory = shouldNotLoadClassSuccessfully(classLoader, binaryName);

    // Then
    assertThat(factory.create())
      .withRepresentation(StandardRepresentation.STANDARD_REPRESENTATION)
      .isEqualTo(
        String.join(
          "%n",
          "",
          "Expecting class loader",
          "  %s",
          "to not successfully load class with name:",
          "  \"%s\"",
          "but a class was successfully returned"
        ),
        classLoader.getExpectedStandardRepresentation(),
        binaryName
      );
  }
}
