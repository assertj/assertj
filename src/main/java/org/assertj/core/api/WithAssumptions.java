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

import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
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
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.util.CheckReturnValue;

/**
 * A unified entry point to all assumptions from both the new Java 8 core API and the pre-Java 8 core API.
 *
 * As a convenience, the methods are defined in an interface so that no static imports are necessary if the test class
 * implements this interface.
 */
public interface WithAssumptions {

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code> assumption.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <T> AbstractObjectArrayAssert<?, T> assumeThat(final T[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code> assumption.
   *
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <K, V> AbstractMapAssert<?, ?, K, V> assumeThat(final Map<K, V> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractShortAssert<?> assumeThat(final short actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractLongAssert<?> assumeThat(final long actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractLongAssert<?> assumeThat(final Long actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractLongArrayAssert<?> assumeThat(final long[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <T> the type of the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <T> AbstractObjectAssert<?, T> assumeThat(final T actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractCharSequenceAssert<?, String> assumeThat(final String actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractDateAssert<?> assumeThat(final Date actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assumeThat(final Throwable actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractBigDecimalAssert<?> assumeThat(final BigDecimal actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractBigIntegerAssert<?> assumeThat(BigInteger actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicBooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AtomicBooleanAssert assumeThat(AtomicBoolean actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicIntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicIntegerAssert assumeThat(AtomicInteger actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates int[] assumption for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AtomicIntegerArrayAssert assumeThat(AtomicIntegerArray actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicIntegerFieldUpdaterAssert}</code> assumption.
   *
   * @param actual the actual value.
   *
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   */
  default <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> assumeThat(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicLongAssert}</code> assumption.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongAssert assumeThat(AtomicLong actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicLongArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongArrayAssert assumeThat(AtomicLongArray actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicLongFieldUpdaterAssert}</code> assumption.
   *
   * @param actual the actual value.
   *
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   */
  default <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> assumeThat(AtomicLongFieldUpdater<OBJECT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicReferenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicReference}.
   *
   * @return the created assertion object.
   */
  default <VALUE> AtomicReferenceAssert<VALUE> assumeThat(AtomicReference<VALUE> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicReferenceArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <ELEMENT> the type of the value contained in the {@link AtomicReferenceArray}.
   *
   * @return the created assertion object.
   */
  default <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> assumeThat(AtomicReferenceArray<ELEMENT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicReferenceFieldUpdaterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <FIELD> the type of the field which gets updated by the {@link AtomicReferenceFieldUpdater}.
   * @param <OBJECT> the type of the object holding the updatable field.
   *
   * @return the created assertion object.
   */
  default <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> assumeThat(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicMarkableReferenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicMarkableReference}.
   *
   * @return the created assertion object.
   */
  default <VALUE> AtomicMarkableReferenceAssert<VALUE> assumeThat(AtomicMarkableReference<VALUE> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link AtomicStampedReferenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicStampedReference}.
   *
   * @return the created assertion object.
   */
  default <VALUE> AtomicStampedReferenceAssert<VALUE> assumeThat(AtomicStampedReference<VALUE> actual) {
    return Assumptions.assumeThat(actual);
  }
  
  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(final CharSequence actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractShortArrayAssert<?> assumeThat(final short[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractShortAssert<?> assumeThat(final Short actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractClassAssert<?> assumeThat(final Class<?> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractCharacterAssert<?> assumeThat(final Character actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractCharArrayAssert<?> assumeThat(final char[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractCharacterAssert<?> assumeThat(final char actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> assumption.
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assumeThat(final T actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code> assumption.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <ELEMENT> FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(final Iterable<? extends ELEMENT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code> assumption.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <ELEMENT> FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(final Iterator<? extends ELEMENT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractBooleanAssert<?> assumeThat(final Boolean actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractBooleanArrayAssert<?> assumeThat(final boolean[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractByteAssert<?> assumeThat(final byte actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractByteAssert<?> assumeThat(final Byte actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractByteArrayAssert<?> assumeThat(final byte[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractBooleanAssert<?> assumeThat(final boolean actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractFloatAssert<?> assumeThat(final float actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractInputStreamAssert<?, ? extends InputStream> assumeThat(final InputStream actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractFileAssert<?> assumeThat(final File actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Create assertion for {@link FutureAssert} assumption.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   *
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <RESULT> AbstractFutureAssert<?, ? extends Future<? extends RESULT>, RESULT> assumeThat(Future<RESULT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link PathAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractPathAssert<?> assumeThat(final Path actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractIntArrayAssert<?> assumeThat(final int[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractFloatAssert<?> assumeThat(final Float actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractIntegerAssert<?> assumeThat(final int actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractFloatArrayAssert<?> assumeThat(final float[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractIntegerAssert<?> assumeThat(final Integer actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractDoubleAssert<?> assumeThat(final double actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractDoubleAssert<?> assumeThat(final Double actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption.
   *
   * @param <ELEMENT> the type of elements.
   * @param list the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default <ELEMENT> FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(List<? extends ELEMENT> list) {
    return Assumptions.assumeThat(list);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link Stream}.
   *
   * @param <ELEMENT> the type of elements.
   * @param stream the Stream to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @CheckReturnValue
  default <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(Stream<? extends ELEMENT> stream) {
    return Assumptions.assumeThat(stream);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link DoubleStream}.
   *
   * @param doubleStream the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractListAssert<?, List<? extends Double>, Double, ObjectAssert<Double>> assumeThat(DoubleStream doubleStream) {
    return Assumptions.assumeThat(doubleStream);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link LongStream}.
   *
   * @param longStream the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractListAssert<?, List<? extends Long>, Long, ObjectAssert<Long>> assumeThat(LongStream longStream) {
    return Assumptions.assumeThat(longStream);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link IntStream}.
   *
   * @param intStream the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractListAssert<?, List<? extends Integer>, Integer, ObjectAssert<Integer>> assumeThat(IntStream intStream) {
    return Assumptions.assumeThat(intStream);
  }
  
  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractDoubleArrayAssert<?> assumeThat(final double[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code> assumption.
   *
   * @param zonedDateTime the actual value.
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  default AbstractZonedDateTimeAssert<?> assumeThat(final ZonedDateTime zonedDateTime) {
    return Assumptions.assumeThat(zonedDateTime);
  }

  /**
   * Creates a new instance of {@link CompletableFutureAssert} assumption.
   *
   * @param <RESULT> the CompletableFuture wrapped type.
   * @param future the CompletableFuture to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  default <RESULT> CompletableFutureAssert<RESULT> assumeThat(final CompletableFuture<RESULT> future) {
    return Assumptions.assumeThat(future);
  }

  /**
   * Creates a new instance of {@link OptionalAssert} assumption.
   *
   * @param <VALUE> the Optional wrapped type.
   * @param optional the Optional to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  default <VALUE> OptionalAssert<VALUE> assumeThat(final Optional<VALUE> optional) {
    return Assumptions.assumeThat(optional);
  }

  /**
   * Creates a new instance of <code>{@link OptionalDoubleAssert}</code> assumption.
   *
   * @param optionalDouble the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default OptionalDoubleAssert assumeThat(final OptionalDouble optionalDouble) {
    return Assumptions.assumeThat(optionalDouble);
  }

  /**
   * Creates a new instance of <code>{@link OptionalIntAssert}</code> assumption.
   *
   * @param optionalInt the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default OptionalIntAssert assumeThat(final OptionalInt optionalInt) {
    return Assumptions.assumeThat(optionalInt);
  }

  /**
   * Creates a new instance of <code>{@link OptionalLongAssert}</code> assumption.
   *
   * @param optionalLong the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default OptionalLongAssert assumeThat(final OptionalLong optionalLong) {
    return Assumptions.assumeThat(optionalLong);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code> assumption.
   *
   * @param localDateTime the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractLocalDateTimeAssert<?> assumeThat(final LocalDateTime localDateTime) {
    return Assumptions.assumeThat(localDateTime);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateAssert}</code> assumption.
   *
   * @param localDate the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractLocalDateAssert<?> assumeThat(final LocalDate localDate) {
    return Assumptions.assumeThat(localDate);
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code> assumption.
   *
   * @param localTime the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractLocalTimeAssert<?> assumeThat(final LocalTime localTime) {
    return Assumptions.assumeThat(localTime);
  }

  /**
   * Creates a new instance of <code>{@link InstantAssert}</code> assumption.
   *
   * @param instant the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractInstantAssert<?> assumeThat(final Instant instant) {
    return Assumptions.assumeThat(instant);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code> assumption.
   *
   * @param offsetTime the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractOffsetTimeAssert<?> assumeThat(final OffsetTime offsetTime) {
    return Assumptions.assumeThat(offsetTime);
  }

  /**
   * Creates a new instance of <code>{@link OffsetDateTimeAssert}</code> assumption.
   *
   * @param offsetDateTime the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractOffsetDateTimeAssert<?> assumeThat(final OffsetDateTime offsetDateTime) {
    return Assumptions.assumeThat(offsetDateTime);
  }

  /**
   * Allows to capture and then assume on a {@link Throwable} (easier done with lambdas).
   * <p>
   * Example :
   * <pre><code class='java'>  {@literal @}Test
   *  public void testException() {
   *    assumeThatThrownBy(() -&gt; { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                              .hasMessageContaining("boom");
   * }</code></pre>
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractThrowableAssert<?, ? extends Throwable> assumeThatThrownBy(
      final ThrowingCallable shouldRaiseThrowable) {
    return Assumptions.assumeThatThrownBy(shouldRaiseThrowable);
  }

  /**
   * Allows to capture and then assume on a {@link Throwable} (easier done with lambdas).
   * <p>
   * Example :
   * <pre><code class='java'> ThrowingCallable callable = () -&gt; {
   *   throw new Exception("boom!");
   * };
   * 
   * // assertion succeeds
   * assumeThatCode(callable).isInstanceOf(Exception.class)
   *                         .hasMessageContaining("boom");
   *                                                      
   * // assertion fails
   * assumeThatCode(callable).doesNotThrowAnyException();</code></pre>
   * <p>
   * This method was not named {@code assumeThat} because the java compiler reported it ambiguous when used directly with a lambda :(  
   *
   * @param shouldRaiseOrNotThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assumeThatCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return assumeThat(catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Creates a new instance of {@link PredicateAssert} assumption.
   *
   * @param <T> the {@link Predicate} type.
   * @param predicate the Predicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  default <T> PredicateAssert<T> assumeThat(final Predicate<T> predicate) {
    return Assumptions.assumeThat(predicate);
  }

  /**
   * Creates a new instance of {@link IntPredicateAssert} assumption.
   *
   * @param intPredicate the IntPredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  default IntPredicateAssert assumeThat(final IntPredicate intPredicate) {
    return Assumptions.assumeThat(intPredicate);
  }

  /**
   * Creates a new instance of {@link LongPredicateAssert} assumption.
   *
   * @param longPredicate the LongPredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  default LongPredicateAssert assumeThat(final LongPredicate longPredicate) {
    return Assumptions.assumeThat(longPredicate);
  }

  /**
   * Creates a new instance of {@link DoublePredicateAssert} assumption.
   *
   * @param doublePredicate the DoublePredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  default DoublePredicateAssert assumeThat(final DoublePredicate doublePredicate) {
    return Assumptions.assumeThat(doublePredicate);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code> assumption.
   *
   * @param url the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractUrlAssert<?> assumeThat(final URL url) {
    return Assumptions.assumeThat(url);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code> assumption.
   *
   * @param uri the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  default AbstractUriAssert<?> assumeThat(final URI uri) {
    return Assumptions.assumeThat(uri);
  }

}
