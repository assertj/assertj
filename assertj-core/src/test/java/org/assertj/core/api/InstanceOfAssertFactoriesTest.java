/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY_2D;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_BOOLEAN;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_INTEGER_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_INTEGER_FIELD_UPDATER;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_LONG;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_LONG_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_LONG_FIELD_UPDATER;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_MARKABLE_REFERENCE;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_REFERENCE;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_REFERENCE_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_REFERENCE_FIELD_UPDATER;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_STAMPED_REFERENCE;
import static org.assertj.core.api.InstanceOfAssertFactories.BIG_DECIMAL;
import static org.assertj.core.api.InstanceOfAssertFactories.BIG_INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.BYTE;
import static org.assertj.core.api.InstanceOfAssertFactories.BYTE_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.BYTE_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.CHARACTER;
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_SEQUENCE;
import static org.assertj.core.api.InstanceOfAssertFactories.CLASS;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;
import static org.assertj.core.api.InstanceOfAssertFactories.COMPLETABLE_FUTURE;
import static org.assertj.core.api.InstanceOfAssertFactories.COMPLETION_STAGE;
import static org.assertj.core.api.InstanceOfAssertFactories.DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE_PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE_STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.DURATION;
import static org.assertj.core.api.InstanceOfAssertFactories.FILE;
import static org.assertj.core.api.InstanceOfAssertFactories.FLOAT;
import static org.assertj.core.api.InstanceOfAssertFactories.FLOAT_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.FLOAT_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.FUTURE;
import static org.assertj.core.api.InstanceOfAssertFactories.INPUT_STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.INSTANT;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.ITERABLE;
import static org.assertj.core.api.InstanceOfAssertFactories.ITERATOR;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG_ADDER;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG_PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG_STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static org.assertj.core.api.InstanceOfAssertFactories.MATCHER;
import static org.assertj.core.api.InstanceOfAssertFactories.OFFSET_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.OFFSET_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL_DOUBLE;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL_INT;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL_LONG;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;
import static org.assertj.core.api.InstanceOfAssertFactories.PERIOD;
import static org.assertj.core.api.InstanceOfAssertFactories.PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.SET;
import static org.assertj.core.api.InstanceOfAssertFactories.SHORT;
import static org.assertj.core.api.InstanceOfAssertFactories.SHORT_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.SHORT_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.SPLITERATOR;
import static org.assertj.core.api.InstanceOfAssertFactories.STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING_BUFFER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING_BUILDER;
import static org.assertj.core.api.InstanceOfAssertFactories.TEMPORAL;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.api.InstanceOfAssertFactories.URI_TYPE;
import static org.assertj.core.api.InstanceOfAssertFactories.URL_TYPE;
import static org.assertj.core.api.InstanceOfAssertFactories.YEAR_MONTH;
import static org.assertj.core.api.InstanceOfAssertFactories.ZONED_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.array;
import static org.assertj.core.api.InstanceOfAssertFactories.array2D;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicIntegerFieldUpdater;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicLongFieldUpdater;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicMarkableReference;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicReference;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicReferenceArray;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicReferenceFieldUpdater;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicStampedReference;
import static org.assertj.core.api.InstanceOfAssertFactories.collection;
import static org.assertj.core.api.InstanceOfAssertFactories.comparable;
import static org.assertj.core.api.InstanceOfAssertFactories.completableFuture;
import static org.assertj.core.api.InstanceOfAssertFactories.completionStage;
import static org.assertj.core.api.InstanceOfAssertFactories.future;
import static org.assertj.core.api.InstanceOfAssertFactories.iterable;
import static org.assertj.core.api.InstanceOfAssertFactories.iterator;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;
import static org.assertj.core.api.InstanceOfAssertFactories.set;
import static org.assertj.core.api.InstanceOfAssertFactories.spliterator;
import static org.assertj.core.api.InstanceOfAssertFactories.stream;
import static org.assertj.core.api.InstanceOfAssertFactories.throwable;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.assertj.core.api.AssertFactory.ValueProvider;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author Stefano Cordio
 */
class InstanceOfAssertFactoriesTest {

  private static final DefaultConversionService SPRING_CONVERSION_SERVICE = new DefaultConversionService();

  @Nested
  class Predicate_Factory {

    private final Object actual = (Predicate<Object>) Objects::isNull;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = PREDICATE.getRawClass();
      // THEN
      then(result).isEqualTo(Predicate.class);
    }

