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
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.function.Predicate;
import org.assertj.core.util.CheckReturnValue;

/**
 * Base class for all implementations of assertions for
 * {@link StackTraceElement[] stack trace arrays} of
 * {@link StackTraceElement stack trace elements}.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @author Ashley Scopes
 */
public abstract class AbstractStackTraceAssert<SELF extends AbstractStackTraceAssert<SELF, ELEMENT_ASSERT>,
                                               ELEMENT_ASSERT extends AbstractStackTraceElementAssert<ELEMENT_ASSERT>>
  extends AbstractObjectArrayAssert<SELF, StackTraceElement> {

  private static final int TOP = 0;
  private static final int BOTTOM = -1;
  private static final StackTraceElement[] EMPTY = {};

  /**
   * Initialize this assertion.
   *
   * @param actual   the stack trace array to perform assertions upon.
   * @param selfType the type of the implementation of this class.
   */
  protected AbstractStackTraceAssert(StackTraceElement[] actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Fetch the innermost/first frame in the stack trace and return assertions to perform on that
   * frame.
   *
   * <p>This is equivalent to calling <code>{@link #element}(0)</code>.
   *
   * @return the stack trace element assertions for the given frame.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace is empty.
   */
  @CheckReturnValue
  public AbstractStackTraceElementAssert<?> first() {
    return element(TOP);
  }

  /**
   * Fetch the outermost/last frame in the stack trace and return assertions to perform on that
   * frame.
   *
   * <p>This is equivalent to calling <pre>{@link #element}(-1)</pre>.
   *
   * @return the stack trace element assertions for the given frame.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace is empty.
   */
  @CheckReturnValue
  public AbstractStackTraceElementAssert<?> last() {
    return element(BOTTOM);
  }

  /**
   * Fetch the frame at the given offset from the top (most recent/innermost frame) of the stack.
   *
   * <p>Negative offsets will be taken from the bottom of the stack trace rather than the top. For
   * example, an offset of {@code -3} would return the third outermost stack trace element.
   *
   * @param offset the offset to fetch the frame for.
   * @return the stack trace element assertions for the given frame.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace is empty.
   * @throws AssertionError if the {@code offset} has a magnitude larger than the {@code actual}
   *                        stack trace being asserted upon.
   */
  @CheckReturnValue
  public AbstractStackTraceElementAssert<?> element(int offset) {
    objects.assertNotNull(info, actual);
    arrays.assertNotEmpty(info, actual);

    int minSize;
    int actualOffset;

    if (offset < 0) {
      // 2s compliment to convert to the min size as a positive int (-4 --> +5, etc)
      minSize = ~offset;
      actualOffset = actual.length + offset;
    } else {
      minSize = offset;
      actualOffset = offset;
    }

    arrays.assertHasSizeGreaterThan(info, actual, minSize);
    return toAssertForElement(actual[actualOffset], actualOffset);
  }

  /**
   * Drop the first {@code count} elements in the stack trace and return a new set of assertions on
   * the remaining frames in the array.
   *
   * <p>If {@code count} is greater than the number of frames, the array will be empty.
   *
   * @param count the number of frames to drop from the start (top, innermost) of the stack trace.
   * @return the assertions to perform on the new stack trace.
   * @throws IllegalArgumentException if {@code count} is less than 0.
   * @throws AssertionError           if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> dropFirst(int count) {
    objects.assertNotNull(info, actual);

    if (count < 0) {
      throw countIsLessThanZero(count);
    }

    StackTraceElement[] trimmedActual = count >= actual.length
      ? EMPTY
      : Arrays.copyOfRange(actual, count, actual.length);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Drop the first elements in the stack while the given predicate is true and while there are
   * still remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> dropFirstWhile(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    objects.assertNotNull(info, actual);

    int index = 0;
    while (index < actual.length && predicate.test(actual[index])) {
      ++index;
    }

    StackTraceElement[] trimmedActual = index >= actual.length
      ? EMPTY
      : Arrays.copyOfRange(actual, index, actual.length);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Drop the first elements in the stack until the given predicate is true and while there are
   * still remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> dropFirstUntil(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    return dropFirstWhile(predicate.negate());
  }

  /**
   * Drop the last {@code count} elements in the stack trace and return a new set of assertions on
   * the remaining frames in the array.
   *
   * <p>If {@code count} is greater than the number of frames, the array will be empty.
   *
   * @param count the number of frames to drop from the end (bottom, outermost) of the stack trace.
   * @return the assertions to perform on the new stack trace.
   * @throws IllegalArgumentException if {@code count} is less than 0.
   * @throws AssertionError           if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> dropLast(int count) {
    objects.assertNotNull(info, actual);

    if (count < 0) {
      throw countIsLessThanZero(count);
    }

    StackTraceElement[] trimmedActual = count >= actual.length
      ? EMPTY
      : Arrays.copyOfRange(actual, 0, actual.length - count);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Drop the last elements in the stack while the given predicate is true and while there are still
   * remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> dropLastWhile(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    objects.assertNotNull(info, actual);

    int index = actual.length - 1;
    while (index >= 0 && predicate.test(actual[index])) {
      --index;
    }

    StackTraceElement[] trimmedActual = index < 0
      ? EMPTY
      : Arrays.copyOfRange(actual, 0, index);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Drop the last elements in the stack until the given predicate is true and while there are still
   * remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> dropLastUntil(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    return dropLastWhile(predicate.negate());
  }

  /**
   * Take the first {@code count} elements in the stack trace and return a new set of assertions on
   * those frames only.
   *
   * <p>If {@code count} is greater than the number of frames, then the original array will be
   * asserted upon.
   *
   * @param count the number of frames to take from the start (top, innermost) of the stack trace.
   * @return the assertions to perform on the new stack trace.
   * @throws IllegalArgumentException if {@code count} is less than 0.
   * @throws AssertionError           if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> takeFirst(int count) {
    objects.assertNotNull(info, actual);

    if (count < 0) {
      throw countIsLessThanZero(count);
    }

    StackTraceElement[] trimmedActual = count >= actual.length
      ? actual
      : Arrays.copyOfRange(actual, 0, count);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Take the first elements in the stack while the given predicate is true and while there are
   * still remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> takeFirstWhile(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    objects.assertNotNull(info, actual);

    int index = 0;
    while (index < actual.length && predicate.test(actual[index])) {
      ++index;
    }

    StackTraceElement[] trimmedActual = index >= actual.length
      ? actual
      : Arrays.copyOfRange(actual, 0, index);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Take the first elements in the stack until the given predicate is true and while there are
   * still remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> takeFirstUntil(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    return takeFirstWhile(predicate.negate());
  }

  /**
   * Take the last {@code count} elements in the stack trace and return a new set of assertions on
   * those frames only.
   *
   * <p>If {@code count} is greater than the number of frames, then the original array will be
   * asserted upon.
   *
   * @param count the number of frames to take from the end (bottom, outermost) of the stack trace.
   * @return the assertions to perform on the new stack trace.
   * @throws IllegalArgumentException if {@code count} is less than 0.
   * @throws AssertionError           if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> takeLast(int count) {
    objects.assertNotNull(info, actual);

    if (count < 0) {
      throw countIsLessThanZero(count);
    }

    StackTraceElement[] trimmedActual = count >= actual.length
      ? actual
      : Arrays.copyOfRange(actual, actual.length - count, actual.length);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Take the last elements in the stack while the given predicate is true and while there are still
   * remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> takeLastWhile(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    objects.assertNotNull(info, actual);

    int index = actual.length - 1;
    while (index >= 0 && predicate.test(actual[index])) {
      --index;
    }

    StackTraceElement[] trimmedActual = index < 0
      ? actual
      : Arrays.copyOfRange(actual, index + 1, actual.length);

    return newObjectArrayAssert(trimmedActual);
  }

  /**
   * Take the last elements in the stack until the given predicate is true and while there are still
   * remaining frames to iterate over.
   *
   * @param predicate the predicate to perform on each stack trace frame.
   * @return the assertions to perform on the remaining frames in the stack trace.
   * @throws AssertionError if the {@code actual} stack trace is {@code null}.
   */
  @CheckReturnValue
  public AbstractStackTraceAssert<?, ?> takeLastUntil(
    Predicate<? super StackTraceElement> predicate
  ) {
    nonNullPredicate(predicate);
    return takeLastWhile(predicate.negate());
  }

  /**
   * Create an assertion for a given {@link StackTraceElement[]} array.
   * @param array the array to create the assertion for.
   * @return the new assertion.
   */
  @CheckReturnValue
  @Override
  protected abstract SELF newObjectArrayAssert(StackTraceElement[] array);

  /**
   * Create an assertion for a {@link StackTraceElement} and return it.
   *
   * @param value the stack trace element to create an assertion for.
   * @param description the description to attach to the assertion.
   * @return the created assertion.
   */
  @CheckReturnValue
  @Override
  protected abstract ELEMENT_ASSERT toAssert(StackTraceElement value, String description);

  @CheckReturnValue
  private IllegalArgumentException countIsLessThanZero(int value) {
    return new IllegalArgumentException("Integer 'count' with value " + value + " is less than 0");
  }

  private void nonNullPredicate(Predicate<? super StackTraceElement> predicate) {
    requireNonNull(predicate, "predicate must not be null");
  }

  private ELEMENT_ASSERT toAssertForElement(StackTraceElement stackTraceElement, int position) {
    return toAssert(
      stackTraceElement,
      navigationDescription("check element for frame #" + position)
    );
  }
}
