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

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.within;
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
import static org.assertj.core.api.InstanceOfAssertFactories.stream;
import static org.assertj.core.api.InstanceOfAssertFactories.throwable;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.assertj.core.test.Maps.mapOf;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
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
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Test;

/**
 * @author Stefano Cordio
 */
class InstanceOfAssertFactoriesTest {

  @Test
  void predicate_factory_createAssert_should_create_predicate_assertions() {
    // GIVEN
    Object value = (Predicate<Object>) Objects::isNull;
    // WHEN
    PredicateAssert<Object> result = PREDICATE.createAssert(value);
    // THEN
    result.accepts((Object) null);
  }

  @Test
  void predicate_typed_factory_createAssert_should_create_predicate_typed_assertions() {
    // GIVEN
    Object value = (Predicate<String>) Strings::isNullOrEmpty;
    // WHEN
    PredicateAssert<String> result = predicate(String.class).createAssert(value);
    // THEN
    result.accepts("");
  }

  @Test
  void int_predicate_factory_createAssert_should_create_int_predicate_assertions() {
    // GIVEN
    Object value = (IntPredicate) i -> i == 0;
    // WHEN
    IntPredicateAssert result = INT_PREDICATE.createAssert(value);
    // THEN
    result.accepts(0);
  }

  @Test
  void long_predicate_factory_createAssert_should_create_long_predicate_assertions() {
    // GIVEN
    Object value = (LongPredicate) l -> l == 0L;
    // WHEN
    LongPredicateAssert result = LONG_PREDICATE.createAssert(value);
    // THEN
    result.accepts(0L);
  }

  @Test
  void double_predicate_factory_createAssert_should_create_double_predicate_assertions() {
    // GIVEN
    Object value = (DoublePredicate) d -> d == 0.0;
    // WHEN
    DoublePredicateAssert result = DOUBLE_PREDICATE.createAssert(value);
    // THEN
    result.accepts(0.0);
  }

