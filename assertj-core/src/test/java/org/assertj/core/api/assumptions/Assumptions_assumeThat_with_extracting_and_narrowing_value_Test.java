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
package org.assertj.core.api.assumptions;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssertions.as;
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
import static org.assertj.core.api.InstanceOfAssertFactories.OFFSET_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.OFFSET_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL_DOUBLE;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL_INT;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL_LONG;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;
import static org.assertj.core.api.InstanceOfAssertFactories.PERIOD;
import static org.assertj.core.api.InstanceOfAssertFactories.PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.SHORT;
import static org.assertj.core.api.InstanceOfAssertFactories.SHORT_2D_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.SHORT_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.SPLITERATOR;
import static org.assertj.core.api.InstanceOfAssertFactories.STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING_BUFFER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING_BUILDER;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.api.InstanceOfAssertFactories.URI_TYPE;
import static org.assertj.core.api.InstanceOfAssertFactories.URL_TYPE;
import static org.assertj.core.api.InstanceOfAssertFactories.ZONED_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionNotMetException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
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
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

class Assumptions_assumeThat_with_extracting_and_narrowing_value_Test {

  private TestData data = new TestData();

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_array() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::array, as(ARRAY)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_array2d() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::array2D, as(ARRAY_2D)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicBoolean() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicBoolean, as(ATOMIC_BOOLEAN)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicInteger() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicInteger, as(ATOMIC_INTEGER)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicIntegerArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicIntegerArray, as(ATOMIC_INTEGER_ARRAY))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicIntegerFieldUpdater() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicIntegerFieldUpdater,
                                                                      as(ATOMIC_INTEGER_FIELD_UPDATER))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicLong() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicLong, as(ATOMIC_LONG)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicLongArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicLongArray, as(ATOMIC_LONG_ARRAY))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicLongFieldUpdater() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicLongFieldUpdater,
                                                                      as(ATOMIC_LONG_FIELD_UPDATER))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicMarkableReference() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicMarkableReference,
                                                                      as(ATOMIC_MARKABLE_REFERENCE))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicReference() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicReference, as(ATOMIC_REFERENCE))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicReferenceArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicReferenceArray, as(ATOMIC_REFERENCE_ARRAY))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicReferenceFieldUpdater() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicReferenceFieldUpdater,
                                                                      as(ATOMIC_REFERENCE_FIELD_UPDATER))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_AtomicStampedReference() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::atomicStampedReference,
                                                                      as(ATOMIC_STAMPED_REFERENCE))
                                                          .isNull());
  }

  // https://github.com/assertj/assertj/issues/2349
  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_BigDecimal() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::bigDecimal, as(BIG_DECIMAL)).isZero());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_BigInteger() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::bigInteger, as(BIG_INTEGER)).isZero());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_boolean() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::booleanPrimitive, as(BOOLEAN)).isFalse());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Boolean() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getBoolean, as(BOOLEAN)).isFalse());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Boolean2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::boolean2DArray, as(BOOLEAN_2D_ARRAY))
                                                          .isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_BooleanArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::booleanArray, as(BOOLEAN_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_byte() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::bytePrimitive, as(BYTE)).isZero());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Byte() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getByte, as(BYTE)).isZero());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Byte2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::byte2DArray, as(BYTE_2D_ARRAY))
                                                          .isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_ByteArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::byteArray, as(BYTE_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_char() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getChar, as(CHARACTER)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Character() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::character, as(CHARACTER)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Char2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::char2DArray, as(CHAR_2D_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_CharArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::charArray, as(CHAR_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_CharSequence() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::charSequence, as(CHAR_SEQUENCE)).isBlank());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Class() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getClass, as(CLASS)).isFinal());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Collection() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::collection, as(COLLECTION)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_CompletableFuture() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::completableFuture, as(COMPLETABLE_FUTURE))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Future() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::future, as(FUTURE)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_CompletionStage() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::completionStage, as(COMPLETION_STAGE))
                                                          .isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Date() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::date, as(DATE)).isInTheFuture());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Double() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getDouble, as(DOUBLE)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_double() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::doublePrimitive, as(DOUBLE)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Double2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::double2DArray, as(DOUBLE_2D_ARRAY))
                                                          .isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_DoubleArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::doubleArray, as(DOUBLE_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_DoublePredicate() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::doublePredicate, as(DOUBLE_PREDICATE))
                                                          .accepts(1));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_DoubleStream() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::doubleStream, as(DOUBLE_STREAM))
                                                          .isSorted());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Duration() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::duration, as(DURATION)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_File() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::file, as(FILE)).isDirectory());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Float() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getFloat, as(FLOAT)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_float() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::floatPrimitive, as(FLOAT)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Float2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::float2DArray, as(FLOAT_2D_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_FloatArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::floatArray, as(FLOAT_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_InputStream() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::inputStream, as(INPUT_STREAM)).hasContent("foo"));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_Instant() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::instant, as(INSTANT)).isAfter(Instant.MAX));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_int() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getInt, as(INTEGER)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_Integer() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::integer, as(INTEGER)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Integer2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::int2DArray, as(INT_2D_ARRAY))
                                                          .isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_int_array() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::intArray, as(INT_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_IntegerPredicate() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::intPredicate, as(INT_PREDICATE)).accepts(1));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_IntegerStream() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::intStream, as(INT_STREAM)).isSorted());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_iterable() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::iterable, as(ITERABLE)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_iterator() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::iterator, as(ITERATOR)).hasNext());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_list() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::list, as(LIST)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_list_of_String() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::stringList, as(list(String.class))).isEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_localDate() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::localDate, as(LOCAL_DATE))
                                                          .isAfter(LocalDate.MAX));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_localDateTime() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::localDateTime, as(LOCAL_DATE_TIME))
                                                          .isAfter(LocalDateTime.MAX));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_localTime() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::localTime, as(LOCAL_TIME))
                                                          .isAfter(LocalTime.MAX));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_long() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::longPrimitive, as(LONG)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Long() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getLong, as(LONG)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Long2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::long2DArray, as(LONG_2D_ARRAY))
                                                          .isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_LongAdder() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::longAdder, as(LONG_ADDER)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_LongArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::longArray, as(LONG_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_LongPredicate() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::longPredicate, as(LONG_PREDICATE)).accepts(1));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_LongStream() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::longStream, as(LONG_STREAM)).isSorted());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Map() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::map, as(MAP)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_offsetDateTime() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::offsetDateTime, as(OFFSET_DATE_TIME))
                                                          .isAfter(OffsetDateTime.MAX));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_offsetTime() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::offsetTime, as(OFFSET_TIME))
                                                          .isAfter(OffsetTime.MAX));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_optional() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::optional, as(OPTIONAL)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_optionalDouble() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::optionalDouble, as(OPTIONAL_DOUBLE))
                                                          .isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_optionalInt() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::optionalInt, as(OPTIONAL_INT)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_an_optionalLong() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::optionalLong, as(OPTIONAL_LONG)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_path() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::path, as(PATH)).isDirectory());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_period() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::period, as(PERIOD)).hasYears(2000));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_predicate() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::predicate, as(PREDICATE)).accepts(123));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_short() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::shortPrimitive, as(SHORT)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Short() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getShort, as(SHORT)).isNegative());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Short2DArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::short2DArray, as(SHORT_2D_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_ShortArray() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::shortArray, as(SHORT_ARRAY)).isNotEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Spliterator() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::spliterator, as(SPLITERATOR)).isNull());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Stream() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::stream, as(STREAM)).isEmpty());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_String() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::getString, as(STRING)).isBlank());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_StringBuffer() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::stringBuffer, as(STRING_BUFFER)).isBlank());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_StringBuilder() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::stringBuilder, as(STRING_BUILDER)).isBlank());
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_Throwable() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::throwable, as(THROWABLE)).hasMessage("foo"));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_URI() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::uri, as(URI_TYPE)).hasHost("google"));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_URL() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::url, as(URL_TYPE)).hasHost("google"));
  }

  @Test
  void should_ignore_test_for_failing_assumption_extracting_and_narrowing_a_ZonedDateTime() {
    expectAssumptionNotMetException(() -> assumeThat(data).extracting(TestData::zonedDateTime, as(ZONED_DATE_TIME))
                                                          .isAfter(ZonedDateTime.now()));
  }

  static class TestData {
    volatile int foo;
    volatile long fooLong;
    volatile TestData fooTestData;

    Object[] array() {
      return new Object[0];
    }

    Object[][] array2D() {
      return new Object[0][0];
    }

    AtomicBoolean atomicBoolean() {
      return new AtomicBoolean(true);
    }

    AtomicInteger atomicInteger() {
      return new AtomicInteger(1);
    }

    AtomicIntegerArray atomicIntegerArray() {
      return new AtomicIntegerArray(0);
    }

    AtomicIntegerFieldUpdater<?> atomicIntegerFieldUpdater() {
      return AtomicIntegerFieldUpdater.newUpdater(TestData.class, "foo");
    }

    AtomicLong atomicLong() {
      return new AtomicLong(1);
    }

    AtomicLongArray atomicLongArray() {
      return new AtomicLongArray(0);
    }

    AtomicLongFieldUpdater<?> atomicLongFieldUpdater() {
      return AtomicLongFieldUpdater.newUpdater(TestData.class, "fooLong");
    }

    AtomicMarkableReference<?> atomicMarkableReference() {
      return new AtomicMarkableReference<>("foo", true);
    }

    AtomicReference<?> atomicReference() {
      return new AtomicReference<>(1);
    }

    AtomicReferenceArray<?> atomicReferenceArray() {
      return new AtomicReferenceArray<>(0);
    }

    AtomicReferenceFieldUpdater<?, ?> atomicReferenceFieldUpdater() {
      return AtomicReferenceFieldUpdater.newUpdater(TestData.class, TestData.class, "fooTestData");
    }

    AtomicStampedReference<?> atomicStampedReference() {
      return new AtomicStampedReference<>("foo", 1);
    }

    BigDecimal bigDecimal() {
      return BigDecimal.ONE;
    }

    BigInteger bigInteger() {
      return BigInteger.ONE;
    }

    boolean booleanPrimitive() {
      return Boolean.TRUE;
    }

    Boolean getBoolean() {
      return Boolean.TRUE;
    }

    boolean[][] boolean2DArray() {
      return new boolean[0][];
    }

    boolean[] booleanArray() {
      return new boolean[0];
    }

    byte bytePrimitive() {
      return 1;
    }

    Byte getByte() {
      return 1;
    }

    byte[][] byte2DArray() {
      return new byte[0][];
    }

    byte[] byteArray() {
      return new byte[0];
    }

    char getChar() {
      return 'a';
    }

    Character character() {
      return 'a';
    }

    char[][] char2DArray() {
      return new char[0][];
    }

    char[] charArray() {
      return new char[0];
    }

    CharSequence charSequence() {
      return "foo";
    }

    Collection<?> collection() {
      return emptyList();
    }

    CompletableFuture<?> completableFuture() {
      return completedFuture("foo");
    }

    CompletionStage<?> completionStage() {
      return completedFuture("foo");
    }

    Date date() {
      return new Date(0);
    }

    double doublePrimitive() {
      return Double.MAX_VALUE;
    }

    Double getDouble() {
      return Double.MAX_VALUE;
    }

    double[][] double2DArray() {
      return new double[0][];
    }

    double[] doubleArray() {
      return new double[0];
    }

    DoublePredicate doublePredicate() {
      return d -> d == 0;
    }

    DoubleStream doubleStream() {
      return DoubleStream.of(1.0, 0.0);
    }

    Duration duration() {
      return Duration.ZERO;
    }

    File file() {
      return new File("foo.txt");
    }

    Float getFloat() {
      return Float.MAX_VALUE;
    }

    float floatPrimitive() {
      return Float.MAX_VALUE;
    }

    float[][] float2DArray() {
      return new float[0][];
    }

    float[] floatArray() {
      return new float[0];
    }

    Future<?> future() {
      return CompletableFuture.completedFuture("foo");
    }

    InputStream inputStream() {
      return new ByteArrayInputStream(new byte[0]);
    }

    Instant instant() {
      return Instant.EPOCH;
    }

    Integer integer() {
      return Integer.MAX_VALUE;
    }

    int getInt() {
      return 1;
    }

    int[][] int2DArray() {
      return new int[0][];
    }

    int[] intArray() {
      return new int[0];
    }

    IntPredicate intPredicate() {
      return i -> i == 0;
    }

    IntStream intStream() {
      return IntStream.of(1, 0);
    }

    Iterable<?> iterable() {
      return emptyList();
    }

    Iterator<?> iterator() {
      return emptyList().iterator();
    }

    List<?> list() {
      return emptyList();
    }

    List<String> stringList() {
      return Lists.list("foo");
    }

    LocalDate localDate() {
      return LocalDate.MIN;
    }

    LocalDateTime localDateTime() {
      return LocalDateTime.MIN;
    }

    LocalTime localTime() {
      return LocalTime.MIN;
    }

    long longPrimitive() {
      return Long.MAX_VALUE;
    }

    Long getLong() {
      return Long.MAX_VALUE;
    }

    long[][] long2DArray() {
      return new long[0][];
    }

    LongAdder longAdder() {
      return new LongAdder();
    }

    long[] longArray() {
      return new long[0];
    }

    LongPredicate longPredicate() {
      return d -> d == 0;
    }

    LongStream longStream() {
      return LongStream.of(1, 0);
    }

    Map<?, ?> map() {
      return newHashMap();
    }

    OffsetDateTime offsetDateTime() {
      return OffsetDateTime.MIN;
    }

    OffsetTime offsetTime() {
      return OffsetTime.MIN;
    }

    Optional<?> optional() {
      return Optional.empty();
    }

    OptionalDouble optionalDouble() {
      return OptionalDouble.empty();
    }

    OptionalInt optionalInt() {
      return OptionalInt.empty();
    }

    OptionalLong optionalLong() {
      return OptionalLong.empty();
    }

    Path path() {
      return Paths.get("src/test/resources/utf8.txt");
    }

    Period period() {
      return Period.ZERO;
    }

    Predicate<?> predicate() {
      return Predicate.isEqual("foo");
    }

    short shortPrimitive() {
      return Short.MAX_VALUE;
    }

    Short getShort() {
      return Short.MAX_VALUE;
    }

    short[][] short2DArray() {
      return new short[0][];
    }

    short[] shortArray() {
      return new short[0];
    }

    Spliterator<?> spliterator() {
      return Stream.of(1, 2).spliterator();
    }

    Stream<?> stream() {
      return Stream.of(1, 2);
    }

    String getString() {
      return "foo";
    }

    StringBuffer stringBuffer() {
      return new StringBuffer("foo");
    }

    StringBuilder stringBuilder() {
      return new StringBuilder("foo");
    }

    Throwable throwable() {
      return new Throwable("boom!");
    }

    URI uri() {
      try {
        return new URI("https://assertj.github.io/doc!");
      } catch (URISyntaxException e) {
        return null;
      }
    }

    URL url() {
      try {
        return new URL("https://assertj.github.io/doc!");
      } catch (MalformedURLException e) {
        return null;
      }
    }

    ZonedDateTime zonedDateTime() {
      return ZonedDateTime.now().minusDays(1);
    }

  }
}
