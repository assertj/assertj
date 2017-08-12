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
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Object[])}
   */
  @CheckReturnValue
  default <T> AbstractObjectArrayAssert<?, T> assumeThat(final T[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Map)}
   */
  @CheckReturnValue
  default <K, V> AbstractMapAssert<?, ?, K, V> assumeThat(final Map<K, V> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(short)}
   */
  @CheckReturnValue
  default AbstractShortAssert<?> assumeThat(final short actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(long)}
   */
  @CheckReturnValue
  default AbstractLongAssert<?> assumeThat(final long actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Long)}
   */
  @CheckReturnValue
  default AbstractLongAssert<?> assumeThat(final Long actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(long[])}
   */
  @CheckReturnValue
  default AbstractLongArrayAssert<?> assumeThat(final long[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Object)}
   */
  @CheckReturnValue
  default <T> AbstractObjectAssert<?, T> assumeThat(final T actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(String)}
   */
  @CheckReturnValue
  default AbstractCharSequenceAssert<?, String> assumeThat(final String actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Date)}
   */
  @CheckReturnValue
  default AbstractDateAssert<?> assumeThat(final Date actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Throwable)}
   */
  @CheckReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assumeThat(final Throwable actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(BigDecimal)}
   */
  @CheckReturnValue
  default AbstractBigDecimalAssert<?> assumeThat(final BigDecimal actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  default AbstractBigIntegerAssert<?> assumeThat(BigInteger actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicBooleanAssert assumeThat(AtomicBoolean actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicIntegerAssert assumeThat(AtomicInteger actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Create int[] assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicIntegerArrayAssert assumeThat(AtomicIntegerArray actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerFieldUpdater}.
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
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongAssert assumeThat(AtomicLong actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongArrayAssert assumeThat(AtomicLongArray actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLongFieldUpdater}.
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
   * Create assertion for {@link AtomicReference}.
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
   * Create assertion for {@link AtomicReferenceArray}.
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
   * Create assertion for {@link AtomicReferenceFieldUpdater}.
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
   * Create assertion for {@link AtomicMarkableReference}.
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
   * Create assertion for {@link AtomicStampedReference}.
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
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(CharSequence)}
   */
  @CheckReturnValue
  default AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(final CharSequence actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(short[])}
   */
  @CheckReturnValue
  default AbstractShortArrayAssert<?> assumeThat(final short[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Short)}
   */
  @CheckReturnValue
  default AbstractShortAssert<?> assumeThat(final Short actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Class)}
   */
  @CheckReturnValue
  default AbstractClassAssert<?> assumeThat(final Class<?> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Character)}
   */
  @CheckReturnValue
  default AbstractCharacterAssert<?> assumeThat(final Character actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(char[])}
   */
  @CheckReturnValue
  default AbstractCharArrayAssert<?> assumeThat(final char[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(char)}
   */
  @CheckReturnValue
  default AbstractCharacterAssert<?> assumeThat(final char actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Comparable)}
   */
  @CheckReturnValue
  default <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assumeThat(final T actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Iterable)}
   */
  @CheckReturnValue
  default <ELEMENT> FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(final Iterable<? extends ELEMENT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Iterator)}
   */
  @CheckReturnValue
  default <ELEMENT> FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(final Iterator<? extends ELEMENT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Boolean)}
   */
  @CheckReturnValue
  default AbstractBooleanAssert<?> assumeThat(final Boolean actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(boolean)}
   */
  @CheckReturnValue
  default AbstractBooleanArrayAssert<?> assumeThat(final boolean[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(byte)}
   */
  @CheckReturnValue
  default AbstractByteAssert<?> assumeThat(final byte actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Byte)}
   */
  @CheckReturnValue
  default AbstractByteAssert<?> assumeThat(final Byte actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(byte[])}
   */
  @CheckReturnValue
  default AbstractByteArrayAssert<?> assumeThat(final byte[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(boolean)}
   */
  @CheckReturnValue
  default AbstractBooleanAssert<?> assumeThat(final boolean actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(float)}
   */
  @CheckReturnValue
  default AbstractFloatAssert<?> assumeThat(final float actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(InputStream)}
   */
  @CheckReturnValue
  default AbstractInputStreamAssert<?, ? extends InputStream> assumeThat(final InputStream actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(File)}
   */
  @CheckReturnValue
  default AbstractFileAssert<?> assumeThat(final File actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Future)}
   * 
   * @since 3.7.0
   */
  @CheckReturnValue
  default <RESULT> AbstractFutureAssert<?, ? extends Future<? extends RESULT>, RESULT> assumeThat(Future<RESULT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Path)}
   */
  @CheckReturnValue
  default AbstractPathAssert<?> assumeThat(final Path actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(int[])}
   */
  @CheckReturnValue
  default AbstractIntArrayAssert<?> assumeThat(final int[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Float)}
   */
  @CheckReturnValue
  default AbstractFloatAssert<?> assumeThat(final Float actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(int)}
   */
  @CheckReturnValue
  default AbstractIntegerAssert<?> assumeThat(final int actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(float[])}
   */
  @CheckReturnValue
  default AbstractFloatArrayAssert<?> assumeThat(final float[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Integer)}
   */
  @CheckReturnValue
  default AbstractIntegerAssert<?> assumeThat(final Integer actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(double)}
   */
  @CheckReturnValue
  default AbstractDoubleAssert<?> assumeThat(final double actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Double)}
   */
  @CheckReturnValue
  default AbstractDoubleAssert<?> assumeThat(final Double actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(List)}
   */
  @CheckReturnValue
  default <ELEMENT> FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(List<? extends ELEMENT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(List)}
   */
  @CheckReturnValue
  default <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(Stream<? extends ELEMENT> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(DoubleStream)}
   */
  @CheckReturnValue
  default AbstractListAssert<?, List<? extends Double>, Double, ObjectAssert<Double>> assumeThat(DoubleStream actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(LongStream)}
   */
  @CheckReturnValue
  default AbstractListAssert<?, List<? extends Long>, Long, ObjectAssert<Long>> assumeThat(LongStream actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(IntStream)}
   */
  @CheckReturnValue
  default AbstractListAssert<?, List<? extends Integer>, Integer, ObjectAssert<Integer>> assumeThat(IntStream actual) {
    return Assumptions.assumeThat(actual);
  }
  
  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(double[])}
   */
  @CheckReturnValue
  default AbstractDoubleArrayAssert<?> assumeThat(final double[] actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(ZonedDateTime)}
   */
  default AbstractZonedDateTimeAssert<?> assumeThat(final ZonedDateTime actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(CompletableFuture)}
   */
  default <RESULT> CompletableFutureAssert<RESULT> assumeThat(final CompletableFuture<RESULT> future) {
    return Assumptions.assumeThat(future);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Optional)}
   */
  default <VALUE> OptionalAssert<VALUE> assumeThat(final Optional<VALUE> optional) {
    return Assumptions.assumeThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(java.util.OptionalDouble)}
   */
  default OptionalDoubleAssert assumeThat(final OptionalDouble optional) {
    return Assumptions.assumeThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(java.util.OptionalInt)}
   */
  default OptionalIntAssert assumeThat(final OptionalInt optional) {
    return Assumptions.assumeThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(java.util.OptionalLong)}
   */
  default OptionalLongAssert assumeThat(final OptionalLong optional) {
    return Assumptions.assumeThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(LocalDateTime)}
   */
  default AbstractLocalDateTimeAssert<?> assumeThat(final LocalDateTime localDateTime) {
    return Assumptions.assumeThat(localDateTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(LocalDate)}
   */
  default AbstractLocalDateAssert<?> assumeThat(final LocalDate localDate) {
    return Assumptions.assumeThat(localDate);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(LocalTime)}
   */
  default AbstractLocalTimeAssert<?> assumeThat(final LocalTime localTime) {
    return Assumptions.assumeThat(localTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(java.time.Instant)}
   * @since 3.7.0
   */
  default AbstractInstantAssert<?> assumeThat(final Instant actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(OffsetTime)}
   */
  default AbstractOffsetTimeAssert<?> assumeThat(final OffsetTime offsetTime) {
    return Assumptions.assumeThat(offsetTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(OffsetDateTime)}
   */
  default AbstractOffsetDateTimeAssert<?> assumeThat(final OffsetDateTime offsetDateTime) {
    return Assumptions.assumeThat(offsetDateTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThatThrownBy(ThrowableAssert.ThrowingCallable)}
   */
  default AbstractThrowableAssert<?, ? extends Throwable> assumeThatThrownBy(
      final ThrowingCallable shouldRaiseThrowable) {
    return Assumptions.assumeThatThrownBy(shouldRaiseThrowable);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * Example :
   * </p>
   *
   * <pre><code class='java'> ThrowingCallable callable = () -> {
   *   throw new Exception("boom!");
   * };
   * 
   * // assertion succeeds
   * assumeThatCode(callable).isInstanceOf(Exception.class)
   *                         .hasMessageContaining("boom");
   *                                                      
   * // assertion fails
   * assumeThatCode(callable).doesNotThrowAnyException();</code></pre>
   *
   * If the provided {@link ThrowingCallable} does not validate against next assertions, an error is immediately raised,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.</br>
   * To use a test description, use {@link #catchThrowable(ThrowableAssert.ThrowingCallable)} as shown below.
   * 
   * <pre><code class='java'> ThrowingCallable doNothing = () -> {
   *   // do nothing 
   * }; 
   * 
   * // assertion fails and "display me" appears in the assertion error
   * assumeThatCode(doNothing).as("display me")
   *                          .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error
   * Throwable thrown = catchThrowable(doNothing);
   * assumeThatCode(thrown).as("display me")
   *                       .isInstanceOf(Exception.class); </code></pre>
   * <p>
   * This method was not named {@code assumeThat} because the java compiler reported it ambiguous when used directly with a lambda :(  
   *
   * @param shouldRaiseOrNotThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @since 3.7.0
   */
  @CheckReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assumeThatCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return assumeThat(catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(Predicate)}
   */
  default <T> PredicateAssert<T> assumeThat(final Predicate<T> actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(IntPredicate)}
   */
  default IntPredicateAssert assumeThat(final IntPredicate actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(LongPredicate)}
   */
  default LongPredicateAssert assumeThat(final LongPredicate actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(DoublePredicate)}
   */
  default DoublePredicateAssert assumeThat(final DoublePredicate actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(URL)}
   */
  default AbstractUrlAssert<?> assumeThat(final URL actual) {
    return Assumptions.assumeThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assumptions#assumeThat(URI)}
   */
  default AbstractUriAssert<?> assumeThat(final URI actual) {
    return Assumptions.assumeThat(actual);
  }

}
