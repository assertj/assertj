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
 * Copyright 2012-2015 the original author or authors.
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
  default public Offset<Float> offset(final Float value) {
    return Assertions.offset(value);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#offset(Double)}
   */
  default public Offset<Double> offset(final Double value) {
    return Assertions.offset(value);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#entry(Object, Object)}
   */
  default public <K, V> MapEntry<K, V> entry(final K key, final V value) {
    return Assertions.entry(key, value);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#fail(String)}
   */
  default public void fail(final String failureMessage) {
    Assertions.fail(failureMessage);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#fail(String,Throwable)}
   */
  default public void fail(final String failureMessage, final Throwable realCause) {
    Assertions.fail(failureMessage, realCause);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#not(Condition)}
   */
  default public <T> Not<T> not(final Condition<? super T> condition) {
    return Assertions.not(condition);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#allOf(Iterable<? extends Condition>)}
   */
  default public <T> Condition<T> allOf(final Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#allOf(Condition[])}
   */
  default public <T> Condition<T> allOf(@SuppressWarnings("unchecked") final Condition<? super T>... conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(T[])}
   */
  default public <T> AbstractObjectArrayAssert<?, T> assertThat(final T[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(AssertDelegateTarget)}
   */
  default public <T extends AssertDelegateTarget> T assertThat(final T assertion) {
    return Assertions.assertThat(assertion);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Map)}
   */
  default public <K, V> AbstractMapAssert<?, ? extends Map<K, V>, K, V> assertThat(final Map<K, V> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(short)}
   */
  default public AbstractShortAssert<?> assertThat(final short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(long)}
   */
  default public AbstractLongAssert<?> assertThat(final long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Long)}
   */
  default public AbstractLongAssert<?> assertThat(final Long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(long[])}
   */
  default public AbstractLongArrayAssert<?> assertThat(final long[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(T)}
   */
  default public <T> AbstractObjectAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(String)}
   */
  default public AbstractCharSequenceAssert<?, String> assertThat(final String actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Date)}
   */
  default public AbstractDateAssert<?> assertThat(final Date actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Throwable)}
   */
  default public AbstractThrowableAssert<?, ? extends Throwable> assertThat(final Throwable actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(BigDecimal)}
   */
  default public AbstractBigDecimalAssert<?> assertThat(final BigDecimal actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(CharSequence)}
   */
  default public AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(final CharSequence actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(short[])}
   */
  default public AbstractShortArrayAssert<?> assertThat(final short[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Short)}
   */
  default public AbstractShortAssert<?> assertThat(final Short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Class)}
   */
  default public AbstractClassAssert<?> assertThat(final Class<?> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Character)}
   */
  default public AbstractCharacterAssert<?> assertThat(final Character actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(char[])}
   */
  default public AbstractCharArrayAssert<?> assertThat(final char[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(char)}
   */
  default public AbstractCharacterAssert<?> assertThat(final char actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Comparable)}
   */
  default public <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterable)}
   */
  @SuppressWarnings("unchecked")
  default public <T> AbstractIterableAssert<?, ? extends Iterable<T>, T> assertThat(final Iterable<T> actual) {
    return (AbstractIterableAssert<?, ? extends Iterable<T>, T>) Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Iterator)}
   */
  @SuppressWarnings("unchecked")
  default public <T> AbstractIterableAssert<?, ? extends Iterable<T>, T> assertThat(final Iterator<T> actual) {
    return (AbstractIterableAssert<?, ? extends Iterable<T>, T>) Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Boolean)}
   */
  default public AbstractBooleanAssert<?> assertThat(final Boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(boolean)}
   */
  default public AbstractBooleanArrayAssert<?> assertThat(final boolean[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(byte)}
   */
  default public AbstractByteAssert<?> assertThat(final byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Byte)}
   */
  default public AbstractByteAssert<?> assertThat(final Byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(byte[])}
   */
  default public AbstractByteArrayAssert<?> assertThat(final byte[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(boolean)}
   */
  default public AbstractBooleanAssert<?> assertThat(final boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(float)}
   */
  default public AbstractFloatAssert<?> assertThat(final float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(InputStream)}
   */
  default public AbstractInputStreamAssert<?, ? extends InputStream> assertThat(final InputStream actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(File)}
   */
  default public AbstractFileAssert<?> assertThat(final File actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(int[])}
   */
  default public AbstractIntArrayAssert<?> assertThat(final int[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Float)}
   */
  default public AbstractFloatAssert<?> assertThat(final Float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(int)}
   */
  default public AbstractIntegerAssert<?> assertThat(final int actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(float[])}
   */
  default public AbstractFloatArrayAssert<?> assertThat(final float[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Integer)}
   */
  default public AbstractIntegerAssert<?> assertThat(final Integer actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(double)}
   */
  default public AbstractDoubleAssert<?> assertThat(final double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Double)}
   */
  default public AbstractDoubleAssert<?> assertThat(final Double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(List)}
   */
  @SuppressWarnings("unchecked")
  default public <T> AbstractListAssert<?, ? extends List<T>, T> assertThat(final List<T> actual) {
    return (AbstractListAssert<?, ? extends List<T>, T>) Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(double[])}
   */
  default public AbstractDoubleArrayAssert<?> assertThat(final double[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#extractProperty(String)}
   */
  default public Properties<Object> extractProperty(final String propertyName) {
    return Assertions.extractProperty(propertyName);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#extractProperty(String,Class)}
   */
  default public <T> Properties<T> extractProperty(final String propertyName, final Class<T> propertyType) {
    return Assertions.extractProperty(propertyName, propertyType);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#tuple(Object[])}
   */
  default public Tuple tuple(final Object... values) {
    return Assertions.tuple(values);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#atIndex(int)}
   */
  default public Index atIndex(final int actual) {
    return Assertions.atIndex(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#within(Double)}
   */
  default public Offset<Double> within(final Double actual) {
    return Assertions.within(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#within(BigDecimal)}
   */
  default public Offset<BigDecimal> within(final BigDecimal actual) {
    return Assertions.within(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#within(Float)}
   */
  default public Offset<Float> within(final Float actual) {
    return Assertions.within(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#anyOf(Iterable)}
   */
  default public <T> Condition<T> anyOf(final Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#anyOf(Condition[])}
   */
  default public <T> Condition<T> anyOf(@SuppressWarnings("unchecked") final Condition<? super T>... conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#doesNotHave(Condition)}
   */
  default public <T> DoesNotHave<T> doesNotHave(final Condition<? super T> condition) {
    return Assertions.doesNotHave(condition);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#contentOf(File,String)}
   */
  default public String contentOf(final File file, final String charsetName) {
    return Assertions.contentOf(file, charsetName);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#contentOf(File)}
   */
  default public String contentOf(final File actual) {
    return Assertions.contentOf(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#contentOf(File,Charset)}
   */
  default public String contentOf(final File file, final Charset charset) {
    return Assertions.contentOf(file, charset);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#linesOf(File)}
   */
  default public List<String> linesOf(final File actual) {
    return Assertions.linesOf(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#linesOf(File,String)}
   */
  default public List<String> linesOf(final File file, final String charsetName) {
    return Assertions.linesOf(file, charsetName);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#linesOf(File,Charset)}
   */
  default public List<String> linesOf(final File actual, final Charset arg1) {
    return Assertions.linesOf(actual, arg1);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#setRemoveAssertJRelatedElementsFromStackTrace}
   */
  default public void setRemoveAssertJRelatedElementsFromStackTrace(final boolean actual) {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#failBecauseExceptionWasNotThrown}
   */
  default public void failBecauseExceptionWasNotThrown(final Class<? extends Throwable> exceptionClass) {
    Assertions.failBecauseExceptionWasNotThrown(exceptionClass);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#setAllowExtractingPrivateFields}
   */
  default public void setAllowExtractingPrivateFields(final boolean actual) {
    Assertions.setAllowExtractingPrivateFields(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#registerCustomDateFormat(DateFormat)}
   */
  default public void registerCustomDateFormat(final DateFormat actual) {
    Assertions.registerCustomDateFormat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#registerCustomDateFormat(String)}
   */
  default public void registerCustomDateFormat(final String actual) {
    Assertions.registerCustomDateFormat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#useDefaultDateFormatsOnly}
   */
  default public void useDefaultDateFormatsOnly() {
    Assertions.useDefaultDateFormatsOnly();
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(ZonedDateTime)}
   */
  default public AbstractZonedDateTimeAssert<?> assertThat(final ZonedDateTime actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(CompletableFuture)}
   */
  default public <T> CompletableFutureAssert<T> assertThat(final CompletableFuture<T> future) {
    return Assertions.assertThat(future);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(Optional)}
   */
  default public <T> OptionalAssert<T> assertThat(final Optional<T> optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(java.util.OptionalDouble)}
   */
  default public OptionalDoubleAssert assertThat(final OptionalDouble optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(java.util.OptionalInt)}
   */
  default public OptionalIntAssert assertThat(final OptionalInt optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(java.util.OptionalLong)}
   */
  default public OptionalLongAssert assertThat(final OptionalLong optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(LocalDateTime)}
   */
  default public AbstractLocalDateTimeAssert<?> assertThat(final LocalDateTime localDateTime) {
    return Assertions.assertThat(localDateTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(LocalDate)}
   */
  default public AbstractLocalDateAssert<?> assertThat(final LocalDate localDate) {
    return Assertions.assertThat(localDate);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(LocalTime)}
   */
  default public AbstractLocalTimeAssert<?> assertThat(final LocalTime localTime) {
    return Assertions.assertThat(localTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(OffsetTime)}
   */
  default public AbstractOffsetTimeAssert<?> assertThat(final OffsetTime offsetTime) {
    return Assertions.assertThat(offsetTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThat(OffsetDateTime)}
   */
  default public AbstractOffsetDateTimeAssert<?> assertThat(final OffsetDateTime offsetDateTime) {
    return Assertions.assertThat(offsetDateTime);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#assertThatThrownBy(ThrowingCallable)}
   */
  default public AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(final ThrowingCallable shouldRaiseThrowable) {
    return Assertions.assertThatThrownBy(shouldRaiseThrowable);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#catchThrowable(ThrowingCallable)}
   */
  default public Throwable catchThrowable(final ThrowingCallable shouldRaiseThrowable) {
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
  default public <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(final Class<? extends T> exceptionType) {
      return Assertions.assertThatExceptionOfType(exceptionType);
  }

  // --------------------------------------------------------------------------------------------------
  // Filter methods : not assertions but here to have a complete entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#filter(E[])}
   */
  default public <E> Filters<E> filter(final E[] array) {
    return Assertions.filter(array);
  }

  /**
   * Delegate call to {@link org.assertj.core.api.Assertions#filter(Iterable)}
   */
  default public <E> Filters<E> filter(final Iterable<E> iterableToFilter) {
    return Assertions.filter(iterableToFilter);
  }
}
