/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.AbstractBigIntegerAssert;
import org.assertj.core.api.AbstractBooleanArrayAssert;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractByteArrayAssert;
import org.assertj.core.api.AbstractByteAssert;
import org.assertj.core.api.AbstractCharArrayAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractCharacterAssert;
import org.assertj.core.api.AbstractClassAssert;
import org.assertj.core.api.AbstractCollectionAssert;
import org.assertj.core.api.AbstractDateAssert;
import org.assertj.core.api.AbstractDoubleArrayAssert;
import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.AbstractDurationAssert;
import org.assertj.core.api.AbstractFileAssert;
import org.assertj.core.api.AbstractFloatArrayAssert;
import org.assertj.core.api.AbstractFloatAssert;
import org.assertj.core.api.AbstractInputStreamAssert;
import org.assertj.core.api.AbstractInstantAssert;
import org.assertj.core.api.AbstractIntArrayAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractLocalDateAssert;
import org.assertj.core.api.AbstractLocalDateTimeAssert;
import org.assertj.core.api.AbstractLocalTimeAssert;
import org.assertj.core.api.AbstractLongArrayAssert;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.AbstractOffsetDateTimeAssert;
import org.assertj.core.api.AbstractOffsetTimeAssert;
import org.assertj.core.api.AbstractPathAssert;
import org.assertj.core.api.AbstractPeriodAssert;
import org.assertj.core.api.AbstractShortArrayAssert;
import org.assertj.core.api.AbstractShortAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.AbstractUrlAssert;
import org.assertj.core.api.AbstractYearMonthAssert;
import org.assertj.core.api.AbstractZonedDateTimeAssert;
import org.assertj.core.api.AtomicBooleanAssert;
import org.assertj.core.api.AtomicIntegerArrayAssert;
import org.assertj.core.api.AtomicIntegerAssert;
import org.assertj.core.api.AtomicLongArrayAssert;
import org.assertj.core.api.AtomicLongAssert;
import org.assertj.core.api.AtomicMarkableReferenceAssert;
import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceAssert;
import org.assertj.core.api.AtomicStampedReferenceAssert;
import org.assertj.core.api.Boolean2DArrayAssert;
import org.assertj.core.api.Byte2DArrayAssert;
import org.assertj.core.api.Char2DArrayAssert;
import org.assertj.core.api.CompletableFutureAssert;
import org.assertj.core.api.Double2DArrayAssert;
import org.assertj.core.api.DoublePredicateAssert;
import org.assertj.core.api.Float2DArrayAssert;
import org.assertj.core.api.FutureAssert;
import org.assertj.core.api.Int2DArrayAssert;
import org.assertj.core.api.IntPredicateAssert;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.IteratorAssert;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.Long2DArrayAssert;
import org.assertj.core.api.LongAdderAssert;
import org.assertj.core.api.LongPredicateAssert;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MatcherAssert;
import org.assertj.core.api.Object2DArrayAssert;
import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.OptionalAssert;
import org.assertj.core.api.OptionalDoubleAssert;
import org.assertj.core.api.OptionalIntAssert;
import org.assertj.core.api.OptionalLongAssert;
import org.assertj.core.api.PredicateAssert;
import org.assertj.core.api.Short2DArrayAssert;
import org.assertj.core.api.SpliteratorAssert;
import org.junit.jupiter.api.Test;

class Assertions_assertThat_Test {

  @Test
  void should_accept_AtomicBoolean() {
    // GIVEN
    AtomicBoolean actual = new AtomicBoolean(false);
    // WHEN
    AtomicBooleanAssert result = assertThat(actual);
    // THEN
    result.isFalse();
  }

  @Test
  void should_accept_AtomicInteger() {
    // GIVEN
    AtomicInteger actual = new AtomicInteger(0);
    // WHEN
    AtomicIntegerAssert result = assertThat(actual);
    // THEN
    result.hasValue(0);
  }

