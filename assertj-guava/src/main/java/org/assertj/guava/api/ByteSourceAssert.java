/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.guava.api;

import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.guava.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.guava.error.ShouldHaveSize.shouldHaveSize;

import java.io.IOException;

import org.assertj.core.api.AbstractAssert;

import com.google.common.io.ByteSource;

/// Assertions for Guava [ByteSource].
///
/// @author Andrew Gaul
public class ByteSourceAssert extends AbstractAssert<ByteSourceAssert, ByteSource> {

  protected ByteSourceAssert(ByteSource actual) {
    super(actual, ByteSourceAssert.class);
  }

  /// Verifies that the actual [ByteSource] has the same content as the provided one.<br>
  ///
  /// Example :
  /// ```java
  /// ByteSource actual = ByteSource.wrap(new byte[1]);
  /// ByteSource other = ByteSource.wrap(new byte[1]);
  ///
  /// assertThat(actual).hasSameContentAs(other);
  /// ```
  ///
  /// @param other ByteSource to compare against.
  /// @return this [ByteSourceAssert] for assertions chaining.
  /// @throws IOException    if [ByteSource#contentEquals] throws one.
  /// @throws AssertionError if the actual [ByteSource] is `null`.
  /// @throws AssertionError if the actual [ByteSource] does not contain the same content.
  public ByteSourceAssert hasSameContentAs(ByteSource other) throws IOException {
    isNotNull();
    if (!actual.contentEquals(other)) throw assertionError(shouldHaveSameContent(actual, other));
    return this;
  }

  /// Verifies that the actual [ByteSource] is empty.
  ///
  /// Example :
  /// ```java
  /// ByteSource actual = ByteSource.wrap(new byte[0]);
  ///
  /// assertThat(actual).isEmpty();
  /// ```
  ///
  /// @throws IOException    if [ByteSource#isEmpty] throws one.
  /// @throws AssertionError if the actual [ByteSource] is `null`.
  /// @throws AssertionError if the actual [ByteSource] is not empty.
  public void isEmpty() throws IOException {
    isNotNull();
    if (!actual.isEmpty()) throw assertionError(shouldBeEmpty(actual));
  }

  /// Verifies that the size of the actual [ByteSource] is equal to the given one.
  ///
  /// Example :
  ///
  /// ```java
  /// ByteSource actual = ByteSource.wrap(new byte[9]);
  ///
  /// assertThat(actual).hasSize(9);
  /// ```
  ///
  /// @param expectedSize the expected size of actual [ByteSource].
  /// @return this [ByteSourceAssert] for assertions chaining.
  /// @throws IOException    if [com.google.common.io.ByteSource#size()] throws one.
  /// @throws AssertionError if the actual [ByteSource] is `null`.
  /// @throws AssertionError if the number of values of the actual [ByteSource] is not equal to the given one.
  public ByteSourceAssert hasSize(long expectedSize) throws IOException {
    isNotNull();
    long sizeOfActual = actual.size();
    if (sizeOfActual != expectedSize) throw assertionError(shouldHaveSize(actual, sizeOfActual, expectedSize));
    return this;
  }

}
