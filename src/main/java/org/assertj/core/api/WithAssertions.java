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

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.DateFormat;
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
import java.util.stream.BaseStream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.condition.DoesNotHave;
import org.assertj.core.condition.Not;
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;
import org.assertj.core.util.CheckReturnValue;

/**
 *
 * A unified entry point to all non-deprecated assertions from both the new Java 8 core API and the pre-Java 8 core API.
 *
 * As a convenience, the methods are defined in an interface so that no static imports are necessary if the test class
 * implements this interface.
 *
 * Based on an idea by David Gageot :
 *
 * http://blog.javabien.net/2014/04/23/what-if-assertj-used-java-8/
 *
 * @author Alan Rothkopf
 *
 */
public interface WithAssertions {

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#offset(Float)}
   */
  default Offset<Float> offset(final Float value) {
    return Assertions.offset(value);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#offset(Double)}
   */
  default Offset<Double> offset(final Double value) {
    return Assertions.offset(value);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#entry(Object, Object)}
   */
  default <K, V> MapEntry<K, V> entry(final K key, final V value) {
    return Assertions.entry(key, value);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#fail(String)}
   */
  default void fail(final String failureMessage) {
    Assertions.fail(failureMessage);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#fail(String,Throwable)}
   */
  default void fail(final String failureMessage, final Throwable realCause) {
    Assertions.fail(failureMessage, realCause);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#not(Condition)}
   */
  default <T> Not<T> not(final Condition<? super T> condition) {
    return Assertions.not(condition);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#allOf(Iterable)}
   */
  default <T> Condition<T> allOf(final Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#allOf(Condition[])}
   */
  default <T> Condition<T> allOf(@SuppressWarnings("unchecked") final Condition<? super T>... conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Object[])}
   */
  @CheckReturnValue
  default <T> AbstractObjectArrayAssert<?, T> assertThat(final T[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(AssertDelegateTarget)}
   */
  @CheckReturnValue
  default <T extends AssertDelegateTarget> T assertThat(final T assertion) {
    return Assertions.assertThat(assertion);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Map)}
   */
  @CheckReturnValue
  default <K, V> MapAssert<K, V> assertThat(final Map<K, V> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(short)}
   */
  @CheckReturnValue
  default AbstractShortAssert<?> assertThat(final short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(long)}
   */
  @CheckReturnValue
  default AbstractLongAssert<?> assertThat(final long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Long)}
   */
  @CheckReturnValue
  default AbstractLongAssert<?> assertThat(final Long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(long[])}
   */
  @CheckReturnValue
  default AbstractLongArrayAssert<?> assertThat(final long[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Object)}
   */
  @CheckReturnValue
  default <T> AbstractObjectAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(String)}
   */
  @CheckReturnValue
  default AbstractCharSequenceAssert<?, String> assertThat(final String actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Date)}
   */
  @CheckReturnValue
  default AbstractDateAssert<?> assertThat(final Date actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Throwable)}
   */
  @CheckReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assertThat(final Throwable actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(BigDecimal)}
   */
  @CheckReturnValue
  default AbstractBigDecimalAssert<?> assertThat(final BigDecimal actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public static AbstractBigIntegerAssert<?> assertThat(BigInteger actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicBooleanAssert assertThat(AtomicBoolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicIntegerAssert assertThat(AtomicInteger actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create int[] assertion for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicIntegerArrayAssert assertThat(AtomicIntegerArray actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicIntegerFieldUpdater}.
   *
   * @param actual the actual value.
   *
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   */
  default <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> assertThat(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLong}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongAssert assertThat(AtomicLong actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default AtomicLongArrayAssert assertThat(AtomicLongArray actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicLongFieldUpdater}.
   *
   * @param actual the actual value.
   *
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assertion object.
   */
  default <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> assertThat(AtomicLongFieldUpdater<OBJECT> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicReference}.
   *
   * @return the created assertion object.
   */
  default <VALUE> AtomicReferenceAssert<VALUE> assertThat(AtomicReference<VALUE> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicReferenceArray}.
   *
   * @param actual the actual value.
   * @param <ELEMENT> the type of the value contained in the {@link AtomicReferenceArray}.
   *
   * @return the created assertion object.
   */
  default <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> assertThat(AtomicReferenceArray<ELEMENT> actual) {
    return Assertions.assertThat(actual);
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
  default <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> assertThat(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicMarkableReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicMarkableReference}.
   *
   * @return the created assertion object.
   */
  default <VALUE> AtomicMarkableReferenceAssert<VALUE> assertThat(AtomicMarkableReference<VALUE> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link AtomicStampedReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicStampedReference}.
   *
   * @return the created assertion object.
   */
  default <VALUE> AtomicStampedReferenceAssert<VALUE> assertThat(AtomicStampedReference<VALUE> actual) {
    return Assertions.assertThat(actual);
  }
  
  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(CharSequence)}
   */
  @CheckReturnValue
  default AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(final CharSequence actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(short[])}
   */
  @CheckReturnValue
  default AbstractShortArrayAssert<?> assertThat(final short[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Short)}
   */
  @CheckReturnValue
  default AbstractShortAssert<?> assertThat(final Short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Class)}
   */
  @CheckReturnValue
  default AbstractClassAssert<?> assertThat(final Class<?> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Character)}
   */
  @CheckReturnValue
  default AbstractCharacterAssert<?> assertThat(final Character actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(char[])}
   */
  @CheckReturnValue
  default AbstractCharArrayAssert<?> assertThat(final char[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(char)}
   */
  @CheckReturnValue
  default AbstractCharacterAssert<?> assertThat(final char actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Comparable)}
   */
  @CheckReturnValue
  default <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterable)}
   */
  @CheckReturnValue
  default <T> IterableAssert<T> assertThat(final Iterable<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterable, AssertFactory)}
   */
  @CheckReturnValue
  default <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
  FactoryBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(Iterable<? extends ELEMENT> actual,
                                                                                     AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return Assertions.assertThat(actual, assertFactory);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterator)}
   */
  @CheckReturnValue
  default <T> IterableAssert<T> assertThat(final Iterator<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterable, Class)}
   */
  @CheckReturnValue
  default <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
  ClassBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(ACTUAL actual,
                                                                                   Class<ELEMENT_ASSERT> assertClass) {
    return Assertions.assertThat(actual, assertClass);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Boolean)}
   */
  @CheckReturnValue
  default AbstractBooleanAssert<?> assertThat(final Boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(boolean)}
   */
  @CheckReturnValue
  default AbstractBooleanArrayAssert<?> assertThat(final boolean[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(byte)}
   */
  @CheckReturnValue
  default AbstractByteAssert<?> assertThat(final byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Byte)}
   */
  @CheckReturnValue
  default AbstractByteAssert<?> assertThat(final Byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(byte[])}
   */
  @CheckReturnValue
  default AbstractByteArrayAssert<?> assertThat(final byte[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(boolean)}
   */
  @CheckReturnValue
  default AbstractBooleanAssert<?> assertThat(final boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(float)}
   */
  @CheckReturnValue
  default AbstractFloatAssert<?> assertThat(final float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(InputStream)}
   */
  @CheckReturnValue
  default AbstractInputStreamAssert<?, ? extends InputStream> assertThat(final InputStream actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(File)}
   */
  @CheckReturnValue
  default AbstractFileAssert<?> assertThat(final File actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Future)}
   * 
   * @since 3.7.0
   */
  @CheckReturnValue
  default <T> AbstractFutureAssert<?, ? extends Future<? extends T>, T> assertThat(Future<T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Path)}
   */
  @CheckReturnValue
  default AbstractPathAssert<?> assertThat(final Path actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(int[])}
   */
  @CheckReturnValue
  default AbstractIntArrayAssert<?> assertThat(final int[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Float)}
   */
  @CheckReturnValue
  default AbstractFloatAssert<?> assertThat(final Float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(int)}
   */
  @CheckReturnValue
  default AbstractIntegerAssert<?> assertThat(final int actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(float[])}
   */
  @CheckReturnValue
  default AbstractFloatArrayAssert<?> assertThat(final float[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Integer)}
   */
  @CheckReturnValue
  default AbstractIntegerAssert<?> assertThat(final Integer actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(double)}
   */
  @CheckReturnValue
  default AbstractDoubleAssert<?> assertThat(final double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Double)}
   */
  @CheckReturnValue
  default AbstractDoubleAssert<?> assertThat(final Double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(List)}
   */
  @CheckReturnValue
  default <T> ListAssert<? extends T> assertThat(final List<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(List, Class)} )}
   */
  @CheckReturnValue
  default <ELEMENT, ACTUAL extends List<? extends ELEMENT>, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
  ClassBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                               Class<ELEMENT_ASSERT> assertClass) {
    return Assertions.assertThat(actual, assertClass);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(List, AssertFactory)} )}
   */
  @CheckReturnValue
  default <ACTUAL extends List<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
  FactoryBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                 AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return Assertions.assertThat(actual, assertFactory);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(List)}
   */
  @CheckReturnValue
  default <ELEMENT, STREAM extends BaseStream<ELEMENT, STREAM>> AbstractListAssert<?, ? extends List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assertThat(
      final BaseStream<? extends ELEMENT, STREAM> actual) {
    return Assertions.assertThat(actual);
  }
  
  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(double[])}
   */
  @CheckReturnValue
  default AbstractDoubleArrayAssert<?> assertThat(final double[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#extractProperty(String)}
   */
  default Properties<Object> extractProperty(final String propertyName) {
    return Assertions.extractProperty(propertyName);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#extractProperty(String,Class)}
   */
  default <T> Properties<T> extractProperty(final String propertyName, final Class<T> propertyType) {
    return Assertions.extractProperty(propertyName, propertyType);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#tuple(Object[])}
   */
  default Tuple tuple(final Object... values) {
    return Assertions.tuple(values);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#atIndex(int)}
   */
  default Index atIndex(final int actual) {
    return Assertions.atIndex(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#within(Double)}
   */
  default Offset<Double> within(final Double actual) {
    return Assertions.within(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#within(BigDecimal)}
   */
  default Offset<BigDecimal> within(final BigDecimal actual) {
    return Assertions.within(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#within(Float)}
   */
  default Offset<Float> within(final Float actual) {
    return Assertions.within(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#anyOf(Iterable)}
   */
  default <T> Condition<T> anyOf(final Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#anyOf(Condition[])}
   */
  default <T> Condition<T> anyOf(@SuppressWarnings("unchecked") final Condition<? super T>... conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#doesNotHave(Condition)}
   */
  default <T> DoesNotHave<T> doesNotHave(final Condition<? super T> condition) {
    return Assertions.doesNotHave(condition);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#contentOf(File,String)}
   */
  default String contentOf(final File file, final String charsetName) {
    return Assertions.contentOf(file, charsetName);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#contentOf(File)}
   */
  default String contentOf(final File actual) {
    return Assertions.contentOf(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#contentOf(File,Charset)}
   */
  default String contentOf(final File file, final Charset charset) {
    return Assertions.contentOf(file, charset);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#linesOf(File)}
   */
  default List<String> linesOf(final File actual) {
    return Assertions.linesOf(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#linesOf(File,String)}
   */
  default List<String> linesOf(final File file, final String charsetName) {
    return Assertions.linesOf(file, charsetName);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#linesOf(File,Charset)}
   */
  default List<String> linesOf(final File actual, final Charset arg1) {
    return Assertions.linesOf(actual, arg1);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#setRemoveAssertJRelatedElementsFromStackTrace}
   */
  default void setRemoveAssertJRelatedElementsFromStackTrace(final boolean actual) {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#failBecauseExceptionWasNotThrown}
   */
  default void failBecauseExceptionWasNotThrown(final Class<? extends Throwable> exceptionClass) {
    Assertions.failBecauseExceptionWasNotThrown(exceptionClass);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#setAllowExtractingPrivateFields}
   */
  default void setAllowExtractingPrivateFields(final boolean actual) {
    Assertions.setAllowExtractingPrivateFields(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#registerCustomDateFormat(DateFormat)}
   */
  default void registerCustomDateFormat(final DateFormat actual) {
    Assertions.registerCustomDateFormat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#registerCustomDateFormat(String)}
   */
  default void registerCustomDateFormat(final String actual) {
    Assertions.registerCustomDateFormat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#useDefaultDateFormatsOnly}
   */
  default void useDefaultDateFormatsOnly() {
    Assertions.useDefaultDateFormatsOnly();
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(ZonedDateTime)}
   */
  default AbstractZonedDateTimeAssert<?> assertThat(final ZonedDateTime actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(CompletableFuture)}
   */
  default <T> CompletableFutureAssert<T> assertThat(final CompletableFuture<T> future) {
    return Assertions.assertThat(future);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Optional)}
   */
  default <T> OptionalAssert<T> assertThat(final Optional<T> optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(java.util.OptionalDouble)}
   */
  default OptionalDoubleAssert assertThat(final OptionalDouble optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(java.util.OptionalInt)}
   */
  default OptionalIntAssert assertThat(final OptionalInt optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(java.util.OptionalLong)}
   */
  default OptionalLongAssert assertThat(final OptionalLong optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(LocalDateTime)}
   */
  default AbstractLocalDateTimeAssert<?> assertThat(final LocalDateTime localDateTime) {
    return Assertions.assertThat(localDateTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(LocalDate)}
   */
  default AbstractLocalDateAssert<?> assertThat(final LocalDate localDate) {
    return Assertions.assertThat(localDate);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(LocalTime)}
   */
  default AbstractLocalTimeAssert<?> assertThat(final LocalTime localTime) {
    return Assertions.assertThat(localTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(OffsetTime)}
   */
  default AbstractOffsetTimeAssert<?> assertThat(final OffsetTime offsetTime) {
    return Assertions.assertThat(offsetTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(OffsetDateTime)}
   */
  default AbstractOffsetDateTimeAssert<?> assertThat(final OffsetDateTime offsetDateTime) {
    return Assertions.assertThat(offsetDateTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThatThrownBy(ThrowableAssert.ThrowingCallable)}
   */
  default AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(
      final ThrowingCallable shouldRaiseThrowable) {
    return Assertions.assertThatThrownBy(shouldRaiseThrowable);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(ThrowableAssert.ThrowingCallable)}
   */
  default AbstractThrowableAssert<?, ? extends Throwable> assertThat(
      final ThrowingCallable shouldRaiseThrowable) {
    return Assertions.assertThat(shouldRaiseThrowable);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#catchThrowable(ThrowableAssert.ThrowingCallable)}
   */
  default Throwable catchThrowable(final ThrowingCallable shouldRaiseThrowable) {
    return Assertions.catchThrowable(shouldRaiseThrowable);
  }
  
  /**
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}  
   * which allows to chain assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatExceptionOfType(IOException.class).isThrownBy(() -> { throw new IOException("boom!"); })
   *                                       .withMessage("boom!"); </code></pre>
   *
   * This method is more or less the same of {@link #assertThatThrownBy(ThrowableAssert.ThrowingCallable)} but in a more natural way.
   * @param exceptionType the actual value.
   * @return the created {@link ThrowableTypeAssert}.
   */
  default <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(final Class<? extends T> exceptionType) {
      return Assertions.assertThatExceptionOfType(exceptionType);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Predicate)}
   */
  default <T> PredicateAssert<T> assertThat(final Predicate<T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(IntPredicate)}
   */
  default IntPredicateAssert assertThat(final IntPredicate actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(LongPredicate)}
   */
  default LongPredicateAssert assertThat(final LongPredicate actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(DoublePredicate)}
   */
  default DoublePredicateAssert assertThat(final DoublePredicate actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(URL)}
   */
  default AbstractUrlAssert<?> assertThat(final URL actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(URI)}
   */
  default AbstractUriAssert<?> assertThat(final URI actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(AssertProvider)}
   */
  default <T> T assertThat(final AssertProvider<T> component) {
    return Assertions.assertThat(component);
  }

  // --------------------------------------------------------------------------------------------------
  // Filter methods : not assertions but here to have a complete entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#filter(Object[])}
   */
  default <E> Filters<E> filter(final E[] array) {
    return Assertions.filter(array);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#filter(Iterable)}
   */
  default <E> Filters<E> filter(final Iterable<E> iterableToFilter) {
    return Assertions.filter(iterableToFilter);
  }
}
