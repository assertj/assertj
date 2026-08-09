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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.sql.SQLException;
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
import java.util.HashSet;
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
import java.util.regex.Matcher;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.annotation.CanIgnoreReturnValue;
import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.jspecify.annotations.Nullable;

/**
 * Provides BDD-style proxied assertions that collect assertion errors.
 */
@CheckReturnValue
public interface BDDSoftAssertionsProvider extends SoftAssertionsProvider {

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BigDecimalAssert then(@Nullable BigDecimal actual) {
    return (BigDecimalAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default BigIntegerAssert then(@Nullable BigInteger actual) {
    return (BigIntegerAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanAssert then(boolean actual) {
    return (BooleanAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanAssert then(@Nullable Boolean actual) {
    return (BooleanAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanArrayAssert then(boolean @Nullable [] actual) {
    return (BooleanArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Boolean2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Boolean2DArrayAssert then(boolean[] @Nullable [] actual) {
    return (Boolean2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteAssert then(byte actual) {
    return (ByteAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteAssert then(@Nullable Byte actual) {
    return (ByteAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteArrayAssert then(byte @Nullable [] actual) {
    return (ByteArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Byte2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Byte2DArrayAssert then(byte[] @Nullable [] actual) {
    return (Byte2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharacterAssert then(char actual) {
    return (CharacterAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharArrayAssert then(char @Nullable [] actual) {
    return (CharArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Char2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Char2DArrayAssert then(char[] @Nullable [] actual) {
    return (Char2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharacterAssert then(@Nullable Character actual) {
    return (CharacterAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>
   * <p>
   * We don't return {@link ClassAssert} as it has overridden methods to annotated with {@link SafeVarargs}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ClassAssert then(@Nullable Class<?> actual) {
    return (ClassAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.21.0
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default <T> CollectionAssert<T> then(Collection<? extends T> actual) {
    return (CollectionAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   * <p>
   * Use this over {@link #then(Collection)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>then</code> for.
   *
   * @param <E>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> thenCollection(Collection<? extends E> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> with standard comparison semantics.
   *
   * @param <T>    the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T extends Comparable<? super T>> AbstractComparableAssert<?, T> then(T actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link UniversalComparableAssert}</code> with standard comparison semantics.
   * <p>
   * Use this over {@link #then(Comparable)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>then</code> for.
   *
   * @param <T>    the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  @SuppressWarnings("unchecked")
  default <T> AbstractUniversalComparableAssert<?, T> thenComparable(Comparable<T> actual) {
    return soft(Assertions.assertThatComparable(actual));
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> IterableAssert<T> then(@Nullable Iterable<? extends T> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   * <p>
   * Use this over {@link #then(Iterable)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>then</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual    the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <ELEMENT> IterableAssert<ELEMENT> thenIterable(Iterable<? extends ELEMENT> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   * <p>
   * <b>This is a breaking change in version 3.12.0:</b> this method used to return an {@link IterableAssert}.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> IteratorAssert<T> then(@Nullable Iterator<? extends T> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   * <p>
   * Use this over {@link #then(Iterator)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>then</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual    the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <ELEMENT> IteratorAssert<ELEMENT> thenIterator(Iterator<? extends ELEMENT> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleAssert then(double actual) {
    return (DoubleAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleAssert then(@Nullable Double actual) {
    return (DoubleAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleArrayAssert then(double @Nullable [] actual) {
    return (DoubleArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Double2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Double2DArrayAssert then(double[] @Nullable [] actual) {
    return (Double2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FileAssert then(@Nullable File actual) {
    return (FileAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FutureAssert}</code>.
   *
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   * @param actual   the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <RESULT> FutureAssert<RESULT> then(@Nullable Future<RESULT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default InputStreamAssert then(@Nullable InputStream actual) {
    return (InputStreamAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatAssert then(float actual) {
    return (FloatAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatAssert then(@Nullable Float actual) {
    return (FloatAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatArrayAssert then(float @Nullable [] actual) {
    return (FloatArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Float2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Float2DArrayAssert then(float[] @Nullable [] actual) {
    return (Float2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntegerAssert then(int actual) {
    return (IntegerAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntArrayAssert then(int @Nullable [] actual) {
    return (IntArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Int2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Int2DArrayAssert then(int[] @Nullable [] actual) {
    return (Int2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntegerAssert then(@Nullable Integer actual) {
    return (IntegerAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ListAssert<T> then(List<? extends T> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   * <p>
   * Use this over {@link #then(List)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>then</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual    the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <ELEMENT> ListAssert<ELEMENT> thenList(List<? extends ELEMENT> actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongAssert then(long actual) {
    return (LongAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongAssert then(@Nullable Long actual) {
    return (LongAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongArrayAssert then(long @Nullable [] actual) {
    return (LongArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Long2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Long2DArrayAssert then(long[] @Nullable [] actual) {
    return (Long2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @param <T>    the type of the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T extends @Nullable Object> ObjectAssert<T> then(T actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ObjectArrayAssert<T> then(T @Nullable [] actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Object2DArrayAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  @SuppressWarnings("unchecked")
  default <T> Object2DArrayAssert<T> then(T[] @Nullable [] actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   * <p>
   * We don't return {@link MapAssert} as it has overridden methods to annotated with {@link SafeVarargs}.
   *
   * @param <K>    the type of keys in the map.
   * @param <V>    the type of values in the map.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <K, V> MapAssert<K, V> then(@Nullable Map<K, V> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortAssert then(short actual) {
    return (ShortAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortAssert then(@Nullable Short actual) {
    return (ShortAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortArrayAssert then(short @Nullable [] actual) {
    return (ShortArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Short2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Short2DArrayAssert then(short[] @Nullable [] actual) {
    return (Short2DArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharSequenceAssert then(@Nullable CharSequence actual) {
    return (CharSequenceAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   * <p>
   * Use this over {@link #then(CharSequence)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>then</code> for.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.25.0
   */
  default CharSequenceAssert thenCharSequence(CharSequence actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuilder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  default CharSequenceAssert then(@Nullable StringBuilder actual) {
    return (CharSequenceAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  default CharSequenceAssert then(@Nullable StringBuffer actual) {
    return (CharSequenceAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default StringAssert then(@Nullable String actual) {
    return (StringAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DateAssert then(@Nullable Date actual) {
    return (DateAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicBooleanAssert then(@Nullable AtomicBoolean actual) {
    return (AtomicBooleanAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicIntegerAssert then(@Nullable AtomicInteger actual) {
    return (AtomicIntegerAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicIntegerArrayAssert then(@Nullable AtomicIntegerArray actual) {
    return (AtomicIntegerArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicIntegerFieldUpdater}.
   *
   * @param <OBJECT> The type of the object holding the updatable field
   * @param actual   the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> then(@Nullable AtomicIntegerFieldUpdater<OBJECT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicLongAssert then(@Nullable AtomicLong actual) {
    return (AtomicLongAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicLongArrayAssert then(@Nullable AtomicLongArray actual) {
    return (AtomicLongArrayAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicLongFieldUpdater}.
   *
   * @param actual   the actual value.
   * @param <OBJECT> the type of the object holding the updatable field
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> then(@Nullable AtomicLongFieldUpdater<OBJECT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicReference}.
   *
   * @param actual  the actual value.
   * @param <VALUE> the type of object referred to by this reference
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <VALUE> AtomicReferenceAssert<VALUE> then(@Nullable AtomicReference<VALUE> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicReferenceArray}.
   *
   * @param actual    the actual value.
   * @param <ELEMENT> the type of object referred to by the {@link AtomicReferenceArray}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> then(@Nullable AtomicReferenceArray<ELEMENT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicReferenceFieldUpdater}.
   *
   * @param <FIELD>  The type of the field
   * @param <OBJECT> the type of the object holding the updatable field
   * @param actual   the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> then(@Nullable AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicMarkableReference}.
   *
   * @param <VALUE> the type of object referred to by this reference
   * @param actual  the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <VALUE> AtomicMarkableReferenceAssert<VALUE> then(@Nullable AtomicMarkableReference<VALUE> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link AtomicStampedReference}.
   *
   * @param <VALUE> the type of value referred to by this reference
   * @param actual  the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  default <VALUE> AtomicStampedReferenceAssert<VALUE> then(@Nullable AtomicStampedReference<VALUE> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param <T>    the type of the actual throwable.
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default <T extends Throwable> ThrowableAssert<T> then(@Nullable T actual) {
    return (ThrowableAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> with a {@link SQLException}.
   *
   * @param <T>    the type of the actual SQLException.
   * @param actual the actual value.
   * @return the created assertion for SQLException.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default <T extends SQLException> ThrowableAssert<T> then(@Nullable T actual) {
    return (ThrowableAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * Java 8 example :
   * <pre><code class='java'> {@literal @}Test
   *  default void testException() {
   *    BDDSoftAssertions softly = new BDDSoftAssertions();
   *    softly.thenThrownBy(() -&gt; { throw new Exception("boom!"); }).isInstanceOf(Exception.class)
   *                                                                .hasMessageContaining("boom");
   *  }</code></pre>
   * <p>
   * Java 7 example :
   * <pre><code class='java'> BDDSoftAssertions softly = new BDDSoftAssertions();
   * softly.thenThrownBy(new ThrowingCallable() {
   *
   *   {@literal @}Override
   *   default Void call() throws Exception {
   *     throw new Exception("boom!");
   *   }
   *
   * }).isInstanceOf(Exception.class)
   *   .hasMessageContaining("boom");</code></pre>
   * <p>
   * If the provided {@link ThrowingCallable} does not raise an exception, an error is immediately thrown,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link Assertions#catchThrowable(ThrowingCallable)} as shown below:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error
   * softly.thenThrownBy(() -&gt; {}).as("display me")
   *                              .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error
   * Throwable thrown = catchThrowable(() -&gt; {});
   * softly.then(thrown).as("display me")
   *                    .isInstanceOf(Exception.class); </code></pre>
   * <p>
   * Alternatively you can also use {@code thenCode(ThrowingCallable)} for the test description provided
   * with {@link AbstractAssert#as(String, Object...) as(String, Object...)} to always be honored.
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   */
  @CanIgnoreReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> thenThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return then(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} like {@code thenThrownBy(ThrowingCallable)} but this method
   * let you set the assertion description the same way you do with {@link AbstractAssert#as(String, Object...) as(String, Object...)}.
   * <p>
   * Example:
   * <pre><code class='java'> {@literal @}Test
   *  default void testException() {
   *    BDDSoftAssertions softly = new BDDSoftAssertions();
   *    // if this assertion failed (but it doesn't), the error message would start with [Test explosive code]
   *    softly.thenThrownBy(() -&gt; { throw new IOException("boom!") }, "Test explosive code")
   *             .isInstanceOf(IOException.class)
   *             .hasMessageContaining("boom");
   * }</code></pre>
   * <p>
   * If the provided {@link ThrowingCallable ThrowingCallable} does not raise an exception, an error is immediately thrown.
   * <p>
   * The test description provided is honored but not the one with {@link AbstractAssert#as(String, Object...) as(String, Object...)}, example:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error message
   * softly.thenThrownBy(() -&gt; {}).as("display me")
   *                              .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error message
   * softly.thenThrownBy(() -&gt; {}, "display me")
   *                               .isInstanceOf(Exception.class);</code></pre>
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @param description          the new description to set.
   * @param args                 optional parameter if description is a format String.
   * @return the created {@link ThrowableAssert}.
   * @since 3.9.0
   */
  @CanIgnoreReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> thenThrownBy(ThrowingCallable shouldRaiseThrowable,
                                                                       String description, Object... args) {
    return then(catchThrowable(shouldRaiseThrowable)).as(description, args).hasBeenThrown();
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * Example :
   * </p>
   *
   * <pre><code class='java'> ThrowingCallable callable = () -&gt; {
   *   throw new Exception("boom!");
   * };
   *
   * // assertion succeeds
   * thenCode(callable).isInstanceOf(Exception.class)
   *                   .hasMessageContaining("boom");
   *
   * // assertion fails
   * thenCode(callable).doesNotThrowAnyException();</code></pre>
   * <p>
   * Contrary to {@code thenThrownBy(ThrowingCallable)} the test description provided with
   * {@link AbstractAssert#as(String, Object...) as(String, Object...)} is always honored as shown below.
   *
   * <pre><code class='java'> ThrowingCallable doNothing = () -&gt; {
   *   // do nothing
   * };
   *
   * // assertion fails and "display me" appears in the assertion error
   * thenCode(doNothing).as("display me")
   *                    .isInstanceOf(Exception.class);</code></pre>
   * <p>
   * This method was not named {@code then} because the java compiler reported it ambiguous when used directly with a lambda :(
   *
   * @param shouldRaiseOrNotThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @since 3.7.0
   */
  default AbstractThrowableAssert<?, ? extends Throwable> thenCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return then(catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.ObjectAssert}</code> for any object.
   *
   * <p>
   * This overload is useful, when an overloaded method of then(...) takes precedence over the generic {@link #then(Object)}.
   * </p>
   *
   * <p>
   * Example:
   * </p>
   * <p>
   * Cast necessary because {@link #then(List)} "forgets" actual type:
   * <pre>{@code then(new LinkedList<>(asList("abc"))).matches(list -> ((Deque<String>) list).getFirst().equals("abc")); }</pre>
   * No cast needed, but also no additional list assertions:
   * <pre>{@code thenObject(new LinkedList<>(asList("abc"))).matches(list -> list.getFirst().equals("abc")); }</pre>
   *
   * @param <T>    the type of the actual value.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.12.0
   */
  default <T extends @Nullable Object> ObjectAssert<T> thenObject(T actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default UriAssert then(@Nullable URI actual) {
    return (UriAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractUrlAssert<?> then(@Nullable URL actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
   * which allows to chain assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> softly.thenExceptionOfType(IOException.class)
   *           .isThrownBy(() -&gt; { throw new IOException("boom!"); })
   *           .withMessage("boom!"); </code></pre>
   * <p>
   * This method is more or less the same of {@link #thenThrownBy(ThrowingCallable)} but in a more natural way.
   *
   * @param <T>           the Throwable type.
   * @param throwableType the Throwable type class.
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0.
   */
  default <T extends Throwable> ThrowableTypeAssert<T> thenExceptionOfType(final Class<T> throwableType) {
    return new SoftThrowableTypeAssert<>(throwableType, this);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link RuntimeException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<RuntimeException> thenRuntimeException() {
    return thenExceptionOfType(RuntimeException.class);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link NullPointerException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<NullPointerException> thenNullPointerException() {
    return thenExceptionOfType(NullPointerException.class);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IllegalArgumentException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<IllegalArgumentException> thenIllegalArgumentException() {
    return thenExceptionOfType(IllegalArgumentException.class);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IOException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<IOException> thenIOException() {
    return thenExceptionOfType(IOException.class);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IllegalStateException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<IllegalStateException> thenIllegalStateException() {
    return thenExceptionOfType(IllegalStateException.class);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link Exception}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<Exception> thenException() {
    return thenExceptionOfType(Exception.class);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link ReflectiveOperationException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<ReflectiveOperationException> thenReflectiveOperationException() {
    return thenExceptionOfType(ReflectiveOperationException.class);
  }

  /**
   * Alias for {@link #thenExceptionOfType(Class)} for {@link IndexOutOfBoundsException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<IndexOutOfBoundsException> thenIndexOutOfBoundsException() {
    return thenExceptionOfType(IndexOutOfBoundsException.class);
  }

  /**
   * Creates a new, proxied instance of a {@link PathAssert}
   *
   * @param actual the path
   * @return the created assertion object
   */
  default PathAssert then(@Nullable Path actual) {
    return (PathAssert) soft(Assertions.assertThat(actual));
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
  default <VALUE> OptionalAssert<VALUE> then(@Nullable Optional<VALUE> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalDoubleAssert then(@Nullable OptionalDouble actual) {
    return (OptionalDoubleAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalIntAssert then(@Nullable OptionalInt actual) {
    return (OptionalIntAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link java.util.OptionalLong}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalLongAssert then(@Nullable OptionalLong actual) {
    return (OptionalLongAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link java.util.regex.Matcher}.
   *
   * @param actual the actual matcher
   *
   * @return the created assertion object.
   */
  @CheckReturnValue
  default MatcherAssert then(@Nullable Matcher actual) {
    return (MatcherAssert) soft(Assertions.assertThat(actual));
  }

  /**
  * Creates a new instance of <code>{@link TemporalAssert}</code>.
  *
  * @param actual the actual value.
  * @return the created assertion object.
  * @since 3.26.1
  */
  default TemporalAssert thenTemporal(Temporal actual) {
    return soft(Assertions.assertThatTemporal(actual));
  }

  /**
  * Creates a new instance of <code>{@link LocalDateAssert}</code>.
  *
  * @param actual the actual value.
  * @return the created assertion object.
  */
  default LocalDateAssert then(@Nullable LocalDate actual) {
    return (LocalDateAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link YearMonthAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.26.0
   */
  default YearMonthAssert then(@Nullable YearMonth actual) {
    return (YearMonthAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LocalDateTimeAssert then(@Nullable LocalDateTime actual) {
    return (LocalDateTimeAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ZonedDateTimeAssert then(@Nullable ZonedDateTime actual) {
    return (ZonedDateTimeAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LocalTimeAssert then(@Nullable LocalTime actual) {
    return (LocalTimeAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default OffsetTimeAssert then(@Nullable OffsetTime actual) {
    return (OffsetTimeAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link OffsetDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default OffsetDateTimeAssert then(@Nullable OffsetDateTime actual) {
    return (OffsetDateTimeAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link InstantAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.7.0
   */
  default InstantAssert then(@Nullable Instant actual) {
    return (InstantAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DurationAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.15.0
   */
  default DurationAssert then(@Nullable Duration actual) {
    return (DurationAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link PeriodAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default PeriodAssert then(@Nullable Period actual) {
    return (PeriodAssert) soft(Assertions.assertThat(actual));
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
  default <RESULT> CompletableFutureAssert<RESULT> then(@Nullable CompletableFuture<RESULT> actual) {
    return soft(Assertions.assertThat(actual));
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
  default <RESULT> CompletableFutureAssert<RESULT> then(@Nullable CompletionStage<RESULT> actual) {
    return soft(Assertions.assertThat(actual));
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
  default <T> PredicateAssert<T> then(@Nullable Predicate<T> actual) {
    return soft(Assertions.assertThat(actual));
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
  default IntPredicateAssert then(@Nullable IntPredicate actual) {
    return (IntPredicateAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  default DoublePredicateAssert then(@Nullable DoublePredicate actual) {
    return (DoublePredicateAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.5.0
   */
  default LongPredicateAssert then(@Nullable LongPredicate actual) {
    return (LongPredicateAssert) soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link Stream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link Stream}. The stream is closed after the list is built.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Stream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> then(@Nullable Stream<? extends ELEMENT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * Use this over {@link #then(Stream)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>then</code> for. 
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link Stream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link Stream}. The stream is closed after the list is built.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  @SuppressWarnings("unchecked")
  default <ELEMENT> ListAssert<ELEMENT> thenStream(Stream<? extends ELEMENT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link DoubleStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link DoubleStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link DoubleStream}. The stream is closed after the list is built.
   *
   * @param actual the actual {@link DoubleStream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default AbstractListAssert<?, List<? extends Double>, Double, ObjectAssert<Double>> then(@Nullable DoubleStream actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link LongStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link LongStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link LongStream}. The stream is closed after the list is built.
   *
   * @param actual the actual {@link LongStream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default AbstractListAssert<?, List<? extends Long>, Long, ObjectAssert<Long>> then(@Nullable LongStream actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link IntStream}.
   * <p>
   * <b>Be aware that to create the returned {@link ListAssert} the given the {@link IntStream} is consumed so it won't be
   * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
   * interacts with the {@link List} built from the {@link IntStream}. The stream is closed after the list is built.
   *
   * @param actual the actual {@link IntStream} value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default AbstractListAssert<?, List<? extends Integer>, Integer, ObjectAssert<Integer>> then(@Nullable IntStream actual) {
    return soft(Assertions.assertThat(actual));
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
  default <ELEMENT> SpliteratorAssert<ELEMENT> then(@Nullable Spliterator<ELEMENT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link HashSetAssert}</code>.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 4.0.0
   */
  default <ELEMENT> HashSetAssert<ELEMENT> then(@Nullable HashSet<? extends ELEMENT> actual) {
    return soft(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link LongAdder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.16.0
   */
  default LongAdderAssert then(@Nullable LongAdder actual) {
    return (LongAdderAssert) soft(Assertions.assertThat(actual));
  }

}
