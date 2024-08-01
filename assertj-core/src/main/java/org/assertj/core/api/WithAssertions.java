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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.DateFormat;
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
import java.time.temporal.TemporalUnit;
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
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.filter.FilterOperator;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.api.filter.InFilter;
import org.assertj.core.api.filter.NotFilter;
import org.assertj.core.api.filter.NotInFilter;
import org.assertj.core.condition.AllOf;
import org.assertj.core.condition.AnyOf;
import org.assertj.core.condition.DoesNotHave;
import org.assertj.core.condition.Not;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.data.TemporalUnitOffset;
import org.assertj.core.description.Description;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.BinaryRepresentation;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.presentation.UnicodeRepresentation;
import org.assertj.core.util.CanIgnoreReturnValue;
import org.assertj.core.util.CheckReturnValue;

/**
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
@CheckReturnValue
public interface WithAssertions extends InstanceOfAssertFactories {

  /**
   * Assertions entry point for float {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, offset(0.2f));</code></pre>
   * @param value the allowed offset
   * @return the created {@code Offset}.
   */
  default Offset<Float> offset(final Float value) {
    return Assertions.offset(value);
  }

  /**
   * Assertions entry point for double {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isEqualTo(8.0, offset(0.1));</code></pre>
   * @param value the allowed offset
   * @return the created {@code Offset}.
   */
  default Offset<Double> offset(final Double value) {
    return Assertions.offset(value);
  }

  /**
   * Only delegate to {@link MapEntry#entry(Object, Object)} so that Assertions offers a full feature entry point to
   * all
   * AssertJ features (but you can use {@link MapEntry} if you prefer).
   * <p>
   * Typical usage is to call <code>entry</code> in MapAssert <code>contains</code> assertion, see examples below :
   *
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = ... // init omitted
   *
   * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));</code></pre>
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param key the key of the entry to create.
   * @param value the value of the entry to create.
   * @return the created {@code MapEntry}.
   */
  default <K, V> MapEntry<K, V> entry(final K key, final V value) {
    return Assertions.entry(key, value);
  }

  /**
   * Throws an {@link AssertionError} with the given message.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("boom")));}.
   * @throws AssertionError with the given message.
   */
  @CanIgnoreReturnValue
  default <T> T fail(final String failureMessage) {
    return Assertions.fail(failureMessage);
  }

  /**
   * Throws an {@link AssertionError} with an empty message to be used in code like:
   * <pre><code class='java'> doSomething(optional.orElseGet(() -> fail()));</code></pre>
   *
   * @param <T> dummy return value type
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail()));}.
   * @throws AssertionError with an empty message.
   * @since 3.26.0
   */
  @CanIgnoreReturnValue
  default <T> T fail() {
    return Assertions.fail();
  }

  /**
   * Throws an {@link AssertionError} with the given message built as {@link String#format(String, Object...)}.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @param args Arguments referenced by the format specifiers in the format string.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("b%s", "oom")));}.
   * @throws AssertionError with the given built message.
   * @since 3.9.0
   */
  @CanIgnoreReturnValue
  default <T> T fail(String failureMessage, Object... args) {
    return Assertions.fail(failureMessage, args);
  }

  /**
   * Throws an {@link AssertionError} with the given message and with the {@link Throwable} that caused the failure.
   *
   * @param <T> dummy return value type
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("boom", cause)));}.
   * @throws AssertionError with the given message and with the {@link Throwable} that caused the failure.
   */
  @CanIgnoreReturnValue
  default <T> T fail(final String failureMessage, final Throwable realCause) {
    return Assertions.fail(failureMessage, realCause);
  }

  /**
   * Throws an {@link AssertionError} with the {@link Throwable} that caused the failure.
   *
   * @param <T> dummy return value type
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail(cause)));}.
   * @throws AssertionError with the {@link Throwable} that caused the failure.
   */
  @CanIgnoreReturnValue
  default <T> T fail(final Throwable realCause) {
    return fail(null, realCause);
  }

  /**
   * Creates a new <code>{@link Not}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The Not condition created.
   */
  default <T> Not<T> not(final Condition<? super T> condition) {
    return Assertions.not(condition);
  }

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AllOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  default <T> Condition<T> allOf(final Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AllOf}.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  @SuppressWarnings("unchecked") // Heap pollution risk. We accept that as we cannot use @SafeVarargs here.
  default <T> Condition<T> allOf(final Condition<? super T>... conditions) {
    return Assertions.allOf(conditions);
  }

  /**
   * Create a new <code>{@link ThrowingConsumer}</code> that delegates the evaluation of the
   * given consumers to {@link AbstractAssert#satisfies(ThrowingConsumer[])}.
   *
   * @param <T> the type of object the given consumers accept
   * @param consumers the consumers to evaluate
   * @return the {@code ThrowingConsumer} instance
   *
   * @since 3.25.0
   */
  @SuppressWarnings("unchecked")
  default <T> ThrowingConsumer<T> allOf(ThrowingConsumer<? super T>... consumers) {
    return Assertions.allOf(consumers);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param <T> the actual's elements type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default <T> ObjectArrayAssert<T> assertThat(final T[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Object2DArrayAssert}</code>.
   *
   * @param <T> the actual's elements type.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default <T> Object2DArrayAssert<T> assertThat(final T[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Returns the given assertion. This method improves code readability by surrounding the given assertion with
   * <code>assertThat</code>.
   * <p>
   * Consider for example the following MyButton and MyButtonAssert classes:
   * <pre><code class='java'> public class MyButton extends JButton {
   *
   *   private boolean blinking;
   *
   *   public boolean isBlinking() { return this.blinking; }
   *
   *   public void setBlinking(boolean blink) { this.blinking = blink; }
   *
   * }
   *
   * private static class MyButtonAssert implements AssertDelegateTarget {
   *
   *   private MyButton button;
   *   MyButtonAssert(MyButton button) { this.button = button; }
   *
   *   void isBlinking() {
   *     // standard assertion from core Assertions.assertThat
   *     assertThat(button.isBlinking()).isTrue();
   *   }
   *
   *   void isNotBlinking() {
   *     // standard assertion from core Assertions.assertThat
   *     assertThat(button.isBlinking()).isFalse();
   *   }
   * }</code></pre>
   *
   * As MyButtonAssert implements AssertDelegateTarget, you can use <code>assertThat(buttonAssert).isBlinking();</code>
   * instead of <code>buttonAssert.isBlinking();</code> to have easier to read assertions:
   * <pre><code class='java'> {@literal @}Test
   * public void AssertDelegateTarget_example() {
   *
   *   MyButton button = new MyButton();
   *   MyButtonAssert buttonAssert = new MyButtonAssert(button);
   *
   *   // you can encapsulate MyButtonAssert assertions methods within assertThat
   *   assertThat(buttonAssert).isNotBlinking(); // same as : buttonAssert.isNotBlinking();
   *
   *   button.setBlinking(true);
   *
   *   assertThat(buttonAssert).isBlinking(); // same as : buttonAssert.isBlinking();
   * }</code></pre>
   *
   * @param <T> the generic type of the user-defined assert.
   * @param assertion the assertion to return.
   * @return the given assertion.
   */
  default <T extends AssertDelegateTarget> T assertThat(final T assertion) {
    return Assertions.assertThat(assertion);
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code>.
   * <p>
   * Returned type is {@link MapAssert} as it overrides method to annotate them with {@link SafeVarargs} avoiding
   * annoying warnings.
   *
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default <K, V> MapAssert<K, V> assertThat(final Map<K, V> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractShortAssert<?> assertThat(final short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractLongAssert<?> assertThat(final long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractLongAssert<?> assertThat(final Long actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractLongArrayAssert<?> assertThat(final long[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Long2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Long2DArrayAssert assertThat(final long[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default <T> ObjectAssert<T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}from a {@link String}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractStringAssert<?> assertThat(final String actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractDateAssert<?> assertThat(final Date actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param <T> the type of the actual throwable.
   * @param actual the actual value.
   * @return the created {@link ThrowableAssert}.
   */
  default <T extends Throwable> AbstractThrowableAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
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
  default AbstractBigIntegerAssert<?> assertThat(BigInteger actual) {
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
   * Create assertion for {@link LongAdder}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  default LongAdderAssert assertThat(LongAdder actual) {
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
   * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(final CharSequence actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuilder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  default AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(final StringBuilder actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  default AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(final StringBuffer actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractShortArrayAssert<?> assertThat(final short[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Short2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Short2DArrayAssert assertThat(final short[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractShortAssert<?> assertThat(final Short actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default ClassAssert assertThat(final Class<?> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractCharacterAssert<?> assertThat(final Character actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractCharArrayAssert<?> assertThat(final char[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Char2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Char2DArrayAssert assertThat(final char[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractCharacterAssert<?> assertThat(final char actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> with standard comparison semantics.
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assertThat(final T actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UniversalComparableAssert}</code> with standard comparison semantics.
   * <p>
   * Use this over {@link #assertThat(Comparable)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <T> AbstractUniversalComparableAssert<?, T> assertThatComparable(Comparable<T> actual) {
    return Assertions.assertThatComparable(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default <T> IterableAssert<T> assertThat(final Iterable<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code>.
   * <p>
   * Use this over {@link #assertThat(Iterable)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <ELEMENT> IterableAssert<ELEMENT> assertThatIterable(Iterable<? extends ELEMENT> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FactoryBasedNavigableIterableAssert}</code> allowing to navigate to any {@code Iterable} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the {@code ELEMENT_ASSERT} parameter of the given
   * {@link AssertFactory AssertFactory&lt;ELEMENT, ELEMENT_ASSERT&gt;} (AssertJ can't figure it out because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newHashSet("frodo", "sam", "pippin");
   *
   * // build an AssertFactory for StringAssert (much nicer with Java 8 lambdas)
   * AssertFactory&lt;String, StringAssert&gt; stringAssertFactory = new AssertFactory&lt;String, StringAssert&gt;() {
   *   {@literal @}Override
   *   public StringAssert createAssert(String string) {
   *     return new StringAssert(string);
   *   }
   * };
   *
   * // assertion succeeds with String assertions chained after first()
   * assertThat(hobbits, stringAssertFactory).first()
   *                                         .startsWith("fro")
   *                                         .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertFactory the factory used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link Iterable} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = Set.of("frodo", "sam", "Pippin");
   * assertThat(hobbits, StringAssert::new).first()
   *                                       .startsWith("fro")
   *                                       .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> assertThat(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *                    .startsWith("fro")
   *                    .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
  default <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>> FactoryBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(Iterable<? extends ELEMENT> actual,
                                                                                                                                                                                                                   AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return Assertions.assertThat(actual, assertFactory);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   * <p>
   * <b>Breaking change in version 3.12.0:</b> this method does not return anymore an {@link IterableAssert} but an {@link IteratorAssert}.<br>
   * In order to access assertions from {@link IterableAssert}, use {@link IteratorAssert#toIterable()}.
   * <p>
   * {@link IteratorAssert} instances have limited assertions because it does not consume iterator's elements.
   * <p>
   * Examples:
   * <pre><code class='java'> Iterator&lt;String&gt; bestBasketBallPlayers = getBestBasketBallPlayers();
   *
   * assertThat(bestBasketBallPlayers).hasNext() // Iterator assertion
   *                                  .toIterable() // switch to Iterable assertions
   *                                  .contains("Jordan", "Magic", "Lebron"); // Iterable assertion </code></pre>
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default <T> IteratorAssert<T> assertThat(final Iterator<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code>.
   * <p>
   * Use this over {@link #assertThat(Iterator)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <ELEMENT> IteratorAssert<ELEMENT> assertThatIterator(Iterator<? extends ELEMENT> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassBasedNavigableIterableAssert}</code> allowing to navigate to any {@code Iterable} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the given {@code assertClass}
   * (AssertJ can't find the element assert type by itself because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newHashSet("frodo", "sam", "pippin");
   *
   * // assertion succeeds with String assertions chained after first()
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertClass the class used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link Iterable} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = Set.of("frodo", "sam", "Pippin");
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> assertThat(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *                    .startsWith("fro")
   *                    .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
  default <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>> ClassBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(ACTUAL actual,
                                                                                                                                                                                                                 Class<ELEMENT_ASSERT> assertClass) {
    return Assertions.assertThat(actual, assertClass);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractBooleanAssert<?> assertThat(final Boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractBooleanArrayAssert<?> assertThat(final boolean[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Boolean2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Boolean2DArrayAssert assertThat(final boolean[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractByteAssert<?> assertThat(final byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractByteAssert<?> assertThat(final Byte actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractByteArrayAssert<?> assertThat(final byte[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Byte2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Byte2DArrayAssert assertThat(final byte[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractBooleanAssert<?> assertThat(final boolean actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractFloatAssert<?> assertThat(final float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractInputStreamAssert<?, ? extends InputStream> assertThat(final InputStream actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractFileAssert<?> assertThat(final File actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.Future}.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   *
   * @return the created assertion object.
   * @since 3.7.0
   */
  default <RESULT> FutureAssert<RESULT> assertThat(Future<RESULT> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of {@link PathAssert}
   *
   * @param actual the path to test
   * @return the created assertion object
   */
  default AbstractPathAssert<?> assertThat(final Path actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of {@link PathAssert}
   * <p>
   * Use this over {@link #assertThat(Path)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   *
   * @param actual the path to test
   * @return the created assertion object
   * @since 3.23.0
   */
  default AbstractPathAssert<?> assertThatPath(Path actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractIntArrayAssert<?> assertThat(final int[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Int2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Int2DArrayAssert assertThat(final int[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractFloatAssert<?> assertThat(final Float actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Float2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Float2DArrayAssert assertThat(final float[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractIntegerAssert<?> assertThat(final int actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractFloatArrayAssert<?> assertThat(final float[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractIntegerAssert<?> assertThat(final Integer actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractDoubleAssert<?> assertThat(final double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractDoubleAssert<?> assertThat(final Double actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.21.0
   */
  default <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> assertThat(final Collection<? extends E> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code>.
   * <p>
   * Use this over {@link #assertThat(Collection)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> assertThatCollection(final Collection<? extends E> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default <T> ListAssert<T> assertThat(final List<? extends T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code>.
   * <p>
   * Use this over {@link #assertThat(List)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <ELEMENT> ListAssert<ELEMENT> assertThatList(List<? extends ELEMENT> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassBasedNavigableListAssert}</code> allowing to navigate to any {@code List} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the given {@code assertClass}
   * (AssertJ can't find the element assert type by itself because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> List&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
   *
   * // assertion succeeds with String assertions chained after first()
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertClass the class used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link List} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> List&lt;String&gt; hobbits = List.of("frodo", "sam", "Pippin");
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> assertThat(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *                    .startsWith("fro")
   *                    .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
  default <ELEMENT, ACTUAL extends List<? extends ELEMENT>, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>> ClassBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                                                                                                                                         Class<ELEMENT_ASSERT> assertClass) {
    return Assertions.assertThat(actual, assertClass);
  }

  /**
   * Creates a new instance of <code>{@link FactoryBasedNavigableListAssert}</code> allowing to navigate to any {@code List} element
   * in order to perform assertions on it.
   * <p>
   * Navigational methods provided:<ul>
   * <li>{@link AbstractIterableAssert#first() first()}</li>
   * <li>{@link AbstractIterableAssert#last() last()}</li>
   * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
   * <li>{@link AbstractIterableAssert#elements(int...) elements(int...)}</li>
   * </ul>
   * <p>
   * The available assertions after navigating to an element depend on the {@code ELEMENT_ASSERT} parameter of the given
   * {@link AssertFactory AssertFactory&lt;ELEMENT, ELEMENT_ASSERT&gt;} (AssertJ can't figure it out because of Java type erasure).
   * <p>
   * Example with {@code String} element assertions:
   * <pre><code class='java'> List&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
   *
   * // build an AssertFactory for StringAssert (much nicer with Java 8 lambdas)
   * AssertFactory&lt;String, StringAssert&gt; stringAssertFactory = new AssertFactory&lt;String, StringAssert&gt;() {
   *   {@literal @}Override
   *   public StringAssert createAssert(String string) {
   *     return new StringAssert(string);
   *   }
   * };
   *
   * // assertion succeeds with String assertions chained after first()
   * assertThat(hobbits, stringAssertFactory).first()
   *                                         .startsWith("fro")
   *                                         .endsWith("do");</code></pre>
   *
   * @param <ACTUAL> The actual type
   * @param <ELEMENT> The actual elements type
   * @param <ELEMENT_ASSERT> The actual elements AbstractAssert type
   * @param actual the actual value.
   * @param assertFactory the factory used to create the elements assert instance.
   * @return the created assertion object.
   * @deprecated
   * This was added to help creating type-specific assertions for the elements of an {@link List} instance.
   * <p>
   * Deprecated way:
   * <pre><code class='java'> List&lt;String&gt; hobbits = List.of("frodo", "sam", "Pippin");
   * assertThat(hobbits, StringAssert::new).first()
   *                                       .startsWith("fro")
   *                                       .endsWith("do");</code></pre>
   *
   * However, there is a better way with {@link InstanceOfAssertFactory} and the corresponding
   * {@link AbstractIterableAssert#first(InstanceOfAssertFactory) first(InstanceOfAssertFactory)}.
   * <p>
   * New way:
   * <pre><code class='java'> assertThat(hobbits).first(STRING) // static import of InstanceOfAssertFactories.STRING
   *                    .startsWith("fro")
   *                    .endsWith("do");</code></pre>
   *
   * The main advantage of the latter is easier discoverability and the use of InstanceOfAssertFactory which is the
   * preferred way to create type-specific assertions in AssertJ API.
   */
  @Deprecated
  default <ACTUAL extends List<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>> FactoryBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> assertThat(List<? extends ELEMENT> actual,
                                                                                                                                                                                                           AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    return Assertions.assertThat(actual, assertFactory);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * <b>Be aware that the {@code Stream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code Stream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link Stream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the Stream as it is converted to a List
   * assertThat(Stream.of(1, 2, 3)).contains(1)
   *                               .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the Stream under test is converted to a List before being compared to the expected Stream:
   * <pre><code class='java'> // FAIL: the Stream under test is converted to a List and compared to a Stream but a List is not a Stream.
   * assertThat(Stream.of(1, 2, 3)).isEqualTo(Stream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the Stream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the Stream to a List
   * Stream&lt;Integer&gt; stream = Stream.of(1, 2, 3);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Stream} value.
   * @return the created assertion object.
   */
  default <ELEMENT> ListAssert<ELEMENT> assertThat(Stream<? extends ELEMENT> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
   * <p>
   * Use this over {@link #assertThat(Stream)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   * <p>
   * <b>Be aware that the {@code Stream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code Stream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link Stream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the Stream as it is converted to a List
   * assertThat(Stream.of(1, 2, 3)).contains(1)
   *                               .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the Stream under test is converted to a List before being compared to the expected Stream:
   * <pre><code class='java'> // FAIL: the Stream under test is converted to a List and compared to a Stream but a List is not a Stream.
   * assertThat(Stream.of(1, 2, 3)).isEqualTo(Stream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the Stream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the Stream to a List
   * Stream&lt;Integer&gt; stream = Stream.of(1, 2, 3);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <ELEMENT> ListAssert<ELEMENT> assertThatStream(Stream<? extends ELEMENT> actual) {
    return assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link DoubleStream}.
   * <p>
   * <b>Be aware that the {@code DoubleStream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code DoubleStream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link DoubleStream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the DoubleStream as it is converted to a List
   * assertThat(DoubleStream.of(1.0, 2.0, 3.0)).contains(1.0)
   *                                           .doesNotContain(42.0);</code></pre>
   * <p>
   * The following assertion fails as the DoubleStream under test is converted to a List before being compared to the expected DoubleStream:
   * <pre><code class='java'> // FAIL: the DoubleStream under test is converted to a List and compared to a DoubleStream but a List is not a DoubleStream.
   * assertThat(DoubleStream.of(1.0, 2.0, 3.0)).isEqualTo(DoubleStream.of(1.0, 2.0, 3.0));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the DoubleStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the DoubleStream to a List
   * DoubleStream stream = DoubleStream.of(1.0, 2.0, 3.0);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link DoubleStream} value.
   * @return the created assertion object.
   */
  default ListAssert<Double> assertThat(DoubleStream actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link LongStream}.
   * <p>
   * <b>Be aware that the {@code LongStream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code LongStream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link LongStream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the LongStream as it is converted to a List
   * assertThat(LongStream.of(1, 2, 3)).contains(1)
   *                                   .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the LongStream under test is converted to a List before being compared to the expected LongStream:
   * <pre><code class='java'> // FAIL: the LongStream under test is converted to a List and compared to a LongStream but a List is not a LongStream.
   * assertThat(LongStream.of(1, 2, 3)).isEqualTo(LongStream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the LongStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the LongStream to a List
   * LongStream stream = LongStream.of(1, 2, 3);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link LongStream} value.
   * @return the created assertion object.
   */
  default ListAssert<Long> assertThat(LongStream actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link IntStream}.
   * <p>
   * <b>Be aware that the {@code IntStream} under test will be converted to a {@code List} when an assertions require to inspect its content.
   * Once this is done the {@code IntStream} can't reused as it would have been consumed.</b>
   * <p>
   * Calling multiple methods on the returned {@link ListAssert} is safe as it only interacts with the {@link List} built from the {@link IntStream}.
   * <p>
   * Examples:
   * <pre><code class='java'> // you can chain multiple assertions on the IntStream as it is converted to a List
   * assertThat(IntStream.of(1, 2, 3)).contains(1)
   *                                  .doesNotContain(42);</code></pre>
   * <p>
   * The following assertion fails as the IntStream under test is converted to a List before being compared to the expected IntStream:
   * <pre><code class='java'> // FAIL: the IntStream under test is converted to a List and compared to a IntStream but a List is not a IntStream.
   * assertThat(IntStream.of(1, 2, 3)).isEqualTo(IntStream.of(1, 2, 3));</code></pre>
   * <p>
   * These assertions succeed as {@code isEqualTo} and {@code isSameAs} checks references which does not require to convert the IntStream to a List.
   * <pre><code class='java'> // The following assertions succeed as it only performs reference checking which does not require to convert the IntStream to a List
   * IntStream stream = IntStream.of(1, 2, 3);
   * assertThat(stream).isEqualTo(stream)
   *                   .isSameAs(stream);</code></pre>
   *
   * @param actual the actual {@link IntStream} value.
   * @return the created assertion object.
   */
  default ListAssert<Integer> assertThat(IntStream actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractDoubleArrayAssert<?> assertThat(final double[] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link Double2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default Double2DArrayAssert assertThat(final double[][] actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
   *
   * <pre><code class='java'> // extract simple property values, as no type has been defined the extracted property will be considered as Object
   * // to define the real property type (here String) use extractProperty(&quot;name&quot;, String.class) instead.
   * assertThat(extractProperty(&quot;name&quot;).from(fellowshipOfTheRing))
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;, &quot;Legolas&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // extracting property works also with user's types (here Race), even though it will be considered as Object
   * // to define the real property type (here String) use extractProperty(&quot;name&quot;, Race.class) instead.
   * assertThat(extractProperty(&quot;race&quot;).from(fellowshipOfTheRing)).contains(HOBBIT, ELF).doesNotContain(ORC);
   *
   * // extract nested property on Race
   * assertThat(extractProperty(&quot;race.name&quot;).from(fellowshipOfTheRing)).contains(&quot;Hobbit&quot;, &quot;Elf&quot;).doesNotContain(&quot;Orc&quot;); </code></pre>
   *
   * @param propertyName the name of the property to be read from the elements of a {@code Iterable}. It may be a nested
   *          property (e.g. "address.street.number").
   * @return the created {@code Properties}.
   * @throws IllegalArgumentException if the given property name is empty.
   * @throws NullPointerException if the given property name is {@code null}.
   */
  default Properties<Object> extractProperty(final String propertyName) {
    return Assertions.extractProperty(propertyName);
  }

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
   *
   * <pre><code class='java'> // extract simple property values having a java standard type (here String)
   * assertThat(extractProperty(&quot;name&quot;, String.class).from(fellowshipOfTheRing))
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;, &quot;Legolas&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // extracting property works also with user's types (here Race)
   * assertThat(extractProperty(&quot;race&quot;, String.class).from(fellowshipOfTheRing))
   *           .contains(HOBBIT, ELF).doesNotContain(ORC);
   *
   * // extract nested property on Race
   * assertThat(extractProperty(&quot;race.name&quot;, String.class).from(fellowshipOfTheRing))
   *           .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *           .doesNotContain(&quot;Orc&quot;);</code></pre>
   * @param <T> the type of value to extract.
   * @param propertyName the name of the property to be read from the elements of a {@code Iterable}. It may be a nested
   *          property (e.g. "address.street.number").
   * @param propertyType the type of property to extract
   * @return the created {@code Properties}.
   * @throws IllegalArgumentException if the given property name is empty.
   * @throws NullPointerException if the given property name is {@code null}.
   */
  default <T> Properties<T> extractProperty(final String propertyName, final Class<T> propertyType) {
    return Assertions.extractProperty(propertyName, propertyType);
  }

  /**
   * Utility method to build nicely a {@link Tuple} when working with {@link IterableAssert#extracting(String...)} or
   * {@link ObjectArrayAssert#extracting(String...)}
   *
   * @param values the values stored in the {@link Tuple}
   * @return the built {@link Tuple}
   */
  default Tuple tuple(final Object... values) {
    return Assertions.tuple(values);
  }

  /**
   * Only delegate to {@link Index#atIndex(int)} so that Assertions offers a full feature entry point to all AssertJ
   * features (but you can use {@link Index} if you prefer).
   * <p>
   * Typical usage :
   * <pre><code class='java'> List&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * assertThat(elvesRings).contains(vilya, atIndex(0)).contains(nenya, atIndex(1)).contains(narya, atIndex(2));</code></pre>
   *
   * @param index the value of the index.
   * @return the created {@code Index}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  default Index atIndex(final int index) {
    return Assertions.atIndex(index);
  }

  /**
   * Alias for {@link #offset(Double)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isCloseTo(8.0, within(0.1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   */
  default Offset<Double> within(final Double value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), within(BigDecimal.ONE));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   */
  default Offset<BigDecimal> within(final BigDecimal value) {
    return Assertions.within(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, within(0.2f));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   */
  default Offset<Float> within(final Float value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for double {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isEqualTo(8.0, offset(0.1));</code></pre>
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Double> withPrecision(Double value) {
    return Assertions.offset(value);
  }

  /**
   * Assertions entry point for float {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, offset(0.2f));</code></pre>
   *
   * @param value the allowed offset
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Float> withPrecision(Float value) {
    return Assertions.offset(value);
  }

  /**
   * Assertions entry point for BigInteger {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigInteger.TEN).isCloseTo(new BigInteger("11"), within(new BigInteger("2")));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<BigInteger> within(BigInteger value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat((byte) 10).isCloseTo((byte) 11, within((byte) 1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Byte> within(Byte value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Integer {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, within(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Integer> within(Integer value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, within(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Short> within(Short value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(5l).isCloseTo(7l, within(2l));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Long> within(Long value) {
    return Assertions.within(value);
  }

  /**
   * Assertions entry point for {@link TemporalUnitOffset} with less than or equal condition
   * to use with isCloseTo temporal assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> LocalTime _07_10 = LocalTime.of(7, 10);
   * LocalTime _07_12 = LocalTime.of(7, 12);
   * assertThat(_07_10).isCloseTo(_07_12, within(5, ChronoUnit.MINUTES));</code></pre>
   *
   * @param value the allowed offset
   * @param unit the {@link TemporalUnit} of the offset
   * @return the created {@code Offset}.
   * @since 3.9.0
   */
  default TemporalUnitOffset within(long value, TemporalUnit unit) {
    return Assertions.within(value, unit);
  }

  /**
   * Syntactic sugar method to use with {@link AbstractDurationAssert#isCloseTo(Duration, Duration)} assertion.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(Duration.ofMinutes(2)).isCloseTo(Duration.ofMinutes(3), withMarginOf(Duration.ofMinutes(1)));</code></pre>
   *
   * @param allowedDifference the allowed difference {@link Duration}.
   * @return the given value.
   */
  default Duration withMarginOf(Duration allowedDifference) {
    return allowedDifference;
  }

  /**
   * Assertions entry point for Double {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11.0).isCloseTo(10.0, withinPercentage(10.0));</code></pre>
   *
   * @param value the required precision percentage
   * @return the created {@code Percentage}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Percentage withinPercentage(Double value) {
    return Assertions.withinPercentage(value);
  }

  /**
   * Assertions entry point for Integer {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11).isCloseTo(10, withinPercentage(10));</code></pre>
   *
   * @param value the required precision percentage
   * @return the created {@code Percentage}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Percentage withinPercentage(Integer value) {
    return Assertions.withinPercentage(value);
  }

  /**
   * Assertions entry point for Long {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11L).isCloseTo(10L, withinPercentage(10L));</code></pre>
   *
   * @param value the required precision percentage
   * @return the created {@code Percentage}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Percentage withinPercentage(Long value) {
    return Assertions.withinPercentage(value);
  }

  /**
   * Alias for {@link #offset(Double)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isCloseTo(8.0, byLessThan(0.1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Double> byLessThan(Double value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, byLessThan(0.2f));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Float> byLessThan(Float value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), byLessThan(BigDecimal.ONE));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<BigDecimal> byLessThan(BigDecimal value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for BigInteger {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigInteger.TEN).isCloseTo(new BigInteger("11"), byLessThan(new BigInteger("2")));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<BigInteger> byLessThan(BigInteger value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat((byte) 10).isCloseTo((byte) 11, byLessThan((byte) 1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Byte> byLessThan(Byte value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Integer {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, byLessThan(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Integer> byLessThan(Integer value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, byLessThan(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Short> byLessThan(Short value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(5l).isCloseTo(7l, byLessThan(2l));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws IllegalArgumentException if the given value is negative.
   * @throws NullPointerException if the given value is {@code null}.
   * @since 3.9.0
   */
  default Offset<Long> byLessThan(Long value) {
    return Assertions.byLessThan(value);
  }

  /**
   * Assertions entry point for {@link TemporalUnitOffset} with strict less than condition
   * to use with {@code isCloseTo} temporal assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> LocalTime _07_10 = LocalTime.of(7, 10);
   * LocalTime _07_12 = LocalTime.of(7, 12);
   * assertThat(_07_10).isCloseTo(_07_12, byLessThan(5, ChronoUnit.MINUTES));</code></pre>
   *
   * @param value the value of the offset.
   * @param unit the {@link TemporalUnit} of the offset.
   * @return the created {@code Offset}.
   * @since 3.9.0
   */
  default TemporalUnitOffset byLessThan(long value, TemporalUnit unit) {
    return Assertions.byLessThan(value, unit);
  }

  /**
   * A syntax sugar to write fluent assertion using {@link ObjectAssert#returns(Object, Function)} and
   * {@link ObjectAssert#doesNotReturn(Object, Function)}.
   * <p>
   * Example:
   * <pre><code class="java"> Jedi yoda = new Jedi("Yoda", "Green");
   * assertThat(yoda).returns("Yoda", from(Jedi::getName))
   *                 .returns(2.4, from(Jedi::getHeight))
   *                 .doesNotReturn(null, from(Jedi::getWeight)); </code></pre>
   *
   * @param extractor A function to extract test subject's property
   * @param <F> Type of test subject
   * @param <T> Type of the property under the assertion
   * @return same instance of {@code extractor}
   * @since 3.9.0
   */
  default <F, T> Function<F, T> from(Function<F, T> extractor) {
    return Assertions.from(extractor);
  }

  /**
   * A syntax sugar to write fluent assertion with methods having an {@link InstanceOfAssertFactory} parameter.
   * <p>
   * Example:
   * <pre><code class="java"> Jedi yoda = new Jedi("Yoda", "Green");
   * assertThat(yoda).extracting(Jedi::getName, as(InstanceOfAssertFactories.STRING))
   *                 .startsWith("Yo");</code></pre>
   *
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @param <T>           the type to use for the cast.
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @return same instance of {@code assertFactory}
   *
   * @see Assertions#as(InstanceOfAssertFactory)
   * @since 3.14.0
   */
  default <T, ASSERT extends AbstractAssert<?, ?>> InstanceOfAssertFactory<T, ASSERT> as(InstanceOfAssertFactory<T, ASSERT> assertFactory) {
    return Assertions.as(assertFactory);
  }

  /**
   * Creates a new <code>{@link AnyOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  default <T> Condition<T> anyOf(final Iterable<? extends Condition<? super T>> conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Only delegate to {@link AnyOf#anyOf(Condition...)} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link AnyOf} if you prefer).
   * <p>
   * Typical usage (<code>jedi</code> and <code>sith</code> are {@link Condition}) :
   *
   * <pre><code class='java'> assertThat(&quot;Vader&quot;).is(anyOf(jedi, sith));</code></pre>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   */
  @SuppressWarnings("unchecked")  // Heap pollution risk. We accept that as we cannot use @SafeVarargs here.
  default <T> Condition<T> anyOf(final Condition<? super T>... conditions) {
    return Assertions.anyOf(conditions);
  }

  /**
   * Create a new <code>{@link ThrowingConsumer}</code> that delegates the evaluation of the
   * given consumers to {@link AbstractAssert#satisfiesAnyOf(ThrowingConsumer[])}.
   *
   * @param <T> the type of object the given consumers accept
   * @param consumers the consumers to evaluate
   * @return the {@code ThrowingConsumer} instance
   *
   * @since 3.25.0
   */
  @SuppressWarnings("unchecked")
  default <T> ThrowingConsumer<T> anyOf(ThrowingConsumer<? super T>... consumers) {
    return Assertions.anyOf(consumers);
  }

  /**
   * Creates a new <code>{@link DoesNotHave}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The DoesNotHave condition created.
   */
  default <T> DoesNotHave<T> doesNotHave(final Condition<? super T> condition) {
    return Assertions.doesNotHave(condition);
  }

  /**
   * Loads the text content of a file, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
   * with {@link #assertThat(File)}.
   * </p>
   *
   * @param file the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws IllegalArgumentException if the given character set is not supported on this platform.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  default String contentOf(final File file, final String charsetName) {
    return Assertions.contentOf(file, charsetName);
  }

  /**
   * Loads the text content of a file with the default character set, so that it can be passed to
   * {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
   * with {@link #assertThat(File)}.
   * </p>
   *
   * @param file the file.
   * @return the content of the file.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  default String contentOf(final File file) {
    return Assertions.contentOf(file);
  }

  /**
   * Loads the text content of a file, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
   * with {@link #assertThat(File)}.
   * </p>
   *
   * @param file the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  default String contentOf(final File file, final Charset charset) {
    return Assertions.contentOf(file, charset);
  }

  /**
   * Loads the text content of a file into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param file the file.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  default List<String> linesOf(final File file) {
    return Assertions.linesOf(file);
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  default List<String> linesOf(final File file, final String charsetName) {
    return Assertions.linesOf(file, charsetName);
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  default List<String> linesOf(final File file, final Charset charset) {
    return Assertions.linesOf(file, charset);
  }

  /**
   * Loads the text content of a file at a given path into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param path the path.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  default List<String> linesOf(final Path path) {
    return Assertions.linesOf(path);
  }

  /**
   * Loads the text content of a file at a given path into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param path the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  default List<String> linesOf(final Path path, final String charsetName) {
    return Assertions.linesOf(path, charsetName);
  }

  /**
   * Loads the text content of a file at a given path into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param path the path.
   * @param charset the character set to use.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  default List<String> linesOf(final Path path, final Charset charset) {
    return Assertions.linesOf(path, charset);
  }

  /**
   * Sets whether we remove elements related to AssertJ from assertion error stack trace.
   *
   * @param removeAssertJRelatedElementsFromStackTrace flag.
   */
  default void setRemoveAssertJRelatedElementsFromStackTrace(final boolean removeAssertJRelatedElementsFromStackTrace) {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * <p>
   * {@link Assertions#shouldHaveThrown(Class)} can be used as a replacement.
   * <p>
   * @param <T> dummy return value type
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> failBecauseExceptionWasNotThrown(IOException.class)));}.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   */
  @CanIgnoreReturnValue
  default <T> T failBecauseExceptionWasNotThrown(final Class<? extends Throwable> throwableClass) {
    return Assertions.failBecauseExceptionWasNotThrown(throwableClass);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * @param <T> dummy return value type
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> shouldHaveThrown(IOException.class)));}.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   * @since 3.9.0
   */
  @CanIgnoreReturnValue
  default <T> T shouldHaveThrown(Class<? extends Throwable> throwableClass) {
    return Assertions.shouldHaveThrown(throwableClass);
  }

  /**
   * In error messages, sets the threshold when iterable/array formatting will on one line (if their String description
   * is less than this parameter) or it will be formatted with one element per line.
   * <p>
   * The following array will be formatted on one line as its length &lt; 80:
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
   *
   * // formatted as:
   *
   * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice"]</code></pre>
   * whereas this array is formatted on multiple lines (one element per line)
   *
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards! (Discworld)");
   *
   * // formatted as:
   *
   * ["A Game of Thrones",
   *  "The Lord of the Rings",
   *  "Assassin's Apprentice",
   *  "Guards! Guards! (Discworld)"]</code></pre>
   *
   * @param maxLengthForSingleLineDescription the maximum length for an iterable/array to be displayed on one line
   * @since 3.9.0
   */
  default void setMaxLengthForSingleLineDescription(int maxLengthForSingleLineDescription) {
    Assertions.setMaxLengthForSingleLineDescription(maxLengthForSingleLineDescription);
  }

  /**
   * Sets the maximum number of elements to display in error messages for iterables, arrays and map .
   * <p>
   * Example with a value of {@code 4}.
   * <p>
   * The following array will be formatted entirely as it's length is &lt;= 4:
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
   * // formatted as:
   * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice"]</code></pre>
   *
   * whereas for this 6 elements array, only the first and last two elements are displayed (4 in total):
   * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards!", "The Lies of Locke Lamora", "Aux Ombres d’Abyme");
   * // formatted as:
   * ["A Game of Thrones", "The Lord of the Rings", ... "The Lies of Locke Lamora", "Aux Ombres d’Abyme"]</code></pre>
   *
   * @param maxElementsForPrinting the maximum elements that would be printed for iterables, arrays and maps.
   * @since 3.9.0
   */
  default void setMaxElementsForPrinting(int maxElementsForPrinting) {
    Assertions.setMaxElementsForPrinting(maxElementsForPrinting);
  }

  /**
   * Sets how many stacktrace elements are included in {@link Throwable} representation (by default this set to 3).
   * <p>
   * Examples:
   * <pre><code class='java'>  static class Test1 {
   *
   *   static void boom() {
   *     Test2.boom2();
   *   }
   *
   *   static class Test2 {
   *     static void boom2() {
   *       throw new RuntimeException();
   *     }
   *   }
   * }</code></pre>
   *
   * {@code Test1.boom()} exception should be represented like this in error messages:
   * <pre><code class='text'> java.lang.RuntimeException
   *  at org.assertj.core.presentation.Test1$Test2.boom2(StandardRepresentation_throwable_format_Test.java:35)
   *  at org.assertj.core.presentation.Test1.boom(StandardRepresentation_throwable_format_Test.java:40);java.lang.RuntimeException
   *  at org.assertj.core.presentation.Test1.lambda$1(StandardRepresentation_throwable_format_Test.java:63)org.assertj.core.util.Throwables_Description_Test$test1$test2.exception_layer_2(Throwables_Description_Test.java:24)
   *  ...(69 remaining lines not displayed - this can be changed with Assertions.setMaxStackTraceElementsDisplayed)org.assertj.core.util.Throwables_Description_Test$test1.exception_layer_1(Throwables_Description_Test.java:30)</code></pre>
   *
   * @param maxStackTraceElementsDisplayed  the maximum number of lines for a stacktrace to be displayed on one throw.
   * @see Configuration
   * @since 3.19.0
   */
  default void setMaxStackTraceElementsDisplayed(int maxStackTraceElementsDisplayed) {
    StandardRepresentation.setMaxStackTraceElementsDisplayed(maxStackTraceElementsDisplayed);
  }

  /**
   * Enable/disable printing assertions description to the console (disabled by default).
   * <p>
   * The printed assertions description include all the successful assertions description and respectively the first failed one for standard assertions and all failed ones for soft assertions.
   * <p>
   * If you want to process the description differently, create a {@link Consumer Consumer&lt;Description&gt;} and register it with {@link #setDescriptionConsumer(Consumer)}.
   *
   * @param printAssertionsDescription whether to print assertions description.
   * @since 3.17.0
   */
  static void setPrintAssertionsDescription(boolean printAssertionsDescription) {
    Assertions.setPrintAssertionsDescription(printAssertionsDescription);
  }

  /**
   * All assertions description will be consumed by the given {@link Consumer Consumer&lt;Description&gt;} allowing for example to record them in a file.
   * <p>
   * The consumed descriptions include all the successful assertions description and respectively the first failed one for standard assertions and all failed ones for soft assertions.
   *
   * @param descriptionConsumer the {@link Description} consumer
   * @since 3.17.0
   */
  static void setDescriptionConsumer(Consumer<Description> descriptionConsumer) {
    Assertions.setDescriptionConsumer(descriptionConsumer);
  }

  /**
   * Globally sets whether
   * <code>{@link org.assertj.core.api.AbstractIterableAssert#extracting(String) IterableAssert#extracting(String)}</code>
   * and
   * <code>{@link org.assertj.core.api.AbstractObjectArrayAssert#extracting(String) ObjectArrayAssert#extracting(String)}</code>
   * should be allowed to extract private fields, if not and they try it fails with exception.
   *
   * @param allowExtractingPrivateFields allow private fields extraction. Default {@code true}.
   */
  default void setAllowExtractingPrivateFields(final boolean allowExtractingPrivateFields) {
    Assertions.setAllowExtractingPrivateFields(allowExtractingPrivateFields);
  }

  /**
   * Globally sets whether the use of private fields is allowed for comparison.
   * The following (incomplete) list of methods will be impacted by this change :
   * <ul>
   * <li>
   * <code>{@link org.assertj.core.api.AbstractIterableAssert#usingElementComparatorOnFields(java.lang.String...)}</code>
   * </li>
   * <li><code>{@link org.assertj.core.api.AbstractObjectAssert#isEqualToComparingFieldByField(Object)}</code></li>
   * </ul>
   *
   * If the value is <code>false</code> and these methods try to compare private fields, it will fail with an exception.
   *
   * @param allowComparingPrivateFields allow private fields comparison. Default {@code true}.
   * @since 3.9.0
   */
  default void setAllowComparingPrivateFields(boolean allowComparingPrivateFields) {
    Assertions.setAllowComparingPrivateFields(allowComparingPrivateFields);
  }

  /**
   * Globally sets whether the extractor considers bare-named property methods like {@code String name()}.
   * Defaults to enabled.
   * @param barenamePropertyMethods whether bare-named property methods are found
   */
  default void setExtractBareNamePropertyMethods(boolean barenamePropertyMethods) {
    Assertions.setExtractBareNamePropertyMethods(barenamePropertyMethods);
  }

  /**
   * Instead of using default strict date/time parsing, it is possible to use lenient parsing mode for default date
   * formats parser to interpret inputs that do not precisely match supported date formats (lenient parsing).
   * <p>
   * With strict parsing, inputs must match exactly date/time format.
   *
   * <p>
   * Example:
   * <pre><code class='java'> final Date date = Dates.parse("2001-02-03");
   * final Date dateTime = parseDatetime("2001-02-03T04:05:06");
   * final Date dateTimeWithMs = parseDatetimeWithMs("2001-02-03T04:05:06.700");
   *
   * Assertions.setLenientDateParsing(true);
   *
   * // assertions will pass
   * assertThat(date).isEqualTo("2001-01-34");
   * assertThat(date).isEqualTo("2001-02-02T24:00:00");
   * assertThat(date).isEqualTo("2001-02-04T-24:00:00.000");
   * assertThat(dateTime).isEqualTo("2001-02-03T04:05:05.1000");
   * assertThat(dateTime).isEqualTo("2001-02-03T04:04:66");
   * assertThat(dateTimeWithMs).isEqualTo("2001-02-03T04:05:07.-300");
   *
   * // assertions will fail
   * assertThat(date).hasSameTimeAs("2001-02-04"); // different date
   * assertThat(dateTime).hasSameTimeAs("2001-02-03 04:05:06"); // leniency does not help here</code></pre>
   *
   * To revert to default strict date parsing, call {@code setLenientDateParsing(false)}.
   *
   * @param value whether lenient parsing mode should be enabled or not
   * @since 3.9.0
   */
  default void setLenientDateParsing(boolean value) {
    Assertions.setLenientDateParsing(value);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is gonna use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
   * <p>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
   * <p>
   * Code examples:
   * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
   * assertThat(date).isEqualTo("2003-04-26");
   *
   * try {
   *   // date with a custom format : failure since the default formats don't match.
   *   assertThat(date).isEqualTo("2003/04/26");
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
   * }
   *
   * // registering a custom date format to make the assertion pass
   * registerCustomDateFormat(new SimpleDateFormat("yyyy/MM/dd")); // registerCustomDateFormat("yyyy/MM/dd") would work to.
   * assertThat(date).isEqualTo("2003/04/26");
   *
   * // the default formats are still available and should work
   * assertThat(date).isEqualTo("2003-04-26");</code></pre>
   *
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   */
  default void registerCustomDateFormat(final DateFormat userCustomDateFormat) {
    Assertions.registerCustomDateFormat(userCustomDateFormat);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is gonna use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
   * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>.
   * <p>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
   * <p>
   * Code examples:
   * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
   * assertThat(date).isEqualTo("2003-04-26");
   *
   * try {
   *   // date with a custom format : failure since the default formats don't match.
   *   assertThat(date).isEqualTo("2003/04/26");
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
   * }
   *
   * // registering a custom date format to make the assertion pass
   * registerCustomDateFormat("yyyy/MM/dd");
   * assertThat(date).isEqualTo("2003/04/26");
   *
   * // the default formats are still available and should work
   * assertThat(date).isEqualTo("2003-04-26");</code></pre>
   *
   * @param userCustomDateFormatPattern the new Date format pattern used for String based Date assertions.
   */
  default void registerCustomDateFormat(final String userCustomDateFormatPattern) {
    Assertions.registerCustomDateFormat(userCustomDateFormatPattern);
  }

  /**
   * Remove all registered custom date formats =&gt; use only the defaults date formats to parse string as date.
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code> (for {@link java.sql.Timestamp} String representation support)</li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   */
  default void useDefaultDateFormatsOnly() {
    Assertions.useDefaultDateFormatsOnly();
  }

  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractZonedDateTimeAssert<?> assertThat(final ZonedDateTime actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletableFuture}.
   *
   * @param future the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletableFuture}.
   *
   * @return the created assertion object.
   */
  default <RESULT> CompletableFutureAssert<RESULT> assertThat(final CompletableFuture<RESULT> future) {
    return Assertions.assertThat(future);
  }

  /**
   * Create assertion for {@link java.util.concurrent.CompletionStage} by converting it to a {@link CompletableFuture} and returning a {@link CompletableFutureAssert}.
   * <p>
   * If the given {@link java.util.concurrent.CompletionStage} is null, the {@link CompletableFuture} in the returned {@link CompletableFutureAssert} will also be null.
   *
   * @param completionStage the actual {@link CompletionStage}.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletionStage}.
   *
   * @return the created assertion object.
   */
  default <RESULT> CompletableFutureAssert<RESULT> assertThat(final CompletionStage<RESULT> completionStage) {
    return Assertions.assertThat(completionStage);
  }

  /**
   * Create assertion for {@link java.util.Optional}.
   *
   * @param optional the actual value.
   * @param <VALUE> the type of the value contained in the {@link java.util.Optional}.
   *
   * @return the created assertion object.
   */
  default <VALUE> OptionalAssert<VALUE> assertThat(final Optional<VALUE> optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param optional the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalDoubleAssert assertThat(final OptionalDouble optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Create assertion for {@link java.util.regex.Matcher}
   *
   *
   * @param matcher the actual matcher.
   * @return the created assertion object.
   */
  default MatcherAssert assertThat(final Matcher matcher) {
    return Assertions.assertThat(matcher);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param optional the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalIntAssert assertThat(final OptionalInt optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Create assertion for {@link java.util.OptionalLong}.
   *
   * @param optional the actual value.
   *
   * @return the created assertion object.
   */
  default OptionalLongAssert assertThat(final OptionalLong optional) {
    return Assertions.assertThat(optional);
  }

  /**
   * Creates a new instance of <code>{@link TemporalAssert}</code>.
   *
   * @param temporal the actual value.
   * @return the created assertion object.
   * @since 3.26.1
   */
  default TemporalAssert assertThatTemporal(final Temporal temporal) {
    return Assertions.assertThatTemporal(temporal);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
   *
   * @param localDateTime the actual value.
   * @return the created assertion object.
   */
  default AbstractLocalDateTimeAssert<?> assertThat(final LocalDateTime localDateTime) {
    return Assertions.assertThat(localDateTime);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateAssert}</code>.
   *
   * @param localDate the actual value.
   * @return the created assertion object.
   */
  default AbstractLocalDateAssert<?> assertThat(final LocalDate localDate) {
    return Assertions.assertThat(localDate);
  }

  /**
   * Creates a new instance of <code>{@link YearMonthAssert}</code>.
   *
   * @param yearMonth the actual value.
   * @return the created assertion object.
   * @since 3.26.0
   */
  default AbstractYearMonthAssert<?> assertThat(final YearMonth yearMonth) {
    return Assertions.assertThat(yearMonth);
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param localTime the actual value.
   * @return the created assertion object.
   */
  default AbstractLocalTimeAssert<?> assertThat(final LocalTime localTime) {
    return Assertions.assertThat(localTime);
  }

  /**
   * Creates a new instance of <code>{@link InstantAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.7.0
   */
  default AbstractInstantAssert<?> assertThat(final Instant actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link DurationAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.15.0
   */
  default AbstractDurationAssert<?> assertThat(final Duration actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link PeriodAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  default AbstractPeriodAssert<?> assertThat(final Period actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param offsetTime the actual value.
   * @return the created assertion object.
   */
  default AbstractOffsetTimeAssert<?> assertThat(final OffsetTime offsetTime) {
    return Assertions.assertThat(offsetTime);
  }

  /**
   * Creates a new instance of <code>{@link OffsetTimeAssert}</code>.
   *
   * @param offsetDateTime the actual value.
   * @return the created assertion object.
   */
  default AbstractOffsetDateTimeAssert<?> assertThat(final OffsetDateTime offsetDateTime) {
    return Assertions.assertThat(offsetDateTime);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} (easier done with lambdas).
   * <p>
   * Example :
   * <pre><code class='java'>  {@literal @}Test
   *  public void testException() {
   *    assertThatThrownBy(() -&gt; { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                              .hasMessageContaining("boom");
   * }</code></pre>
   *
   * If the provided {@link ThrowingCallable} does not raise an exception, an error is immediately thrown,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link #catchThrowable(ThrowingCallable)} as shown below:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error
   * assertThatThrownBy(() -&gt; {}).as("display me")
   *                             .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error
   * Throwable thrown = catchThrowable(() -&gt; {});
   * assertThat(thrown).as("display me")
   *                   .isInstanceOf(Exception.class);</code></pre>
   *
   * Alternatively you can also use <code>assertThatCode(ThrowingCallable)</code> for the test description provided
   * with {@link AbstractAssert#as(String, Object...) as(String, Object...)} to always be honored.
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created {@link ThrowableAssert}.
   */
  @CanIgnoreReturnValue
  default AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(final ThrowingCallable shouldRaiseThrowable) {
    return Assertions.assertThatThrownBy(shouldRaiseThrowable);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} like {@code assertThatThrownBy(ThrowingCallable)} but this method
   * let you set the assertion description the same way you do with {@link AbstractAssert#as(String, Object...) as(String, Object...)}.
   * <p>
   * Example:
   * <pre><code class='java'> {@literal @}Test
   *  public void testException() {
   *    // if this assertion failed (but it doesn't), the error message would start with [Test explosive code]
   *    assertThatThrownBy(() -&gt; { throw new IOException("boom!") }, "Test explosive code")
   *             .isInstanceOf(IOException.class)
   *             .hasMessageContaining("boom");
   * }</code></pre>
   *
   * If the provided {@link ThrowingCallable ThrowingCallable} does not raise an exception, an error is immediately thrown.
   * <p>
   * The test description provided is honored but not the one with {@link AbstractAssert#as(String, Object...) as(String, Object...)}, example:
   * <pre><code class='java'> // assertion will fail but "display me" won't appear in the error message
   * assertThatThrownBy(() -&gt; {}).as("display me")
   *                             .isInstanceOf(Exception.class);
   *
   * // assertion will fail AND "display me" will appear in the error message
   * assertThatThrownBy(() -&gt; {}, "display me")
   *                             .isInstanceOf(Exception.class);</code></pre>
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
   * To use a test description, use {@link #catchThrowable(ThrowingCallable)} as shown below.
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
  default <T> ObjectAssert<T> assertThatObject(T actual) {
    return assertThat(actual);
  }

  /**
   * Uses the given instance as the instance under test for all the assertions expressed as the passed {@link Consumer}.
   * <p>
   * This is useful to avoid repeating getting the instance to test, a bit like a <a href="https://mrhaki.blogspot.com/2009/09/groovy-goodness-with-method.html">with</a> block which turns the target into
   * the equivalent of {@code this} (as  in Groovy for example).
   * <p>
   * Example:
   * <pre><code> assertWith(team.getPlayers().get(0).getStats(),
   *            stats -&gt; assertThat(stats.pointPerGame).isGreaterThan(25.7),
   *            stats -&gt; assertThat(stats.assistsPerGame).isGreaterThan(7.2),
   *            stats -&gt; assertThat(stats.reboundsPerGame).isBetween(9, 12)
   *            );</code></pre>
   * <p>
   * {@code assertWith} is variation of {@link AbstractAssert#satisfies(Consumer[])} hopefully easier to find for some users.
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @param requirements to assert on the actual object - must not be null.
   * @return the created assertion object.
   * @since 3.20.0
   */
  @CanIgnoreReturnValue
  @SuppressWarnings("unchecked")  // Heap pollution risk. We accept that as we cannot use @SafeVarargs here.
  default <T> ObjectAssert<T> assertWith(T actual, Consumer<T>... requirements) {
    return assertThat(actual).satisfies(requirements);
  }

  /**
   * Allows to catch an {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * <p>
   * This caught {@link Throwable} can then be asserted.
   * </p>
   *
   * <p>
   * Example:
   * </p>
   *
   * <pre><code class='java'>{@literal @}Test
   * public void testException() {
   *   // when
   *   Throwable thrown = catchThrowable(() -&gt; { throw new Exception("boom!"); });
   *
   *   // then
   *   assertThat(thrown).isInstanceOf(Exception.class)
   *                     .hasMessageContaining("boom");
   * } </code></pre>
   *
   * @param shouldRaiseThrowable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowableOfType(Class, ThrowingCallable)
   */
  default Throwable catchThrowable(final ThrowingCallable shouldRaiseThrowable) {
    return Assertions.catchThrowable(shouldRaiseThrowable);
  }

  /**
   * Allows catching a {@link Throwable} of a specific type.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown {@code catchThrowableOfType} returns null,
   * otherwise it checks that the caught {@link Throwable} has the specified type then casts it to it before returning it,
   * making it convenient to perform subtype-specific assertions on the result.
   * <p>
   * Example:
   * <pre><code class='java'> class CustomParseException extends Exception {
   *   int line;
   *   int column;
   *
   *   public CustomParseException(String msg, int l, int c) {
   *     super(msg);
   *     line = l;
   *     column = c;
   *   }
   * }
   *
   * CustomParseException e = catchThrowableOfType(() -&gt; { throw new CustomParseException("boom!", 1, 5); },
   *                                               CustomParseException.class);
   * // assertions pass
   * assertThat(e).hasMessageContaining("boom");
   * assertThat(e.line).isEqualTo(1);
   * assertThat(e.column).isEqualTo(5);
   *
   * // fails as CustomParseException is not a RuntimeException
   * catchThrowableOfType(() -&gt; { throw new CustomParseException("boom!", 1, 5); },
   *                      RuntimeException.class);</code></pre>
   *
   * @param <THROWABLE> the {@link Throwable} type.
   * @param shouldRaiseThrowable The lambda with the code that should raise the exception.
   * @param type The type of exception that the code is expected to raise.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.9.0
   * @deprecated use {@link #catchThrowableOfType(Class, ThrowingCallable)} instead.
   */
  @Deprecated
  default <THROWABLE extends Throwable> THROWABLE catchThrowableOfType(ThrowingCallable shouldRaiseThrowable,
                                                                       Class<THROWABLE> type) {
    return catchThrowableOfType(type, shouldRaiseThrowable);
  }

  /**
   * Allows catching a {@link Throwable} of a specific type.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown {@code catchThrowableOfType} returns null,
   * otherwise it checks that the caught {@link Throwable} has the specified type then casts it to it before returning it,
   * making it convenient to perform subtype-specific assertions on the result.
   * <p>
   * Example:
   * <pre><code class='java'> class CustomParseException extends Exception {
   *   int line;
   *   int column;
   *
   *   public CustomParseException(String msg, int l, int c) {
   *     super(msg);
   *     line = l;
   *     column = c;
   *   }
   * }
   *
   * CustomParseException e = catchThrowableOfType(() -&gt; { throw new CustomParseException("boom!", 1, 5); },
   *                                               CustomParseException.class);
   * // assertions pass
   * assertThat(e).hasMessageContaining("boom");
   * assertThat(e.line).isEqualTo(1);
   * assertThat(e.column).isEqualTo(5);
   *
   * // fails as CustomParseException is not a RuntimeException
   * catchThrowableOfType(RuntimeException.class,
   *                      () -&gt; { throw new CustomParseException("boom!", 1, 5); });</code></pre>
   *
   * @param <THROWABLE> the {@link Throwable} type.
   * @param shouldRaiseThrowable The lambda with the code that should raise the exception.
   * @param type The type of exception that the code is expected to raise.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.26.0
   */
  default <THROWABLE extends Throwable> THROWABLE catchThrowableOfType(Class<THROWABLE> type,
                                                                       ThrowingCallable shouldRaiseThrowable) {
    return ThrowableAssert.catchThrowableOfType(type, shouldRaiseThrowable);
  }

  /**
   * Allows catching an instance of {@link Exception}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link Exception} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * Exception exception = catchException(() -&gt; {throw new Exception("boom!");});
   * // assertions succeed
   * assertThat(exception).hasMessage("boom!");
   *
   * // succeeds as catchException returns null when the code does not throw any exceptions
   * assertThat(catchException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an Exception
   * catchException(() -&gt; {throw new Throwable("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default Exception catchException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(Exception.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link RuntimeException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link RuntimeException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * RuntimeException runtimeException = catchRuntimeException(() -&gt; {throw new RuntimeException("boom!");});
   * // assertions succeed
   * assertThat(runtimeException).hasMessage("boom!");
   *
   * // succeeds as catchRuntimeException returns null when the code does not throw any exceptions
   * assertThat(catchRuntimeException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not a RuntimeException
   * catchRuntimeException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default RuntimeException catchRuntimeException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(RuntimeException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link NullPointerException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link RuntimeException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * NullPointerException nullPointerException = catchNullPointerException(() -&gt; {throw new NullPointerException("boom!");});
   * // assertions succeed
   * assertThat(nullPointerException).hasMessage("boom!");
   *
   * // succeeds as catchNullPointerException returns null when the code does not throw any exceptions
   * assertThat(catchNullPointerException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not a NullPointerException
   * catchNullPointerException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default NullPointerException catchNullPointerException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(NullPointerException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IllegalArgumentException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IllegalArgumentException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IllegalArgumentException illegalArgumentException = catchIllegalArgumentException(() -&gt; {throw new IllegalArgumentException("boom!");});
   * // assertions succeed
   * assertThat(illegalArgumentException).hasMessage("boom!");
   *
   * // succeeds as catchNullPointerException returns null when the code does not throw any exceptions
   * assertThat(catchIllegalArgumentException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IllegalArgumentException
   * catchIllegalArgumentException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default IllegalArgumentException catchIllegalArgumentException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IllegalArgumentException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IOException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IOException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IOException iOException = catchIOException(() -&gt; {throw new IOException("boom!");});
   * // assertions succeed
   * assertThat(iOException).hasMessage("boom!");
   *
   * // succeeds as catchIOException returns null when the code does not throw any exceptions
   * assertThat(catchIOException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchIOException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default IOException catchIOException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IOException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link ReflectiveOperationException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link ReflectiveOperationException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * ReflectiveOperationException reflectiveOperationException = catchReflectiveOperationException(() -&gt; {throw new ReflectiveOperationException("boom!");});
   * // assertions succeed
   * assertThat(reflectiveOperationException).hasMessage("boom!");
   *
   * // succeeds as catchReflectiveOperationException returns null when the code does not throw any exceptions
   * assertThat(catchReflectiveOperationException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchReflectiveOperationException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default ReflectiveOperationException catchReflectiveOperationException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(ReflectiveOperationException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IllegalStateException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IllegalStateException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IllegalStateException illegalStateException = catchIllegalStateException(() -&gt; {throw new IllegalStateException("boom!");});
   * // assertions succeed
   * assertThat(illegalStateException).hasMessage("boom!");
   *
   * // succeeds as catchReflectiveOperationException returns null when the code does not throw any exceptions
   * assertThat(catchIllegalStateException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchIllegalStateException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default IllegalStateException catchIllegalStateException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IllegalStateException.class, throwingCallable);
  }

  /**
   * Allows catching an instance of {@link IndexOutOfBoundsException}.
   * <p>
   * A call is made to {@code catchThrowable(ThrowingCallable)}, if no exception is thrown it returns null
   * otherwise it checks that the caught {@link Throwable} is of type {@link IndexOutOfBoundsException} and casts it making it convenient to perform subtype-specific assertions on it.
   * <p>
   * Example:
   * <pre><code class='java'>
   * IndexOutOfBoundsException indexOutOfBoundsException = catchIndexOutOfBoundsException(() -&gt; {throw new IndexOutOfBoundsException("boom!");});
   * // assertions succeed
   * assertThat(indexOutOfBoundsException).hasMessage("boom!");
   *
   * // succeeds as catchIndexOutOfBoundsException returns null when the code does not throw any exceptions
   * assertThat(catchIndexOutOfBoundsException(() -&gt; {})).isNull();
   *
   * // fails as the thrown instance is not an IOException
   * catchIndexOutOfBoundsException(() -&gt; {throw new Exception("boom!");});</code></pre>
   *
   * @param throwingCallable The lambda with the code that should raise the exception.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowingCallable)
   * @since 3.22.0
   */
  default IndexOutOfBoundsException catchIndexOutOfBoundsException(ThrowingCallable throwingCallable) {
    return catchThrowableOfType(IndexOutOfBoundsException.class, throwingCallable);
  }

  /**
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
   * which allows to chain assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatExceptionOfType(IOException.class)
   *           .isThrownBy(() -&gt; { throw new IOException("boom!"); })
   *           .withMessage("boom!"); </code></pre>
   *
   * This method is more or less the same of {@link #assertThatThrownBy(ThrowingCallable)} but in a more natural way.
   *
   * @param <T> the exception type.
   * @param exceptionType the exception type class.
   * @return the created {@link ThrowableTypeAssert}.
   */
  default <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(final Class<? extends T> exceptionType) {
    return Assertions.assertThatExceptionOfType(exceptionType);
  }

  /**
   * Entry point to check that no exception of any type is thrown by a given {@code throwingCallable}.
   * <p>
   * Example:
   * <pre><code class='java'>assertThatNoException().isThrownBy(() -&gt; { System.out.println("OK"); });</code></pre>
   *
   * This method is more or less the same of {@code assertThatCode(...).doesNotThrowAnyException();} but in a more natural way.
   *
   * @return the created {@link NotThrownAssert}.
   * @since 3.17.0
   */
  default NotThrownAssert assertThatNoException() {
    return Assertions.assertThatNoException();
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link NullPointerException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.9.0
   */
  default ThrowableTypeAssert<NullPointerException> assertThatNullPointerException() {
    return assertThatExceptionOfType(NullPointerException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link IllegalArgumentException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.9.0
   */
  default ThrowableTypeAssert<IllegalArgumentException> assertThatIllegalArgumentException() {
    return assertThatExceptionOfType(IllegalArgumentException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link IOException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.9.0
   */
  default ThrowableTypeAssert<IOException> assertThatIOException() {
    return assertThatExceptionOfType(IOException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link IllegalStateException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.9.0
   */
  default ThrowableTypeAssert<IllegalStateException> assertThatIllegalStateException() {
    return assertThatExceptionOfType(IllegalStateException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link Exception}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<Exception> assertThatException() {
    return assertThatExceptionOfType(Exception.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link IndexOutOfBoundsException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<IndexOutOfBoundsException> assertThatIndexOutOfBoundsException() {
    return assertThatExceptionOfType(IndexOutOfBoundsException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link ReflectiveOperationException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<ReflectiveOperationException> assertThatReflectiveOperationException() {
    return assertThatExceptionOfType(ReflectiveOperationException.class);
  }

  /**
   * Alias for {@link #assertThatExceptionOfType(Class)} for {@link RuntimeException}.
   *
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  default ThrowableTypeAssert<RuntimeException> assertThatRuntimeException() {
    return assertThatExceptionOfType(RuntimeException.class);
  }

  /**
   * Create assertion for {@link Predicate}.
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   * @return the created assertion object.
   *
   */
  default <T> PredicateAssert<T> assertThat(final Predicate<T> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link Predicate}.
   * <p>
   * Use this over {@link #assertThat(Iterable)} in case of ambiguous method resolution when the object under test 
   * implements several interfaces Assertj provides <code>assertThat</code> for. 
   *
   * @param actual the actual value.
   * @param <T> the type of the value contained in the {@link Predicate}.
   * @return the created assertion object.
   * @since 3.23.0
   */
  default <T> PredicateAssert<T> assertThatPredicate(Predicate<T> actual) {
    return assertThat(actual);
  }

  /**
   * Create assertion for {@link IntPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default IntPredicateAssert assertThat(final IntPredicate actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link LongPredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default LongPredicateAssert assertThat(final LongPredicate actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Create assertion for {@link DoublePredicate}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default DoublePredicateAssert assertThat(final DoublePredicate actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractUrlAssert<?> assertThat(final URL actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  default AbstractUriAssert<?> assertThat(final URI actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link SpliteratorAssert}</code>.
   *
   * @param <ELEMENT> the type of the elements.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.14.0
   */
  default <ELEMENT> SpliteratorAssert<ELEMENT> assertThat(final Spliterator<ELEMENT> actual) {
    return Assertions.assertThat(actual);
  }

  /**
   * Delegates the creation of the {@link Assert} to the {@link AssertProvider#assertThat()} of the given component.
   *
   * <p>
   * Read the comments on {@link AssertProvider} for an example of its usage.
   * </p>
   *
   * @param <T> the AssertProvider wrapped type.
   * @param component
   *          the component that creates its own assert
   * @return the associated {@link Assert} of the given component
   */
  default <T> T assertThat(final AssertProvider<T> component) {
    return Assertions.assertThat(component);
  }

  // --------------------------------------------------------------------------------------------------
  // Filter methods : not assertions but here to have a complete entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link Filters} if you prefer).
   * <p>
   * Note that the given array is not modified, the filters are performed on an {@link Iterable} copy of the array.
   * <p>
   * Typical usage with {@link Condition} :
   *
   * <pre><code class='java'> assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   * <p>
   * and with filter language based on java bean property :
   *
   * <pre><code class='java'> assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;).greaterThan(7).get())
   *           .containsOnly(james, rose);</code></pre>
   *
   * @param <E> the array elements type.
   * @param array the array to filter.
   * @return the created <code>{@link Filters}</code>.
   */
  default <E> Filters<E> filter(final E[] array) {
    return Assertions.filter(array);
  }

  /**
   * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link Filters} if you prefer).
   * <p>
   * Note that the given {@link Iterable} is not modified, the filters are performed on a copy.
   * <p>
   * Typical usage with {@link Condition} :
   *
   * <pre><code class='java'> assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   * <p>
   * and with filter language based on java bean property :
   *
   * <pre><code class='java'> assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20)
   *                           .and(&quot;assistsPerGame&quot;).greaterThan(7).get())
   *           .containsOnly(james, rose);</code></pre>
   *
   * @param <E> the {@link Iterable} elements type.
   * @param iterableToFilter the {@code Iterable} to filter.
   * @return the created <code>{@link Filters}</code>.
   */
  default <E> Filters<E> filter(final Iterable<E> iterableToFilter) {
    return Assertions.filter(iterableToFilter);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches one of the given values.
   * <p>
   * As often, an example helps:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
   *
   * assertThat(employees).filteredOn("age", in(800, 26))
   *                      .containsOnly(yoda, obiwan, luke);</code></pre>
   *
   * @param values values to match (one match is sufficient)
   * @return the created "in" filter
   * @since 3.9.0
   */
  default InFilter in(Object... values) {
    return Assertions.in(values);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches does not match any of the given values.
   * <p>
   * As often, an example helps:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
   *
   * assertThat(employees).filteredOn("age", notIn(800, 50))
   *                      .containsOnly(luke);</code></pre>
   *
   * @param valuesNotToMatch values not to match (none of the values must match)
   * @return the created "not in" filter
   * @since 3.9.0
   */
  default NotInFilter notIn(Object... valuesNotToMatch) {
    return Assertions.notIn(valuesNotToMatch);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches does not match the given value.
   * <p>
   * As often, an example helps:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
   *
   * assertThat(employees).filteredOn("age", not(800))
   *                      .containsOnly(luke, noname);</code></pre>
   *
   * @param valueNotToMatch the value not to match
   * @return the created "not" filter
   * @since 3.9.0
   */
  default NotFilter not(Object valueNotToMatch) {
    return Assertions.not(valueNotToMatch);
  }

  // --------------------------------------------------------------------------------------------------
  // URL/Resource methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Loads the text content of a URL, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire contents in memory.
   * </p>
   *
   * @param url the URL.
   * @param charset the character set to use.
   * @return the content of the URL.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   * @since 3.9.0
   */
  default String contentOf(URL url, Charset charset) {
    return Assertions.contentOf(url, charset);
  }

  /**
   * Loads the text content of a URL, so that it can be passed to {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire contents in memory.
   * </p>
   *
   * @param url the URL.
   * @param charsetName the name of the character set to use.
   * @return the content of the URL.
   * @throws IllegalArgumentException if the given character set is not supported on this platform.
   * @throws UncheckedIOException if an I/O exception occurs.
   * @since 3.9.0
   */
  default String contentOf(URL url, String charsetName) {
    return Assertions.contentOf(url, charsetName);
  }

  /**
   * Loads the text content of a URL with the default character set, so that it can be passed to
   * {@link #assertThat(String)}.
   * <p>
   * Note that this will load the entire file in memory; for larger files.
   * </p>
   *
   * @param url the URL.
   * @return the content of the file.
   * @throws UncheckedIOException if an I/O exception occurs.
   * @since 3.9.0
   */
  default String contentOf(URL url) {
    return Assertions.contentOf(url);
  }

  /**
   * Loads the text content of a URL into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param url the URL.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   * @since 3.9.0
   */
  default List<String> linesOf(URL url) {
    return Assertions.linesOf(url);
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   * @since 3.9.0
   */
  default List<String> linesOf(URL url, Charset charset) {
    return Assertions.linesOf(url, charset);
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   * @since 3.9.0
   */
  default List<String> linesOf(URL url, String charsetName) {
    return Assertions.linesOf(url, charsetName);
  }

  /**
   * Register a {@link Representation} that will be used in all following assertions.
   * <p>
   * {@link Representation} are used to format types in assertions error messages.
   * <p>
   * An alternative way of using a different representation is to register one as a service,
   * this approach is described in {@link Representation}, it requires more work than this method
   * but has the advantage of not having to do anything in your tests and it would be applied to all the tests globally
   * <p>
   * Example :
   * <pre><code class='java'> private class Example {}
   *
   * private class CustomRepresentation extends StandardRepresentation {
   *
   *   // override needed to hook specific formatting
   *   {@literal @}Override
   *   public String toStringOf(Object o) {
   *     if (o instanceof Example) return "Example";
   *     // fallback to default formatting.
   *     return super.toStringOf(o);
   *   }
   *
   *   // change String representation
   *   {@literal @}Override
   *   protected String toStringOf(String s) {
   *     return "$" + s + "$";
   *   }
   * }
   *
   * Assertions.useRepresentation(new CustomRepresentation());
   *
   * // this assertion fails ...
   * assertThat(new Example()).isNull();
   * // ... with error :
   * // "expected:&lt;[null]&gt; but was:&lt;[Example]&gt;"
   *
   * // this one fails ...
   * assertThat("foo").startsWith("bar");
   * // ... with error :
   * // Expecting:
   * //   &lt;$foo$&gt;
   * // to start with:
   * //   &lt;$bar$&gt;</code></pre>
   *
   * @param customRepresentation the {@link Representation} to use
   * @since 3.9.0
   */
  default void useRepresentation(Representation customRepresentation) {
    Assertions.useRepresentation(customRepresentation);
  }

  /**
   * Assertions error messages uses a {@link Representation} to format the different types involved, using this method
   * you can control the formatting of a given type by providing a specific formatter.
   *
   *
   * <p>
   * Registering a formatter makes it available for all AssertJ {@link Representation}:
   * <ul>
   * <li>{@link StandardRepresentation}</li>
   * <li>{@link UnicodeRepresentation}</li>
   * <li>{@link HexadecimalRepresentation}</li>
   * <li>{@link BinaryRepresentation}</li>
   * </ul>
   * <p>
   * Example :
   * <pre><code class='java'> // without specific formatter
   * assertThat(STANDARD_REPRESENTATION.toStringOf(123L)).isEqualTo("123L");
   *
   * // register a formatter for Long
   * Assertions.registerFormatterForType(Long.class, value -&gt; "$" + value + "$");
   *
   * // now Long will be formatted between in $$ in error message.
   * assertThat(STANDARD_REPRESENTATION.toStringOf(longNumber)).isEqualTo("$123$");
   *
   * // fails with error : expected:&lt;$456$&gt; but was:&lt;$123$&gt;
   * assertThat(123L).isEqualTo(456L);</code></pre>
   *
   * @param <T> the type of format.
   * @param type the class of the type to format
   * @param formatter the formatter {@link Function}
   * @since 3.9.0
   */
  default <T> void registerFormatterForType(Class<T> type, Function<T, String> formatter) {
    Assertions.registerFormatterForType(type, formatter);
  }

  /**
   * Fallback to use {@link StandardRepresentation} to revert the effect of calling {@link #useRepresentation(Representation)}.
   * @since 3.9.0
   */
  default void useDefaultRepresentation() {
    Assertions.useDefaultRepresentation();
  }
}
