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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_BOOLEAN;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_INTEGER_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.ATOMIC_INTEGER_FIELD_UPDATER;
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
import static org.assertj.core.api.InstanceOfAssertFactories.STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING_BUFFER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING_BUILDER;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.api.InstanceOfAssertFactories.URI_TYPE;
import static org.assertj.core.api.InstanceOfAssertFactories.URL_TYPE;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.assertj.core.util.Sets.newHashSet;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
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

import org.assertj.core.data.TolkienCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SoftAssertions_combined_with_asInstanceOf_Test extends BaseAssertionsTest {

  private SoftAssertions softly;

  @BeforeEach
  public void setup() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
    softly = new SoftAssertions();
  }

  @Test
  public void soft_assertions_should_work() {
    // GIVEN
    Object value = "abc";
    // WHEN
    softly.assertThat(value)
          .as("startsWith")
          .asInstanceOf(STRING)
          .startsWith("b");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("[startsWith]");
  }

  @ParameterizedTest(name = "with {1}")
  @MethodSource("should_work_with_any_InstanceOfFactory_source")
  public void should_work_with_any_InstanceOfFactory(Object actual, InstanceOfAssertFactory<?, ?> instanceOfAssertFactory) {
    softly.assertThat(actual).asInstanceOf(instanceOfAssertFactory);
  }

  public static Stream<Arguments> should_work_with_any_InstanceOfFactory_source() throws MalformedURLException {
    Future<String> future = completedFuture("foo");
    CompletionStage<String> completionStage = completedFuture("foo");
    CharSequence charSequence = new StringBuilder();
    return Stream.of(arguments(new Object[0], ARRAY),
                     arguments(new AtomicBoolean(true), ATOMIC_BOOLEAN),
                     arguments(new AtomicInteger(1), ATOMIC_INTEGER),
                     arguments(new AtomicIntegerArray(5), ATOMIC_INTEGER_ARRAY),
                     arguments(AtomicIntegerFieldUpdater.newUpdater(Data.class, "intField"), ATOMIC_INTEGER_FIELD_UPDATER),
                     arguments(new AtomicLong(5l), InstanceOfAssertFactories.ATOMIC_LONG),
                     arguments(new AtomicLongArray(5), InstanceOfAssertFactories.ATOMIC_LONG_ARRAY),
                     arguments(AtomicLongFieldUpdater.newUpdater(Data.class, "longField"), ATOMIC_LONG_FIELD_UPDATER),
                     arguments(new AtomicMarkableReference<>("", false), ATOMIC_MARKABLE_REFERENCE),
                     arguments(new AtomicReference<>("abc"), ATOMIC_REFERENCE),
                     arguments(new AtomicReferenceArray<>(3), ATOMIC_REFERENCE_ARRAY),
                     arguments(newUpdater(Data.class, String.class, "stringField"), ATOMIC_REFERENCE_FIELD_UPDATER),
                     arguments(new AtomicStampedReference<>(0, 0), ATOMIC_STAMPED_REFERENCE),
                     arguments(BigDecimal.ONE, BIG_DECIMAL),
                     arguments(BigInteger.ONE, BIG_INTEGER),
                     arguments(true, BOOLEAN),
                     arguments(new boolean[0], BOOLEAN_ARRAY),
                     arguments(new boolean[0][0], BOOLEAN_2D_ARRAY),
                     arguments((byte) 1, BYTE),
                     arguments(Byte.valueOf("1"), BYTE),
                     arguments(new byte[0], BYTE_ARRAY),
                     arguments(new byte[0][0], BYTE_2D_ARRAY),
                     arguments(new char[0], CHAR_ARRAY),
                     arguments(new char[0][0], CHAR_2D_ARRAY),
                     arguments(charSequence, CHAR_SEQUENCE),
                     arguments('a', CHARACTER),
                     arguments(Character.valueOf('a'), CHARACTER),
                     arguments(TolkienCharacter.class, CLASS),
                     arguments(completedFuture("foo"), COMPLETABLE_FUTURE),
                     arguments(completionStage, COMPLETION_STAGE),
                     arguments(new Date(), DATE),
                     arguments(0d, DOUBLE),
                     arguments(Double.valueOf(0d), DOUBLE),
                     arguments(new double[0], DOUBLE_ARRAY),
                     arguments(new double[0][0], DOUBLE_2D_ARRAY),
                     arguments((DoublePredicate) d -> d == 0.0, DOUBLE_PREDICATE),
                     arguments(DoubleStream.empty(), DOUBLE_STREAM),
                     arguments(Duration.ZERO, DURATION),
                     arguments(Period.ZERO, PERIOD),
                     arguments(new File("foo"), FILE),
                     arguments(Float.valueOf("0.0"), FLOAT),
                     arguments(new float[0], FLOAT_ARRAY),
                     arguments(new float[0][0], FLOAT_2D_ARRAY),
                     arguments(future, FUTURE),
                     arguments(new ByteArrayInputStream("stream".getBytes()), INPUT_STREAM),
                     arguments(Instant.now(), INSTANT),
                     arguments(new int[0], INT_ARRAY),
                     arguments(new int[0][0], INT_2D_ARRAY),
                     arguments((IntPredicate) i -> i == 0, INT_PREDICATE),
                     arguments(IntStream.empty(), INT_STREAM),
                     arguments(1, INTEGER),
                     arguments(newHashSet(), ITERABLE),
                     arguments(list("foo").iterator(), ITERATOR),
                     arguments(list("foo"), LIST),
                     arguments(LocalDate.now(), LOCAL_DATE),
                     arguments(LocalDateTime.now(), LOCAL_DATE_TIME),
                     arguments(5L, LONG),
                     arguments(new LongAdder(), LONG_ADDER),
                     arguments(new long[0], LONG_ARRAY),
                     arguments(new long[0][0], LONG_2D_ARRAY),
                     arguments((LongPredicate) l -> l == 0, LONG_PREDICATE),
                     arguments(LongStream.empty(), LONG_STREAM),
                     arguments(newHashMap("k", "v"), MAP),
                     arguments(OffsetDateTime.now(), OFFSET_DATE_TIME),
                     arguments(OffsetTime.now(), OFFSET_TIME),
                     arguments(Optional.empty(), OPTIONAL),
                     arguments(OptionalDouble.empty(), OPTIONAL_DOUBLE),
                     arguments(OptionalInt.empty(), OPTIONAL_INT),
                     arguments(OptionalLong.empty(), OPTIONAL_LONG),
                     arguments(Paths.get("."), PATH),
                     arguments((Predicate<String>) s -> s.isEmpty(), PREDICATE),
                     arguments(Short.MIN_VALUE, SHORT),
                     arguments(new short[0], SHORT_ARRAY),
                     arguments(new short[0][0], SHORT_2D_ARRAY),
                     arguments(Stream.empty(), STREAM),
                     arguments("foo", STRING),
                     arguments(new StringBuffer(), STRING_BUFFER),
                     arguments(new StringBuilder(), STRING_BUILDER),
                     arguments(new Exception(), THROWABLE),
                     arguments(URI.create("http://localhost"), URI_TYPE),
                     arguments(URI.create("http://localhost").toURL(), URL_TYPE),
                     arguments(ZonedDateTime.now(), InstanceOfAssertFactories.ZONED_DATE_TIME));
  }

  static class Data {
    volatile int intField;
    volatile long longField;
    volatile String stringField;
  }

}
