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
import org.assertj.core.util.CheckReturnValue;

/**
 * AbstractBDDSoftAssertions compatible with Android. Duplicated from {@link AbstractBDDSoftAssertions}.
 *
 * @see AbstractBDDSoftAssertions
 * 
 * @since 2.5.0 / 3.5.0
 */
public class Java6AbstractBDDSoftAssertions extends AbstractSoftAssertions {
  // then* methods duplicated from BDDAssertions

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public BigDecimalAssert then(BigDecimal actual) {
    return proxy(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public BigIntegerAssert then(BigInteger actual) {
    return proxy(BigIntegerAssert.class, BigInteger.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public BooleanAssert then(boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public BooleanAssert then(Boolean actual) {
    return proxy(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public BooleanArrayAssert then(boolean[] actual) {
    return proxy(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public ByteAssert then(byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public ByteAssert then(Byte actual) {
    return proxy(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public ByteArrayAssert then(byte[] actual) {
    return proxy(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public CharacterAssert then(char actual) {
    return proxy(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public CharArrayAssert then(char[] actual) {
    return proxy(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public CharacterAssert then(Character actual) {
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
  @CheckReturnValue
  public SoftAssertionClassAssert then(Class<?> actual) {
    return proxy(SoftAssertionClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link org.assertj.core.api.GenericComparableAssert}</code> with
   * standard comparison semantics.
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T extends Comparable<? super T>> AbstractComparableAssert<?, T> then(T actual) {
    return proxy(GenericComparableAssert.class, Comparable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> SoftAssertionIterableAssert<T> then(Iterable<? extends T> actual) {
    return proxy(SoftAssertionIterableAssert.class, Iterable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>. The <code>{@link Iterator}</code> is first
   * converted
   * into an <code>{@link Iterable}</code>
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> SoftAssertionIterableAssert<T> then(Iterator<? extends T> actual) {
    return proxy(SoftAssertionIterableAssert.class, Iterator.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public DoubleAssert then(double actual) {
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public DoubleAssert then(Double actual) {
    return proxy(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public DoubleArrayAssert then(double[] actual) {
    return proxy(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public FileAssert then(File actual) {
    return proxy(FileAssert.class, File.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FutureAssert}</code>.
   *
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <RESULT> FutureAssert<RESULT> then(Future<RESULT> actual) {
    return proxy(FutureAssert.class, Future.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public InputStreamAssert then(InputStream actual) {
    return proxy(InputStreamAssert.class, InputStream.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public FloatAssert then(float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public FloatAssert then(Float actual) {
    return proxy(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public FloatArrayAssert then(float[] actual) {
    return proxy(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public IntegerAssert then(int actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public IntArrayAssert then(int[] actual) {
    return proxy(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public IntegerAssert then(Integer actual) {
    return proxy(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> SoftAssertionListAssert<T> then(List<? extends T> actual) {
    return proxy(SoftAssertionListAssert.class, List.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public LongAssert then(long actual) {
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public LongAssert then(Long actual) {
    return proxy(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public LongArrayAssert then(long[] actual) {
    return proxy(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param actual the actual value.
   * @param <T> the type of the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> ObjectAssert<T> then(T actual) {
    return proxy(ObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <T> ObjectArrayAssert<T> then(T[] actual) {
    return proxy(ObjectArrayAssert.class, Object[].class, actual);
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
  @CheckReturnValue
  public <K, V> SoftAssertionMapAssert<K, V> then(Map<K, V> actual) {
    return proxy(SoftAssertionMapAssert.class, Map.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public ShortAssert then(short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public ShortAssert then(Short actual) {
    return proxy(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public ShortArrayAssert then(short[] actual) {
    return proxy(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public CharSequenceAssert then(CharSequence actual) {
    return proxy(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public StringAssert then(String actual) {
    return proxy(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public DateAssert then(Date actual) {
    return proxy(DateAssert.class, Date.class, actual);
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicBooleanAssert then(AtomicBoolean actual) {
    return proxy(AtomicBooleanAssert.class, AtomicBoolean.class, actual);
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicIntegerAssert then(AtomicInteger actual) {
    return proxy(AtomicIntegerAssert.class, AtomicInteger.class, actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicIntegerArrayAssert then(AtomicIntegerArray actual) {
    return proxy(AtomicIntegerArrayAssert.class, AtomicIntegerArray.class, actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerFieldUpdater}.
   *
   * @param <OBJECT> The type of the object holding the updatable field
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> then(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return proxy(AtomicIntegerFieldUpdaterAssert.class, AtomicIntegerFieldUpdater.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicLongAssert then(AtomicLong actual) {
    return proxy(AtomicLongAssert.class, AtomicLong.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicLongArrayAssert then(AtomicLongArray actual) {
    return proxy(AtomicLongArrayAssert.class, AtomicLongArray.class, actual);
  }

  /**
   * Create assertion for {@link AtomicLongFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> then(AtomicLongFieldUpdater<OBJECT> actual) {
    return proxy(AtomicLongFieldUpdaterAssert.class, AtomicLongFieldUpdater.class, actual);
  }

  /**
   * Create assertion for {@link AtomicReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of object referred to by this reference
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <VALUE> AtomicReferenceAssert<VALUE> then(AtomicReference<VALUE> actual) {
    return proxy(AtomicReferenceAssert.class, AtomicReference.class, actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceArray}.
   *
   * @param actual the actual value.
   * @param <ELEMENT> the type of object referred to by the {@link AtomicReferenceArray}.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> then(AtomicReferenceArray<ELEMENT> actual) {
    return proxy(AtomicReferenceArrayAssert.class, AtomicReferenceArray.class, actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceFieldUpdater}.
   *
   * @param <FIELD> The type of the field
   * @param <OBJECT> the type of the object holding the updatable field
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> then(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return proxy(AtomicReferenceFieldUpdaterAssert.class, AtomicReferenceFieldUpdater.class, actual);
  }

  /**
   * Create assertion for {@link AtomicMarkableReference}.
   *
   * @param <VALUE> the type of object referred to by this reference
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <VALUE> AtomicMarkableReferenceAssert<VALUE> then(AtomicMarkableReference<VALUE> actual) {
    return proxy(AtomicMarkableReferenceAssert.class, AtomicMarkableReference.class, actual);
  }

  /**
   * Create assertion for {@link AtomicStampedReference}.
   *
   * @param <VALUE> the type of value referred to by this reference
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public <VALUE> AtomicStampedReferenceAssert<VALUE> then(AtomicStampedReference<VALUE> actual) {
    return proxy(AtomicStampedReferenceAssert.class, AtomicStampedReference.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion Throwable.
   */
  @CheckReturnValue
  public ThrowableAssert then(Throwable actual) {
    return proxy(ThrowableAssert.class, Throwable.class, actual);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * Java 8 example :
   * <pre><code class='java'> {@literal @}Test
   *  public void testException() {
   *    BDDSoftAssertions softly = new BDDSoftAssertions();
   *    softly.thenThrownBy(() -&gt; { throw new Exception("boom!"); }).isInstanceOf(Exception.class)
   *                                                               .hasMessageContaining("boom");
   *  }</code></pre>
   *
   * Java 7 example :
   * <pre><code class='java'> BDDSoftAssertions softly = new BDDSoftAssertions();
   * softly.thenThrownBy(new ThrowingCallable() {
   *
   *   {@literal @}Override
   *   public Void call() throws Exception {
   *     throw new Exception("boom!");
   *   }
   *
   * }).isInstanceOf(Exception.class)
   *   .hasMessageContaining("boom");</code></pre>
   *   
   * If the provided {@link ThrowingCallable} does not raise an exception, an error is immediately thrown,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link Assertions#catchThrowable(ThrowableAssert.ThrowingCallable)} as shown below:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error
   * softly.thenThrownBy(() -&gt; {}).as("display me")
   *                               .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error
   * Throwable thrown = catchThrowable(() -&gt; {});
   * softly.then(thrown).as("display me")
   *                    .isInstanceOf(Exception.class); </code></pre>
   * 
   * Alternatively you can also use {@code thenCode(ThrowingCallable)} for the test description provided 
   * with {@link AbstractAssert#as(String, Object...) as(String, Object...)} to always be honored.
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   */
  @CheckReturnValue
  public AbstractThrowableAssert<?, ? extends Throwable> thenThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return then(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
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
   *
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
  @CheckReturnValue
  public AbstractThrowableAssert<?, ? extends Throwable> thenCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return then(catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public UriAssert then(URI actual) {
    return proxy(UriAssert.class, URI.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public AbstractUrlAssert<?> then(URL actual) {
    return proxy(AbstractUrlAssert.class, URL.class, actual);
  }
}
