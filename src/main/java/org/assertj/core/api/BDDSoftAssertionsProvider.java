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

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.util.CheckReturnValue;

@CheckReturnValue
public interface BDDSoftAssertionsProvider extends Java6BDDSoftAssertionsProvider {

  /**
   * Creates a new, proxied instance of a {@link PathAssert}
   *
   * @param actual the path
   * @return the created assertion object
   */
  default PathAssert then(Path actual) {
    return proxy(PathAssert.class, Path.class, actual);
  }

  /**
   * Creates a new instance of {@link PathAssert}
   * <p>
   * Use this over {@link #then(Path)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param actual the path to test
   * @return the created assertion object
   * @since 3.23.0
   */
  default AbstractPathAssert<?> thenPath(Path actual) {
    return then(actual);
  }

  /**
   * Create assertion for {@link Optional}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link Optional}.
   *
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <VALUE> OptionalAssert<VALUE> then(Optional<VALUE> actual) {
    return proxy(OptionalAssert.class, Optional.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalDoubleAssert then(OptionalDouble actual) {
    return proxy(OptionalDoubleAssert.class, OptionalDouble.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalIntAssert then(OptionalInt actual) {
    return proxy(OptionalIntAssert.class, OptionalInt.class, actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalLong}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalLongAssert then(OptionalLong actual) {
    return proxy(OptionalLongAssert.class, OptionalLong.class, actual);
  }

  /**
   * Create assertion for {@link java.util.regex.Matcher}.
   *
   * @param actual the actual matcher
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  default MatcherAssert then(Matcher actual) {
    return proxy(MatcherAssert.class, Matcher.class, actual);
  }

  /**
  * Creates a new instance of <code>{@link LocalDateAssert}</code>.
  *
  * @param actual the actual value.
  * @return the created assertion object.
  */
  default LocalDateAssert then(LocalDate actual) {
    return proxy(LocalDateAssert.class, LocalDate.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LocalDateTimeAssert then(LocalDateTime actual) {
    return proxy(LocalDateTimeAssert.class, LocalDateTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ZonedDateTimeAssert then(ZonedDateTime actual) {
    return proxy(ZonedDateTimeAssert.class, ZonedDateTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LocalTimeAssert then(LocalTime actual) {
    return proxy(LocalTimeAssert.class, LocalTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default OffsetTimeAssert then(OffsetTime actual) {
    return proxy(OffsetTimeAssert.class, OffsetTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default OffsetDateTimeAssert then(OffsetDateTime actual) {
    return proxy(OffsetDateTimeAssert.class, OffsetDateTime.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InstantAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.7.0
   */
  default InstantAssert then(Instant actual) {
    return proxy(InstantAssert.class, Instant.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DurationAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.15.0
   */
  default DurationAssert then(Duration actual) {
    return proxy(DurationAssert.class, Duration.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link PeriodAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default PeriodAssert then(Period actual) {
    return proxy(PeriodAssert.class, Period.class, actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletableFuture}.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletableFuture}.
   *
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <RESULT> CompletableFutureAssert<RESULT> then(CompletableFuture<RESULT> actual) {
    return proxy(CompletableFutureAssert.class, CompletableFuture.class, actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletionStage} by converting it to a {@link CompletableFuture} and returning a {@link CompletableFutureAssert}.
   * <p>
   * If the given {@link java.util.concurrent.CompletionStage} is null, the {@link CompletableFuture} in the returned {@link CompletableFutureAssert} will also be null.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletionStage}.
   *
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <RESULT> CompletableFutureAssert<RESULT> then(CompletionStage<RESULT> actual) {
    return proxy(CompletableFutureAssert.class, CompletionStage.class, actual);
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
  default <T> PredicateAssert<T> then(Predicate<T> actual) {
    return proxy(PredicateAssert.class, Predicate.class, actual);
  }

  /**
   * Create assertion for {@link Predicate}.
   * <p>
   * Use this over {@link #then(Predicate)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <T> PredicateAssert<T> thenPredicate(Predicate<T> actual) {
    return then(actual);
  }

  /**
   * Create assertion for {@link IntPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  default IntPredicateAssert then(IntPredicate actual) {
    return proxy(IntPredicateAssert.class, IntPredicate.class, actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  default DoublePredicateAssert then(DoublePredicate actual) {
    return proxy(DoublePredicateAssert.class, DoublePredicate.class, actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  default LongPredicateAssert then(LongPredicate actual) {
    return proxy(LongPredicateAssert.class, LongPredicate.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link Stream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link Stream}.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Stream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> then(Stream<? extends ELEMENT> actual) {
    return proxy(ListAssert.class, Stream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * Use this over {@link #then(Stream)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link Stream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link Stream}.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  @SuppressWarnings("unchecked")
  default <ELEMENT> ListAssert<ELEMENT> thenStream(Stream<? extends ELEMENT> actual) {
    return proxy(ListAssert.class, Stream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link DoubleStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link DoubleStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link DoubleStream}.
   *
   * @param actual the actual {@link DoubleStream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default AbstractListAssert<?, List<? extends Double>, Double, ObjectAssert<Double>> then(DoubleStream actual) {
    return proxy(ListAssert.class, DoubleStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link LongStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link LongStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link LongStream}.
   *
   * @param actual the actual {@link LongStream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default AbstractListAssert<?, List<? extends Long>, Long, ObjectAssert<Long>> then(LongStream actual) {
    return proxy(ListAssert.class, LongStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link IntStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link IntStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link IntStream}.
   *
   * @param actual the actual {@link IntStream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default AbstractListAssert<?, List<? extends Integer>, Integer, ObjectAssert<Integer>> then(IntStream actual) {
    return proxy(ListAssert.class, IntStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link SpliteratorAssert}</code> from the given {@link Spliterator}.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Spliterator} value.
   * @return the created assertion object.
   * @since 3.14.0
   */
  @SuppressWarnings("unchecked")
  default <ELEMENT> SpliteratorAssert<ELEMENT> then(Spliterator<ELEMENT> actual) {
    return proxy(SpliteratorAssert.class, Spliterator.class, actual);
  }

  /**
   * Create assertion for {@link LongAdder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.16.0
   */
  default LongAdderAssert then(LongAdder actual) {
    return proxy(LongAdderAssert.class, LongAdder.class, actual);
  }

}
