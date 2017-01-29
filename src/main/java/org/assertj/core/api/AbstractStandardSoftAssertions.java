/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.util.CheckReturnValue;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractStandardSoftAssertions extends Java6AbstractStandardSoftAssertions {

  /**
   * Creates a new, proxied instance of a {@link PathAssert}
   *
   * @param actual the path
   * @return the created assertion object
   */
  @CheckReturnValue
  public PathAssert assertThat(Path actual) {
    return proxy(PathAssert.class, Path.class, actual);
  }

  /**
   * Create assertion for {@link java.util.Optional}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link java.util.Optional}.
   *
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> OptionalAssert<T> assertThat(Optional<T> actual) {
    return proxy(OptionalAssert.class, Optional.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public OptionalDoubleAssert assertThat(OptionalDouble actual) {
      return proxy(OptionalDoubleAssert.class, OptionalDouble.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalLong}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public OptionalLongAssert assertThat(OptionalLong actual) {
      return proxy(OptionalLongAssert.class, OptionalLong.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  public OptionalIntAssert assertThat(OptionalInt actual) {
      return proxy(OptionalIntAssert.class, OptionalInt.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public LocalDateAssert assertThat(LocalDate actual) {
    return proxy(LocalDateAssert.class, LocalDate.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public LocalDateTimeAssert assertThat(LocalDateTime actual) {
    return proxy(LocalDateTimeAssert.class, LocalDateTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public ZonedDateTimeAssert assertThat(ZonedDateTime actual) {
    return proxy(ZonedDateTimeAssert.class, ZonedDateTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public LocalTimeAssert assertThat(LocalTime actual) {
    return proxy(LocalTimeAssert.class, LocalTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public OffsetTimeAssert assertThat(OffsetTime actual) {
      return proxy(OffsetTimeAssert.class, OffsetTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public OffsetDateTimeAssert assertThat(OffsetDateTime actual) {
    return proxy(OffsetDateTimeAssert.class, OffsetDateTime.class, actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletableFuture}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link java.util.concurrent.CompletableFuture}.
   *
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> CompletableFutureAssert<T> assertThat(CompletableFuture<T> actual) {
    return proxy(CompletableFutureAssert.class, CompletableFuture.class, actual);
  }

  /**
   * Create assertion for {@link Predicate}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   *
   * @return the created assertion object.
   *
   * @since 3.5.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> SoftAssertionPredicateAssert<T> assertThat(Predicate<T> actual) {
    return proxy(SoftAssertionPredicateAssert.class, Predicate.class, actual);
  }

  /**
   * Create assertion for {@link IntPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  @CheckReturnValue
  public IntPredicateAssert assertThat(IntPredicate actual) {
    return proxy(IntPredicateAssert.class, IntPredicate.class, actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  @CheckReturnValue
  public DoublePredicateAssert assertThat(DoublePredicate actual) {
    return proxy(DoublePredicateAssert.class, DoublePredicate.class, actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  @CheckReturnValue
  public LongPredicateAssert assertThat(LongPredicate actual) {
    return proxy(LongPredicateAssert.class, LongPredicate.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link Stream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link Stream}.
   *
   * @param actual the actual {@link Stream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <ELEMENT> ListAssert<ELEMENT> assertThat(Stream<? extends ELEMENT> actual) {
    return proxy(ListAssert.class, Stream.class, actual);
  }

}
