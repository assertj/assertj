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
import static org.assertj.core.api.AssumptionExceptionFactory.assumptionNotMet;

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

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.configuration.PreferredAssumptionException;
import org.assertj.core.util.Throwables;

/**
 * Entry point for assumption methods for different types, which allow to skip test execution on failed assumptions.
 * @since 2.9.0 / 3.9.0
 */
@CheckReturnValue
public class Assumptions {

  private static final AssertionErrorHandler ASSUMPTION_ERROR_HANDLER = new AssertionErrorHandler() {
    @Override
    public void handleError(AssertionError error) {
      try {
        throw assumptionNotMet(error);
      } catch (ReflectiveOperationException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public void succeeded() {
      // no-op for assumptions
    }
  };

  /**
   * Makes the given assertion instance assumption-aware.
   * When an assertion fails, the error is converted to an assumption exception (skipping the test).
   */
  @SuppressWarnings("rawtypes")
  static <T> T assumption(T assertion) {
    if (assertion instanceof AbstractAssert abstractAssert) {
      abstractAssert.assertionErrorHandler = ASSUMPTION_ERROR_HANDLER;
    }
    return assertion;
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <T> the type of the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <T> ObjectAssert<T> assumeThat(T actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractStringAssert<?> assumeThat(String actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBigDecimalAssert<?> assumeThat(BigDecimal actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBigIntegerAssert<?> assumeThat(BigInteger actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractUriAssert<?> assumeThat(URI actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractUrlAssert<?> assumeThat(URL actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBooleanAssert<?> assumeThat(boolean actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBooleanAssert<?> assumeThat(Boolean actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBooleanArrayAssert<?> assumeThat(boolean[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Boolean2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Boolean2DArrayAssert assumeThat(boolean[][] actual) {
    return (Boolean2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractByteAssert<?> assumeThat(byte actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractByteAssert<?> assumeThat(Byte actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractByteArrayAssert<?> assumeThat(byte[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Byte2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Byte2DArrayAssert assumeThat(byte[][] actual) {
    return (Byte2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharacterAssert<?> assumeThat(char actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharacterAssert<?> assumeThat(Character actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharArrayAssert<?> assumeThat(char[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Char2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Char2DArrayAssert assumeThat(char[][] actual) {
    return (Char2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(CharSequence actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(CharSequence)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.25.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThatCharSequence(CharSequence actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption from a {@link StringBuilder}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(StringBuilder actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(StringBuffer actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractShortAssert<?> assumeThat(short actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractShortAssert<?> assumeThat(Short actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractShortArrayAssert<?> assumeThat(short[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Short2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Short2DArrayAssert assumeThat(short[][] actual) {
    return (Short2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractIntegerAssert<?> assumeThat(int actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractIntegerAssert<?> assumeThat(Integer actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractIntArrayAssert<?> assumeThat(int[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Int2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Int2DArrayAssert assumeThat(int[][] actual) {
    return (Int2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractLongAssert<?> assumeThat(long actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractLongAssert<?> assumeThat(Long actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractLongArrayAssert<?> assumeThat(long[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Long2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Long2DArrayAssert assumeThat(long[][] actual) {
    return (Long2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFloatAssert<?> assumeThat(float actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFloatAssert<?> assumeThat(Float actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFloatArrayAssert<?> assumeThat(float[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Float2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Float2DArrayAssert assumeThat(float[][] actual) {
    return (Float2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDoubleAssert<?> assumeThat(double actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDoubleAssert<?> assumeThat(Double actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDoubleArrayAssert<?> assumeThat(double[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Double2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Double2DArrayAssert assumeThat(double[][] actual) {
    return (Double2DArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicBooleanAssert assumeThat(AtomicBoolean actual) {
    return (AtomicBooleanAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicIntegerAssert assumeThat(AtomicInteger actual) {
    return (AtomicIntegerAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates int[] assumption for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicIntegerArrayAssert assumeThat(AtomicIntegerArray actual) {
    return (AtomicIntegerArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicIntegerFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> assumeThat(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link LongAdder}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.16.0
   */
  public static LongAdderAssert assumeThat(LongAdder actual) {
    return (LongAdderAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicLongAssert assumeThat(AtomicLong actual) {
    return (AtomicLongAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicLongArrayAssert assumeThat(AtomicLongArray actual) {
    return (AtomicLongArrayAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicLongFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> assumeThat(AtomicLongFieldUpdater<OBJECT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicReference}.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <VALUE> AtomicReferenceAssert<VALUE> assumeThat(AtomicReference<VALUE> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicReferenceArray}.
   *
   * @param actual the actual value.
   * @param <ELEMENT> the type of the value contained in the {@link AtomicReferenceArray}.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> assumeThat(AtomicReferenceArray<ELEMENT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicReferenceFieldUpdater}.
   *
   * @param actual the actual value.
   * @param <FIELD> the type of the field which gets updated by the {@link AtomicReferenceFieldUpdater}.
   * @param <OBJECT> the type of the object holding the updatable field.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> assumeThat(
                                                                                            AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicMarkableReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicMarkableReference}.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <VALUE> AtomicMarkableReferenceAssert<VALUE> assumeThat(AtomicMarkableReference<VALUE> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assumption for {@link AtomicStampedReference}.
   *
   * @param actual the actual value.
   * @param <VALUE> the type of the value contained in the {@link AtomicStampedReference}.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <VALUE> AtomicStampedReferenceAssert<VALUE> assumeThat(AtomicStampedReference<VALUE> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static ClassAssert assumeThat(Class<?> actual) {
    return (ClassAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDateAssert<?> assumeThat(Date actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFileAssert<?> assumeThat(File actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link PathAssert} assumption.
   *
   * @param actual the path to test
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractPathAssert<?> assumeThat(Path actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link PathAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(Path)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static AbstractPathAssert<?> assumeThatPath(Path actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractInputStreamAssert<?, ? extends InputStream> assumeThat(InputStream actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Create assertion for {@link FutureAssert} assumption.
   *
   * @param future the actual value.
   * @param <RESULT> the type of the value contained in the {@link java.util.concurrent.Future}.
   *
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <RESULT> AbstractFutureAssert<?, ? extends Future<? extends RESULT>, RESULT> assumeThat(Future<RESULT> future) {
    return assumption(Assertions.assertThat(future));
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code> assumption.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <ELEMENT> IterableAssert<ELEMENT> assumeThat(Iterable<? extends ELEMENT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IterableAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(Iterable)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> IterableAssert<ELEMENT> assumeThatIterable(Iterable<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code> assumption.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <ELEMENT> IteratorAssert<ELEMENT> assumeThat(Iterator<? extends ELEMENT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(Iterator)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> IteratorAssert<ELEMENT> assumeThatIterator(Iterator<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code> assumption.
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.21.0
   */
  @SuppressWarnings("unchecked")
  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> assumeThat(Collection<? extends E> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link CollectionAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(Collection)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> assumeThatCollection(Collection<? extends E> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <ELEMENT> FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(List<? extends ELEMENT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(List)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThatList(List<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code> assumption.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <T> ObjectArrayAssert<T> assumeThat(T[] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link Object2DArrayAssert}</code> assumption.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  @SuppressWarnings("unchecked")
  public static <T> Object2DArrayAssert<T> assumeThat(T[][] actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link MapAssert}</code> assumption.
   *
   * @param <K> the type of keys in the map.
   * @param <V> the type of values in the map.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <K, V> MapAssert<K, V> assumeThat(Map<K, V> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link GenericComparableAssert}</code> assumption.
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> assumeThat(T actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link UniversalComparableAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(Comparable)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param <T> the type of actual.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  @SuppressWarnings("unchecked")
  public static <T> AbstractUniversalComparableAssert<?, T> assumeThatComparable(Comparable<T> actual) {
    return assumption(Assertions.assertThatComparable(actual));
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> assumption.
   *
   * @param <T> the type of the actual throwable.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <T extends Throwable> AbstractThrowableAssert<?, T> assumeThat(T actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> assumption.
   * <p>
   * This overload's purpose is to disambiguate the call for <code>{@link SQLException}</code>.
   * Indeed, this class implements <code>{@link Iterable}</code> and is considered ambiguous.
   *
   * @param <T> the type of the actual SQL exception.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 4.0.0
   */
  @SuppressWarnings("unchecked")
  public static <T extends SQLException> AbstractThrowableAssert<?, T> assumeThat(T actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
   * which allows to chain assertions on the thrown exception.
   *
   * @param <T> the exception type.
   * @param exceptionType the exception type class.
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  public static <T extends Throwable> ThrowableTypeAssert<T> assumeThatExceptionOfType(final Class<? extends T> exceptionType) {
    return new ThrowableTypeAssert<>(exceptionType);
  }

  /**
   * Alias for {@link #assumeThatExceptionOfType(Class)} for {@link Exception}.
   *
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<Exception> assumeThatException() {
    return assumeThatExceptionOfType(Exception.class);
  }

  /**
   * Alias for {@link #assumeThatExceptionOfType(Class)} for {@link RuntimeException}.
   *
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<RuntimeException> assumeThatRuntimeException() {
    return assumeThatExceptionOfType(RuntimeException.class);
  }

  /**
   * Alias for {@link #assumeThatExceptionOfType(Class)} for {@link NullPointerException}.
   *
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<NullPointerException> assumeThatNullPointerException() {
    return assumeThatExceptionOfType(NullPointerException.class);
  }

  /**
   * Alias for {@link #assumeThatExceptionOfType(Class)} for {@link IllegalArgumentException}.
   *
   * @return the created assumption for assertion object.
   *
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<IllegalArgumentException> assumeThatIllegalArgumentException() {
    return assumeThatExceptionOfType(IllegalArgumentException.class);
  }

  /**
   * Alias for {@link #assumeThatExceptionOfType(Class)} for {@link IOException}.
   *
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<IOException> assumeThatIOException() {
    return assumeThatExceptionOfType(IOException.class);
  }

  /**
   * Alias for {@link #assumeThatExceptionOfType(Class)} for {@link IndexOutOfBoundsException}.
   *
   * @return the created assumption for assertion object.
   *
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<IndexOutOfBoundsException> assumeThatIndexOutOfBoundsException() {
    return assumeThatExceptionOfType(IndexOutOfBoundsException.class);
  }

  /**
   * Alias for {@link #assumeThatExceptionOfType(Class)} for {@link ReflectiveOperationException}.
   *
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<ReflectiveOperationException> assumeThatReflectiveOperationException() {
    return assumeThatExceptionOfType(ReflectiveOperationException.class);
  }

  /**
   * Allows to capture and then assume on a {@link Throwable} (easier done with lambdas).
   * <p>
   * Example :
   * <pre><code class='java'>  {@literal @}Test
   *  public void testException() {
   *    assumeThatThrownBy(() -&gt; { throw new Exception("boom!") }).isInstanceOf(Exception.class)
   *                                                              .hasMessageContaining("boom");
   * }</code></pre>
   *
   * @param shouldRaiseThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return assumption(Assertions.assertThat(catchThrowable(shouldRaiseThrowable)).hasBeenThrown());
  }

  /**
   * Allows to capture and then assume on a {@link Throwable} (easier done with lambdas).
   * <p>
   * The main difference with {@code assumeThatThrownBy(ThrowingCallable)} is that this method does not fail if no exception was thrown.
   * <p>
   * Example :
   * <pre><code class='java'> ThrowingCallable callable = () -&gt; {
   *   throw new Exception("boom!");
   * };
   *
   * // assertion succeeds
   * assumeThatCode(callable).isInstanceOf(Exception.class)
   *                         .hasMessageContaining("boom");
   *
   * // assertion fails
   * assumeThatCode(callable).doesNotThrowAnyException();</code></pre>
   * <p>
   * This method was not named {@code assumeThat} because the java compiler reported it ambiguous when used directly with a lambda :(
   *
   * @param shouldRaiseOrNotThrowable The {@link ThrowingCallable} or lambda with the code that should raise the throwable.
   * @return the created {@link ThrowableAssert}.
   * @since 3.9.0
   */
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThatCode(ThrowingCallable shouldRaiseOrNotThrowable) {
    return assumeThat(Throwables.catchThrowable(shouldRaiseOrNotThrowable));
  }

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code> assumption.
   * <p>
   * This overload is useful, when an overloaded method of assertThat(...) takes precedence over the generic {@link
   * #assumeThat(Object)}.
   * <p>
   * Example:
   * <p>
   * Cast necessary because {@link #assumeThat(List)} "forgets" actual type:
   * <pre>{@code assumeThat(new LinkedList<>(asList("abc"))).matches(list -> ((Deque<String>) list).getFirst().equals("abc")); }</pre>
   * No cast needed, but also no additional list assertions:
   * <pre>{@code assumeThatObject(new LinkedList<>(asList("abc"))).matches(list -> list.getFirst().equals("abc")); }</pre>
   *
   * @param <T> the type of the actual value.
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.12.0
   */
  public static <T> ObjectAssert<T> assumeThatObject(T actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of {@link PredicateAssert} assumption.
   *
   * @param <T> the {@link Predicate} type.
   * @param actual the Predicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <T> PredicateAssert<T> assumeThat(Predicate<T> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link PredicateAssert}</code> assumption.
   * <p>
   * Use this over {@link #assumeThat(Predicate)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <T> PredicateAssert<T> assumeThatPredicate(Predicate<T> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of {@link IntPredicateAssert} assumption.
   *
   * @param actual the IntPredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static IntPredicateAssert assumeThat(IntPredicate actual) {
    return (IntPredicateAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link LongPredicateAssert} assumption.
   *
   * @param actual the LongPredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static LongPredicateAssert assumeThat(LongPredicate actual) {
    return (LongPredicateAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link DoublePredicateAssert} assumption.
   *
   * @param actual the DoublePredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static DoublePredicateAssert assumeThat(DoublePredicate actual) {
    return (DoublePredicateAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link CompletableFutureAssert} assumption.
   *
   * @param <RESULT> the CompletableFuture wrapped type.
   * @param actual the CompletableFuture to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <RESULT> CompletableFutureAssert<RESULT> assumeThat(CompletableFuture<RESULT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link CompletableFutureAssert} assumption for a {@link java.util.concurrent.CompletionStage}
   * by converting it to a {@link CompletableFuture} and returning a {@link CompletableFutureAssert}.
   * <p>
   * If the given {@link java.util.concurrent.CompletionStage} is null, the {@link CompletableFuture} in the returned {@link CompletableFutureAssert} will also be null.
   *
   * @param <RESULT> the CompletableFuture wrapped type.
   * @param actual the CompletableFuture to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <RESULT> CompletableFutureAssert<RESULT> assumeThat(CompletionStage<RESULT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link OptionalAssert} assumption.
   *
   * @param <VALUE> the Optional wrapped type.
   * @param actual the Optional to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <VALUE> OptionalAssert<VALUE> assumeThat(Optional<VALUE> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link OptionalDoubleAssert} assumption.
   *
   * @param actual the OptionalDouble to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static OptionalDoubleAssert assumeThat(OptionalDouble actual) {
    return (OptionalDoubleAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link MatcherAssert} assumption.
   *
   * @param actual the Matcher to test
   * @return the created assumption for assertion object.
   */
  public static MatcherAssert assumeThat(Matcher actual) {
    return (MatcherAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link OptionalIntAssert} assumption.
   *
   * @param actual the OptionalInt to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static OptionalIntAssert assumeThat(OptionalInt actual) {
    return (OptionalIntAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link OptionalLongAssert} assumption.
   *
   * @param actual the OptionalLong to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static OptionalLongAssert assumeThat(OptionalLong actual) {
    return (OptionalLongAssert) assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link TemporalAssert} assumption.
   *
   * @param actual the Temporal to test
   * @return the created assumption for the given object.
   * @since 3.26.1
   */
  public static TemporalAssert assumeThatTemporal(Temporal actual) {
    return assumption(Assertions.assertThatTemporal(actual));
  }

  /**
   * Creates a new instance of {@link ZonedDateTimeAssert} assumption.
   *
   * @param actual the ZonedDateTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractZonedDateTimeAssert<?> assumeThat(ZonedDateTime actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link LocalDateTimeAssert} assumption.
   *
   * @param actual the LocalDateTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractLocalDateTimeAssert<?> assumeThat(LocalDateTime actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link OffsetDateTimeAssert} assumption.
   *
   * @param actual the OffsetDateTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractOffsetDateTimeAssert<?> assumeThat(OffsetDateTime actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link OffsetTimeAssert} assumption.
   *
   * @param actual the LocalTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractOffsetTimeAssert<?> assumeThat(OffsetTime actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link LocalTimeAssert} assumption.
   *
   * @param actual the LocalTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractLocalTimeAssert<?> assumeThat(LocalTime actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link LocalDateAssert} assumption.
   *
   * @param actual the LocalDate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractLocalDateAssert<?> assumeThat(LocalDate actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link YearMonthAssert} assumption.
   *
   * @param actual the YearMonth to test
   * @return the created assumption for assertion object.
   * @since 3.26.0
   */
  public static AbstractYearMonthAssert<?> assumeThat(YearMonth actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link InstantAssert} assumption.
   *
   * @param actual the Instant to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractInstantAssert<?> assumeThat(Instant actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link DurationAssert} assumption.
   *
   * @param actual the Duration to test
   * @return the created assumption for assertion object.
   * @since 3.15.0
   */
  public static AbstractDurationAssert<?> assumeThat(Duration actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of {@link PeriodAssert} assumption.
   *
   * @param actual the Period to test
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static AbstractPeriodAssert<?> assumeThat(Period actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link Stream}.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the Stream to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(Stream<? extends ELEMENT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link Stream}.
   * <p>
   * Use this over {@link #assumeThat(Stream)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>assumeThat</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThatStream(Stream<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link DoubleStream}.
   *
   * @param actual the DoubleStream to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static AbstractListAssert<?, List<? extends Double>, Double, ObjectAssert<Double>> assumeThat(DoubleStream actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link LongStream}.
   *
   * @param actual the LongStream to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static AbstractListAssert<?, List<? extends Long>, Long, ObjectAssert<Long>> assumeThat(LongStream actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link ListAssert}</code> assumption from the given {@link IntStream}.
   *
   * @param actual the LongStream to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static AbstractListAssert<?, List<? extends Integer>, Integer, ObjectAssert<Integer>> assumeThat(IntStream actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Creates a new instance of <code>{@link SpliteratorAssert}</code> assumption from the given {@link Spliterator}.
   *
   * @param <ELEMENT> the type of the elements
   * @param actual the Spliterator to test
   * @return the created assumption for assertion object.
   */
  @SuppressWarnings("unchecked")
  public static <ELEMENT> AbstractSpliteratorAssert<?, ELEMENT> assumeThat(Spliterator<ELEMENT> actual) {
    return assumption(Assertions.assertThat(actual));
  }

  /**
   * Sets which exception is thrown if an assumption is not met. 
   * <p>
   * This method is useful if you are using a testing framework that supports assumptions and expect a specific exception to be thrown when an assumption is not met. 
   * <p>
   * You can choose one of:
   * <ul>
   * <li>{@link PreferredAssumptionException#TEST_NG} to throw a {@code org.testng.SkipException} if you are using TestNG</li>
   * <li>{@link PreferredAssumptionException#JUNIT5} a {@code org.opentest4j.TestAbortedException} if you are using JUnit 5</li>
   * <li>{@link PreferredAssumptionException#AUTO_DETECT} to get the default behavior where AssertJ tries different exception (explained later on)</li>
   * </ul>
   * <p>
   * Make sure that the exception you choose can be found in the classpath otherwise AssertJ will throw an {@link IllegalStateException}.
   * <p>
   * For example TestNG expects {@code org.testng.SkipException}, you can tell AssertJ to use it as shown below:
   * <pre><code class='java'> // after this call, AssertJ will throw an org.testng.SkipException when an assumption is not met
   * Assertions.setPreferredAssumptionExceptions(PreferredAssumptionException.TEST_NG); </code></pre>
   * <p>
   * By default, AssertJ uses the {@link PreferredAssumptionException#AUTO_DETECT AUTO_DETECT} mode and tries to throw one of the following exceptions, in this order:
   * <ol>
   * <li>{@code org.testng.SkipException} for TestNG (if available in the classpath)</li>
   * <li>{@code org.opentest4j.TestAbortedException} for JUnit 5</li>
   * </ol> 
   *
   * @param preferredAssumptionException the preferred exception to use with {@link Assumptions}.
   * @since 3.21.0
   */
  public static void setPreferredAssumptionException(PreferredAssumptionException preferredAssumptionException) {
    AssumptionExceptionFactory.setPreferredAssumptionException(preferredAssumptionException);
  }

}
