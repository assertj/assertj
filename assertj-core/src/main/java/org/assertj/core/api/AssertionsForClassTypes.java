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

import static org.assertj.core.data.Percentage.withPercentage;

import java.io.File;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;

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
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CanIgnoreReturnValue;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.Files;
import org.assertj.core.util.Paths;
import org.assertj.core.util.URLs;
import org.assertj.core.util.introspection.FieldSupport;

/**
 * Java 8 is picky when choosing the right <code>assertThat</code> method if the object under test is generic and bounded,
 * for example if foo is instance of T that extends Exception, java 8  will complain that it can't resolve
 * the proper <code>assertThat</code> method (normally <code>assertThat(Throwable)</code> as foo might implement an interface like List,
 * if that occurred <code>assertThat(List)</code> would also be a possible choice - thus confusing java 8.
 * <p>
 * This why {@link Assertions} have been split in {@link AssertionsForClassTypes} and {@link AssertionsForInterfaceTypes}
 * (see http://stackoverflow.com/questions/29499847/ambiguous-method-in-java-8-why).
 */
@CheckReturnValue
public class AssertionsForClassTypes {

  /**
   * Create assertion for {@link java.util.concurrent.CompletableFuture}.
   *
   * @param actual the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.CompletableFuture}.
   *
   * @return the created assertion object.
   */
  public static <RESULT> CompletableFutureAssert<RESULT> assertThat(CompletableFuture<RESULT> actual) {
    return new CompletableFutureAssert<>(actual);
  }