  @Test
  void should_accept_AtomicIntegerArray() {
    // GIVEN
    AtomicIntegerArray actual = new AtomicIntegerArray(new int[] { 0, 1 });
    // WHEN
    AtomicIntegerArrayAssert result = assertThat(actual);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void should_accept_AtomicLong() {
    // GIVEN
    AtomicLong actual = new AtomicLong(0L);
    // WHEN
    AtomicLongAssert result = assertThat(actual);
    // THEN
    result.hasValue(0L);
  }

  @Test
  void should_accept_AtomicLongArray() {
    // GIVEN
    AtomicLongArray actual = new AtomicLongArray(new long[] { 0L, 1L });
    // WHEN
    AtomicLongArrayAssert result = assertThat(actual);
    // THEN
    result.containsExactly(0L, 1L);
  }

  @Test
  void should_accept_AtomicMarkableReference() {
    // GIVEN
    AtomicMarkableReference<String> actual = new AtomicMarkableReference<>("Yoda", false);
    // WHEN
    AtomicMarkableReferenceAssert<String> result = assertThat(actual);
    // THEN
    result.hasReference("Yoda");
  }

  @Test
  void should_accept_AtomicReference() {
    // GIVEN
    AtomicReference<String> actual = new AtomicReference<>("Yoda");
    // WHEN
    AtomicReferenceAssert<String> result = assertThat(actual);
    // THEN
    result.hasValue("Yoda");
  }

  @Test
  void should_accept_AtomicReferenceArray() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(new String[] { "Yoda" });
    // WHEN
    AtomicReferenceArrayAssert<String> result = assertThat(actual);
    // THEN
    result.containsExactly("Yoda");
  }

  @Test
  void should_accept_AtomicStampedReference() {
    // GIVEN
    AtomicStampedReference<String> actual = new AtomicStampedReference<>("Yoda", 0);
    // WHEN
    AtomicStampedReferenceAssert<String> result = assertThat(actual);
    // THEN
    result.hasReference("Yoda");
  }

  @Test
  void should_accept_BigDecimal() {
    // GIVEN
    BigDecimal actual = BigDecimal.valueOf(0.0);
    // WHEN
    AbstractBigDecimalAssert<?> result = assertThat(actual);
    // THEN
    result.isEqualTo("0.0");
  }

  @Test
  void should_accept_BigInteger() {
    // GIVEN
    BigInteger actual = BigInteger.ZERO;
    // WHEN
    AbstractBigIntegerAssert<?> result = assertThat(actual);
    // THEN
    result.isZero();
  }

  @Test
  void should_accept_Boolean() {
    // GIVEN
    Boolean actual = Boolean.TRUE;
    // WHEN
    AbstractBooleanAssert<?> result = assertThat(actual);
    // THEN
    result.isTrue();
  }

