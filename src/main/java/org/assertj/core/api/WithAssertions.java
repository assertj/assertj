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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
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
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.condition.DoesNotHave;
import org.assertj.core.condition.Not;
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;

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
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(T[])}
   */
  default <T> AbstractObjectArrayAssert<?, T> assertThat(final T[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(AssertDelegateTarget)}
   */
  default <T extends AssertDelegateTarget> T assertThat(final T assertion) {
    return Assertions.assertThat(assertion);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Map)}
   */
  default <K, V> AbstractMapAssert<?, ? extends Map<K, V>, K, V> assertThat(final Map<K, V> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(short)}
   */
  default AbstractShortAssert<?> assertThat(final short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(long)}
   */
  default AbstractLongAssert<?> assertThat(final long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Long)}
   */
  default AbstractLongAssert<?> assertThat(final Long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(long[])}
   */
  default AbstractLongArrayAssert<?> assertThat(final long[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(T)}
   */
  default <T> AbstractObjectAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(String)}
   */
  default AbstractCharSequenceAssert<?, String> assertThat(final String actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Date)}
   */
  default AbstractDateAssert<?> assertThat(final Date actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Throwable)}
   */
  default AbstractThrowableAssert<?, ? extends Throwable> assertThat(final Throwable actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(BigDecimal)}
   */
  default AbstractBigDecimalAssert<?> assertThat(final BigDecimal actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(CharSequence)}
   */
  default AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(final CharSequence actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(short[])}
   */
  default AbstractShortArrayAssert<?> assertThat(final short[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Short)}
   */
  default AbstractShortAssert<?> assertThat(final Short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Class)}
   */
  default AbstractClassAssert<?> assertThat(final Class<?> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Character)}
   */
  default AbstractCharacterAssert<?> assertThat(final Character actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(char[])}
   */
  default AbstractCharArrayAssert<?> assertThat(final char[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(char)}
   */
  default AbstractCharacterAssert<?> assertThat(final char actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Comparable)}
   */
  default <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterable)}
   */
  default <T> AbstractIterableAssert<?, Iterable<? extends T>, T, ObjectAssert<T>> assertThat(
      final Iterable<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterator)}
   */
  default <T> AbstractIterableAssert<?, Iterable<? extends T>, T, ObjectAssert<T>> assertThat(
      final Iterator<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Boolean)}
   */
  default AbstractBooleanAssert<?> assertThat(final Boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(boolean)}
   */
  default AbstractBooleanArrayAssert<?> assertThat(final boolean[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(byte)}
   */
  default AbstractByteAssert<?> assertThat(final byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Byte)}
   */
  default AbstractByteAssert<?> assertThat(final Byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(byte[])}
   */
  default AbstractByteArrayAssert<?> assertThat(final byte[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(boolean)}
   */
  default AbstractBooleanAssert<?> assertThat(final boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(float)}
   */
  default AbstractFloatAssert<?> assertThat(final float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(InputStream)}
   */
  default AbstractInputStreamAssert<?, ? extends InputStream> assertThat(final InputStream actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(File)}
   */
  default AbstractFileAssert<?> assertThat(final File actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(int[])}
   */
  default AbstractIntArrayAssert<?> assertThat(final int[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Float)}
   */
  default AbstractFloatAssert<?> assertThat(final Float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(int)}
   */
  default AbstractIntegerAssert<?> assertThat(final int actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(float[])}
   */
  default AbstractFloatArrayAssert<?> assertThat(final float[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Integer)}
   */
  default AbstractIntegerAssert<?> assertThat(final Integer actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(double)}
   */
  default AbstractDoubleAssert<?> assertThat(final double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Double)}
   */
  default AbstractDoubleAssert<?> assertThat(final Double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(List)}
   */
  default <T> AbstractListAssert<?, List<? extends T>, T, ObjectAssert<T>> assertThat(final List<? extends T> actual) {
    return Assertions.assertThat(actual);
  } 

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(List)}
   */
  default <T> AbstractListAssert<?, ? extends List<? extends T>, T, ObjectAssert<T>> assertThat(
      final Stream<? extends T> actual) {
    return Assertions.assertThat(actual);
  }
  
  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(double[])}
   */
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
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThatThrownBy(ThrowingCallable)}
   */
  default AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(
      final ThrowingCallable shouldRaiseThrowable) {
    return Assertions.assertThatThrownBy(shouldRaiseThrowable);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#catchThrowable(ThrowingCallable)}
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
   * This method is more or less the same of {@link #assertThatThrownBy(ThrowingCallable)} but in a more natural way.
   * @param actual the actual value.
   * @return the created {@link ThrowableTypeAssert}.
   */
  default <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(final Class<? extends T> exceptionType) {
      return Assertions.assertThatExceptionOfType(exceptionType);
  }

  // --------------------------------------------------------------------------------------------------
  // Filter methods : not assertions but here to have a complete entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#filter(E[])}
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
