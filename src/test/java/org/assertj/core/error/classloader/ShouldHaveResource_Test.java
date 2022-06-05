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
import static org.assertj.core.error.classloader.ShouldHaveResource.shouldHaveResource;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ShouldHaveResource#shouldHaveResource(ClassLoader, String)}.
 *
 * @author Ashley Scopes
 */
@SuppressWarnings("ConstantConditions")
class ShouldHaveResource_Test {

  @Test
  void should_disallow_null_class_loader() {
    // Given
    StubClassLoader classLoader = null;
    String resource = "java/lang/String.class";

    // Then
    assertThatCode(() -> shouldHaveResource(classLoader, resource))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("classLoader should not be null");
  }

  @Test
  void should_disallow_null_path() {
    // Given
    StubClassLoader classLoader = new StubClassLoader();
    String resource = null;

    // Then
    assertThatCode(() -> shouldHaveResource(classLoader, resource))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("path should not be null");
  }

  @Test
  void should_create_error_message_for_class_loader_and_resource() {
    // Given
    StubClassLoader classLoader = new StubClassLoader();

    // When
    ErrorMessageFactory factory = shouldHaveResource(classLoader, "foo/bar/baz.hcl");

    // Then
    assertThat(factory.create())
      .withRepresentation(StandardRepresentation.STANDARD_REPRESENTATION)
      .isEqualTo(
        String.join(
          "%n",
          "",
          "Expecting class loader",
          "  %s",
          "to have a valid resource at:",
          "  \"%s\"",
          "but none were found"
        ),
        classLoader.getExpectedStandardRepresentation(),
        "foo/bar/baz.hcl"
      );
  }
}
