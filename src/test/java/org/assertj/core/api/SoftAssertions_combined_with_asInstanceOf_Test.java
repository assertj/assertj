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
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.BYTE;
import static org.assertj.core.api.InstanceOfAssertFactories.BYTE_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.CHARACTER;
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_SEQUENCE;
import static org.assertj.core.api.InstanceOfAssertFactories.CLASS;
import static org.assertj.core.api.InstanceOfAssertFactories.COMPLETABLE_FUTURE;
import static org.assertj.core.api.InstanceOfAssertFactories.COMPLETION_STAGE;
import static org.assertj.core.api.InstanceOfAssertFactories.DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE_PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE_STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.FILE;
import static org.assertj.core.api.InstanceOfAssertFactories.FLOAT;
import static org.assertj.core.api.InstanceOfAssertFactories.FLOAT_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.FUTURE;
import static org.assertj.core.api.InstanceOfAssertFactories.INPUT_STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.INSTANT;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_ARRAY;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.INT_STREAM;
import static org.assertj.core.api.InstanceOfAssertFactories.ITERABLE;
import static org.assertj.core.api.InstanceOfAssertFactories.ITERATOR;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
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
import static org.assertj.core.api.InstanceOfAssertFactories.PREDICATE;
import static org.assertj.core.api.InstanceOfAssertFactories.SHORT;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    return Stream.of(Arguments.of(new Object[0], ARRAY),
                     Arguments.of(new AtomicBoolean(true), ATOMIC_BOOLEAN),
                     Arguments.of(new AtomicInteger(1), ATOMIC_INTEGER),
                     Arguments.of(new AtomicIntegerArray(5), ATOMIC_INTEGER_ARRAY),
                     Arguments.of(AtomicIntegerFieldUpdater.newUpdater(Data.class, "intField"), ATOMIC_INTEGER_FIELD_UPDATER),
                     Arguments.of(new AtomicLong(5l), InstanceOfAssertFactories.ATOMIC_LONG),
                     Arguments.of(new AtomicLongArray(5), InstanceOfAssertFactories.ATOMIC_LONG_ARRAY),
                     Arguments.of(AtomicLongFieldUpdater.newUpdater(Data.class, "longField"), ATOMIC_LONG_FIELD_UPDATER),
                     Arguments.of(new AtomicMarkableReference<>("", false), ATOMIC_MARKABLE_REFERENCE),
                     Arguments.of(new AtomicReference<>("abc"), ATOMIC_REFERENCE),
                     Arguments.of(new AtomicReferenceArray<>(3), ATOMIC_REFERENCE_ARRAY),
                     Arguments.of(newUpdater(Data.class, String.class, "stringField"), ATOMIC_REFERENCE_FIELD_UPDATER),
                     Arguments.of(new AtomicStampedReference<>(0, 0), ATOMIC_STAMPED_REFERENCE),
                     Arguments.of(BigDecimal.ONE, BIG_DECIMAL),
                     Arguments.of(BigInteger.ONE, BIG_INTEGER),
                     Arguments.of(true, BOOLEAN),
                     Arguments.of(new boolean[0], BOOLEAN_ARRAY),
                     Arguments.of((byte) 1, BYTE),
                     Arguments.of(Byte.valueOf("1"), BYTE),
                     Arguments.of(new byte[0], BYTE_ARRAY),
                     Arguments.of(new char[0], CHAR_ARRAY),
                     Arguments.of(charSequence, CHAR_SEQUENCE),
                     Arguments.of('a', CHARACTER),
                     Arguments.of(Character.valueOf('a'), CHARACTER),
                     Arguments.of(TolkienCharacter.class, CLASS),
                     Arguments.of(completedFuture("foo"), COMPLETABLE_FUTURE),
                     Arguments.of(completionStage, COMPLETION_STAGE),
                     Arguments.of(new Date(), DATE),
                     Arguments.of(0d, DOUBLE),
                     Arguments.of(Double.valueOf(0d), DOUBLE),
                     Arguments.of(new double[0], DOUBLE_ARRAY),
                     Arguments.of((DoublePredicate) d -> d == 0.0, DOUBLE_PREDICATE),
                     Arguments.of(DoubleStream.empty(), DOUBLE_STREAM),
                     Arguments.of(new File("foo"), FILE),
                     Arguments.of(Float.valueOf("0.0"), FLOAT),
                     Arguments.of(new float[0], FLOAT_ARRAY),
                     Arguments.of(future, FUTURE),
                     Arguments.of(new ByteArrayInputStream("stream".getBytes()), INPUT_STREAM),
                     Arguments.of(Instant.now(), INSTANT),
                     Arguments.of(new int[0], INT_ARRAY),
                     Arguments.of((IntPredicate) i -> i == 0, INT_PREDICATE),
                     Arguments.of(IntStream.empty(), INT_STREAM),
                     Arguments.of(1, INTEGER),
                     Arguments.of(newHashSet(), ITERABLE),
                     Arguments.of(list("foo").iterator(), ITERATOR),
                     Arguments.of(list("foo"), LIST),
                     Arguments.of(LocalDate.now(), LOCAL_DATE),
                     Arguments.of(LocalDateTime.now(), LOCAL_DATE_TIME),
                     Arguments.of(5l, LONG),
                     Arguments.of(new long[0], LONG_ARRAY),
                     Arguments.of((LongPredicate) l -> l == 0, LONG_PREDICATE),
                     Arguments.of(LongStream.empty(), LONG_STREAM),
                     Arguments.of(newHashMap("k", "v"), MAP),
                     Arguments.of(OffsetDateTime.now(), OFFSET_DATE_TIME),
                     Arguments.of(OffsetTime.now(), OFFSET_TIME),
                     Arguments.of(Optional.empty(), OPTIONAL),
                     Arguments.of(OptionalDouble.empty(), OPTIONAL_DOUBLE),
                     Arguments.of(OptionalInt.empty(), OPTIONAL_INT),
                     Arguments.of(OptionalLong.empty(), OPTIONAL_LONG),
                     Arguments.of(Paths.get("."), PATH),
                     Arguments.of((Predicate<String>) s -> s.isEmpty(), PREDICATE),
                     Arguments.of(Short.MIN_VALUE, SHORT),
                     Arguments.of(new short[0], SHORT_ARRAY),
                     Arguments.of(Stream.empty(), STREAM),
                     Arguments.of("foo", STRING),
                     Arguments.of(new StringBuffer(), STRING_BUFFER),
                     Arguments.of(new StringBuilder(), STRING_BUILDER),
                     Arguments.of(new Exception(), THROWABLE),
                     Arguments.of(URI.create("http://localhost"), URI_TYPE),
                     Arguments.of(URI.create("http://localhost").toURL(), URL_TYPE),
                     Arguments.of(ZonedDateTime.now(), InstanceOfAssertFactories.ZONED_DATE_TIME));
  }

  static class Data {
    volatile int intField;
    volatile long longField;
    volatile String stringField;
  }

}
