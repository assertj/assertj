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
import static org.assertj.core.error.classloader.ShouldLoadClassSuccessfully.shouldLoadClassSuccessfully;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.stream.Stream;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ShouldLoadClassSuccessfully#shouldLoadClassSuccessfully}.
 *
 * @author Ashley Scopes
 */
@SuppressWarnings("ConstantConditions")
class ShouldLoadClassSuccessfully_Test {

  @Test
  void should_disallow_null_class_loader() {
    // Given
    StubClassLoader classLoader = null;
    String binaryName = "foo.bar.Baz";
    ClassNotFoundException ex = new ClassNotFoundException("foo.bar.Baz");

    // Then
    assertThatCode(() -> shouldLoadClassSuccessfully(classLoader, binaryName, ex))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("classLoader should not be null");
  }

  @Test
  void should_disallow_null_binary_name() {
    // Given
    StubClassLoader classLoader = new StubClassLoader();
    String binaryName = null;
    ClassNotFoundException ex = new ClassNotFoundException("foo.bar.Baz");

    // Then
    assertThatCode(() -> shouldLoadClassSuccessfully(classLoader, binaryName, ex))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("binaryName should not be null");
  }

  @Test
  void should_disallow_null_exception() {
    // Given
    StubClassLoader classLoader = new StubClassLoader();
    String binaryName = "foo.bar.Baz";
    ClassNotFoundException ex = null;

    // Then
    assertThatCode(() -> shouldLoadClassSuccessfully(classLoader, binaryName, ex))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("ex should not be null");
  }

  @MethodSource("expectedExceptions")
  @ParameterizedTest
  void should_create_error_message_for_class_loader(Throwable ex) {
    // Given
    StubClassLoader classLoader = new StubClassLoader();
    String binaryName = "bork.qux.Quxx";

    // When
    ErrorMessageFactory factory = shouldLoadClassSuccessfully(classLoader, binaryName, ex);

    // Then
    assertThat(factory.create())
      .withRepresentation(StandardRepresentation.STANDARD_REPRESENTATION)
      .isEqualTo(
        String.join(
          "%n",
          "",
          "Expecting class loader",
          "  %s",
          "to be able to successfully load class with binary name:",
          "  \"%s\"",
          "but it threw an exception:",
          "%s"

        ),
        classLoader.getExpectedStandardRepresentation(),
        binaryName,
        StandardRepresentation.STANDARD_REPRESENTATION.toStringOf(ex)
      );
  }

  static Stream<Throwable> expectedExceptions() {
    return Stream.of(
      new ClassNotFoundException("foo.bar.baz"),
      new ClassNotFoundException("lorem.ipsum.dolor.sit.amet"),
      new ClassNotFoundException(),
      new LinkageError("Linkage error!!"),
      new LinkageError(),
      new NoClassDefFoundError(),
      new NoClassDefFoundError("That class ain't gonna load!"),
      new ClassCircularityError(),
      new ClassCircularityError("Circular dependency"),
      new UnsupportedClassVersionError(),
      new UnsupportedClassVersionError("Java v99213213589238 does not exist yet"),
      new GenericSignatureFormatError(),
      new GenericSignatureFormatError("I can't read your signature"),
      new RuntimeException("That is bad"),
      new NullPointerException("That is also bad"),
      new ClassCastException("You cast something badly")
    );
  }
}