  @Test
  void should_accept_boolean_2d_array() {
    // GIVEN
    boolean[][] actual = { { true, false }, { false, true } };
    // WHEN
    Boolean2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_boolean_array() {
    // GIVEN
    boolean[] actual = { true, false };
    // WHEN
    AbstractBooleanArrayAssert<?> result = assertThat(actual);
    // THEN
    result.containsExactly(true, false);
  }

  @Test
  void should_accept_Byte() {
    // GIVEN
    Byte actual = (byte) 0;
    // WHEN
    AbstractByteAssert<?> result = assertThat(actual);
    // THEN
    result.isZero();
  }

  @Test
  void should_accept_byte_2d_array() {
    // GIVEN
    byte[][] actual = { { 0, 1 }, { 2, 3 } };
    // WHEN
    Byte2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_byte_array() {
    // GIVEN
    byte[] actual = { 0, 1 };
    // WHEN
    AbstractByteArrayAssert<?> result = assertThat(actual);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void should_accept_char_2d_array() {
    // GIVEN
    char[][] actual = { { 'a', 'b' }, { 'c', 'd' } };
    // WHEN
    Char2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_char_array() {
    // GIVEN
    char[] actual = { 'a', 'b' };
    // WHEN
    AbstractCharArrayAssert<?> result = assertThat(actual);
    // THEN
    result.doesNotHaveDuplicates();
  }

  @Test
  void should_accept_Character() {
    // GIVEN
    Character actual = 'a';
    // WHEN
    AbstractCharacterAssert<?> result = assertThat(actual);
    // THEN
    result.isLowerCase();
  }

  @Test
  void should_accept_CharSequence() {
    // GIVEN
    CharSequence actual = "Yoda";
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = assertThat(actual);
    // THEN
    result.startsWith("Yo");
  }

  @Test
  void should_accept_Class() {
    // GIVEN
    Class<?> actual = Object.class;
    // WHEN
    AbstractClassAssert<?> result = assertThat(actual);
    // THEN
    result.isPublic();
  }

  @Test
  void should_accept_Collection() {
    // GIVEN
    Collection<String> actual = List.of("Yoda", "Luke");
    // WHEN
    AbstractCollectionAssert<?, Collection<? extends String>, String, ObjectAssert<String>> result = assertThat(actual);
    // THEN
    result.contains("Yoda");
  }

  @Test
  void should_accept_CompletableFuture() {
    // GIVEN
    CompletableFuture<String> actual = CompletableFuture.completedFuture("Yoda");
    // WHEN
    CompletableFutureAssert<String> result = assertThat(actual);
    // THEN
    result.isDone();
  }

  @Test
  void should_accept_CompletionStage() {
    // GIVEN
    CompletionStage<String> actual = CompletableFuture.completedFuture("Yoda");
    // WHEN
    CompletableFutureAssert<String> result = assertThat(actual);
    // THEN
    result.isDone();
  }

  @Test
  void should_accept_Date() {
    // GIVEN
    Date actual = new Date();
    // WHEN
    AbstractDateAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(new Date());
  }

  @Test
  void should_accept_Double() {
    // GIVEN
    Double actual = 0.0;
    // WHEN
    AbstractDoubleAssert<?> result = assertThat(actual);
    // THEN
    result.isZero();
  }

  @Test
  void should_accept_double_2d_array() {
    // GIVEN
    double[][] actual = { { 0.0, 1.0 }, { 2.0, 3.0 } };
    // WHEN
    Double2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_double_array() {
    // GIVEN
    double[] actual = { 0.0, 1.0 };
    // WHEN
    AbstractDoubleArrayAssert<?> result = assertThat(actual);
    // THEN
    result.containsExactly(0.0, 1.0);
  }

  @Test
  void should_accept_DoublePredicate() {
    // GIVEN
    DoublePredicate actual = d -> true;
    // WHEN
    DoublePredicateAssert result = assertThat(actual);
    // THEN
    result.accepts(1.0);
  }

  @Test
  void should_accept_DoubleStream() {
    // GIVEN
    DoubleStream actual = DoubleStream.of(1.0, 2.0, 3.0);
    // WHEN
    ListAssert<Double> result = assertThat(actual);
    // THEN
    result.contains(1.0);
  }

  @Test
  void should_accept_Duration() {
    // GIVEN
    Duration actual = Duration.ofHours(10);
    // WHEN
    AbstractDurationAssert<?> result = assertThat(actual);
    // THEN
    result.hasHours(10);
  }

  @Test
  void should_accept_File() {
    // GIVEN
    File actual = new File("yoda.txt");
    // WHEN
    AbstractFileAssert<?> result = assertThat(actual);
    // THEN
    result.doesNotExist();
  }

  @Test
  void should_accept_Float() {
    // GIVEN
    Float actual = 0.0f;
    // WHEN
    AbstractFloatAssert<?> result = assertThat(actual);
    // THEN
    result.isZero();
  }

  @Test
  void should_accept_float_2d_array() {
    // GIVEN
    float[][] actual = { { 0.0f, 1.0f }, { 2.0f, 3.0f } };
    // WHEN
    Float2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_float_array() {
    // GIVEN
    float[] actual = { 0.0f, 1.0f };
    // WHEN
    AbstractFloatArrayAssert<?> result = assertThat(actual);
    // THEN
    result.containsExactly(0.0f, 1.0f);
  }

  @Test
  void should_accept_Future() {
    // GIVEN
    Future<String> actual = CompletableFuture.completedFuture("Yoda");
    // WHEN
    FutureAssert<String> result = assertThat(actual);
    // THEN
    result.isDone();
  }

  @Test
  void should_accept_InputStream() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("Yoda".getBytes());
    // WHEN
    AbstractInputStreamAssert<?, ? extends InputStream> result = assertThat(actual);
    // THEN
    result.hasContent("Yoda");
  }

  @Test
  void should_accept_Instant() {
    // GIVEN
    Instant actual = Instant.now();
    // WHEN
    AbstractInstantAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(Instant.now());
  }

  @Test
  void should_accept_int_2d_array() {
    // GIVEN
    int[][] actual = { { 0, 1 }, { 2, 3 } };
    // WHEN
    Int2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_int_array() {
    // GIVEN
    int[] actual = { 0, 1 };
    // WHEN
    AbstractIntArrayAssert<?> result = assertThat(actual);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void should_accept_Integer() {
    // GIVEN
    Integer actual = 0;
    // WHEN
    AbstractIntegerAssert<?> result = assertThat(actual);
    // THEN
    result.isZero();
  }

  @Test
  void should_accept_IntPredicate() {
    // GIVEN
    IntPredicate actual = i -> true;
    // WHEN
    IntPredicateAssert result = assertThat(actual);
    // THEN
    result.accepts(1);
  }

  @Test
  void should_accept_IntStream() {
    // GIVEN
    IntStream actual = IntStream.of(1, 2, 3);
    // WHEN
    ListAssert<Integer> result = assertThat(actual);
    // THEN
    result.contains(1);
  }

  @Test
  void should_accept_Iterable() {
    // GIVEN
    Iterable<String> actual = List.of("Yoda", "Luke");
    // WHEN
    IterableAssert<String> result = assertThat(actual);
    // THEN
    result.contains("Yoda");
  }

  @Test
  void should_accept_Iterator() {
    // GIVEN
    Iterator<String> actual = List.of("Yoda").iterator();
    // WHEN
    IteratorAssert<String> result = assertThat(actual);
    // THEN
    result.hasNext();
  }

  @Test
  void should_accept_List() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Luke");
    // WHEN
    ListAssert<String> result = assertThat(actual);
    // THEN
    result.contains("Yoda");
  }

  @Test
  void should_accept_LocalDate() {
    // GIVEN
    LocalDate actual = LocalDate.now();
    // WHEN
    AbstractLocalDateAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(LocalDate.now());
  }

  @Test
  void should_accept_LocalDateTime() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.now();
    // WHEN
    AbstractLocalDateTimeAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(LocalDateTime.now());
  }

  @Test
  void should_accept_LocalTime() {
    // GIVEN
    LocalTime actual = LocalTime.now();
    // WHEN
    AbstractLocalTimeAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(LocalTime.now());
  }

  @Test
  void should_accept_Long() {
    // GIVEN
    Long actual = 0L;
    // WHEN
    AbstractLongAssert<?> result = assertThat(actual);
    // THEN
    result.isZero();
  }

  @Test
  void should_accept_long_2d_array() {
    // GIVEN
    long[][] actual = { { 0L, 1L }, { 2L, 3L } };
    // WHEN
    Long2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_long_array() {
    // GIVEN
    long[] actual = { 0L, 1L };
    // WHEN
    AbstractLongArrayAssert<?> result = assertThat(actual);
    // THEN
    result.containsExactly(0L, 1L);
  }

  @Test
  void should_accept_LongAdder() {
    // GIVEN
    LongAdder actual = new LongAdder();
    // WHEN
    LongAdderAssert result = assertThat(actual);
    // THEN
    result.hasValue(0L);
  }

  @Test
  void should_accept_LongPredicate() {
    // GIVEN
    LongPredicate actual = l -> true;
    // WHEN
    LongPredicateAssert result = assertThat(actual);
    // THEN
    result.accepts(1L);
  }

  @Test
  void should_accept_LongStream() {
    // GIVEN
    LongStream actual = LongStream.of(1L, 2L, 3L);
    // WHEN
    ListAssert<Long> result = assertThat(actual);
    // THEN
    result.contains(1L);
  }

  @Test
  void should_accept_Map() {
    // GIVEN
    Map<String, String> actual = Map.of("key", "value");
    // WHEN
    MapAssert<String, String> result = assertThat(actual);
    // THEN
    result.containsKey("key");
  }

  @Test
  void should_accept_Matcher() {
    // GIVEN
    Matcher actual = Pattern.compile(".*Yoda.*").matcher("Yoda");
    // WHEN
    MatcherAssert result = assertThat(actual);
    // THEN
    result.matches();
  }

  @Test
  void should_accept_object_2d_array() {
    // GIVEN
    String[][] actual = { { "Yoda", "Luke" }, { "Anakin", "Leia" } };
    // WHEN
    Object2DArrayAssert<String> result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_object_array() {
    // GIVEN
    String[] actual = { "Yoda", "Luke" };
    // WHEN
    ObjectArrayAssert<String> result = assertThat(actual);
    // THEN
    result.containsExactly("Yoda", "Luke");
  }

  @Test
  void should_accept_OffsetDateTime() {
    // GIVEN
    OffsetDateTime actual = OffsetDateTime.now();
    // WHEN
    AbstractOffsetDateTimeAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  void should_accept_OffsetTime() {
    // GIVEN
    OffsetTime actual = OffsetTime.now();
    // WHEN
    AbstractOffsetTimeAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(OffsetTime.now());
  }

  @Test
  void should_accept_Optional() {
    // GIVEN
    Optional<String> actual = Optional.of("Yoda");
    // WHEN
    OptionalAssert<String> result = assertThat(actual);
    // THEN
    result.contains("Yoda");
  }

  @Test
  void should_accept_OptionalDouble() {
    // GIVEN
    OptionalDouble actual = OptionalDouble.of(1.0);
    // WHEN
    OptionalDoubleAssert result = assertThat(actual);
    // THEN
    result.hasValue(1.0);
  }

  @Test
  void should_accept_OptionalInt() {
    // GIVEN
    OptionalInt actual = OptionalInt.of(1);
    // WHEN
    OptionalIntAssert result = assertThat(actual);
    // THEN
    result.hasValue(1);
  }

  @Test
  void should_accept_OptionalLong() {
    // GIVEN
    OptionalLong actual = OptionalLong.of(1L);
    // WHEN
    OptionalLongAssert result = assertThat(actual);
    // THEN
    result.hasValue(1L);
  }

  @Test
  void should_accept_Path() {
    // GIVEN
    Path actual = Paths.get("yoda.txt");
    // WHEN
    AbstractPathAssert<?> result = assertThat(actual);
    // THEN
    result.doesNotExist();
  }

  @Test
  void should_accept_Period() {
    // GIVEN
    Period actual = Period.ofYears(1);
    // WHEN
    AbstractPeriodAssert<?> result = assertThat(actual);
    // THEN
    result.hasYears(1);
  }

  @Test
  void should_accept_Predicate() {
    // GIVEN
    Predicate<String> actual = s -> true;
    // WHEN
    PredicateAssert<String> result = assertThat(actual);
    // THEN
    result.accepts("Yoda");
  }

  @Test
  void should_accept_Short() {
    // GIVEN
    Short actual = (short) 0;
    // WHEN
    AbstractShortAssert<?> result = assertThat(actual);
    // THEN
    result.isZero();
  }

  @Test
  void should_accept_short_2d_array() {
    // GIVEN
    short[][] actual = { { (short) 0, (short) 1 }, { (short) 2, (short) 3 } };
    // WHEN
    Short2DArrayAssert result = assertThat(actual);
    // THEN
    result.hasDimensions(2, 2);
  }

  @Test
  void should_accept_short_array() {
    // GIVEN
    short[] actual = { (short) 0, (short) 1 };
    // WHEN
    AbstractShortArrayAssert<?> result = assertThat(actual);
    // THEN
    result.containsExactly((short) 0, (short) 1);
  }

  @Test
  void should_accept_Spliterator() {
    // GIVEN
    Spliterator<String> actual = List.of("Yoda").spliterator();
    // WHEN
    SpliteratorAssert<String> result = assertThat(actual);
    // THEN
    result.hasCharacteristics(Spliterator.ORDERED);
  }

  @Test
  void should_accept_Stream() {
    // GIVEN
    Stream<String> actual = Stream.of("Yoda", "Luke");
    // WHEN
    ListAssert<String> result = assertThat(actual);
    // THEN
    result.contains("Yoda");
  }

  @Test
  void should_accept_String() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AbstractStringAssert<?> result = assertThat(actual);
    // THEN
    result.startsWith("Yo");
  }

  @Test
  void should_accept_StringBuffer() {
    // GIVEN
    StringBuffer actual = new StringBuffer("Yoda");
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = assertThat(actual);
    // THEN
    result.startsWith("Yo");
  }

  @Test
  void should_accept_StringBuilder() {
    // GIVEN
    StringBuilder actual = new StringBuilder("Yoda");
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = assertThat(actual);
    // THEN
    result.startsWith("Yo");
  }

  @Test
  void should_accept_Throwable() {
    // GIVEN
    RuntimeException actual = new RuntimeException("message");
    // WHEN
    AbstractThrowableAssert<?, RuntimeException> result = assertThat(actual);
    // THEN
    result.hasMessage("message");
  }

  @Test
  void should_accept_URI() {
    // GIVEN
    URI actual = URI.create("http://localhost");
    // WHEN
    AbstractUriAssert<?> result = assertThat(actual);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void should_accept_URL() throws MalformedURLException {
    // GIVEN
    URL actual = URI.create("http://localhost").toURL();
    // WHEN
    AbstractUrlAssert<?> result = assertThat(actual);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void should_accept_YearMonth() {
    // GIVEN
    YearMonth actual = YearMonth.now();
    // WHEN
    AbstractYearMonthAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(YearMonth.now());
  }

  @Test
  void should_accept_ZonedDateTime() {
    // GIVEN
    ZonedDateTime actual = ZonedDateTime.now();
    // WHEN
    AbstractZonedDateTimeAssert<?> result = assertThat(actual);
    // THEN
    result.isBeforeOrEqualTo(ZonedDateTime.now());
  }

}