    @Test
    void createAssert() {
      // WHEN
      PredicateAssert<Object> result = PREDICATE.createAssert(actual);
      // THEN
      result.accepts((Object) null);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      PredicateAssert<Object> result = PREDICATE.createAssert(valueProvider);
      // THEN
      result.accepts((Object) null);
      verify(valueProvider).apply(parameterizedType(Predicate.class, Object.class));
    }

  }

  @Nested
  class Predicate_Typed_Factory {

    private final Object actual = (Predicate<String>) Strings::isNullOrEmpty;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = PREDICATE.getRawClass();
      // THEN
      then(result).isEqualTo(Predicate.class);
    }

    @Test
    void createAssert() {
      // WHEN
      PredicateAssert<String> result = predicate(String.class).createAssert(actual);
      // THEN
      result.accepts("");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      PredicateAssert<String> result = predicate(String.class).createAssert(valueProvider);
      // THEN
      result.accepts("");
      verify(valueProvider).apply(parameterizedType(Predicate.class, String.class));
    }

  }

  @Nested
  class IntPredicate_Factory {

    private final Object actual = (IntPredicate) i -> i == 0;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = INT_PREDICATE.getRawClass();
      // THEN
      then(result).isEqualTo(IntPredicate.class);
    }

    @Test
    void createAssert() {
      // WHEN
      IntPredicateAssert result = INT_PREDICATE.createAssert(actual);
      // THEN
      result.accepts(0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      IntPredicateAssert result = INT_PREDICATE.createAssert(valueProvider);
      // THEN
      result.accepts(0);
      verify(valueProvider).apply(IntPredicate.class);
    }

  }

  @Nested
  class LongPredicate_Factory {

    private final Object actual = (LongPredicate) l -> l == 0L;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LONG_PREDICATE.getRawClass();
      // THEN
      then(result).isEqualTo(LongPredicate.class);
    }

    @Test
    void createAssert() {
      // WHEN
      LongPredicateAssert result = LONG_PREDICATE.createAssert(actual);
      // THEN
      result.accepts(0L);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      LongPredicateAssert result = LONG_PREDICATE.createAssert(valueProvider);
      // THEN
      result.accepts(0L);
      verify(valueProvider).apply(LongPredicate.class);
    }

  }

  @Nested
  class DoublePredicate_Factory {

    private final Object actual = (DoublePredicate) d -> d == 0.0;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = DOUBLE_PREDICATE.getRawClass();
      // THEN
      then(result).isEqualTo(DoublePredicate.class);
    }

    @Test
    void createAssert() {
      // WHEN
      DoublePredicateAssert result = DOUBLE_PREDICATE.createAssert(actual);
      // THEN
      result.accepts(0.0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      DoublePredicateAssert result = DOUBLE_PREDICATE.createAssert(valueProvider);
      // THEN
      result.accepts(0.0);
      verify(valueProvider).apply(DoublePredicate.class);
    }

  }

  @Nested
  class CompletableFuture_Factory {

    private final Object actual = completedFuture("done");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = COMPLETABLE_FUTURE.getRawClass();
      // THEN
      then(result).isEqualTo(CompletableFuture.class);
    }

    @Test
    void createAssert() {
      // WHEN
      CompletableFutureAssert<Object> result = COMPLETABLE_FUTURE.createAssert(actual);
      // THEN
      result.isDone();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      CompletableFutureAssert<Object> result = COMPLETABLE_FUTURE.createAssert(valueProvider);
      // THEN
      result.isDone();
      verify(valueProvider).apply(parameterizedType(CompletableFuture.class, Object.class));
    }

  }

  @Nested
  class CompletableFuture_Typed_Factory {

    private final Object actual = completedFuture("done");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = completableFuture(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(CompletableFuture.class);
    }

    @Test
    void createAssert() {
      // WHEN
      CompletableFutureAssert<String> result = completableFuture(String.class).createAssert(actual);
      // THEN
      result.isDone();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      CompletableFutureAssert<String> result = completableFuture(String.class).createAssert(valueProvider);
      // THEN
      result.isDone();
      verify(valueProvider).apply(parameterizedType(CompletableFuture.class, String.class));
    }

  }

  @Nested
  class CompletionStage_Factory {

    private final Object actual = completedFuture("done");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = COMPLETION_STAGE.getRawClass();
      // THEN
      then(result).isEqualTo(CompletionStage.class);
    }

    @Test
    void createAssert() {
      // WHEN
      CompletableFutureAssert<Object> result = COMPLETION_STAGE.createAssert(actual);
      // THEN
      result.isDone();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      CompletableFutureAssert<Object> result = COMPLETION_STAGE.createAssert(valueProvider);
      // THEN
      result.isDone();
      verify(valueProvider).apply(parameterizedType(CompletionStage.class, Object.class));
    }

  }

  @Nested
  class CompletionStage_Typed_Factory {

    private final Object actual = completedFuture("done");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = COMPLETION_STAGE.getRawClass();
      // THEN
      then(result).isEqualTo(CompletionStage.class);
    }

    @Test
    void createAssert() {
      // WHEN
      CompletableFutureAssert<String> result = completionStage(String.class).createAssert(actual);
      // THEN
      result.isDone();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      CompletableFutureAssert<String> result = completionStage(String.class).createAssert(valueProvider);
      // THEN
      result.isDone();
      verify(valueProvider).apply(parameterizedType(CompletionStage.class, String.class));
    }

  }

  @Nested
  class Optional_Factory {

    private final Object actual = Optional.of("something");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = OPTIONAL.getRawClass();
      // THEN
      then(result).isEqualTo(Optional.class);
    }

    @Test
    void createAssert() {
      // WHEN
      OptionalAssert<Object> result = OPTIONAL.createAssert(actual);
      // THEN
      result.isPresent();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      OptionalAssert<Object> result = OPTIONAL.createAssert(valueProvider);
      // THEN
      result.isPresent();
      verify(valueProvider).apply(parameterizedType(Optional.class, Object.class));
    }

  }

  @Nested
  class Optional_Typed_Factory {

    private final Object actual = Optional.of("something");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = optional(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(Optional.class);
    }

    @Test
    void createAssert() {
      // WHEN
      OptionalAssert<String> result = optional(String.class).createAssert(actual);
      // THEN
      result.isPresent();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      OptionalAssert<String> result = optional(String.class).createAssert(valueProvider);
      // THEN
      result.isPresent();
      verify(valueProvider).apply(parameterizedType(Optional.class, String.class));
    }

  }

  @Nested
  class OptionalDouble_Factory {

    private final Object actual = OptionalDouble.of(0.0);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = OPTIONAL_DOUBLE.getRawClass();
      // THEN
      then(result).isEqualTo(OptionalDouble.class);
    }

    @Test
    void createAssert() {
      // WHEN
      OptionalDoubleAssert result = OPTIONAL_DOUBLE.createAssert(actual);
      // THEN
      result.isPresent();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      OptionalDoubleAssert result = OPTIONAL_DOUBLE.createAssert(valueProvider);
      // THEN
      result.isPresent();
      verify(valueProvider).apply(OptionalDouble.class);
    }

  }

  @Nested
  class OptionalInt_Factory {

    private final Object actual = OptionalInt.of(0);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = OPTIONAL_INT.getRawClass();
      // THEN
      then(result).isEqualTo(OptionalInt.class);
    }

    @Test
    void createAssert() {
      // WHEN
      OptionalIntAssert result = OPTIONAL_INT.createAssert(actual);
      // THEN
      result.isPresent();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      OptionalIntAssert result = OPTIONAL_INT.createAssert(valueProvider);
      // THEN
      result.isPresent();
      verify(valueProvider).apply(OptionalInt.class);
    }

  }

  @Nested
  class OptionalLong_Factory {

    private final Object actual = OptionalLong.of(0L);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = OPTIONAL_LONG.getRawClass();
      // THEN
      then(result).isEqualTo(OptionalLong.class);
    }

    @Test
    void createAssert() {
      // WHEN
      OptionalLongAssert result = OPTIONAL_LONG.createAssert(actual);
      // THEN
      result.isPresent();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      OptionalLongAssert result = OPTIONAL_LONG.createAssert(valueProvider);
      // THEN
      result.isPresent();
      verify(valueProvider).apply(OptionalLong.class);
    }

  }

  @Nested
  class Matcher_Factory {

    private final Object actual = Pattern.compile("a*").matcher("aaa");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = MATCHER.getRawClass();
      // THEN
      then(result).isEqualTo(Matcher.class);
    }

    @Test
    void createAssert() {
      // WHEN
      MatcherAssert result = MATCHER.createAssert(actual);
      // THEN
      result.matches();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      MatcherAssert result = MATCHER.createAssert(valueProvider);
      // THEN
      result.matches();
      verify(valueProvider).apply(Matcher.class);
    }

  }

  @Nested
  class BigDecimal_Factory {

    private final Object actual = BigDecimal.valueOf(0.0);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BIG_DECIMAL.getRawClass();
      // THEN
      then(result).isEqualTo(BigDecimal.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractBigDecimalAssert<?> result = BIG_DECIMAL.createAssert(actual);
      // THEN
      result.isEqualTo("0.0");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractBigDecimalAssert<?> result = BIG_DECIMAL.createAssert(valueProvider);
      // THEN
      result.isEqualTo("0.0");
      verify(valueProvider).apply(BigDecimal.class);
    }

  }

  @Nested
  class BigInteger_Factory {

    private final Object actual = BigInteger.valueOf(0L);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BIG_INTEGER.getRawClass();
      // THEN
      then(result).isEqualTo(BigInteger.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractBigIntegerAssert<?> result = BIG_INTEGER.createAssert(actual);
      // THEN
      result.isEqualTo(0L);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractBigIntegerAssert<?> result = BIG_INTEGER.createAssert(valueProvider);
      // THEN
      result.isEqualTo(0L);
      verify(valueProvider).apply(BigInteger.class);
    }

  }

  @Nested
  class URI_Factory {

    private final Object actual = URI.create("http://localhost");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = URI_TYPE.getRawClass();
      // THEN
      then(result).isEqualTo(URI.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractUriAssert<?> result = URI_TYPE.createAssert(actual);
      // THEN
      result.hasHost("localhost");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractUriAssert<?> result = URI_TYPE.createAssert(valueProvider);
      // THEN
      result.hasHost("localhost");
      verify(valueProvider).apply(URI.class);
    }

  }

  @Nested
  class URL_Factory {

    private final Object actual = new URL("http://localhost");

    URL_Factory() throws MalformedURLException {}

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = URL_TYPE.getRawClass();
      // THEN
      then(result).isEqualTo(URL.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractUrlAssert<?> result = URL_TYPE.createAssert(actual);
      // THEN
      result.hasHost("localhost");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractUrlAssert<?> result = URL_TYPE.createAssert(valueProvider);
      // THEN
      result.hasHost("localhost");
      verify(valueProvider).apply(URL.class);
    }

  }

  @Nested
  class Boolean_Factory {

    private final Object actual = true;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BOOLEAN.getRawClass();
      // THEN
      then(result).isEqualTo(Boolean.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractBooleanAssert<?> result = BOOLEAN.createAssert(actual);
      // THEN
      result.isTrue();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractBooleanAssert<?> result = BOOLEAN.createAssert(valueProvider);
      // THEN
      result.isTrue();
      verify(valueProvider).apply(Boolean.class);
    }

  }

  @Nested
  class Boolean_Array_Factory {

    private final Object actual = new boolean[] { true, false };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BOOLEAN_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(boolean[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractBooleanArrayAssert<?> result = BOOLEAN_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(true, false);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractBooleanArrayAssert<?> result = BOOLEAN_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(true, false);
      verify(valueProvider).apply(boolean[].class);
    }

  }

  @Nested
  class Boolean_2D_Array_Factory {

    private final Object actual = new boolean[][] { { true, false }, { false, true } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BOOLEAN_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(boolean[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Boolean2DArrayAssert result = BOOLEAN_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Boolean2DArrayAssert result = BOOLEAN_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(boolean[][].class);
    }

  }

  @Nested
  class Byte_Factory {

    private final Object actual = (byte) 0;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BYTE.getRawClass();
      // THEN
      then(result).isEqualTo(Byte.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractByteAssert<?> result = BYTE.createAssert(actual);
      // THEN
      result.isEqualTo((byte) 0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractByteAssert<?> result = BYTE.createAssert(valueProvider);
      // THEN
      result.isEqualTo((byte) 0);
      verify(valueProvider).apply(Byte.class);
    }

  }

  @Nested
  class Byte_Array_Factory {

    private final Object actual = new byte[] { 0, 1 };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BYTE_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(byte[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractByteArrayAssert<?> result = BYTE_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0, 1);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractByteArrayAssert<?> result = BYTE_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0, 1);
      verify(valueProvider).apply(byte[].class);
    }

  }

  @Nested
  class Byte_2D_Array_Factory {

    private final Object actual = new byte[][] { { 0, 1 }, { 2, 3 } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = BYTE_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(byte[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Byte2DArrayAssert result = BYTE_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Byte2DArrayAssert result = BYTE_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(byte[][].class);
    }

  }

  @Nested
  class Character_Factory {

    private final Object actual = 'a';

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = CHARACTER.getRawClass();
      // THEN
      then(result).isEqualTo(Character.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCharacterAssert<?> result = CHARACTER.createAssert(actual);
      // THEN
      result.isLowerCase();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractCharacterAssert<?> result = CHARACTER.createAssert(valueProvider);
      // THEN
      result.isLowerCase();
      verify(valueProvider).apply(Character.class);
    }

  }

  @Nested
  class Char_Array_Factory {

    private final Object actual = new char[] { 'a', 'b' };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = CHAR_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(char[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCharArrayAssert<?> result = CHAR_ARRAY.createAssert(actual);
      // THEN
      result.doesNotHaveDuplicates();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractCharArrayAssert<?> result = CHAR_ARRAY.createAssert(valueProvider);
      // THEN
      result.doesNotHaveDuplicates();
      verify(valueProvider).apply(char[].class);
    }

  }

  @Nested
  class Char_2D_Array_Factory {

    private final Object actual = new char[][] { { 'a', 'b' }, { 'c', 'd' } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = CHAR_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(char[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Char2DArrayAssert result = CHAR_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Char2DArrayAssert result = CHAR_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(char[][].class);
    }

  }

  @Nested
  class Class_Factory {

    private final Object actual = Function.class;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = CLASS.getRawClass();
      // THEN
      then(result).isEqualTo(Class.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ClassAssert result = CLASS.createAssert(actual);
      // THEN
      result.hasAnnotations(FunctionalInterface.class);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      ClassAssert result = CLASS.createAssert(valueProvider);
      // THEN
      result.hasAnnotations(FunctionalInterface.class);
      verify(valueProvider).apply(Class.class);
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class Double_Factory {

    private final Object actual = 0.0;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = DOUBLE.getRawClass();
      // THEN
      then(result).isEqualTo(Double.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractDoubleAssert<?> result = DOUBLE.createAssert(actual);
      // THEN
      result.isZero();
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      AbstractDoubleAssert<?> result = DOUBLE.createAssert(valueProvider);
      // THEN
      result.isZero();
      verify(valueProvider).apply(Double.class);
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert("0.0", type));
    }

  }

  @Nested
  class Double_Array_Factory {

    private final Object actual = new double[] { 0.0, 1.0 };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = DOUBLE_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(double[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractDoubleArrayAssert<?> result = DOUBLE_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0.0, 1.0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractDoubleArrayAssert<?> result = DOUBLE_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0.0, 1.0);
      verify(valueProvider).apply(double[].class);
    }

  }

  @Nested
  class Double_2D_Array_Factory {

    private final Object actual = new double[][] { { 0.0, 1.0 }, { 2.0, 3.0 } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = DOUBLE_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(double[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Double2DArrayAssert result = DOUBLE_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Double2DArrayAssert result = DOUBLE_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(double[][].class);
    }

  }

  @Nested
  class File_Factory {

    private final Object actual = new File("non-existing-file");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = FILE.getRawClass();
      // THEN
      then(result).isEqualTo(File.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractFileAssert<?> result = FILE.createAssert(actual);
      // THEN
      result.doesNotExist();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractFileAssert<?> result = FILE.createAssert(valueProvider);
      // THEN
      result.doesNotExist();
      verify(valueProvider).apply(File.class);
    }

  }

  @Nested
  class Future_Factory {

    private final Object actual = mock(Future.class);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = FUTURE.getRawClass();
      // THEN
      then(result).isEqualTo(Future.class);
    }

    @Test
    void createAssert() {
      // WHEN
      FutureAssert<Object> result = FUTURE.createAssert(actual);
      // THEN
      result.isNotDone();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      FutureAssert<Object> result = FUTURE.createAssert(valueProvider);
      // THEN
      result.isNotDone();
      verify(valueProvider).apply(parameterizedType(Future.class, Object.class));
    }

  }

  @Nested
  class Future_Typed_Factory {

    private final Object actual = mock(Future.class);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = future(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(Future.class);
    }

    @Test
    void createAssert() {
      // WHEN
      FutureAssert<String> result = future(String.class).createAssert(actual);
      // THEN
      result.isNotDone();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      FutureAssert<String> result = future(String.class).createAssert(valueProvider);
      // THEN
      result.isNotDone();
      verify(valueProvider).apply(parameterizedType(Future.class, String.class));
    }

  }

  @Nested
  class InputStream_Factory {

    private final Object actual = new ByteArrayInputStream("stream".getBytes());

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = INPUT_STREAM.getRawClass();
      // THEN
      then(result).isEqualTo(InputStream.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractInputStreamAssert<?, ?> result = INPUT_STREAM.createAssert(actual);
      // THEN
      result.hasContent("stream");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractInputStreamAssert<?, ?> result = INPUT_STREAM.createAssert(valueProvider);
      // THEN
      result.hasContent("stream");
      verify(valueProvider).apply(InputStream.class);
    }

  }

  @Nested
  class Float_Factory {

    private final Object actual = 0.0f;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = FLOAT.getRawClass();
      // THEN
      then(result).isEqualTo(Float.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractFloatAssert<?> result = FLOAT.createAssert(actual);
      // THEN
      result.isZero();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractFloatAssert<?> result = FLOAT.createAssert(valueProvider);
      // THEN
      result.isZero();
      verify(valueProvider).apply(Float.class);
    }

  }

  @Nested
  class Float_Array_Factory {

    private final Object actual = new float[] { 0.0f, 1.0f };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = FLOAT_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(float[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractFloatArrayAssert<?> result = FLOAT_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0.0f, 1.0f);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractFloatArrayAssert<?> result = FLOAT_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0.0f, 1.0f);
      verify(valueProvider).apply(float[].class);
    }

  }

  @Nested
  class Float_2D_Array_Factory {

    private final Object actual = new float[][] { { 0.0f, 1.0f }, { 2.0f, 3.0f } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = FLOAT_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(float[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Float2DArrayAssert result = FLOAT_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Float2DArrayAssert result = FLOAT_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(float[][].class);
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class Integer_Factory {

    private final Object actual = 0;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = INTEGER.getRawClass();
      // THEN
      then(result).isEqualTo(Integer.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractIntegerAssert<?> result = INTEGER.createAssert(actual);
      // THEN
      result.isZero();
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      AbstractIntegerAssert<?> result = INTEGER.createAssert(valueProvider);
      // THEN
      result.isZero();
      verify(valueProvider).apply(Integer.class);
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert("0", type));
    }

  }

  @Nested
  class Int_Array_Factory {

    private final Object actual = new int[] { 0, 1 };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = INT_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(int[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractIntArrayAssert<?> result = INT_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0, 1);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractIntArrayAssert<?> result = INT_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0, 1);
      verify(valueProvider).apply(int[].class);
    }

  }

  @Nested
  class Int_2D_Array_Factory {

    private final Object actual = new int[][] { { 0, 1 }, { 2, 3 } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = INT_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(int[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Int2DArrayAssert result = INT_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Int2DArrayAssert result = INT_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(int[][].class);
    }

  }

  @Nested
  class Long_Factory {

    private final Object actual = 0L;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LONG.getRawClass();
      // THEN
      then(result).isEqualTo(Long.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractLongAssert<?> result = LONG.createAssert(actual);
      // THEN
      result.isZero();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractLongAssert<?> result = LONG.createAssert(valueProvider);
      // THEN
      result.isZero();
      verify(valueProvider).apply(Long.class);
    }

  }

  @Nested
  class Long_Array_Factory {

    private final Object actual = new long[] { 0L, 1L };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LONG_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(long[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractLongArrayAssert<?> result = LONG_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0L, 1L);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractLongArrayAssert<?> result = LONG_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0L, 1L);
      verify(valueProvider).apply(long[].class);
    }

  }

  @Nested
  class Long_2D_Array_Factory {

    private final Object actual = new long[][] { { 0L, 1L }, { 2L, 3L } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LONG_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(long[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Long2DArrayAssert result = LONG_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Long2DArrayAssert result = LONG_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(long[][].class);
    }

  }

  @Nested
  class Type_Factory {

    private final Object actual = "string";

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = type(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(String.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ObjectAssert<String> result = type(String.class).createAssert(actual);
      // THEN
      result.extracting(String::isEmpty).isEqualTo(false);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      ObjectAssert<String> result = type(String.class).createAssert(valueProvider);
      // THEN
      result.extracting(String::isEmpty).isEqualTo(false);
      verify(valueProvider).apply(String.class);
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class Array_Factory {

    private final Object actual = new Object[] { 0, "" };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(Object[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      ObjectArrayAssert<Object> result = ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0, "");
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      ObjectArrayAssert<Object> result = ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0, "");
      verify(valueProvider).apply(Object[].class);
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert(Lists.list(0, ""), type));
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class Array_Typed_Factory {

    private final Object actual = new Integer[] { 0, 1 };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = array(Integer[].class).getRawClass();
      // THEN
      then(result).isEqualTo(Integer[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      ObjectArrayAssert<Integer> result = array(Integer[].class).createAssert(actual);
      // THEN
      result.containsExactly(0, 1);
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      ObjectArrayAssert<Integer> result = array(Integer[].class).createAssert(valueProvider);
      // THEN
      result.containsExactly(0, 1);
      verify(valueProvider).apply(Integer[].class);
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert("0,1", type));
    }

  }

  @Nested
  class Array_2D_Factory {

    private final Object actual = new Object[][] { { 0, "" }, { 3.0, 'b' } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ARRAY_2D.getRawClass();
      // THEN
      then(result).isEqualTo(Object[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Object2DArrayAssert<Object> result = ARRAY_2D.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Object2DArrayAssert<Object> result = ARRAY_2D.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(Object[][].class);
    }

  }

  @Nested
  class Array_2D_Typed_Factory {

    private final Object actual = new Integer[][] { { 0, 1 }, { 2, 3 } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = array2D(Integer[][].class).getRawClass();
      // THEN
      then(result).isEqualTo(Integer[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Object2DArrayAssert<Integer> result = array2D(Integer[][].class).createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Object2DArrayAssert<Integer> result = array2D(Integer[][].class).createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(Integer[][].class);
    }

  }

  @Nested
  class Short_Factory {

    private final Object actual = (short) 0;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = SHORT.getRawClass();
      // THEN
      then(result).isEqualTo(Short.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractShortAssert<?> result = SHORT.createAssert(actual);
      // THEN
      result.isZero();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractShortAssert<?> result = SHORT.createAssert(valueProvider);
      // THEN
      result.isZero();
      verify(valueProvider).apply(Short.class);
    }

  }

  @Nested
  class Short_Array_Factory {

    private final Object actual = new short[] { 0, 1 };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = SHORT_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(short[].class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractShortArrayAssert<?> result = SHORT_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly((short) 0, (short) 1);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractShortArrayAssert<?> result = SHORT_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly((short) 0, (short) 1);
      verify(valueProvider).apply(short[].class);
    }

  }

  @Nested
  class Short_2D_Array_Factory {

    private final Object actual = new short[][] { { 0, 1 }, { 2, 3 } };

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = SHORT_2D_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(short[][].class);
    }

    @Test
    void createAssert() {
      // WHEN
      Short2DArrayAssert result = SHORT_2D_ARRAY.createAssert(actual);
      // THEN
      result.hasDimensions(2, 2);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Short2DArrayAssert result = SHORT_2D_ARRAY.createAssert(valueProvider);
      // THEN
      result.hasDimensions(2, 2);
      verify(valueProvider).apply(short[][].class);
    }

  }

  @Nested
  class Date_Factory {

    private final Object actual = new Date();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = DATE.getRawClass();
      // THEN
      then(result).isEqualTo(Date.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractDateAssert<?> result = DATE.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(new Date());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractDateAssert<?> result = DATE.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(new Date());
      verify(valueProvider).apply(Date.class);
    }

  }

  @Nested
  class Temporal_Factory {

    private final Object actual = ZonedDateTime.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = TEMPORAL.getRawClass();
      // THEN
      then(result).isEqualTo(Temporal.class);
    }

    @Test
    void createAssert() {
      // WHEN
      TemporalAssert result = TEMPORAL.createAssert(actual);
      // THEN
      result.isCloseTo(ZonedDateTime.now(), within(10, SECONDS));
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      TemporalAssert result = TEMPORAL.createAssert(valueProvider);
      // THEN
      result.isCloseTo(ZonedDateTime.now(), within(10, SECONDS));
      verify(valueProvider).apply(Temporal.class);
    }

  }

  @Nested
  class ZonedDateTime_Factory {

    private final Object actual = ZonedDateTime.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ZONED_DATE_TIME.getRawClass();
      // THEN
      then(result).isEqualTo(ZonedDateTime.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractZonedDateTimeAssert<?> result = ZONED_DATE_TIME.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(ZonedDateTime.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractZonedDateTimeAssert<?> result = ZONED_DATE_TIME.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(ZonedDateTime.now());
      verify(valueProvider).apply(ZonedDateTime.class);
    }

  }

  @Nested
  class LocalDateTime_Factory {

    private final Object actual = LocalDateTime.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LOCAL_DATE_TIME.getRawClass();
      // THEN
      then(result).isEqualTo(LocalDateTime.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractLocalDateTimeAssert<?> result = LOCAL_DATE_TIME.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractLocalDateTimeAssert<?> result = LOCAL_DATE_TIME.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(LocalDateTime.now());
      verify(valueProvider).apply(LocalDateTime.class);
    }

  }

  @Nested
  class OffsetDateTime_Factory {

    private final Object actual = OffsetDateTime.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = OFFSET_DATE_TIME.getRawClass();
      // THEN
      then(result).isEqualTo(OffsetDateTime.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractOffsetDateTimeAssert<?> result = OFFSET_DATE_TIME.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractOffsetDateTimeAssert<?> result = OFFSET_DATE_TIME.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(OffsetDateTime.now());
      verify(valueProvider).apply(OffsetDateTime.class);
    }

  }

  @Nested
  class OffsetTime_Factory {

    private final Object actual = OffsetTime.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = OFFSET_TIME.getRawClass();
      // THEN
      then(result).isEqualTo(OffsetTime.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractOffsetTimeAssert<?> result = OFFSET_TIME.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(OffsetTime.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractOffsetTimeAssert<?> result = OFFSET_TIME.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(OffsetTime.now());
      verify(valueProvider).apply(OffsetTime.class);
    }

  }

  @Nested
  class LocalTime_Factory {

    private final Object actual = LocalTime.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LOCAL_TIME.getRawClass();
      // THEN
      then(result).isEqualTo(LocalTime.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractLocalTimeAssert<?> result = LOCAL_TIME.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(LocalTime.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractLocalTimeAssert<?> result = LOCAL_TIME.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(LocalTime.now());
      verify(valueProvider).apply(LocalTime.class);
    }

  }

  @Nested
  class LocalDate_Factory {

    private final Object actual = LocalDate.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LOCAL_DATE.getRawClass();
      // THEN
      then(result).isEqualTo(LocalDate.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractLocalDateAssert<?> result = LOCAL_DATE.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(LocalDate.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractLocalDateAssert<?> result = LOCAL_DATE.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(LocalDate.now());
      verify(valueProvider).apply(LocalDate.class);
    }

  }

  @Nested
  class YearMonth_Factory {

    private final Object actual = YearMonth.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = YEAR_MONTH.getRawClass();
      // THEN
      then(result).isEqualTo(YearMonth.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractYearMonthAssert<?> result = YEAR_MONTH.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(YearMonth.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractYearMonthAssert<?> result = YEAR_MONTH.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(YearMonth.now());
      verify(valueProvider).apply(YearMonth.class);
    }

  }

  @Nested
  class Instant_Factory {

    private final Object actual = Instant.now();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = INSTANT.getRawClass();
      // THEN
      then(result).isEqualTo(Instant.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractInstantAssert<?> result = INSTANT.createAssert(actual);
      // THEN
      result.isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractInstantAssert<?> result = INSTANT.createAssert(valueProvider);
      // THEN
      result.isBeforeOrEqualTo(Instant.now());
      verify(valueProvider).apply(Instant.class);
    }

  }

  @Nested
  class Duration_Factory {

    private final Object actual = Duration.ofHours(10);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = DURATION.getRawClass();
      // THEN
      then(result).isEqualTo(Duration.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractDurationAssert<?> result = DURATION.createAssert(actual);
      // THEN
      result.hasHours(10);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractDurationAssert<?> result = DURATION.createAssert(valueProvider);
      // THEN
      result.hasHours(10);
      verify(valueProvider).apply(Duration.class);
    }

  }

  @Nested
  class Period_Factory {

    private final Object actual = Period.ofYears(1);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = PERIOD.getRawClass();
      // THEN
      then(result).isEqualTo(Period.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractPeriodAssert<?> result = PERIOD.createAssert(actual);
      // THEN
      result.hasYears(1);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractPeriodAssert<?> result = PERIOD.createAssert(valueProvider);
      // THEN
      result.hasYears(1);
      verify(valueProvider).apply(Period.class);
    }

  }

  @Nested
  class AtomicBoolean_Factory {

    private final Object actual = new AtomicBoolean();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_BOOLEAN.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicBoolean.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicBooleanAssert result = ATOMIC_BOOLEAN.createAssert(actual);
      // THEN
      result.isFalse();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicBooleanAssert result = ATOMIC_BOOLEAN.createAssert(valueProvider);
      // THEN
      result.isFalse();
      verify(valueProvider).apply(AtomicBoolean.class);
    }

  }

  @Nested
  class AtomicInteger_Factory {

    private final Object actual = new AtomicInteger();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_INTEGER.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicInteger.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicIntegerAssert result = ATOMIC_INTEGER.createAssert(actual);
      // THEN
      result.hasValue(0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicIntegerAssert result = ATOMIC_INTEGER.createAssert(valueProvider);
      // THEN
      result.hasValue(0);
      verify(valueProvider).apply(AtomicInteger.class);
    }

  }

  @Nested
  class AtomicIntegerArray_Factory {

    private final Object actual = new AtomicIntegerArray(new int[] { 0, 1 });

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_INTEGER_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicIntegerArray.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicIntegerArrayAssert result = ATOMIC_INTEGER_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0, 1);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicIntegerArrayAssert result = ATOMIC_INTEGER_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0, 1);
      verify(valueProvider).apply(AtomicIntegerArray.class);
    }

  }

  @Nested
  class AtomicIntegerFieldUpdater_Factory {

    private final Object actual = AtomicIntegerFieldUpdater.newUpdater(Container.class, "intField");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_INTEGER_FIELD_UPDATER.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicIntegerFieldUpdater.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicIntegerFieldUpdaterAssert<Object> result = ATOMIC_INTEGER_FIELD_UPDATER.createAssert(actual);
      // THEN
      result.hasValue(0, new Container());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicIntegerFieldUpdaterAssert<Object> result = ATOMIC_INTEGER_FIELD_UPDATER.createAssert(valueProvider);
      // THEN
      result.hasValue(0, new Container());
      verify(valueProvider).apply(parameterizedType(AtomicIntegerFieldUpdater.class, Object.class));
    }

    private class Container {

      volatile int intField;

    }

  }

  @Nested
  class AtomicIntegerFieldUpdater_Typed_Factory {

    private final Object actual = AtomicIntegerFieldUpdater.newUpdater(Container.class, "intField");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = atomicIntegerFieldUpdater(Container.class).getRawClass();
      // THEN
      then(result).isEqualTo(AtomicIntegerFieldUpdater.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicIntegerFieldUpdaterAssert<Container> result = atomicIntegerFieldUpdater(Container.class).createAssert(actual);
      // THEN
      result.hasValue(0, new Container());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicIntegerFieldUpdaterAssert<Container> result = atomicIntegerFieldUpdater(Container.class).createAssert(valueProvider);
      // THEN
      result.hasValue(0, new Container());
      verify(valueProvider).apply(parameterizedType(AtomicIntegerFieldUpdater.class, Container.class));
    }

    private class Container {

      volatile int intField;

    }

  }

  @Nested
  class LongAdder_Factory {

    private final Object actual = new LongAdder();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LONG_ADDER.getRawClass();
      // THEN
      then(result).isEqualTo(LongAdder.class);
    }

    @Test
    void createAssert() {
      // WHEN
      LongAdderAssert result = LONG_ADDER.createAssert(actual);
      // THEN
      result.hasValue(0L);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      LongAdderAssert result = LONG_ADDER.createAssert(valueProvider);
      // THEN
      result.hasValue(0L);
      verify(valueProvider).apply(LongAdder.class);
    }

  }

  @Nested
  class AtomicLong_Factory {

    private final Object actual = new AtomicLong();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_LONG.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicLong.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicLongAssert result = ATOMIC_LONG.createAssert(actual);
      // THEN
      result.hasValue(0L);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicLongAssert result = ATOMIC_LONG.createAssert(valueProvider);
      // THEN
      result.hasValue(0L);
      verify(valueProvider).apply(AtomicLong.class);
    }

  }

  @Nested
  class AtomicLongArray_Factory {

    private final Object actual = new AtomicLongArray(new long[] { 0L, 1L });

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_LONG_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicLongArray.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicLongArrayAssert result = ATOMIC_LONG_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0L, 1L);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicLongArrayAssert result = ATOMIC_LONG_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0L, 1L);
      verify(valueProvider).apply(AtomicLongArray.class);
    }

  }

  @Nested
  class AtomicLongFieldUpdater_Factory {

    private final Object actual = AtomicLongFieldUpdater.newUpdater(Container.class, "longField");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_LONG_FIELD_UPDATER.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicLongFieldUpdater.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicLongFieldUpdaterAssert<Object> result = ATOMIC_LONG_FIELD_UPDATER.createAssert(actual);
      // THEN
      result.hasValue(0L, new Container());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicLongFieldUpdaterAssert<Object> result = ATOMIC_LONG_FIELD_UPDATER.createAssert(valueProvider);
      // THEN
      result.hasValue(0L, new Container());
      verify(valueProvider).apply(parameterizedType(AtomicLongFieldUpdater.class, Object.class));
    }

    private class Container {

      volatile long longField;

    }

  }

  @Nested
  class AtomicLongFieldUpdater_Typed_Factory {

    private final Object actual = AtomicLongFieldUpdater.newUpdater(Container.class, "longField");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = atomicLongFieldUpdater(Container.class).getRawClass();
      // THEN
      then(result).isEqualTo(AtomicLongFieldUpdater.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicLongFieldUpdaterAssert<Container> result = atomicLongFieldUpdater(Container.class).createAssert(actual);
      // THEN
      result.hasValue(0L, new Container());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicLongFieldUpdaterAssert<Container> result = atomicLongFieldUpdater(Container.class).createAssert(valueProvider);
      // THEN
      result.hasValue(0L, new Container());
      verify(valueProvider).apply(parameterizedType(AtomicLongFieldUpdater.class, Container.class));
    }

    private class Container {

      volatile long longField;

    }

  }

  @Nested
  class AtomicReference_Factory {

    private final Object actual = new AtomicReference<>();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_REFERENCE.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicReference.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicReferenceAssert<Object> result = ATOMIC_REFERENCE.createAssert(actual);
      // THEN
      result.hasValue(null);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicReferenceAssert<Object> result = ATOMIC_REFERENCE.createAssert(valueProvider);
      // THEN
      result.hasValue(null);
      verify(valueProvider).apply(parameterizedType(AtomicReference.class, Object.class));
    }

  }

  @Nested
  class AtomicReference_Typed_Factory {

    private final Object actual = new AtomicReference<>(0);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = atomicReference(Integer.class).getRawClass();
      // THEN
      then(result).isEqualTo(AtomicReference.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicReferenceAssert<Integer> result = atomicReference(Integer.class).createAssert(actual);
      // THEN
      result.hasValue(0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicReferenceAssert<Integer> result = atomicReference(Integer.class).createAssert(valueProvider);
      // THEN
      result.hasValue(0);
      verify(valueProvider).apply(parameterizedType(AtomicReference.class, Integer.class));
    }

  }

  @Nested
  class AtomicReferenceArray_Factory {

    private final Object actual = new AtomicReferenceArray<>(new Object[] { 0, "" });

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_REFERENCE_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicReferenceArray.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicReferenceArrayAssert<Object> result = ATOMIC_REFERENCE_ARRAY.createAssert(actual);
      // THEN
      result.containsExactly(0, "");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicReferenceArrayAssert<Object> result = ATOMIC_REFERENCE_ARRAY.createAssert(valueProvider);
      // THEN
      result.containsExactly(0, "");
      verify(valueProvider).apply(parameterizedType(AtomicReferenceArray.class, Object.class));
    }

  }

  @Nested
  class AtomicReferenceArray_Typed_Factory {

    private final Object actual = new AtomicReferenceArray<>(new Integer[] { 0, 1 });

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_REFERENCE_ARRAY.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicReferenceArray.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicReferenceArrayAssert<Integer> result = atomicReferenceArray(Integer.class).createAssert(actual);
      // THEN
      result.containsExactly(0, 1);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicReferenceArrayAssert<Integer> result = atomicReferenceArray(Integer.class).createAssert(valueProvider);
      // THEN
      result.containsExactly(0, 1);
      verify(valueProvider).apply(parameterizedType(AtomicReferenceArray.class, Integer.class));
    }

  }

  @Nested
  class AtomicReferenceFieldUpdater_Factory {

    private final Object actual = AtomicReferenceFieldUpdater.newUpdater(Container.class, String.class, "stringField");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_REFERENCE_FIELD_UPDATER.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicReferenceFieldUpdater.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicReferenceFieldUpdaterAssert<Object, Object> result = ATOMIC_REFERENCE_FIELD_UPDATER.createAssert(actual);
      // THEN
      result.hasValue(null, new Container());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicReferenceFieldUpdaterAssert<Object, Object> result = ATOMIC_REFERENCE_FIELD_UPDATER.createAssert(valueProvider);
      // THEN
      result.hasValue(null, new Container());
      verify(valueProvider).apply(parameterizedType(AtomicReferenceFieldUpdater.class, Object.class, Object.class));
    }

    private class Container {

      volatile String stringField;

    }

  }

  @Nested
  class AtomicReferenceFieldUpdater_Typed_Factory {

    private final Object actual = AtomicReferenceFieldUpdater.newUpdater(Container.class, String.class, "stringField");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = atomicReferenceFieldUpdater(String.class, Container.class).getRawClass();
      // THEN
      then(result).isEqualTo(AtomicReferenceFieldUpdater.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicReferenceFieldUpdaterAssert<String, Container> result = atomicReferenceFieldUpdater(String.class,
                                                                                                Container.class).createAssert(actual);
      // THEN
      result.hasValue(null, new Container());
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicReferenceFieldUpdaterAssert<String, Container> result = atomicReferenceFieldUpdater(String.class,
                                                                                                Container.class).createAssert(valueProvider);
      // THEN
      result.hasValue(null, new Container());
      verify(valueProvider).apply(parameterizedType(AtomicReferenceFieldUpdater.class, String.class,
                                                    Container.class));
    }

    private class Container {

      volatile String stringField;

    }

  }

  @Nested
  class AtomicMarkableReference_Factory {

    private final Object actual = new AtomicMarkableReference<>(null, false);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_MARKABLE_REFERENCE.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicMarkableReference.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicMarkableReferenceAssert<Object> result = ATOMIC_MARKABLE_REFERENCE.createAssert(actual);
      // THEN
      result.hasReference(null);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicMarkableReferenceAssert<Object> result = ATOMIC_MARKABLE_REFERENCE.createAssert(valueProvider);
      // THEN
      result.hasReference(null);
      verify(valueProvider).apply(parameterizedType(AtomicMarkableReference.class, Object.class));
    }

  }

  @Nested
  class AtomicMarkableReference_Typed_Factory {

    private final Object actual = new AtomicMarkableReference<>(0, false);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = atomicMarkableReference(Integer.class).getRawClass();
      // THEN
      then(result).isEqualTo(AtomicMarkableReference.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicMarkableReferenceAssert<Integer> result = atomicMarkableReference(Integer.class).createAssert(actual);
      // THEN
      result.hasReference(0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicMarkableReferenceAssert<Integer> result = atomicMarkableReference(Integer.class).createAssert(valueProvider);
      // THEN
      result.hasReference(0);
      verify(valueProvider).apply(parameterizedType(AtomicMarkableReference.class, Integer.class));
    }

  }

  @Nested
  class AtomicStampedReference_Factory {

    private final Object actual = new AtomicStampedReference<>(null, 0);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_STAMPED_REFERENCE.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicStampedReference.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicStampedReferenceAssert<Object> result = ATOMIC_STAMPED_REFERENCE.createAssert(actual);
      // THEN
      result.hasReference(null);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicStampedReferenceAssert<Object> result = ATOMIC_STAMPED_REFERENCE.createAssert(valueProvider);
      // THEN
      result.hasReference(null);
      verify(valueProvider).apply(parameterizedType(AtomicStampedReference.class, Object.class));
    }

  }

  @Nested
  class AtomicStampedReference_Typed_Factory {

    private final Object actual = new AtomicStampedReference<>(0, 0);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ATOMIC_STAMPED_REFERENCE.getRawClass();
      // THEN
      then(result).isEqualTo(AtomicStampedReference.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AtomicStampedReferenceAssert<Integer> result = atomicStampedReference(Integer.class).createAssert(actual);
      // THEN
      result.hasReference(0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AtomicStampedReferenceAssert<Integer> result = atomicStampedReference(Integer.class).createAssert(valueProvider);
      // THEN
      result.hasReference(0);
      verify(valueProvider).apply(parameterizedType(AtomicStampedReference.class, Integer.class));
    }

  }

  @Nested
  class Throwable_Factory {

    private final Object actual = new RuntimeException("message");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = THROWABLE.getRawClass();
      // THEN
      then(result).isEqualTo(Throwable.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractThrowableAssert<?, Throwable> result = THROWABLE.createAssert(actual);
      // THEN
      result.hasMessage("message");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractThrowableAssert<?, Throwable> result = THROWABLE.createAssert(valueProvider);
      // THEN
      result.hasMessage("message");
      verify(valueProvider).apply(Throwable.class);
    }

  }

  @Nested
  class Throwable_Typed_Factory {

    private final Object actual = new RuntimeException("message");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = throwable(RuntimeException.class).getRawClass();
      // THEN
      then(result).isEqualTo(RuntimeException.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractThrowableAssert<?, RuntimeException> result = throwable(RuntimeException.class).createAssert(actual);
      // THEN
      result.hasMessage("message");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractThrowableAssert<?, RuntimeException> result = throwable(RuntimeException.class).createAssert(valueProvider);
      // THEN
      result.hasMessage("message");
      verify(valueProvider).apply(RuntimeException.class);
    }

  }

  @Nested
  class CharSequence_Factory {

    private final Object actual = "string";

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = CHAR_SEQUENCE.getRawClass();
      // THEN
      then(result).isEqualTo(CharSequence.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCharSequenceAssert<?, ? extends CharSequence> result = CHAR_SEQUENCE.createAssert(actual);
      // THEN
      result.startsWith("str");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractCharSequenceAssert<?, ? extends CharSequence> result = CHAR_SEQUENCE.createAssert(valueProvider);
      // THEN
      result.startsWith("str");
      verify(valueProvider).apply(CharSequence.class);
    }

  }

  @Nested
  class StringBuilder_Factory {

    private final Object actual = new StringBuilder("string");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = STRING_BUILDER.getRawClass();
      // THEN
      then(result).isEqualTo(StringBuilder.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCharSequenceAssert<?, ? extends CharSequence> result = STRING_BUILDER.createAssert(actual);
      // THEN
      result.startsWith("str");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractCharSequenceAssert<?, ? extends CharSequence> result = STRING_BUILDER.createAssert(valueProvider);
      // THEN
      result.startsWith("str");
      verify(valueProvider).apply(StringBuilder.class);
    }

  }

  @Nested
  class StringBuffer_Factory {

    private final Object actual = new StringBuffer("string");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = STRING_BUFFER.getRawClass();
      // THEN
      then(result).isEqualTo(StringBuffer.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCharSequenceAssert<?, ? extends CharSequence> result = STRING_BUFFER.createAssert(actual);
      // THEN
      result.startsWith("str");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractCharSequenceAssert<?, ? extends CharSequence> result = STRING_BUFFER.createAssert(valueProvider);
      // THEN
      result.startsWith("str");
      verify(valueProvider).apply(StringBuffer.class);
    }

  }

  @Nested
  class String_Factory {

    private final Object actual = "string";

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = STRING.getRawClass();
      // THEN
      then(result).isEqualTo(String.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractStringAssert<?> result = STRING.createAssert(actual);
      // THEN
      result.startsWith("str");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractStringAssert<?> result = STRING.createAssert(valueProvider);
      // THEN
      result.startsWith("str");
      verify(valueProvider).apply(String.class);
    }

  }

  @Nested
  class Iterable_Factory {

    private final Object actual = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ITERABLE.getRawClass();
      // THEN
      then(result).isEqualTo(Iterable.class);
    }

    @Test
    void createAssert() {
      // WHEN
      IterableAssert<Object> result = ITERABLE.createAssert(actual);
      // THEN
      result.contains("Bart", "Lisa");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      IterableAssert<Object> result = ITERABLE.createAssert(valueProvider);
      // THEN
      result.contains("Bart", "Lisa");
      verify(valueProvider).apply(parameterizedType(Iterable.class, Object.class));
    }

  }

  @Nested
  class Iterable_Typed_Factory {

    private final Object actual = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = iterable(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(Iterable.class);
    }

    @Test
    void createAssert() {
      // WHEN
      IterableAssert<String> result = iterable(String.class).createAssert(actual);
      // THEN
      result.contains("Bart", "Lisa");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      IterableAssert<String> result = iterable(String.class).createAssert(valueProvider);
      // THEN
      result.contains("Bart", "Lisa");
      verify(valueProvider).apply(parameterizedType(Iterable.class, String.class));
    }

  }

  @Nested
  class Iterator_Factory {

    private final Object actual = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie").iterator();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = ITERATOR.getRawClass();
      // THEN
      then(result).isEqualTo(Iterator.class);
    }

    @Test
    void createAssert() {
      // WHEN
      IteratorAssert<Object> result = ITERATOR.createAssert(actual);
      // THEN
      result.hasNext();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      IteratorAssert<Object> result = ITERATOR.createAssert(valueProvider);
      // THEN
      result.hasNext();
      verify(valueProvider).apply(parameterizedType(Iterator.class, Object.class));
    }

  }

  @Nested
  class Iterator_Typed_Factory {

    private final Object actual = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie").iterator();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = iterator(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(Iterator.class);
    }

    @Test
    void createAssert() {
      // WHEN
      IteratorAssert<String> result = iterator(String.class).createAssert(actual);
      // THEN
      result.hasNext();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      IteratorAssert<String> result = iterator(String.class).createAssert(valueProvider);
      // THEN
      result.hasNext();
      verify(valueProvider).apply(parameterizedType(Iterator.class, String.class));
    }

  }

  @Nested
  class Collection_Factory {

    private final Object actual = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = COLLECTION.getRawClass();
      // THEN
      then(result).isEqualTo(Collection.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>> result = COLLECTION.createAssert(actual);
      // THEN
      result.contains("Bart", "Lisa");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>> result = COLLECTION.createAssert(valueProvider);
      // THEN
      result.contains("Bart", "Lisa");
      verify(valueProvider).apply(parameterizedType(Collection.class, Object.class));
    }

  }

  @Nested
  class Collection_Typed_Factory {

    private final Object actual = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = collection(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(Collection.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCollectionAssert<?, Collection<? extends String>, String, ObjectAssert<String>> result = collection(String.class).createAssert(actual);
      // THEN
      result.contains("Bart", "Lisa");
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractCollectionAssert<?, Collection<? extends String>, String, ObjectAssert<String>> result = collection(String.class).createAssert(valueProvider);
      // THEN
      result.contains("Bart", "Lisa");
      verify(valueProvider).apply(parameterizedType(Collection.class, String.class));
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class Set_Factory {

    private final Object actual = Sets.set(123, 456, 789);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = SET.getRawClass();
      // THEN
      then(result).isEqualTo(Set.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>> result = SET.createAssert(actual);
      // THEN
      result.contains(456, 789);
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>> result = SET.createAssert(valueProvider);
      // THEN
      result.contains(456, 789);
      verify(valueProvider).apply(parameterizedType(Set.class, Object.class));
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert(new int[] { 123, 456, 789 }, type));
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class Set_Typed_Factory {

    private final Object actual = Sets.set(123, 456, 789);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = set(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(Set.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractCollectionAssert<?, Collection<? extends Integer>, Integer, ObjectAssert<Integer>> result = set(Integer.class).createAssert(actual);
      // THEN
      result.contains(456, 789);
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      AbstractCollectionAssert<?, Collection<? extends Integer>, Integer, ObjectAssert<Integer>> result = set(Integer.class).createAssert(valueProvider);
      // THEN
      result.contains(456, 789);
      verify(valueProvider).apply(parameterizedType(Set.class, Integer.class));
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert(new String[] { "123", "456", "789" }, type));
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class List_Factory {

    private final Object actual = Lists.list(123, 456, 789);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LIST.getRawClass();
      // THEN
      then(result).isEqualTo(List.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ListAssert<Object> result = LIST.createAssert(actual);
      // THEN
      result.contains(456, 789);
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      ListAssert<Object> result = LIST.createAssert(valueProvider);
      // THEN
      result.contains(456, 789);
      verify(valueProvider).apply(parameterizedType(List.class, Object.class));
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert(new int[] { 123, 456, 789 }, type));
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class List_Typed_Factory {

    private final Object actual = Lists.list(123, 456, 789);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = list(String.class).getRawClass();
      // THEN
      then(result).isEqualTo(List.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ListAssert<Integer> result = list(Integer.class).createAssert(actual);
      // THEN
      result.contains(456, 789);
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      ListAssert<Integer> result = list(Integer.class).createAssert(valueProvider);
      // THEN
      result.contains(456, 789);
      verify(valueProvider).apply(parameterizedType(List.class, Integer.class));
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert(new String[] { "123", "456", "789" }, type));
    }

  }

  @Nested
  class Stream_Factory {

    private final Object actual = Stream.of(1, 2, 3);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = STREAM.getRawClass();
      // THEN
      then(result).isEqualTo(Stream.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ListAssert<Object> result = STREAM.createAssert(actual);
      // THEN
      result.containsExactly(1, 2, 3);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      ListAssert<Object> result = STREAM.createAssert(valueProvider);
      // THEN
      result.containsExactly(1, 2, 3);
      verify(valueProvider).apply(parameterizedType(Stream.class, Object.class));
    }

  }

  @Nested
  class Stream_Typed_Factory {

    private final Object actual = Stream.of(1, 2, 3);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = stream(Integer.class).getRawClass();
      // THEN
      then(result).isEqualTo(Stream.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ListAssert<Integer> result = stream(Integer.class).createAssert(actual);
      // THEN
      result.containsExactly(1, 2, 3);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      ListAssert<Integer> result = stream(Integer.class).createAssert(valueProvider);
      // THEN
      result.containsExactly(1, 2, 3);
      verify(valueProvider).apply(parameterizedType(Stream.class, Integer.class));
    }

  }

  @Nested
  class DoubleStream_Factory {

    private final Object actual = DoubleStream.of(1.0, 2.0, 3.0);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = DOUBLE_STREAM.getRawClass();
      // THEN
      then(result).isEqualTo(DoubleStream.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ListAssert<Double> result = DOUBLE_STREAM.createAssert(actual);
      // THEN
      result.containsExactly(1.0, 2.0, 3.0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      ListAssert<Double> result = DOUBLE_STREAM.createAssert(valueProvider);
      // THEN
      result.containsExactly(1.0, 2.0, 3.0);
      verify(valueProvider).apply(DoubleStream.class);
    }

  }

  @Nested
  class LongStream_Factory {

    private final Object actual = LongStream.of(1L, 2L, 3L);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = LONG_STREAM.getRawClass();
      // THEN
      then(result).isEqualTo(LongStream.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ListAssert<Long> result = LONG_STREAM.createAssert(actual);
      // THEN
      result.containsExactly(1L, 2L, 3L);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      ListAssert<Long> result = LONG_STREAM.createAssert(valueProvider);
      // THEN
      result.containsExactly(1L, 2L, 3L);
      verify(valueProvider).apply(LongStream.class);
    }

  }

  @Nested
  class IntStream_Factory {

    private final Object actual = IntStream.of(1, 2, 3);

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = INT_STREAM.getRawClass();
      // THEN
      then(result).isEqualTo(IntStream.class);
    }

    @Test
    void createAssert() {
      // WHEN
      ListAssert<Integer> result = INT_STREAM.createAssert(actual);
      // THEN
      result.containsExactly(1, 2, 3);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      ListAssert<Integer> result = INT_STREAM.createAssert(valueProvider);
      // THEN
      result.containsExactly(1, 2, 3);
      verify(valueProvider).apply(IntStream.class);
    }

  }

  @Nested
  class Path_Factory {

    private final Object actual = Paths.get("non-existing");

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = PATH.getRawClass();
      // THEN
      then(result).isEqualTo(Path.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractPathAssert<?> result = PATH.createAssert(actual);
      // THEN
      result.doesNotExist();
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractPathAssert<?> result = PATH.createAssert(valueProvider);
      // THEN
      result.doesNotExist();
      verify(valueProvider).apply(Path.class);
    }

  }

  @Nested
  class Spliterator_Factory {

    private final Object actual = Stream.of(1, 2).spliterator();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = SPLITERATOR.getRawClass();
      // THEN
      then(result).isEqualTo(Spliterator.class);
    }

    @Test
    void createAssert() {
      // WHEN
      SpliteratorAssert<Object> result = SPLITERATOR.createAssert(actual);
      // THEN
      result.hasCharacteristics(Spliterator.SIZED);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      SpliteratorAssert<Object> result = SPLITERATOR.createAssert(valueProvider);
      // THEN
      result.hasCharacteristics(Spliterator.SIZED);
      verify(valueProvider).apply(parameterizedType(Spliterator.class, Object.class));
    }

  }

  @Nested
  class Spliterator_Typed_Factory {

    private final Object actual = Stream.of(1, 2).spliterator();

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = spliterator(Integer.class).getRawClass();
      // THEN
      then(result).isEqualTo(Spliterator.class);
    }

    @Test
    void createAssert() {
      // WHEN
      SpliteratorAssert<Integer> result = spliterator(Integer.class).createAssert(actual);
      // THEN
      result.hasCharacteristics(Spliterator.SIZED);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      SpliteratorAssert<Integer> result = spliterator(Integer.class).createAssert(valueProvider);
      // THEN
      result.hasCharacteristics(Spliterator.SIZED);
      verify(valueProvider).apply(parameterizedType(Spliterator.class, Integer.class));
    }

  }

  @Nested
  class Map_Factory {

    private final Object actual = mapOf(entry("key", "value"));

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = MAP.getRawClass();
      // THEN
      then(result).isEqualTo(Map.class);
    }

    @Test
    void createAssert() {
      // WHEN
      MapAssert<Object, Object> result = MAP.createAssert(actual);
      // THEN
      result.containsExactly(entry("key", "value"));
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      MapAssert<Object, Object> result = MAP.createAssert(valueProvider);
      // THEN
      result.containsExactly(entry("key", "value"));
      verify(valueProvider).apply(parameterizedType(Map.class, Object.class, Object.class));
    }

  }

  @Nested
  @TestInstance(PER_CLASS)
  class Map_Typed_Factory {

    private final Object actual = mapOf(entry(123, 456));

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = map(Integer.class, Integer.class).getRawClass();
      // THEN
      then(result).isEqualTo(Map.class);
    }

    @Test
    void createAssert() {
      // WHEN
      MapAssert<Integer, Integer> result = map(Integer.class, Integer.class).createAssert(actual);
      // THEN
      result.containsExactly(entry(123, 456));
    }

    @ParameterizedTest
    @MethodSource("valueProviders")
    void createAssert_with_ValueProvider(ValueProvider<?> delegate) {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(delegate);
      // WHEN
      MapAssert<Integer, Integer> result = map(Integer.class, Integer.class).createAssert(valueProvider);
      // THEN
      result.containsExactly(entry(123, 456));
      verify(valueProvider).apply(parameterizedType(Map.class, Integer.class, Integer.class));
    }

    private Stream<ValueProvider<?>> valueProviders() {
      return Stream.of(type -> actual,
                       type -> convert(mapOf(entry("123", "456")), type));
    }

  }

  @Nested
  class Comparable_Factory {

    private final Object actual = 0;

    @Test
    void getRawClass() {
      // WHEN
      Class<?> result = comparable(Integer.class).getRawClass();
      // THEN
      then(result).isEqualTo(Integer.class);
    }

    @Test
    void createAssert() {
      // WHEN
      AbstractComparableAssert<?, Integer> result = comparable(Integer.class).createAssert(actual);
      // THEN
      result.isEqualByComparingTo(0);
    }

    @Test
    void createAssert_with_ValueProvider() {
      // GIVEN
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      AbstractComparableAssert<?, Integer> result = comparable(Integer.class).createAssert(valueProvider);
      // THEN
      result.isEqualByComparingTo(0);
      verify(valueProvider).apply(Integer.class);
    }

  }

  @SuppressWarnings("unchecked")
  @SafeVarargs
  private static <T> T mockThatDelegatesTo(T delegate, T... reified) {
    if (reified.length > 0) {
      throw new IllegalArgumentException("Leave the vararg parameter empty. That is used to detect the class instance automagically.");
    }
    return mock((Class<T>) reified.getClass().getComponentType(), delegatesTo(delegate));
  }

  private static ParameterizedType parameterizedType(Class<?> rawClass, Class<?>... typeArguments) {
    return argThat(argument -> {
      assertThat(argument).returns(typeArguments, from(ParameterizedType::getActualTypeArguments))
                          .returns(rawClass, from(ParameterizedType::getRawType))
                          .returns(null, from(ParameterizedType::getOwnerType));
      return true;
    });
  }

  @SuppressWarnings("unchecked")
  private static <T> T convert(Object instance, Type type) {
    return (T) SPRING_CONVERSION_SERVICE.convert(instance, new TypeDescriptor(ResolvableType.forType(type), null, null));
  }

}