  @Test
  void completable_future_factory_createAssert_should_create_completable_future_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<Object> result = COMPLETABLE_FUTURE.createAssert(value);
    // THEN
    result.isDone();
  }

  @Test
  void completable_future_typed_factory_createAssert_should_create_completable_future_typed_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<String> result = completableFuture(String.class).createAssert(value);
    // THEN
    result.isDone();
  }

  @Test
  void completion_stage_factory_createAssert_should_create_completable_future_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<Object> result = COMPLETION_STAGE.createAssert(value);
    // THEN
    result.isDone();
  }

  @Test
  void completion_stage_typed_factory_createAssert_should_create_completable_future_typed_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<String> result = completionStage(String.class).createAssert(value);
    // THEN
    result.isDone();
  }

  @Test
  void optional_factory_createAssert_should_create_optional_assertions() {
    // GIVEN
    Object value = Optional.of("something");
    // WHEN
    OptionalAssert<Object> result = OPTIONAL.createAssert(value);
    // THEN
    result.isPresent();
  }

  @Test
  void optional_typed_factory_createAssert_should_create_optional_typed_assertions() {
    // GIVEN
    Object value = Optional.of("something");
    // WHEN
    OptionalAssert<String> result = optional(String.class).createAssert(value);
    // THEN
    result.isPresent();
  }

  @Test
  void optional_double_factory_createAssert_should_create_optional_double_assertions() {
    // GIVEN
    Object value = OptionalDouble.of(0.0);
    // WHEN
    OptionalDoubleAssert result = OPTIONAL_DOUBLE.createAssert(value);
    // THEN
    result.isPresent();
  }

  @Test
  void optional_int_factory_createAssert_should_create_optional_int_assertions() {
    // GIVEN
    Object value = OptionalInt.of(0);
    // WHEN
    OptionalIntAssert result = OPTIONAL_INT.createAssert(value);
    // THEN
    result.isPresent();
  }

  @Test
  void optional_long_factory_createAssert_should_create_optional_long_assertions() {
    // GIVEN
    Object value = OptionalLong.of(0L);
    // WHEN
    OptionalLongAssert result = OPTIONAL_LONG.createAssert(value);
    // THEN
    result.isPresent();
  }

  @Test
  void matcher_factory_createAssert_should_create_matcher_assertions() {
    // GIVEN
    Object value = Pattern.compile("a*").matcher("aaa");
    // WHEN
    MatcherAssert result = MATCHER.createAssert(value);
    // THEN
    result.matches();
  }

  @Test
  void big_decimal_factory_createAssert_should_create_big_decimal_assertions() {
    // GIVEN
    Object value = BigDecimal.valueOf(0.0);
    // WHEN
    AbstractBigDecimalAssert<?> result = BIG_DECIMAL.createAssert(value);
    // THEN
    result.isEqualTo("0.0");
  }

  @Test
  void big_integer_factory_createAssert_should_create_big_integer_assertions() {
    // GIVEN
    Object value = BigInteger.valueOf(0L);
    // WHEN
    AbstractBigIntegerAssert<?> result = BIG_INTEGER.createAssert(value);
    // THEN
    result.isEqualTo(0L);
  }

  @Test
  void uri_type_factory_createAssert_should_create_uri_assertions() {
    // GIVEN
    Object value = URI.create("http://localhost");
    // WHEN
    AbstractUriAssert<?> result = URI_TYPE.createAssert(value);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void url_type_factory_createAssert_should_create_url_assertions() throws MalformedURLException {
    // GIVEN
    Object value = new URL("http://localhost");
    // WHEN
    AbstractUrlAssert<?> result = URL_TYPE.createAssert(value);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void boolean_factory_createAssert_should_create_boolean_assertions() {
    // GIVEN
    Object value = true;
    // WHEN
    AbstractBooleanAssert<?> result = BOOLEAN.createAssert(value);
    // THEN
    result.isTrue();
  }

  @Test
  void boolean_array_factory_createAssert_should_create_boolean_array_assertions() {
    // GIVEN
    Object value = new boolean[] { true, false };
    // WHEN
    AbstractBooleanArrayAssert<?> result = BOOLEAN_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(true, false);
  }

  @Test
  void boolean_2d_array_factory_createAssert_should_create_boolean_2d_array_assertions() {
    // GIVEN
    Object value = new boolean[][] { { true, false }, { false, true } };
    // WHEN
    Boolean2DArrayAssert result = BOOLEAN_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void byte_factory_createAssert_should_create_byte_assertions() {
    // GIVEN
    Object value = (byte) 0;
    // WHEN
    AbstractByteAssert<?> result = BYTE.createAssert(value);
    // THEN
    result.isEqualTo((byte) 0);
  }

  @Test
  void byte_array_factory_createAssert_should_create_byte_array_assertions() {
    // GIVEN
    Object value = new byte[] { 0, 1 };
    // WHEN
    AbstractByteArrayAssert<?> result = BYTE_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void byte_2d_array_factory_createAssert_should_create_byte_2d_array_assertions() {
    // GIVEN
    Object value = new byte[][] { { 0, 1 }, { 2, 3 } };
    // WHEN
    Byte2DArrayAssert result = BYTE_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void character_factory_createAssert_should_create_character_assertions() {
    // GIVEN
    Object value = 'a';
    // WHEN
    AbstractCharacterAssert<?> result = CHARACTER.createAssert(value);
    // THEN
    result.isLowerCase();
  }

  @Test
  void char_array_factory_createAssert_should_create_char_array_assertions() {
    // GIVEN
    Object value = new char[] { 'a', 'b' };
    // WHEN
    AbstractCharArrayAssert<?> result = CHAR_ARRAY.createAssert(value);
    // THEN
    result.doesNotHaveDuplicates();
  }

  @Test
  void char_2d_array_factory_createAssert_should_create_char_2d_array_assertions() {
    // GIVEN
    Object value = new char[][] { { 'a', 'b' }, { 'c', 'd' } };
    // WHEN
    Char2DArrayAssert result = CHAR_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void class_factory_createAssert_should_create_class_assertions() {
    // GIVEN
    Object value = Function.class;
    // WHEN
    ClassAssert result = CLASS.createAssert(value);
    // THEN
    result.hasAnnotations(FunctionalInterface.class);
  }

  @Test
  void double_factory_createAssert_should_create_double_assertions() {
    // GIVEN
    Object value = 0.0;
    // WHEN
    AbstractDoubleAssert<?> result = DOUBLE.createAssert(value);
    // THEN
    result.isZero();
  }

  @Test
  void double_array_factory_createAssert_should_create_double_array_assertions() {
    // GIVEN
    Object value = new double[] { 0.0, 1.0 };
    // WHEN
    AbstractDoubleArrayAssert<?> result = DOUBLE_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0.0, 1.0);
  }

  @Test
  void double_2d_array_factory_createAssert_should_create_double_2d_array_assertions() {
    // GIVEN
    Object value = new double[][] { { 0.0, 1.0 }, { 2.0, 3.0 } };
    // WHEN
    Double2DArrayAssert result = DOUBLE_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void file_factory_createAssert_should_create_file_assertions() {
    // GIVEN
    Object value = new File("random-file-which-does-not-exist");
    // WHEN
    AbstractFileAssert<?> result = FILE.createAssert(value);
    // THEN
    result.doesNotExist();
  }

  @Test
  void future_factory_createAssert_should_create_future_assertions() {
    // GIVEN
    Object value = mock(Future.class);
    // WHEN
    FutureAssert<Object> result = FUTURE.createAssert(value);
    // THEN
    result.isNotDone();
  }

  @Test
  void future_typed_factory_createAssert_should_create_future_typed_assertions() {
    // GIVEN
    Object value = mock(Future.class);
    // WHEN
    FutureAssert<String> result = future(String.class).createAssert(value);
    // THEN
    result.isNotDone();
  }

  @Test
  void input_stream_factory_createAssert_should_create_input_stream_assertions() {
    // GIVEN
    Object value = new ByteArrayInputStream("stream".getBytes());
    // WHEN
    AbstractInputStreamAssert<?, ?> result = INPUT_STREAM.createAssert(value);
    // THEN
    result.hasContent("stream");
  }

  @Test
  void float_factory_createAssert_should_create_float_assertions() {
    // GIVEN
    Object value = 0.0f;
    // WHEN
    AbstractFloatAssert<?> result = FLOAT.createAssert(value);
    // THEN
    result.isZero();
  }

  @Test
  void float_array_factory_createAssert_should_create_float_array_assertions() {
    // GIVEN
    Object value = new float[] { 0.0f, 1.0f };
    // WHEN
    AbstractFloatArrayAssert<?> result = FLOAT_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0.0f, 1.0f);
  }

  @Test
  void float_2d_array_factory_createAssert_should_create_float_2d_array_assertions() {
    // GIVEN
    Object value = new float[][] { { 0.0f, 1.0f }, { 2.0f, 3.0f } };
    // WHEN
    Float2DArrayAssert result = FLOAT_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void integer_factory_createAssert_should_create_integer_assertions() {
    // GIVEN
    Object value = 0;
    // WHEN
    AbstractIntegerAssert<?> result = INTEGER.createAssert(value);
    // THEN
    result.isZero();
  }

  @Test
  void int_array_factory_createAssert_should_create_int_array_assertions() {
    // GIVEN
    Object value = new int[] { 0, 1 };
    // WHEN
    AbstractIntArrayAssert<?> result = INT_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void int_2d_array_factory_createAssert_should_create_int_2d_array_assertions() {
    // GIVEN
    Object value = new int[][] { { 0, 1 }, { 2, 3 } };
    // WHEN
    Int2DArrayAssert result = INT_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void long_factory_createAssert_should_create_long_assertions() {
    // GIVEN
    Object value = 0L;
    // WHEN
    AbstractLongAssert<?> result = LONG.createAssert(value);
    // THEN
    result.isZero();
  }

  @Test
  void long_array_factory_createAssert_should_create_long_array_assertions() {
    // GIVEN
    Object value = new long[] { 0L, 1L };
    // WHEN
    AbstractLongArrayAssert<?> result = LONG_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void long_2d_array_factory_createAssert_should_create_long_2d_array_assertions() {
    // GIVEN
    Object value = new long[][] { { 0L, 1L }, { 2L, 3L } };
    // WHEN
    Long2DArrayAssert result = LONG_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void type_factory_createAssert_should_create_object_typed_assertions() {
    // GIVEN
    Object value = "string";
    // WHEN
    ObjectAssert<String> result = type(String.class).createAssert(value);
    // THEN
    result.extracting(String::isEmpty).isEqualTo(false);
  }

  @Test
  void array_factory_createAssert_should_create_array_assertions() {
    // GIVEN
    Object value = new Object[] { 0, "" };
    // WHEN
    ObjectArrayAssert<Object> result = ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0, "");
  }

  @Test
  void array_typed_factory_createAssert_should_create_array_typed_assertions() {
    // GIVEN
    Object value = new Integer[] { 0, 1 };
    // WHEN
    ObjectArrayAssert<Integer> result = array(Integer[].class).createAssert(value);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void array_2d_factory_createAssert_should_create_2d_array_assertions() {
    // GIVEN
    Object value = new Object[][] { { 0, "" }, { 3.0, 'b' } };
    // WHEN
    Object2DArrayAssert<Object> result = ARRAY_2D.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void array_2d_typed_factory_createAssert_should_create_2d_array_typed_assertions() {
    // GIVEN
    Object value = new Integer[][] { { 0, 1 }, { 2, 3 } };
    // WHEN
    Object2DArrayAssert<Integer> result = array2D(Integer[][].class).createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void short_factory_createAssert_should_create_short_assertions() {
    // GIVEN
    Object value = (short) 0;
    // WHEN
    AbstractShortAssert<?> result = SHORT.createAssert(value);
    // THEN
    result.isZero();
  }

  @Test
  void short_array_factory_createAssert_should_create_short_array_assertions() {
    // GIVEN
    Object value = new short[] { 0, 1 };
    // WHEN
    AbstractShortArrayAssert<?> result = SHORT_ARRAY.createAssert(value);
    // THEN
    result.containsExactly((short) 0, (short) 1);
  }

  @Test
  void short_2d_array_factory_createAssert_should_create_short_2d_array_assertions() {
    // GIVEN
    Object value = new short[][] { { 0, 1 }, { 2, 3 } };
    // WHEN
    Short2DArrayAssert result = SHORT_2D_ARRAY.createAssert(value);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void date_factory_createAssert_should_create_date_assertions() {
    // GIVEN
    Object value = new Date();
    // WHEN
    AbstractDateAssert<?> result = DATE.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(new Date());
  }

  @Test
  void temporal_factory_createAssert_should_create_temporal_assertions() {
    // GIVEN
    Object value = ZonedDateTime.now();
    // WHEN
    TemporalAssert result = TEMPORAL.createAssert(value);
    // THEN
    result.isCloseTo(ZonedDateTime.now(), within(10, ChronoUnit.SECONDS));
  }

  @Test
  void zoned_date_time_factory_createAssert_should_create_zoned_date_time_assertions() {
    // GIVEN
    Object value = ZonedDateTime.now();
    // WHEN
    AbstractZonedDateTimeAssert<?> result = ZONED_DATE_TIME.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(ZonedDateTime.now());
  }

  @Test
  void local_date_time_factory_createAssert_should_create_local_date_time_assertions() {
    // GIVEN
    Object value = LocalDateTime.now();
    // WHEN
    AbstractLocalDateTimeAssert<?> result = LOCAL_DATE_TIME.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(LocalDateTime.now());
  }

  @Test
  void offset_date_time_factory_createAssert_should_create_offset_date_time_assertions() {
    // GIVEN
    Object value = OffsetDateTime.now();
    // WHEN
    AbstractOffsetDateTimeAssert<?> result = OFFSET_DATE_TIME.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  void offset_time_factory_createAssert_should_create_offset_time_assertions() {
    // GIVEN
    Object value = OffsetTime.now();
    // WHEN
    AbstractOffsetTimeAssert<?> result = OFFSET_TIME.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(OffsetTime.now());
  }

  @Test
  void local_time_factory_createAssert_should_create_local_time_assertions() {
    // GIVEN
    Object value = LocalTime.now();
    // WHEN
    AbstractLocalTimeAssert<?> result = LOCAL_TIME.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(LocalTime.now());
  }

  @Test
  void local_date_factory_createAssert_should_create_local_date_assertions() {
    // GIVEN
    Object value = LocalDate.now();
    // WHEN
    AbstractLocalDateAssert<?> result = LOCAL_DATE.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(LocalDate.now());
  }

  @Test
  void year_month_factory_createAssert_should_create_year_month_assertions() {
    // GIVEN
    Object value = YearMonth.now();
    // WHEN
    AbstractYearMonthAssert<?> result = YEAR_MONTH.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(YearMonth.now());
  }

  @Test
  void instant_factory_createAssert_should_create_instant_assertions() {
    // GIVEN
    Object value = Instant.now();
    // WHEN
    AbstractInstantAssert<?> result = INSTANT.createAssert(value);
    // THEN
    result.isBeforeOrEqualTo(Instant.now());
  }

  @Test
  void duration_factory_createAssert_should_create_duration_assertions() {
    // GIVEN
    Object value = Duration.ofHours(10);
    // WHEN
    AbstractDurationAssert<?> result = DURATION.createAssert(value);
    // THEN
    result.isPositive();
  }

  @Test
  void period_factory_createAssert_should_create_period_assertions() {
    // GIVEN
    Object value = Period.of(1, 1, 1);
    // WHEN
    AbstractPeriodAssert<?> result = PERIOD.createAssert(value);
    // THEN
    result.hasDays(1);
  }

  @Test
  void atomic_boolean_factory_createAssert_should_create_atomic_boolean_assertions() {
    // GIVEN
    Object value = new AtomicBoolean();
    // WHEN
    AtomicBooleanAssert result = ATOMIC_BOOLEAN.createAssert(value);
    // THEN
    result.isFalse();
  }

  @Test
  void atomic_integer_factory_createAssert_should_create_atomic_integer_assertions() {
    // GIVEN
    Object value = new AtomicInteger();
    // WHEN
    AtomicIntegerAssert result = ATOMIC_INTEGER.createAssert(value);
    // THEN
    result.hasValue(0);
  }

  @Test
  void atomic_integer_array_factory_createAssert_should_create_atomic_integer_array_assertions() {
    // GIVEN
    Object value = new AtomicIntegerArray(new int[] { 0, 1 });
    // WHEN
    AtomicIntegerArrayAssert result = ATOMIC_INTEGER_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void atomic_integer_field_updater_factory_createAssert_should_create_atomic_integer_field_updater_assertions() {
    // GIVEN
    Object value = AtomicIntegerFieldUpdater.newUpdater(VolatileFieldContainer.class, "intField");
    // WHEN
    AtomicIntegerFieldUpdaterAssert<Object> result = ATOMIC_INTEGER_FIELD_UPDATER.createAssert(value);
    // THEN
    result.hasValue(0, new VolatileFieldContainer());
  }

  @Test
  void atomic_integer_field_updater_typed_factory_createAssert_should_create_atomic_integer_field_updater_typed_assertions() {
    // GIVEN
    Object value = AtomicIntegerFieldUpdater.newUpdater(VolatileFieldContainer.class, "intField");
    // WHEN
    AtomicIntegerFieldUpdaterAssert<VolatileFieldContainer> result = atomicIntegerFieldUpdater(VolatileFieldContainer.class).createAssert(value);
    // THEN
    result.hasValue(0, new VolatileFieldContainer());
  }

  @Test
  void long_adder_factory_createAssert_should_create_long_adder_assertions() {
    // GIVEN
    Object value = new LongAdder();
    // WHEN
    LongAdderAssert result = LONG_ADDER.createAssert(value);
    // THEN
    result.hasValue(0L);
  }

  @Test
  void atomic_long_factory_createAssert_should_create_atomic_long_assertions() {
    // GIVEN
    Object value = new AtomicLong();
    // WHEN
    AtomicLongAssert result = ATOMIC_LONG.createAssert(value);
    // THEN
    result.hasValue(0L);
  }

  @Test
  void atomic_long_array_factory_createAssert_should_create_atomic_long_array_assertions() {
    // GIVEN
    Object value = new AtomicLongArray(new long[] { 0L, 1L });
    // WHEN
    AtomicLongArrayAssert result = ATOMIC_LONG_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0L, 1L);
  }

  @Test
  void atomic_long_field_updater_factory_createAssert_should_create_atomic_long_field_updater_assertions() {
    // GIVEN
    Object value = AtomicLongFieldUpdater.newUpdater(VolatileFieldContainer.class, "longField");
    // WHEN
    AtomicLongFieldUpdaterAssert<Object> result = ATOMIC_LONG_FIELD_UPDATER.createAssert(value);
    // THEN
    result.hasValue(0L, new VolatileFieldContainer());
  }

  @Test
  void atomic_long_field_updater_typed_factory_createAssert_should_create_atomic_long_field_updater_typed_assertions() {
    // GIVEN
    Object value = AtomicLongFieldUpdater.newUpdater(VolatileFieldContainer.class, "longField");
    // WHEN
    AtomicLongFieldUpdaterAssert<VolatileFieldContainer> result = atomicLongFieldUpdater(VolatileFieldContainer.class).createAssert(value);
    // THEN
    result.hasValue(0L, new VolatileFieldContainer());
  }

  @Test
  void atomic_reference_factory_createAssert_should_create_atomic_reference_assertions() {
    // GIVEN
    Object value = new AtomicReference<>();
    // WHEN
    AtomicReferenceAssert<Object> result = ATOMIC_REFERENCE.createAssert(value);
    // THEN
    result.hasValue(null);
  }

  @Test
  void atomic_reference_typed_factory_createAssert_should_create_atomic_reference_typed_assertions() {
    // GIVEN
    Object value = new AtomicReference<>(0);
    // WHEN
    AtomicReferenceAssert<Integer> result = atomicReference(Integer.class).createAssert(value);
    // THEN
    result.hasValue(0);
  }

  @Test
  void atomic_reference_array_factory_createAssert_should_create_atomic_reference_array_assertions() {
    // GIVEN
    Object value = new AtomicReferenceArray<>(new Object[] { 0, "" });
    // WHEN
    AtomicReferenceArrayAssert<Object> result = ATOMIC_REFERENCE_ARRAY.createAssert(value);
    // THEN
    result.containsExactly(0, "");
  }

  @Test
  void atomic_reference_array_typed_factory_createAssert_should_create_atomic_reference_array_typed_assertions() {
    // GIVEN
    Object value = new AtomicReferenceArray<>(new Integer[] { 0, 1 });
    // WHEN
    AtomicReferenceArrayAssert<Integer> result = atomicReferenceArray(Integer.class).createAssert(value);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void atomic_reference_field_updater_factory_createAssert_should_create_atomic_reference_field_updater_assertions() {
    // GIVEN
    Object value = AtomicReferenceFieldUpdater.newUpdater(VolatileFieldContainer.class, String.class, "stringField");
    // WHEN
    AtomicReferenceFieldUpdaterAssert<Object, Object> result = ATOMIC_REFERENCE_FIELD_UPDATER.createAssert(value);
    // THEN
    result.hasValue(null, new VolatileFieldContainer());
  }

  @Test
  void atomic_reference_field_updater_typed_factory_createAssert_should_create_atomic_reference_field_updater_typed_assertions() {
    // GIVEN
    Object value = AtomicReferenceFieldUpdater.newUpdater(VolatileFieldContainer.class, String.class, "stringField");
    // WHEN
    AtomicReferenceFieldUpdaterAssert<String, VolatileFieldContainer> result = atomicReferenceFieldUpdater(String.class,
                                                                                                           VolatileFieldContainer.class).createAssert(value);
    // THEN
    result.hasValue(null, new VolatileFieldContainer());
  }

  @Test
  void atomic_markable_reference_factory_createAssert_should_create_atomic_markable_reference_assertions() {
    // GIVEN
    Object value = new AtomicMarkableReference<>(null, false);
    // WHEN
    AtomicMarkableReferenceAssert<Object> result = ATOMIC_MARKABLE_REFERENCE.createAssert(value);
    // THEN
    result.hasReference(null);
  }

  @Test
  void atomic_markable_reference_typed_factory_createAssert_should_create_atomic_markable_reference_typed_assertions() {
    // GIVEN
    Object value = new AtomicMarkableReference<>(0, false);
    // WHEN
    AtomicMarkableReferenceAssert<Integer> result = atomicMarkableReference(Integer.class).createAssert(value);
    // THEN
    result.hasReference(0);
  }

  @Test
  void atomic_stamped_reference_factory_createAssert_should_create_atomic_stamped_reference_assertions() {
    // GIVEN
    Object value = new AtomicStampedReference<>(null, 0);
    // WHEN
    AtomicStampedReferenceAssert<Object> result = ATOMIC_STAMPED_REFERENCE.createAssert(value);
    // THEN
    result.hasReference(null);
  }

  @Test
  void atomic_stamped_reference_typed_factory_createAssert_should_create_atomic_stamped_reference_typed_assertions() {
    // GIVEN
    Object value = new AtomicStampedReference<>(0, 0);
    // WHEN
    AtomicStampedReferenceAssert<Integer> result = atomicStampedReference(Integer.class).createAssert(value);
    // THEN
    result.hasReference(0);
  }

  @Test
  void throwable_factory_createAssert_should_create_throwable_assertions() {
    // GIVEN
    Object value = new RuntimeException("message");
    // WHEN
    AbstractThrowableAssert<?, ? extends Throwable> result = THROWABLE.createAssert(value);
    // THEN
    result.hasMessage("message");
  }

  @Test
  void throwable_typed_factory_createAssert_should_create_throwable_typed_assertions() {
    // GIVEN
    Object value = new RuntimeException("message");
    // WHEN
    AbstractThrowableAssert<?, RuntimeException> result = throwable(RuntimeException.class).createAssert(value);
    // THEN
    result.hasMessage("message");
  }

  @Test
  void char_sequence_factory_createAssert_should_create_char_sequence_assertions() {
    // GIVEN
    Object value = "string";
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = CHAR_SEQUENCE.createAssert(value);
    // THEN
    result.startsWith("str");
  }

  @Test
  void string_builder_factory_createAssert_should_create_char_sequence_assertions() {
    // GIVEN
    Object value = new StringBuilder("string");
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = STRING_BUILDER.createAssert(value);
    // THEN
    result.startsWith("str");
  }

  @Test
  void string_buffer_factory_createAssert_should_create_char_sequence_assertions() {
    // GIVEN
    Object value = new StringBuffer("string");
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = STRING_BUFFER.createAssert(value);
    // THEN
    result.startsWith("str");
  }

  @Test
  void string_factory_createAssert_should_create_string_assertions() {
    // GIVEN
    Object value = "string";
    // WHEN
    AbstractStringAssert<?> result = STRING.createAssert(value);
    // THEN
    result.startsWith("str");
  }

  @Test
  void iterable_factory_createAssert_should_create_iterable_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    IterableAssert<Object> result = ITERABLE.createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void iterable_typed_factory_createAssert_should_create_iterable_typed_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    IterableAssert<String> result = iterable(String.class).createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void iterator_factory_createAssert_should_create_iterator_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie").iterator();
    // WHEN
    IteratorAssert<Object> result = ITERATOR.createAssert(value);
    // THEN
    result.hasNext();
  }

  @Test
  void iterator_typed_factory_createAssert_should_create_iterator_typed_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie").iterator();
    // WHEN
    IteratorAssert<String> result = iterator(String.class).createAssert(value);
    // THEN
    result.hasNext();
  }

  @Test
  void collection_factory_createAssert_should_create_collection_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>> result = COLLECTION.createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void collection_typed_factory_createAssert_should_create_collection_typed_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    AbstractCollectionAssert<?, Collection<? extends String>, String, ObjectAssert<String>> result = collection(String.class).createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void set_factory_createAssert_should_create_collection_assertions() {
    // GIVEN
    Object value = Sets.set("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>> result = SET.createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void set_typed_factory_createAssert_should_create_collection_typed_assertions() {
    // GIVEN
    Object value = Sets.set("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    AbstractCollectionAssert<?, Collection<? extends String>, String, ObjectAssert<String>> result = set(String.class).createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void list_factory_createAssert_should_create_list_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    ListAssert<Object> result = LIST.createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void list_typed_factory_createAssert_should_create_typed_list_assertions() {
    // GIVEN
    Object value = Lists.list("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    ListAssert<String> result = list(String.class).createAssert(value);
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void stream_factory_createAssert_should_create_list_assertions() {
    // GIVEN
    Object value = Stream.of(1, 2, 3);
    // WHEN
    ListAssert<Object> result = STREAM.createAssert(value);
    // THEN
    result.containsExactly(1, 2, 3);
  }

  @Test
  void stream_typed_factory_createAssert_should_create_typed_list_typed_assertions() {
    // GIVEN
    Object value = Stream.of(1, 2, 3);
    // WHEN
    ListAssert<Integer> result = stream(Integer.class).createAssert(value);
    // THEN
    result.containsExactly(1, 2, 3);
  }

  @Test
  void double_stream_factory_createAssert_should_create_double_list_assertions() {
    // GIVEN
    Object value = DoubleStream.of(1.0, 2.0, 3.0);
    // WHEN
    ListAssert<Double> result = DOUBLE_STREAM.createAssert(value);
    // THEN
    result.containsExactly(1.0, 2.0, 3.0);
  }

  @Test
  void long_stream_factory_createAssert_should_create_long_list_assertions() {
    // GIVEN
    Object value = LongStream.of(1L, 2L, 3L);
    // WHEN
    ListAssert<Long> result = LONG_STREAM.createAssert(value);
    // THEN
    result.containsExactly(1L, 2L, 3L);
  }

  @Test
  void int_stream_factory_createAssert_should_create_int_list_assertions() {
    // GIVEN
    Object value = IntStream.of(1, 2, 3);
    // WHEN
    ListAssert<Integer> result = INT_STREAM.createAssert(value);
    // THEN
    result.containsExactly(1, 2, 3);
  }

  @Test
  void path_factory_createAssert_should_create_path_assertions() {
    // GIVEN
    Object value = Paths.get("random-file-which-does-not-exist");
    // WHEN
    AbstractPathAssert<?> result = PATH.createAssert(value);
    // THEN
    result.doesNotExist();
  }

  @Test
  void spliterator_factory_createAssert_should_create_spliterator_assertions() {
    // GIVEN
    Object value = Stream.of(1, 2).spliterator();
    // WHEN
    SpliteratorAssert<Object> result = SPLITERATOR.createAssert(value);
    // THEN
    result.hasCharacteristics(Spliterator.SIZED);
  }

  @Test
  void map_factory_createAssert_should_create_map_assertions() {
    // GIVEN
    Object value = mapOf(entry("key", "value"));
    // WHEN
    MapAssert<Object, Object> result = MAP.createAssert(value);
    // THEN
    result.containsExactly(entry("key", "value"));
  }

  @Test
  void map_typed_factory_createAssert_should_create_map_typed_assertions() {
    // GIVEN
    Object value = mapOf(entry("key", "value"));
    // WHEN
    MapAssert<String, String> result = map(String.class, String.class).createAssert(value);
    // THEN
    result.containsExactly(entry("key", "value"));
  }

  @Test
  void comparable_factory_createAssert_should_create_comparable_assertions() {
    // GIVEN
    Object value = 0;
    // WHEN
    AbstractComparableAssert<?, Integer> result = comparable(Integer.class).createAssert(value);
    // THEN
    result.isEqualByComparingTo(0);
  }

  @SuppressWarnings("unused")
  private static class VolatileFieldContainer {

    volatile int intField;
    volatile long longField;
    volatile String stringField;

  }

}
