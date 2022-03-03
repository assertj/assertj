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
package org.assertj.guava.api;

import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.guava.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.guava.error.ShouldHaveSize.shouldHaveSize;

import java.io.IOException;

import org.assertj.core.api.AbstractAssert;

import com.google.common.io.ByteSource;

/**
 * Assertions for Guava {@link ByteSource}.
 *
 * @author Andrew Gaul
 */
public class ByteSourceAssert extends AbstractAssert<ByteSourceAssert, ByteSource> {

  protected ByteSourceAssert(ByteSource actual) {
    super(actual, ByteSourceAssert.class);
  }

  /**
   * Verifies that the actual {@link ByteSource} has the same content as the provided one.<br>
   * <p>
   * Example :
   * <pre><code class='java'> ByteSource actual = ByteSource.wrap(new byte[1]);
   * ByteSource other = ByteSource.wrap(new byte[1]);
   *
   * assertThat(actual).hasSameContentAs(other);</code></pre>
   *
   * @param other ByteSource to compare against.
   * @return this {@link ByteSourceAssert} for assertions chaining.
   * @throws IOException    if {@link ByteSource#contentEquals} throws one.
   * @throws AssertionError if the actual {@link ByteSource} is {@code null}.
   * @throws AssertionError if the actual {@link ByteSource} does not contain the same content.
   */
  public ByteSourceAssert hasSameContentAs(ByteSource other) throws IOException {
    isNotNull();
    if (!actual.contentEquals(other)) throw assertionError(shouldHaveSameContent(actual, other));
    return this;
  }

  /**
   * Verifies that the actual {@link ByteSource} is empty.
   * <p>
   * Example :
   * <pre><code class='java'> ByteSource actual = ByteSource.wrap(new byte[0]);
   *
   * assertThat(actual).isEmpty();</code></pre>
   *
   * @throws IOException    if {@link ByteSource#isEmpty} throws one.
   * @throws AssertionError if the actual {@link ByteSource} is {@code null}.
   * @throws AssertionError if the actual {@link ByteSource} is not empty.
   */
  public void isEmpty() throws IOException {
    isNotNull();
    if (!actual.isEmpty()) throw assertionError(shouldBeEmpty(actual));
  }

  /**
   * Verifies that the size of the actual {@link ByteSource} is equal to the given one.
   * <p>
   * Example :
   *
   * <pre><code class='java'> ByteSource actual = ByteSource.wrap(new byte[9]);
   *
   * assertThat(actual).hasSize(9);</code></pre>
   *
   * @param expectedSize the expected size of actual {@link ByteSource}.
   * @return this {@link ByteSourceAssert} for assertions chaining.
   * @throws IOException    if {@link com.google.common.io.ByteSource#size()} throws one.
   * @throws AssertionError if the actual {@link ByteSource} is {@code null}.
   * @throws AssertionError if the number of values of the actual {@link ByteSource} is not equal to the given one.
   */
  public ByteSourceAssert hasSize(long expectedSize) throws IOException {
    isNotNull();
    long sizeOfActual = actual.size();
    if (sizeOfActual != expectedSize) throw assertionError(shouldHaveSize(actual, sizeOfActual, expectedSize));
    return this;
  }

}
