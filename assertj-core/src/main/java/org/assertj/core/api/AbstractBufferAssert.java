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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldHaveCapacity.shouldHaveCapacity;
import static org.assertj.core.error.ShouldHaveLimit.shouldHaveLimit;
import static org.assertj.core.error.ShouldHavePosition.shouldHavePosition;
import static org.assertj.core.error.ShouldHaveRemaining.shouldHaveRemaining;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldBeFlipped.shouldBeFlipped;

import java.nio.Buffer;

/**
 * Base class for all implementations of assertions for {@link Buffer}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" buffer.
 *
 * @author Jean de Leeuw
 */
public abstract class AbstractBufferAssert<SELF extends AbstractBufferAssert<SELF, ACTUAL>, ACTUAL extends Buffer>
    extends AbstractAssert<SELF, ACTUAL> {

  public AbstractBufferAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code Buffer} has a capacity equal to the given expected value.
   *
   * Example:
   *
   * <pre><code class='java'>
   * byte[] testArray = "test".getBytes();
   * Buffer buffer = ByteBuffer.wrap(testArray);
   *
   * // this assertion succeeds ...
   * assertThat(buffer).hasCapacity(testArray.length);
   *
   * // ... but this one fails as "buffer" has a different capacity than the given expected value.
   * assertThat(buffer).hasCapacity(testArray.length - 1);
   * </code></pre>
   *
   * @param expected integer value representing the expected capacity of the buffer.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} capacity is different than the given expected value.
   */
  public SELF hasCapacity(int expected) {
    isNotNull();
    if (actual.capacity() != expected) throwAssertionError(shouldHaveCapacity(expected, actual.capacity(), actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Buffer} has a limit equal to the given expected value.
   *
   * Example:
   *
   * <pre><code class='java'>
   * byte[] testArray = "test".getBytes();
   * Buffer buffer = ByteBuffer.wrap(testArray);
   *
   * // this assertion succeeds ...
   * assertThat(buffer).hasLimit(testArray.length); // Limit of a buffer is equal to the capacity before the buffer is flipped.
   *
   * // ... but this one fails as "buffer" has a different limit than the given expected value.
   * assertThat(buffer).hasLimit(testArray.length - 1);
   * </code></pre>
   *
   * @param expected integer value representing the expected limit of the buffer.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} limit is different than the given expected value.
   */
  public SELF hasLimit(int expected) {
    isNotNull();
    if (actual.limit() != expected) throwAssertionError(shouldHaveLimit(expected, actual.limit(), actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Buffer}'s position is equal to the given expected value.
   *
   * Example:
   *
   * <pre><code class='java'>
   * Buffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).hasPosition(0);
   *
   * // ... but this one fails as the position of "buffer" is different than the given expected value.
   * assertThat(buffer).hasPosition(1);
   * </code></pre>
   *
   * @param expected integer value representing the expected position of the buffer.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer}'s position is different than the given expected value.
   */
  public SELF hasPosition(int expected) {
    isNotNull();
    if (actual.position() != expected) throwAssertionError(shouldHavePosition(expected, actual.position(), actual));
    return myself;
  }

  /**
   * Verifies that the actual {@Code Buffer} has a number of remaining elements equal to the given expected value.
   *
   * Example:
   *
   * <pre><code class='java'>
   * byte[] testArray = "test".getBytes();
   * Buffer buffer = ByteBuffer.wrap(testArray);
   *
   * // this assertion succeeds ...
   * assertThat(buffer).hasRemaining(testArray.length);
   *
   * // ... but this one fails as "buffer" has a different number of remaining elements than the given expected value.
   * assertThat(buffer).hasRemaining(testArray.length - 1);
   * </code></pre>
   *
   * @param expected integer value representing the expected number of remaining elements in the buffer.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} number of remaining elements is different than the given expected value.
   */
  public SELF hasRemaining(int expected) {
    isNotNull();
    if (actual.remaining() != expected) throwAssertionError(shouldHaveRemaining(expected, actual.remaining(), actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Buffer} has been flipped.
   *
   * <b>Important:</b>
   * Due to the limitations of the Buffer API there is, short of looping over the buffer and checking the content one
   * by one, no way to check if the Buffer is actually flipped. It is impossible to make a distinction between
   * a buffer that is filled to its capacity and a buffer that simply has not been flipped (or manual repositioning
   * of the buffer). Due to these limitations, this method only checks if the position of the buffer equals 0.
   * <p>
   * Example:
   *
   * <pre><code class='java'>
   * Buffer buffer = ByteBuffer.allocate(10);
   *
   * // this assertion succeeds ...
   * buffer.flip();
   * assertThat(buffer).isFlipped();
   *
   * // ... but this one fails as "allocate" does not flip the buffer.
   * assertThat(buffer).isFlipped();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} has not been flipped.
   */
  public SELF isFlipped() {
    isNotNull();
    if (actual.position() != 0) throwAssertionError(shouldBeFlipped(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Buffer} is empty.
   *
   * Example:
   *
   * <pre><code class='java'>
   * // this assertion succeeds ...
   * Buffer buffer = ByteBuffer.wrap("".getBytes());
   * assertThat(buffer).isEmpty();
   *
   * // ... but this one fails as "buffer" is not empty.
   * Buffer buffer = ByteBuffer.wrap("test".getBytes());
   * assertThat(buffer).isEmpty();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} has not been flipped.
   * @throws AssertionError if the actual {@code Buffer} is not empty.
   */
  public SELF isEmpty() {
    isNotNull();
    isFlipped();
    if (actual.limit() != 0) throwAssertionError(shouldBeEmpty(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Buffer} is not empty.
   *
   * Example:
   *
   * <pre><code class='java'>
   * // this assertion succeeds ...
   * Buffer buffer = ByteBuffer.wrap("test".getBytes());
   * assertThat(buffer).isNotEmpty();
   *
   * // ... but this one fails as "buffer" is empty.
   * Buffer buffer = ByteBuffer.wrap("".getBytes());
   * assertThat(buffer).isNotEmpty();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} has not been flipped.
   * @throws AssertionError if the actual {@code Buffer} is empty.
   */
  public SELF isNotEmpty() {
    isNotNull();
    isFlipped();
    if (actual.limit() == 0) throwAssertionError(shouldNotBeEmpty());
    return myself;
  }
}
