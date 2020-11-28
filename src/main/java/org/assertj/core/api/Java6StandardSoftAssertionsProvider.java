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

import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.util.CanIgnoreReturnValue;
import org.assertj.core.util.CheckReturnValue;

/**
 * AbstractStandardSoftAssertions compatible with Android. Duplicated from {@link StandardSoftAssertionsProvider}.
 *
 * @see StandardSoftAssertionsProvider
 *
 * @since 2.5.0 / 3.5.0
 */
@CheckReturnValue
public interface Java6StandardSoftAssertionsProvider extends SoftAssertionsProvider {
  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BigDecimalAssert assertThat(BigDecimal actual) {
    return proxy(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigInteger}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  default BigIntegerAssert assertThat(BigInteger actual) {
    return proxy(BigIntegerAssert.class, BigInteger.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanAssert assertThat(boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanAssert assertThat(Boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default BooleanArrayAssert assertThat(boolean[] actual) {
    return proxy(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Boolean2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Boolean2DArrayAssert assertThat(boolean[][] actual) {
    return proxy(Boolean2DArrayAssert.class, boolean[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteAssert assertThat(byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteAssert assertThat(Byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ByteArrayAssert assertThat(byte[] actual) {
    return proxy(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Byte2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Byte2DArrayAssert assertThat(byte[][] actual) {
    return proxy(Byte2DArrayAssert.class, byte[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharacterAssert assertThat(char actual) {
    return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharArrayAssert assertThat(char[] actual) {
    return proxy(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Char2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Char2DArrayAssert assertThat(char[][] actual) {
    return proxy(Char2DArrayAssert.class, char[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharacterAssert assertThat(Character actual) {
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
  default ProxyableClassAssert assertThat(Class<?> actual) {
    return proxy(ProxyableClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> with
   * standard comparison semantics.
   *
   * @param <T> the actual type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(T actual) {
    return proxy(GenericComparableAssert.class, Comparable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   * <p>
   * We don't return {@link IterableAssert} as it has overridden methods to annotated with {@link SafeVarargs}.
   *
   * @param <T> the actual element's type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ProxyableIterableAssert<T> assertThat(Iterable<? extends T> actual) {
    return proxy(ProxyableIterableAssert.class, Iterable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   * <p>
   * <b>This is a breaking change in version 3.12.0:</b> this method used to return an {@link ProxyableIterableAssert}.
   *
   * @param <T> the actual element's type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> IteratorAssert<T> assertThat(Iterator<? extends T> actual) {
    return proxy(IteratorAssert.class, Iterator.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleAssert assertThat(double actual) {
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleAssert assertThat(Double actual) {
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoubleArrayAssert assertThat(double[] actual) {
    return proxy(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Double2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Double2DArrayAssert assertThat(double[][] actual) {
    return proxy(Double2DArrayAssert.class, double[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FileAssert assertThat(File actual) {
    return proxy(FileAssert.class, File.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FutureAssert}</code>.
   *
   * @param <RESULT> the {@link Future} element type.
   * @param actual the actual value
   * @return the created assertion object
   */
  @SuppressWarnings("unchecked")
  default <RESULT> FutureAssert<RESULT> assertThat(Future<RESULT> actual) {
    return proxy(FutureAssert.class, Future.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default InputStreamAssert assertThat(InputStream actual) {
    return proxy(InputStreamAssert.class, InputStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatAssert assertThat(float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatAssert assertThat(Float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default FloatArrayAssert assertThat(float[] actual) {
    return proxy(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Float2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Float2DArrayAssert assertThat(float[][] actual) {
    return proxy(Float2DArrayAssert.class, float[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntegerAssert assertThat(int actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntArrayAssert assertThat(int[] actual) {
    return proxy(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Int2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Int2DArrayAssert assertThat(int[][] actual) {
    return proxy(Int2DArrayAssert.class, int[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntegerAssert assertThat(Integer actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   * <p>
   * We don't return {@link IterableAssert} as it has overridden methods to annotated with {@link SafeVarargs}.
   *
   * @param <T> the actual element's type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ProxyableListAssert<T> assertThat(List<? extends T> actual) {
    return proxy(ProxyableListAssert.class, List.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongAssert assertThat(long actual) {
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongAssert assertThat(Long actual) {
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongArrayAssert assertThat(long[] actual) {
    return proxy(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Long2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Long2DArrayAssert assertThat(long[][] actual) {
    return proxy(Long2DArrayAssert.class, long[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @param <T> the type of the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ProxyableObjectAssert<T> assertThat(T actual) {
    return proxy(ProxyableObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @param <T> the type values of the actual array.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <T> ProxyableObjectArrayAssert<T> assertThat(T[] actual) {
    return proxy(ProxyableObjectArrayAssert.class, Object[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Object2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @param <T> the type values of the actual array.
   * @return the created assertion object.
   * @since 3.17.0
   */
  @SuppressWarnings("unchecked")
  default <T> Object2DArrayAssert<T> assertThat(T[][] actual) {
    return proxy(Object2DArrayAssert.class, Object[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   * <p>
   * We don't return {@link MapAssert} as it has overridden methods to annotated with {@link SafeVarargs}.
   *
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <K, V> ProxyableMapAssert<K, V> assertThat(Map<K, V> actual) {
    return proxy(ProxyableMapAssert.class, Map.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortAssert assertThat(short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortAssert assertThat(Short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ShortArrayAssert assertThat(short[] actual) {
    return proxy(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Short2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Short2DArrayAssert assertThat(short[][] actual) {
    return proxy(Short2DArrayAssert.class, short[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default CharSequenceAssert assertThat(CharSequence actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuilder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  default CharSequenceAssert assertThat(StringBuilder actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  default CharSequenceAssert assertThat(StringBuffer actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default StringAssert assertThat(String actual) {
    return proxy(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DateAssert assertThat(Date actual) {
    return proxy(DateAssert.class, Date.class, actual);
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicBooleanAssert assertThat(AtomicBoolean actual) {
    return proxy(AtomicBooleanAssert.class, AtomicBoolean.class, actual);
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicIntegerAssert assertThat(AtomicInteger actual) {
    return proxy(AtomicIntegerAssert.class, AtomicInteger.class, actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicIntegerArrayAssert assertThat(AtomicIntegerArray actual) {
    return proxy(AtomicIntegerArrayAssert.class, AtomicIntegerArray.class, actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerFieldUpdater}.
   *
   * @param actual the actual value.
   *
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> assertThat(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return proxy(AtomicIntegerFieldUpdaterAssert.class, AtomicIntegerFieldUpdater.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongAssert assertThat(AtomicLong actual) {
    return proxy(AtomicLongAssert.class, AtomicLong.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongArrayAssert assertThat(AtomicLongArray actual) {
    return proxy(AtomicLongArrayAssert.class, AtomicLongArray.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLongFieldUpdater}.
   *
   * @param actual the actual value.
   *
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> assertThat(AtomicLongFieldUpdater<OBJECT> actual) {
    return proxy(AtomicLongFieldUpdaterAssert.class, AtomicLongFieldUpdater.class, actual);
  }

  /**
   * Create assertion for {@link AtomicReference}.
   *
   * @param actual the actual value.
   *
   * @param <VALUE> the type of object referred to by the {@link AtomicReference}.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <VALUE> AtomicReferenceAssert<VALUE> assertThat(AtomicReference<VALUE> actual) {
    return proxy(AtomicReferenceAssert.class, AtomicReference.class, actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceArray}.
   *
   * @param <ELEMENT> the type of object referred to by the {@link AtomicReferenceArray}.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> assertThat(AtomicReferenceArray<ELEMENT> actual) {
    return proxy(AtomicReferenceArrayAssert.class, AtomicReferenceArray.class, actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceFieldUpdater}.
   *
   * @param actual the actual value.
   *
   * @param <FIELD> the type of the field which gets updated by the {@link AtomicReferenceFieldUpdater}.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> assertThat(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return proxy(AtomicReferenceFieldUpdaterAssert.class, AtomicReferenceFieldUpdater.class, actual);
  }

  /**
   * Create assertion for {@link AtomicMarkableReference}.
   *
   * @param <VALUE> The type of object referred to by this reference
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <VALUE> AtomicMarkableReferenceAssert<VALUE> assertThat(AtomicMarkableReference<VALUE> actual) {
    return proxy(AtomicMarkableReferenceAssert.class, AtomicMarkableReference.class, actual);
  }

  /**
   * Create assertion for {@link AtomicStampedReference}.
   *
   * @param <VALUE> The type of object referred to by this reference
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  default <VALUE> AtomicStampedReferenceAssert<VALUE> assertThat(AtomicStampedReference<VALUE> actual) {
    return proxy(AtomicStampedReferenceAssert.class, AtomicStampedReference.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  default ThrowableAssert assertThat(Throwable actual) {
    return proxy(ThrowableAssert.class, Throwable.class, actual);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * Java 8 example :
   * <pre><code class='java'>  {@literal @}Test
   *  default void testException() {
   *    SoftAssertions softly = new SoftAssertions();
   *    softly.assertThatThrownBy(() -&gt; { throw new Exception("boom!"); }).isInstanceOf(Exception.class)
   *                                                                      .hasMessageContaining("boom");
   *  }</code></pre>
   *
   * Java 7 example :
   * <pre><code class='java'> SoftAssertions softly = new SoftAssertions();
   * softly.assertThatThrownBy(new ThrowingCallable() {
   *
   *   {@literal @}Override
   *   default Void call() throws Exception {
   *     throw new Exception("boom!");
   *   }
   *
   * }).isInstanceOf(Exception.class)
   *   .hasMessageContaining("boom");</code></pre>
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   */
  @CanIgnoreReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} like {@code assertThatThrownBy(ThrowingCallable)} but this method
   * let you set the assertion description the same way you do with {@link AbstractAssert#as(String, Object...) as(String, Object...)}.
   * <p>
   * Example:
   * <pre><code class='java'> {@literal @}Test
   *  default void testException() {
   *    SoftAssertions softly = new SoftAssertions();
   *    // if this assertion failed (but it doesn't), the error message would start with [Test explosive code]
   *    softly.assertThatThrownBy(() -&gt; { throw new IOException("boom!") }, "Test explosive code")
   *             .isInstanceOf(IOException.class)
   *             .hasMessageContaining("boom");
   * }</code></pre>
   *
   * If the provided {@link ThrowingCallable ThrowingCallable} does not raise an exception, an error is immediately thrown.
   * <p>
   * The test description provided is honored but not the one with {@link AbstractAssert#as(String, Object...) as(String, Object...)}, example:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error message
   * softly.assertThatThrownBy(() -&gt; {}).as("display me")
   *                                    .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error message
   * softly.assertThatThrownBy(() -&gt; {}, "display me")
   *                                    .isInstanceOf(Exception.class);</code></pre>
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   *
   * @return the created {@link ThrowableAssert}.
   *
   * @since 3.9.0
   */
  @CanIgnoreReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(ThrowingCallable shouldRaiseThrowable,
                                                                             String description, Object... args) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).as(description, args).hasBeenThrown();
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
   * assertThatCode(callable).isInstanceOf(Exception.class)
   *                         .hasMessageContaining("boom");
   *
   * // assertion fails
   * assertThatCode(callable).doesNotThrowAnyException();</code></pre>
   *
   * If the provided {@link ThrowingCallable} does not validate against next assertions, an error is immediately raised,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link #assertThatCode(ThrowableAssert.ThrowingCallable)} as shown below.
   *
   * <pre><code class='java'> ThrowingCallable doNothing = () -&gt; {
   *   // do nothing
   * };
   *
   * // assertion fails and "display me" appears in the assertion error
   * assertThatCode(doNothing).as("display me")
   *                          .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error
   * Throwable thrown = catchThrowable(doNothing);
   * assertThatCode(thrown).as("display me")
   *                       .isInstanceOf(Exception.class); </code></pre>
   * <p>
   * This method was not named {@code assertThat} because the java compiler reported it ambiguous when used directly with a lambda :(
   *
   * @param shouldRaiseOrNotThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @since 3.7.0
   */
  default AbstractThrowableAssert<?, ? extends Throwable> assertThatCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return assertThat(catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code> for any object.
   *
   * <p>
   * This overload is useful, when an overloaded method of assertThat(...) takes precedence over the generic {@link #assertThat(Object)}.
   * </p>
   *
   * <p>
   * Example:
   * </p>
   *
   * Cast necessary because {@link #assertThat(List)} "forgets" actual type:
   * <pre>{@code assertThat(new LinkedList<>(asList("abc"))).matches(list -> ((Deque<String>) list).getFirst().equals("abc")); }</pre>
   * No cast needed, but also no additional list assertions:
   * <pre>{@code assertThatObject(new LinkedList<>(asList("abc"))).matches(list -> list.getFirst().equals("abc")); }</pre>
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.12.0
   */
  default <T> ProxyableObjectAssert<T> assertThatObject(T actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default UriAssert assertThat(URI actual) {
    return proxy(UriAssert.class, URI.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractUrlAssert<?> assertThat(URL actual) {
    return proxy(UrlAssert.class, URL.class, actual);
  }
}
