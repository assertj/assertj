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
import static org.assertj.core.error.classloader.ShouldNotHaveParent.shouldNotHaveParent;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ShouldNotHaveParent#shouldNotHaveParent(ClassLoader, ClassLoader)}.
 *
 * @author Ashley Scopes
 */
@SuppressWarnings("ConstantConditions")
class ShouldNotHaveParent_Test {

  @Test
  void should_disallow_null_child_class_loader() {
    // Given
    StubClassLoader childClassLoader = null;
    StubClassLoader parentClassLoader = new StubClassLoader();

    // Then
    assertThatCode(() -> shouldNotHaveParent(childClassLoader, parentClassLoader))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("child should not be null");
  }

  @Test
  void should_disallow_null_parent_class_loader() {
    // Given
    StubClassLoader childClassLoader = new StubClassLoader();
    StubClassLoader parentClassLoader = null;

    // Then
    assertThatCode(() -> shouldNotHaveParent(childClassLoader, parentClassLoader))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("parent should not be null");
  }

  @Test
  void should_create_error_message_for_class_loader() {
    // Given
    StubClassLoader parentClassLoader = new StubClassLoader();
    StubClassLoader childClassLoader = new StubClassLoader(parentClassLoader);

    // When
    ErrorMessageFactory factory = shouldNotHaveParent(childClassLoader, parentClassLoader);

    // Then
    assertThat(factory.create())
      .withRepresentation(StandardRepresentation.STANDARD_REPRESENTATION)
      .isEqualTo(
        String.join(
          "%n",
          "",
          "Expecting class loader",
          "  %s",
          "to not have a parent class loader, but found:",
          "  %s"
        ),
        childClassLoader.getExpectedStandardRepresentation(),
        parentClassLoader.getExpectedStandardRepresentation()
      );
  }
}