  /**
   * Create assertion for {@link java.util.Optional}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link java.util.Optional}.
   *
   * @return the created assertion object.
   */
  public static <VALUE> OptionalAssert<VALUE> assertThat(Optional<VALUE> actual) {
    return new OptionalAssert<>(actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalDouble}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  public static OptionalDoubleAssert assertThat(OptionalDouble actual) {
    return new OptionalDoubleAssert(actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  public static OptionalIntAssert assertThat(OptionalInt actual) {
    return new OptionalIntAssert(actual);
  }

  /**
   * Create assertion for {@link java.util.regex.Matcher}
   *
   * @param actual the actual value
   *
   * @return the created assertion object
   */
  public static MatcherAssert assertThat(Matcher actual) {
    return new MatcherAssert(actual);
  }

  /**
   * Create assertion for {@link java.util.OptionalInt}.
   *
   * @param actual the actual value.
   *
   * @return the created assertion object.
   */
  public static OptionalLongAssert assertThat(OptionalLong actual) {
    return new OptionalLongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBigDecimalAssert<?> assertThat(BigDecimal actual) {
    return new BigDecimalAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractUriAssert<?> assertThat(URI actual) {
    return new UriAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractUrlAssert<?> assertThat(URL actual) {
    return new UrlAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanAssert<?> assertThat(boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanAssert<?> assertThat(Boolean actual) {
    return new BooleanAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractBooleanArrayAssert<?> assertThat(boolean[] actual) {
    return new BooleanArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Boolean2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Boolean2DArrayAssert assertThat(boolean[][] actual) {
    return new Boolean2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteAssert<?> assertThat(byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteAssert<?> assertThat(Byte actual) {
    return new ByteAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractByteArrayAssert<?> assertThat(byte[] actual) {
    return new ByteArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Byte2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Byte2DArrayAssert assertThat(byte[][] actual) {
    return new Byte2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharacterAssert<?> assertThat(char actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharArrayAssert<?> assertThat(char[] actual) {
    return new CharArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Char2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Char2DArrayAssert assertThat(char[][] actual) {
    return new Char2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractCharacterAssert<?> assertThat(Character actual) {
    return new CharacterAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static ClassAssert assertThat(Class<?> actual) {
    return new ClassAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleAssert<?> assertThat(double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleAssert<?> assertThat(Double actual) {
    return new DoubleAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDoubleArrayAssert<?> assertThat(double[] actual) {
    return new DoubleArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Double2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Double2DArrayAssert assertThat(double[][] actual) {
    return new Double2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFileAssert<?> assertThat(File actual) {
    return new FileAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractInputStreamAssert<?, ? extends InputStream> assertThat(InputStream actual) {
    return new InputStreamAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatAssert<?> assertThat(float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatAssert<?> assertThat(Float actual) {
    return new FloatAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractFloatArrayAssert<?> assertThat(float[] actual) {
    return new FloatArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Float2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Float2DArrayAssert assertThat(float[][] actual) {
    return new Float2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntegerAssert<?> assertThat(int actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntArrayAssert<?> assertThat(int[] actual) {
    return new IntArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Int2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Int2DArrayAssert assertThat(int[][] actual) {
    return new Int2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractIntegerAssert<?> assertThat(Integer actual) {
    return new IntegerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongAssert<?> assertThat(long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongAssert<?> assertThat(Long actual) {
    return new LongAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLongArrayAssert<?> assertThat(long[] actual) {
    return new LongArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Long2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Long2DArrayAssert assertThat(long[][] actual) {
    return new Long2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code>.
   *
   * @param <T> the actual value type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ObjectAssert<T> assertThat(T actual) {
    return new ObjectAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
   *
   * @param <T> the actual elements type.
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static <T> ObjectArrayAssert<T> assertThat(T[] actual) {
    return new ObjectArrayAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link Object2DArrayAssert}</code>.
   *
   * @param <T> the actual elements type.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static <T> Object2DArrayAssert<T> assertThat(T[][] actual) {
    return new Object2DArrayAssert<>(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortAssert<?> assertThat(short actual) {
    return new ShortAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortAssert<?> assertThat(Short actual) {
    return new ShortAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractShortArrayAssert<?> assertThat(short[] actual) {
    return new ShortArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link Short2DArrayAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static Short2DArrayAssert assertThat(short[][] actual) {
    return new Short2DArrayAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuilder}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(StringBuilder actual) {
    return new CharSequenceAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(StringBuffer actual) {
    return new CharSequenceAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractStringAssert<?> assertThat(String actual) {
    return new StringAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractDateAssert<?> assertThat(Date actual) {
    return new DateAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractZonedDateTimeAssert<?> assertThat(ZonedDateTime actual) {
    return new ZonedDateTimeAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLocalDateTimeAssert<?> assertThat(LocalDateTime actual) {
    return new LocalDateTimeAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link java.time.OffsetDateTime}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractOffsetDateTimeAssert<?> assertThat(OffsetDateTime actual) {
    return new OffsetDateTimeAssert(actual);
  }

  /**
   * Create assertion for {@link java.time.OffsetTime}.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractOffsetTimeAssert<?> assertThat(OffsetTime actual) {
    return new OffsetTimeAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static AbstractLocalTimeAssert<?> assertThat(LocalTime actual) {
    return new LocalTimeAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link LocalDateAssert}</code>.
   *
   * @param localDate the actual value.
   * @return the created assertion object.
   */
  public static AbstractLocalDateAssert<?> assertThat(LocalDate localDate) {
    return new LocalDateAssert(localDate);
  }

  /**
   * Creates a new instance of <code>{@link YearMonthAssert}</code>.
   *
   * @param yearMonth the actual value.
   * @return the created assertion object.
   * @since 3.26.0
   */
  public static AbstractYearMonthAssert<?> assertThat(YearMonth yearMonth) {
    return new YearMonthAssert(yearMonth);
  }

  /**
   * Creates a new instance of <code>{@link InstantAssert}</code>.
   *
   * @param instant the actual value.
   * @return the created assertion object.
   * @since 3.7.0
   */
  public static AbstractInstantAssert<?> assertThat(Instant instant) {
    return new InstantAssert(instant);
  }

  /**
   * Creates a new instance of <code>{@link DurationAssert}</code>.
   *
   * @param duration the actual value.
   * @return the created assertion object.
   * @since 3.15.0
   */
  public static AbstractDurationAssert<?> assertThat(Duration duration) {
    return new DurationAssert(duration);
  }

  /**
   * Creates a new instance of <code>{@link PeriodAssert}</code>.
   *
   * @param period the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static AbstractPeriodAssert<?> assertThat(Period period) {
    return new PeriodAssert(period);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code>.
   *
   * @param <T> the type of the actual throwable.
   * @param actual the actual value.
   * @return the created {@link ThrowableAssert}.
   */
  public static <T extends Throwable> AbstractThrowableAssert<?, T> assertThat(T actual) {
    return new ThrowableAssert<>(actual);
  }

  /**
   * Allows to capture and then assert on a {@link Throwable} (easier done with lambdas).
   * <p>
   * Java 8 example :
   * <pre><code class='java'> {@literal @}Test
   *  public void testException() {
   *    assertThatThrownBy(() -&gt; { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                              .hasMessageContaining("boom");
   * }</code></pre>
   *
   * If the provided {@link ThrowingCallable} does not raise an exception, an error is immediately thrown,
   * in that case the test description provided with {@link AbstractAssert#as(String, Object...) as(String, Object...)} is not honored.<br>
   * To use a test description, use {@link #catchThrowable(ThrowableAssert.ThrowingCallable)} as shown below:
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
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
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
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(ThrowingCallable shouldRaiseThrowable,
                                                                                   String description, Object... args) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).as(description, args).hasBeenThrown();
  }

  /**
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
   * which allows chaining assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatExceptionOfType(IOException.class)
   *              .isThrownBy(() -&gt; { throw new IOException("boom!"); })
   *              .withMessage("boom!"); </code></pre>
   *
   * This method is more or less the same of {@link #assertThatThrownBy(ThrowableAssert.ThrowingCallable)} but in a more natural way.
   *
   * @param <T> the exception type.
   * @param exceptionType the class of exception type.
   * @return the created {@link ThrowableTypeAssert}.
   */
  public static <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(final Class<? extends T> exceptionType) {
    return new ThrowableTypeAssert<>(exceptionType);
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
  public static NotThrownAssert assertThatNoException() {
    return new NotThrownAssert();
  }

  /**
   * Allows capturing and then assert on a {@link Throwable} more easily when used with Java 8 lambdas.
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
   * To use a test description, use {@link #catchThrowable(ThrowableAssert.ThrowingCallable)} as shown below.
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
  public static AbstractThrowableAssert<?, ? extends Throwable> assertThatCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return assertThat(catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Allows catching a {@link Throwable} more easily when used with Java 8 lambdas.
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
   * @see AssertionsForClassTypes#catchThrowableOfType(Class, ThrowableAssert.ThrowingCallable)
   */
  public static Throwable catchThrowable(ThrowingCallable shouldRaiseThrowable) {
    return ThrowableAssert.catchThrowable(shouldRaiseThrowable);
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
   * @see #catchThrowable(ThrowableAssert.ThrowingCallable)
   * @since 3.9.0
   * @deprecated use {@link #catchThrowableOfType(Class, ThrowingCallable)} instead.
   */
  @Deprecated
  public static <THROWABLE extends Throwable> THROWABLE catchThrowableOfType(ThrowingCallable shouldRaiseThrowable,
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
   * CustomParseException e = catchThrowableOfType(CustomParseException.class,
   *                                               () -&gt; { throw new CustomParseException("boom!", 1, 5); });
   * // assertions pass
   * assertThat(e).hasMessageContaining("boom");
   * assertThat(e.line).isEqualTo(1);
   * assertThat(e.column).isEqualTo(5);
   *
   * // fails as CustomParseException is not a RuntimeException
   * catchThrowableOfType(RuntimeException.class,
   *                     () -&gt; { throw new CustomParseException("boom!", 1, 5); });</code></pre>
   *
   * @param <THROWABLE> the {@link Throwable} type.
   * @param shouldRaiseThrowable The lambda with the code that should raise the exception.
   * @param type The type of exception that the code is expected to raise.
   * @return The captured exception or <code>null</code> if none was raised by the callable.
   * @see #catchThrowable(ThrowableAssert.ThrowingCallable)
   * @since 3.26.0
   */
  public static <THROWABLE extends Throwable> THROWABLE catchThrowableOfType(Class<THROWABLE> type,
                                                                             ThrowingCallable shouldRaiseThrowable) {
    return ThrowableAssert.catchThrowableOfType(type, shouldRaiseThrowable);
  }
  // -------------------------------------------------------------------------------------------------
  // fail methods : not assertions but here to have a single entry point to all AssertJ features.
  // -------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Fail#setRemoveAssertJRelatedElementsFromStackTrace(boolean)} so that Assertions offers a
   * full feature entry point to all AssertJ Assert features (but you can use {@link Fail} if you prefer).
   *
   * @param removeAssertJRelatedElementsFromStackTrace flag.
   */
  public static void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    Fail.setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  /**
   * Only delegate to {@link Fail#fail(String)} so that Assertions offers a full feature entry point to all Assertj
   * Assert features (but you can use Fail if you prefer).
   *
   * @param failureMessage error message.
   * @throws AssertionError with the given message.
   */
  public static void fail(String failureMessage) {
    Fail.fail(failureMessage);
  }

  /**
   * Only delegate to {@link Fail#fail()} so that {@link Assertions} offers a full feature entry point to all Assertj
   * features (but you can use {@code Fail}  if you prefer).
   *
   * @throws AssertionError without message.
   */
  public static void fail() {
    Fail.fail();
  }

  /**
   * Only delegate to {@link Fail#fail(String, Throwable)} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use Fail if you prefer).
   *
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   * @throws AssertionError with the given message and with the {@link Throwable} that caused the failure.
   */
  public static void fail(String failureMessage, Throwable realCause) {
    Fail.fail(failureMessage, realCause);
  }

  /**
   * Only delegate to {@link Fail#fail(Throwable)} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use Fail if you prefer).
   *
   * @param realCause cause of the error.
   * @throws AssertionError with the {@link Throwable} that caused the failure.
   */
  public static void fail(Throwable realCause) {
    Fail.fail(realCause);
  }

  /**
   * Only delegate to {@link Fail#failBecauseExceptionWasNotThrown(Class)} so that Assertions offers a full feature
   * entry point to all AssertJ features (but you can use Fail if you prefer).
   * <p>
   * {@link Assertions#shouldHaveThrown(Class)} can be used as a replacement.
   *
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   */
  public static void failBecauseExceptionWasNotThrown(Class<? extends Throwable> throwableClass) {
    Fail.shouldHaveThrown(throwableClass);
  }

  /**
   * Only delegate to {@link Fail#shouldHaveThrown(Class)} so that Assertions offers a full feature
   * entry point to all AssertJ features (but you can use Fail if you prefer).
   *
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   */
  public static void shouldHaveThrown(Class<? extends Throwable> throwableClass) {
    Fail.shouldHaveThrown(throwableClass);
  }

  /**
   * In error messages, sets the threshold when iterable/array formatting will on one line (if their String description
   * is less than this parameter) or it will be formatted with one element per line.
   * <p>
   * The following array will be formatted on one line as its length &lt; 80
   *
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
   */
  public static void setMaxLengthForSingleLineDescription(int maxLengthForSingleLineDescription) {
    StandardRepresentation.setMaxLengthForSingleLineDescription(maxLengthForSingleLineDescription);
  }

  // ------------------------------------------------------------------------------------------------------
  // properties methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
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
   *
   * @param <T> the type of value to extract.
   * @param propertyName the name of the property to be read from the elements of a {@code Iterable}. It may be a nested
   *          property (e.g. "address.street.number").
   * @param propertyType the type of property to extract
   * @return the created {@code Properties}.
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   */
  public static <T> Properties<T> extractProperty(String propertyName, Class<T> propertyType) {
    return Properties.extractProperty(propertyName, propertyType);
  }

  /**
   * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
   * to
   * all AssertJ features (but you can use {@link Properties} if you prefer).
   * <p>
   * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
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
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   */
  public static Properties<Object> extractProperty(String propertyName) {
    return Properties.extractProperty(propertyName);
  }

  /**
   * Utility method to build nicely a {@link Tuple} when working with {@link IterableAssert#extracting(String...)} or
   * {@link ObjectArrayAssert#extracting(String...)}
   *
   * @param values the values stored in the {@link Tuple}
   * @return the built {@link Tuple}
   */
  public static Tuple tuple(Object... values) {
    return Tuple.tuple(values);
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
  public static void setAllowExtractingPrivateFields(boolean allowExtractingPrivateFields) {
    FieldSupport.extraction().setAllowUsingPrivateFields(allowExtractingPrivateFields);
  }

  /**
   * Globally sets whether the use of private fields is allowed for comparison.
   * The following (incomplete) list of methods will be impacted by this change :
   * <ul>
   * <li><code>{@link org.assertj.core.api.AbstractIterableAssert#usingElementComparatorOnFields(java.lang.String...)}</code></li>
   * <li><code>{@link org.assertj.core.api.AbstractObjectAssert#isEqualToComparingFieldByField(Object)}</code></li>
   * </ul>
   *
   * If the value is <code>false</code> and these methods try to compare private fields, it will fail with an exception.
   *
   * @param allowComparingPrivateFields allow private fields comparison. Default {@code true}.
   */
  public static void setAllowComparingPrivateFields(boolean allowComparingPrivateFields) {
    FieldSupport.comparison().setAllowUsingPrivateFields(allowComparingPrivateFields);
  }

  // ------------------------------------------------------------------------------------------------------
  // Data utility methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link MapEntry#entry(Object, Object)} so that Assertions offers a full feature entry point to
   * all
   * AssertJ features (but you can use {@link MapEntry} if you prefer).
   * <p>
   * Typical usage is to call <code>entry</code> in MapAssert <code>contains</code> assertion, see examples below :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = ... // init omitted
   *
   * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));</code></pre>
   *
   * @param <K> the type of the key of this entry.
   * @param <V> the type of the value of this entry.
   * @param key the key of the entry to create.
   * @param value the value of the entry to create.
   * @return the created {@code MapEntry}.
   */
  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return MapEntry.entry(key, value);
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
  public static Index atIndex(int index) {
    return Index.atIndex(index);
  }

  /**
   * Assertions entry point for double {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isEqualTo(8.0, offset(0.1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Double> offset(Double value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for float {@link Offset}.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, offset(0.2f));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Float> offset(Float value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Double)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.1).isCloseTo(8.0, within(0.1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Double> within(Double value) {
    return Offset.offset(value);
  }

  /**
   * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(8.2f).isCloseTo(8.0f, within(0.2f));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Float> within(Float value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), within(BigDecimal.ONE));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<BigDecimal> within(BigDecimal value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat((byte)10).isCloseTo((byte)11, within((byte)1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Byte> within(Byte value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Integer {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, within(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Integer> within(Integer value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(10).isCloseTo(11, within(1));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Short> within(Short value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Long {@link Offset} to use with isCloseTo assertions.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(5l).isCloseTo(7l, within(2l));</code></pre>
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Long> within(Long value) {
    return Offset.offset(value);
  }

  /**
   * Assertions entry point for Double {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11.0).isCloseTo(10.0, withinPercentage(10.0));</code></pre>
   *
   * @param value the value of the percentage.
   * @return the created {@code Percentage}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Percentage withinPercentage(Double value) {
    return withPercentage(value);
  }

  /**
   * Assertions entry point for Integer {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11).isCloseTo(10, withinPercentage(10));</code></pre>
   *
   * @param value the value of the percentage.
   * @return the created {@code Percentage}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Percentage withinPercentage(Integer value) {
    return withPercentage(value);
  }

  /**
   * Assertions entry point for Long {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
   * percentages.
   * <p>
   * Typical usage :
   * <pre><code class='java'> assertThat(11L).isCloseTo(10L, withinPercentage(10L));</code></pre>
   *
   * @param value the value of the percentage.
   * @return the created {@code Percentage}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Percentage withinPercentage(Long value) {
    return withPercentage(value);
  }

  // ------------------------------------------------------------------------------------------------------
  // Condition methods : not assertions but here to have a single entry point to all AssertJ features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  @SafeVarargs
  public static <T> Condition<T> allOf(Condition<? super T>... conditions) {
    return AllOf.allOf(conditions);
  }

  /**
   * Creates a new <code>{@link AllOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  public static <T> Condition<T> allOf(Iterable<? extends Condition<? super T>> conditions) {
    return AllOf.allOf(conditions);
  }

  /**
   * Only delegate to {@link AnyOf#anyOf(Condition...)} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link AnyOf} if you prefer).
   * <p>
   * Typical usage (<code>jedi</code> and <code>sith</code> are {@link Condition}) :
   * <pre><code class='java'> assertThat(&quot;Vader&quot;).is(anyOf(jedi, sith));</code></pre>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  @SafeVarargs
  public static <T> Condition<T> anyOf(Condition<? super T>... conditions) {
    return AnyOf.anyOf(conditions);
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
  public static <T> Condition<T> anyOf(Iterable<? extends Condition<? super T>> conditions) {
    return AnyOf.anyOf(conditions);
  }

  /**
   * Creates a new <code>{@link DoesNotHave}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The Not condition created.
   */
  public static <T> DoesNotHave<T> doesNotHave(Condition<? super T> condition) {
    return DoesNotHave.doesNotHave(condition);
  }

  /**
   * Creates a new <code>{@link Not}</code>.
   *
   * @param <T> the type of object the given condition accept.
   * @param condition the condition to inverse.
   * @return The Not condition created.
   */
  public static <T> Not<T> not(Condition<? super T> condition) {
    return Not.not(condition);
  }

  // --------------------------------------------------------------------------------------------------
  // Filter methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link Filters} if you prefer).
   * <p>
   * Note that the given array is not modified, the filters are performed on an {@link Iterable} copy of the array.
   * <p>
   * Typical usage with {@link Condition} :
   * <pre><code class='java'> assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   * <p>
   * and with filter language based on java bean property :
   * <pre><code class='java'> assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;).greaterThan(7).get())
   *           .containsOnly(james, rose);</code></pre>
   *
   * @param <E> the array elements type.
   * @param array the array to filter.
   * @return the created <code>{@link Filters}</code>.
   * @throws NullPointerException if the given array is {@code null}.
   */
  public static <E> Filters<E> filter(E[] array) {
    return Filters.filter(array);
  }

  /**
   * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
   * AssertJ features (but you can use {@link Filters} if you prefer).
   * <p>
   * Note that the given {@link Iterable} is not modified, the filters are performed on a copy.
   * <p>
   * Typical usage with {@link Condition} :
   * <pre><code class='java'> assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   * <p>
   * and with filter language based on java bean property :
   * <pre><code class='java'> assertThat(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20)
   *                           .and(&quot;assistsPerGame&quot;).greaterThan(7).get())
   *           .containsOnly(james, rose);</code></pre>
   *
   * @param <E> the iterable elements type.
   * @param iterableToFilter the iterable to filter.
   * @return the created <code>{@link Filters}</code>.
   * @throws NullPointerException if the given array is {@code null}.
   */
  public static <E> Filters<E> filter(Iterable<E> iterableToFilter) {
    return Filters.filter(iterableToFilter);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches one of the given values.
   * <p>
   * As often, an example helps:
   *
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
   */
  public static InFilter in(Object... values) {
    return InFilter.in(values);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches does not match any of the given values.
   * <p>
   * As often, an example helps:
   *
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
   */
  public static NotInFilter notIn(Object... valuesNotToMatch) {
    return NotInFilter.notIn(valuesNotToMatch);
  }

  /**
   * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
   * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
   * value matches does not match the given value.
   * <p>
   * As often, an example helps:
   *
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
   */
  public static NotFilter not(Object valueNotToMatch) {
    return NotFilter.not(valueNotToMatch);
  }

  // --------------------------------------------------------------------------------------------------
  // File methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

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
  public static String contentOf(File file, Charset charset) {
    return Files.contentOf(file, charset);
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
  public static String contentOf(File file, String charsetName) {
    return Files.contentOf(file, charsetName);
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
  public static String contentOf(File file) {
    return Files.contentOf(file, Charset.defaultCharset());
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
  public static List<String> linesOf(File file) {
    return Files.linesOf(file, Charset.defaultCharset());
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
  public static List<String> linesOf(File file, Charset charset) {
    return Files.linesOf(file, charset);
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
  public static List<String> linesOf(File file, String charsetName) {
    return Files.linesOf(file, charsetName);
  }

  /**
   * Loads the text content of a path into a list of strings with the default charset, each string corresponding to a
   * line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param path the path.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  public static List<String> linesOf(Path path) {
    return Paths.linesOf(path, Charset.defaultCharset());
  }

  /**
   * Loads the text content of a path into a list of strings, each string corresponding to a line.
   * The line endings are either \n, \r or \r\n.
   *
   * @param path the path.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  public static List<String> linesOf(Path path, Charset charset) {
    return Paths.linesOf(path, charset);
  }

  /**
   * Loads the text content of a path into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param path the path.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   *
   * @since 3.23.0
   */
  public static List<String> linesOf(Path path, String charsetName) {
    return Paths.linesOf(path, charsetName);
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
   */
  public static String contentOf(URL url, Charset charset) {
    return URLs.contentOf(url, charset);
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
   */
  public static String contentOf(URL url, String charsetName) {
    return URLs.contentOf(url, charsetName);
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
   */
  public static String contentOf(URL url) {
    return URLs.contentOf(url, Charset.defaultCharset());
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
   */
  public static List<String> linesOf(URL url) {
    return URLs.linesOf(url, Charset.defaultCharset());
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
   */
  public static List<String> linesOf(URL url, Charset charset) {
    return URLs.linesOf(url, charset);
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
   */
  public static List<String> linesOf(URL url, String charsetName) {
    return URLs.linesOf(url, charsetName);
  }

  // --------------------------------------------------------------------------------------------------
  // Date formatting methods : not assertions but here to have a single entry point to all AssertJ features.
  // --------------------------------------------------------------------------------------------------

  /**
   * Instead of using default strict date/time parsing, it is possible to use lenient parsing mode for default date
   * formats parser to interpret inputs that do not precisely match supported date formats (lenient parsing).
   * <p>
   * With strict parsing, inputs must match exactly date/time format.
   *
   * <p>
   * Example:
   * </p>
   *
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
   */
  public static void setLenientDateParsing(boolean value) {
    AbstractDateAssert.setLenientDateParsing(value);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is going to use any date formats registered with one of these methods :
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
   *
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
  public static void registerCustomDateFormat(DateFormat userCustomDateFormat) {
    AbstractDateAssert.registerCustomDateFormat(userCustomDateFormat);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is going to use any date formats registered with one of these methods :
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
   *
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
  public static void registerCustomDateFormat(String userCustomDateFormatPattern) {
    AbstractDateAssert.registerCustomDateFormat(userCustomDateFormatPattern);
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
  public static void useDefaultDateFormatsOnly() {
    AbstractDateAssert.useDefaultDateFormatsOnly();
  }

  /**
   * Creates a new <code>{@link AssertionsForClassTypes}</code>.
   */
  protected AssertionsForClassTypes() {}
}
