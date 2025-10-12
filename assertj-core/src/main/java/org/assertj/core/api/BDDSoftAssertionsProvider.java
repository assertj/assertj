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
 * Copyright 2012-2025 the original author or authors.
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

@CheckReturnValue
public interface BDDSoftAssertionsProvider extends SoftAssertionsProvider {

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BigDecimalAssert then(BigDecimal actual) {
    return proxy(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default BigIntegerAssert then(BigInteger actual) {
    return proxy(BigIntegerAssert.class, BigInteger.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanAssert then(boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanAssert then(Boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanArrayAssert then(boolean[] actual) {
    return proxy(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Boolean2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Boolean2DArrayAssert then(boolean[][] actual) {
    return proxy(Boolean2DArrayAssert.class, boolean[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteAssert then(byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteAssert then(Byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteArrayAssert then(byte[] actual) {
    return proxy(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Byte2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Byte2DArrayAssert then(byte[][] actual) {
    return proxy(Byte2DArrayAssert.class, byte[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharacterAssert then(char actual) {
    return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharArrayAssert then(char[] actual) {
    return proxy(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Char2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Char2DArrayAssert then(char[][] actual) {
    return proxy(Char2DArrayAssert.class, char[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharacterAssert then(Character actual) {
    return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>
   * <p>
   * We don't return {@link ClassAssert} as it has overridden methods to annotated with {@link SafeVarargs}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ClassAssert then(Class<?> actual) {
    return proxy(ClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.21.0
   */
  @SuppressWarnings("unchecked")
  default <T> CollectionAssert<T> then(Collection<? extends T> actual) {
    return proxy(CollectionAssert.class, Collection.class, actual);
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
    return proxy(GenericComparableAssert.class, Comparable.class, actual);
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
    return proxy(UniversalComparableAssert.class, Comparable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> IterableAssert<T> then(Iterable<? extends T> actual) {
    return proxy(IterableAssert.class, Iterable.class, actual);
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
  default <T> IteratorAssert<T> then(Iterator<? extends T> actual) {
    return proxy(IteratorAssert.class, Iterator.class, actual);
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
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleAssert then(Double actual) {
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleArrayAssert then(double[] actual) {
    return proxy(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Double2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Double2DArrayAssert then(double[][] actual) {
    return proxy(Double2DArrayAssert.class, double[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FileAssert then(File actual) {
    return proxy(FileAssert.class, File.class, actual);
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
  default <RESULT> FutureAssert<RESULT> then(Future<RESULT> actual) {
    return proxy(FutureAssert.class, Future.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default InputStreamAssert then(InputStream actual) {
    return proxy(InputStreamAssert.class, InputStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatAssert then(float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatAssert then(Float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatArrayAssert then(float[] actual) {
    return proxy(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Float2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Float2DArrayAssert then(float[][] actual) {
    return proxy(Float2DArrayAssert.class, float[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntegerAssert then(int actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntArrayAssert then(int[] actual) {
    return proxy(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Int2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Int2DArrayAssert then(int[][] actual) {
    return proxy(Int2DArrayAssert.class, int[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntegerAssert then(Integer actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
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
    return proxy(ListAssert.class, List.class, actual);
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
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongAssert then(Long actual) {
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongArrayAssert then(long[] actual) {
    return proxy(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Long2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Long2DArrayAssert then(long[][] actual) {
    return proxy(Long2DArrayAssert.class, long[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @param <T>    the type of the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ObjectAssert<T> then(T actual) {
    return proxy(ObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param <T>    the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ObjectArrayAssert<T> then(T[] actual) {
    return proxy(ObjectArrayAssert.class, Object[].class, actual);
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
  default <T> Object2DArrayAssert<T> then(T[][] actual) {
    return proxy(Object2DArrayAssert.class, Object[][].class, actual);
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
  default <K, V> MapAssert<K, V> then(Map<K, V> actual) {
    return proxy(MapAssert.class, Map.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortAssert then(short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortAssert then(Short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortArrayAssert then(short[] actual) {
    return proxy(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Short2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Short2DArrayAssert then(short[][] actual) {
    return proxy(Short2DArrayAssert.class, short[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharSequenceAssert then(CharSequence actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
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
  default CharSequenceAssert then(StringBuilder actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  default CharSequenceAssert then(StringBuffer actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default StringAssert then(String actual) {
    return proxy(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DateAssert then(Date actual) {
    return proxy(DateAssert.class, Date.class, actual);
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicBooleanAssert then(AtomicBoolean actual) {
    return proxy(AtomicBooleanAssert.class, AtomicBoolean.class, actual);
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicIntegerAssert then(AtomicInteger actual) {
    return proxy(AtomicIntegerAssert.class, AtomicInteger.class, actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicIntegerArrayAssert then(AtomicIntegerArray actual) {
    return proxy(AtomicIntegerArrayAssert.class, AtomicIntegerArray.class, actual);
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
  default <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> then(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return proxy(AtomicIntegerFieldUpdaterAssert.class, AtomicIntegerFieldUpdater.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicLongAssert then(AtomicLong actual) {
    return proxy(AtomicLongAssert.class, AtomicLong.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default AtomicLongArrayAssert then(AtomicLongArray actual) {
    return proxy(AtomicLongArrayAssert.class, AtomicLongArray.class, actual);
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
  default <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> then(AtomicLongFieldUpdater<OBJECT> actual) {
    return proxy(AtomicLongFieldUpdaterAssert.class, AtomicLongFieldUpdater.class, actual);
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
  default <VALUE> AtomicReferenceAssert<VALUE> then(AtomicReference<VALUE> actual) {
    return proxy(AtomicReferenceAssert.class, AtomicReference.class, actual);
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
  default <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> then(AtomicReferenceArray<ELEMENT> actual) {
    return proxy(AtomicReferenceArrayAssert.class, AtomicReferenceArray.class, actual);
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
  default <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> then(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return proxy(AtomicReferenceFieldUpdaterAssert.class, AtomicReferenceFieldUpdater.class, actual);
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
  default <VALUE> AtomicMarkableReferenceAssert<VALUE> then(AtomicMarkableReference<VALUE> actual) {
    return proxy(AtomicMarkableReferenceAssert.class, AtomicMarkableReference.class, actual);
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
  default <VALUE> AtomicStampedReferenceAssert<VALUE> then(AtomicStampedReference<VALUE> actual) {
    return proxy(AtomicStampedReferenceAssert.class, AtomicStampedReference.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param <T>    the type of the actual throwable.
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  @SuppressWarnings("unchecked")
  default <T extends Throwable> ThrowableAssert<T> then(T actual) {
    return proxy(ThrowableAssert.class, Throwable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> with a {@link SQLException}.
   *
   * @param <T>    the type of the actual SQLException.
   * @param actual the actual value.
   * @return the created assertion for SQLException.
   */
  @SuppressWarnings("unchecked")
  default <T extends SQLException> ThrowableAssert<T> then(T actual) {
    return proxy(ThrowableAssert.class, SQLException.class, actual);
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
  default <T> ObjectAssert<T> thenObject(T actual) {
    return then(actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default UriAssert then(URI actual) {
    return proxy(UriAssert.class, URI.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractUrlAssert<?> then(URL actual) {
    return proxy(UrlAssert.class, URL.class, actual);
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
  * Creates a new instance of <code>{@link TemporalAssert}</code>.
  *
  * @param actual the actual value.
  * @return the created assertion object.
  * @since 3.26.1
  */
  default TemporalAssert thenTemporal(Temporal actual) {
    return proxy(TemporalAssert.class, Temporal.class, actual);
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
   * Creates a new instance of <code>{@link YearMonthAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.26.0
   */
  default YearMonthAssert then(YearMonth actual) {
    return proxy(YearMonthAssert.class, YearMonth.class, actual);
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
   * interacts with the {@link List} built from the {@link Stream}. The stream is closed after the list is built.
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
   * interacts with the {@link List} built from the {@link Stream}. The stream is closed after the list is built.
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
   * interacts with the {@link List} built from the {@link DoubleStream}. The stream is closed after the list is built.
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
   * interacts with the {@link List} built from the {@link LongStream}. The stream is closed after the list is built.
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
   * interacts with the {@link List} built from the {@link IntStream}. The stream is closed after the list is built.
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
   * Creates a new instance of <code>{@link HashSetAssert}</code>.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 4.0.0
   */
  default <ELEMENT> HashSetAssert<ELEMENT> then(HashSet<? extends ELEMENT> actual) {
    return proxy(HashSetAssert.class, HashSet.class, actual);
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
