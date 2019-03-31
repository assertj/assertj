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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.buffer.ShouldBeFlipped.shouldBeFlipped;
import static org.assertj.core.error.buffer.ShouldHaveLength.shouldHaveLength;
import static org.assertj.core.error.buffer.ShouldHaveRemainingLength.shouldHaveRemainingLength;

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
public abstract class AbstractBufferAssert<SELF extends AbstractBufferAssert<SELF, ACTUAL>, ACTUAL extends Buffer> extends AbstractAssert<SELF, ACTUAL> {

  public AbstractBufferAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
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

  /**
   * Verifies that the actual {@code Buffer} has a length equal to the given expected value.
   *
   * Example:
   *
   * <pre><code class='java'>
   * byte[] testArray = "test".getBytes();
   * Buffer buffer = ByteBuffer.wrap(testArray);
   *
   * // this assertion succeeds ...
   * assertThat(buffer).hasLength(testArray.length);
   *
   * // ... but this one fails as "buffer" has a different length than the given expected value.
   * assertThat(buffer).hasLength(testArray.length - 1);
   * </code></pre>
   *
   * @param expected integer value representing the expected length of the buffer.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} has not been flipped.
   * @throws AssertionError if the actual {@code Buffer} length is different than the given expected value.
   */
  public SELF hasLength(int expected) {
    isNotNull();
    isFlipped();
    if (actual.limit() != expected) throwAssertionError(shouldHaveLength(expected, actual.limit(), actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Buffer} has a remaining length equal to the given expected value.
   *
   * The remaining length is the amount of space the buffer still has left, or in other words the space in the buffer
   * that is not filled. It is defined as the capacity of the buffer minus the limit of the buffer.
   *
   * Example:
   *
   * <pre><code class='java'>
   * byte[] testArray = "test".getBytes();
   * ByteBuffer buffer = ByteBuffer.allocate(10);
   * buffer.put(testArray);
   * buffer.flip();
   *
   * // this assertion succeeds ...
   * assertThat(buffer).hasRemainingLength(10 - testArray.length);
   *
   * // ... but this one fails as "buffer" has a different remaining length than the given expected value.
   * assertThat(buffer).hasRemainingLength(10);
   * </code></pre>
   *
   * @param expected integer value representing the expected length of the buffer.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code Buffer} is {@code null}.
   * @throws AssertionError if the actual {@code Buffer} has not been flipped.
   * @throws AssertionError if the actual {@code Buffer}s remaining length is different than the given expected value.
   */
  public SELF hasRemainingLength(int expected) {
    isNotNull();
    isFlipped();

    int remaining_length = actual.capacity() - actual.limit();
    if (remaining_length != expected) throwAssertionError(shouldHaveRemainingLength(expected, remaining_length, actual));
    return myself;
  }
}
