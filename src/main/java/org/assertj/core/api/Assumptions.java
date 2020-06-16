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

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.ClassLoadingStrategyFactory.classLoadingStrategy;
import static org.assertj.core.util.Arrays.array;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.Callable;
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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.util.CheckReturnValue;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.TypeCache.SimpleKey;
import net.bytebuddy.TypeCache.Sort;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Entry point for assumption methods for different types, which allow to skip test execution on failed assumptions.
 * @since 2.9.0 / 3.9.0
 */
@CheckReturnValue
public class Assumptions {

  /**
   * This NamingStrategy takes the original class's name and adds a suffix to distinguish it.
   * The default is ByteBuddy but for debugging purposes, it makes sense to add AssertJ as a name.
   */
  private static final ByteBuddy BYTE_BUDDY = new ByteBuddy().with(TypeValidation.DISABLED)
                                                             .with(new AuxiliaryType.NamingStrategy.SuffixingRandom("Assertj$Assumptions"));

  private static final Implementation ASSUMPTION = MethodDelegation.to(AssumptionMethodInterceptor.class);

  private static final TypeCache<TypeCache.SimpleKey> CACHE = new TypeCache.WithInlineExpunction<>(Sort.SOFT);

  private static final class AssumptionMethodInterceptor {

    @RuntimeType
    public static Object intercept(@This AbstractAssert<?, ?> assertion, @SuperCall Callable<Object> proxy) throws Exception {
      try {
        Object result = proxy.call();
        if (result != assertion && result instanceof AbstractAssert) {
          return asAssumption((AbstractAssert<?, ?>) result).withAssertionState(assertion);
        }
        return result;
      } catch (AssertionError e) {
        throw assumptionNotMet(e);
      }
    }
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
  public static <T> ProxyableObjectAssert<T> assumeThat(T actual) {
    return asAssumption(ProxyableObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractStringAssert<?> assumeThat(String actual) {
    return asAssumption(StringAssert.class, String.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigDecimalAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBigDecimalAssert<?> assumeThat(BigDecimal actual) {
    return asAssumption(BigDecimalAssert.class, BigDecimal.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BigIntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBigIntegerAssert<?> assumeThat(BigInteger actual) {
    return asAssumption(BigIntegerAssert.class, BigInteger.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UriAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractUriAssert<?> assumeThat(URI actual) {
    return asAssumption(UriAssert.class, URI.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link UrlAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractUrlAssert<?> assumeThat(URL actual) {
    return asAssumption(UrlAssert.class, URL.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBooleanAssert<?> assumeThat(boolean actual) {
    return asAssumption(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBooleanAssert<?> assumeThat(Boolean actual) {
    return asAssumption(BooleanAssert.class, Boolean.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link BooleanArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractBooleanArrayAssert<?> assumeThat(boolean[] actual) {
    return asAssumption(BooleanArrayAssert.class, boolean[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Boolean2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Boolean2DArrayAssert assumeThat(boolean[][] actual) {
    return asAssumption(Boolean2DArrayAssert.class, boolean[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractByteAssert<?> assumeThat(byte actual) {
    return asAssumption(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractByteAssert<?> assumeThat(Byte actual) {
    return asAssumption(ByteAssert.class, Byte.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ByteArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractByteArrayAssert<?> assumeThat(byte[] actual) {
    return asAssumption(ByteArrayAssert.class, byte[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Byte2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Byte2DArrayAssert assumeThat(byte[][] actual) {
    return asAssumption(Byte2DArrayAssert.class, byte[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharacterAssert<?> assumeThat(char actual) {
    return asAssumption(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharacterAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharacterAssert<?> assumeThat(Character actual) {
    return asAssumption(CharacterAssert.class, Character.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharArrayAssert<?> assumeThat(char[] actual) {
    return asAssumption(CharArrayAssert.class, char[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Char2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Char2DArrayAssert assumeThat(char[][] actual) {
    return asAssumption(Char2DArrayAssert.class, char[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(CharSequence actual) {
    return asAssumption(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption from a {@link StringBuilder}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(StringBuilder actual) {
    return asAssumption(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link CharSequenceAssert}</code> assumption from a {@link StringBuffer}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.11.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> assumeThat(StringBuffer actual) {
    return asAssumption(CharSequenceAssert.class, CharSequence.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractShortAssert<?> assumeThat(short actual) {
    return asAssumption(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractShortAssert<?> assumeThat(Short actual) {
    return asAssumption(ShortAssert.class, Short.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ShortArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractShortArrayAssert<?> assumeThat(short[] actual) {
    return asAssumption(ShortArrayAssert.class, short[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Short2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Short2DArrayAssert assumeThat(short[][] actual) {
    return asAssumption(Short2DArrayAssert.class, short[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractIntegerAssert<?> assumeThat(int actual) {
    return asAssumption(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntegerAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractIntegerAssert<?> assumeThat(Integer actual) {
    return asAssumption(IntegerAssert.class, Integer.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IntArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractIntArrayAssert<?> assumeThat(int[] actual) {
    return asAssumption(IntArrayAssert.class, int[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Int2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Int2DArrayAssert assumeThat(int[][] actual) {
    return asAssumption(Int2DArrayAssert.class, int[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractLongAssert<?> assumeThat(long actual) {
    return asAssumption(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractLongAssert<?> assumeThat(Long actual) {
    return asAssumption(LongAssert.class, Long.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link LongArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractLongArrayAssert<?> assumeThat(long[] actual) {
    return asAssumption(LongArrayAssert.class, long[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Long2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Long2DArrayAssert assumeThat(long[][] actual) {
    return asAssumption(Long2DArrayAssert.class, long[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFloatAssert<?> assumeThat(float actual) {
    return asAssumption(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFloatAssert<?> assumeThat(Float actual) {
    return asAssumption(FloatAssert.class, Float.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FloatArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFloatArrayAssert<?> assumeThat(float[] actual) {
    return asAssumption(FloatArrayAssert.class, float[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Float2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Float2DArrayAssert assumeThat(float[][] actual) {
    return asAssumption(Float2DArrayAssert.class, float[][].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDoubleAssert<?> assumeThat(double actual) {
    return asAssumption(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDoubleAssert<?> assumeThat(Double actual) {
    return asAssumption(DoubleAssert.class, Double.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DoubleArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDoubleArrayAssert<?> assumeThat(double[] actual) {
    return asAssumption(DoubleArrayAssert.class, double[].class, actual);
  }

  /**
   * Creates a new instance of <code>{@link Double2DArrayAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static Double2DArrayAssert assumeThat(double[][] actual) {
    return asAssumption(Double2DArrayAssert.class, double[][].class, actual);
  }

  /**
   * Create assumption for {@link AtomicBoolean}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicBooleanAssert assumeThat(AtomicBoolean actual) {
    return asAssumption(AtomicBooleanAssert.class, AtomicBoolean.class, actual);
  }

  /**
   * Create assumption for {@link AtomicInteger}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicIntegerAssert assumeThat(AtomicInteger actual) {
    return asAssumption(AtomicIntegerAssert.class, AtomicInteger.class, actual);
  }

  /**
   * Creates int[] assumption for {@link AtomicIntegerArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicIntegerArrayAssert assumeThat(AtomicIntegerArray actual) {
    return asAssumption(AtomicIntegerArrayAssert.class, AtomicIntegerArray.class, actual);
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
    return asAssumption(AtomicIntegerFieldUpdaterAssert.class, AtomicIntegerFieldUpdater.class, actual);
  }

  /**
   * Create assumption for {@link LongAdder}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.16.0
   */
  public static LongAdderAssert assumeThat(LongAdder actual) {
    return asAssumption(LongAdderAssert.class, LongAdder.class, actual);
  }

  /**
   * Create assumption for {@link AtomicLong}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicLongAssert assumeThat(AtomicLong actual) {
    return asAssumption(AtomicLongAssert.class, AtomicLong.class, actual);
  }

  /**
   * Create assumption for {@link AtomicLongArray}.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AtomicLongArrayAssert assumeThat(AtomicLongArray actual) {
    return asAssumption(AtomicLongArrayAssert.class, AtomicLongArray.class, actual);
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
    return asAssumption(AtomicLongFieldUpdaterAssert.class, AtomicLongFieldUpdater.class, actual);
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
    return asAssumption(AtomicReferenceAssert.class, AtomicReference.class, actual);
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
    return asAssumption(AtomicReferenceArrayAssert.class, AtomicReferenceArray.class, actual);
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
    return asAssumption(AtomicReferenceFieldUpdaterAssert.class, AtomicReferenceFieldUpdater.class, actual);
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
    return asAssumption(AtomicMarkableReferenceAssert.class, AtomicMarkableReference.class, actual);
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
    return asAssumption(AtomicStampedReferenceAssert.class, AtomicStampedReference.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ClassAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractClassAssert<?> assumeThat(Class<?> actual) {
    return asAssumption(ProxyableClassAssert.class, Class.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link DateAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractDateAssert<?> assumeThat(Date actual) {
    return asAssumption(DateAssert.class, Date.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link FileAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractFileAssert<?> assumeThat(File actual) {
    return asAssumption(FileAssert.class, File.class, actual);
  }

  /**
   * Creates a new instance of {@link PathAssert} assumption.
   *
   * @param actual the path to test
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractPathAssert<?> assumeThat(Path actual) {
    return asAssumption(PathAssert.class, Path.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link InputStreamAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractInputStreamAssert<?, ? extends InputStream> assumeThat(InputStream actual) {
    return asAssumption(InputStreamAssert.class, InputStream.class, actual);
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
    return asAssumption(FutureAssert.class, Future.class, future);
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
  public static <ELEMENT> ProxyableIterableAssert<ELEMENT> assumeThat(Iterable<? extends ELEMENT> actual) {
    return asAssumption(ProxyableIterableAssert.class, Iterable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link IteratorAssert}</code> assumption.
   * <p>
   * <b>Breaking change in version 3.12.0:</b> this method does not return anymore an {@link ProxyableIterableAssert} but an {@link IteratorAssert}.<br>
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  public static <ELEMENT> IteratorAssert<ELEMENT> assumeThat(Iterator<? extends ELEMENT> actual) {
    return asAssumption(IteratorAssert.class, Iterator.class, actual);
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
  public static <ELEMENT> FactoryBasedNavigableListAssert<ProxyableListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assumeThat(List<? extends ELEMENT> actual) {
    return asAssumption(ProxyableListAssert.class, List.class, actual);
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
  public static <T> ProxyableObjectArrayAssert<T> assumeThat(T[] actual) {
    return asAssumption(ProxyableObjectArrayAssert.class, Object[].class, actual);
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
    return asAssumption(Object2DArrayAssert.class, Object[][].class, actual);
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
  public static <K, V> AbstractMapAssert<?, ?, K, V> assumeThat(Map<K, V> actual) {
    return asAssumption(ProxyableMapAssert.class, Map.class, actual);
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
    return asAssumption(GenericComparableAssert.class, Comparable.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link ThrowableAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 2.9.0 / 3.9.0
   */
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThat(Throwable actual) {
    return asAssumption(ThrowableAssert.class, Throwable.class, actual);
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
  public static AbstractThrowableAssert<?, ? extends Throwable> assumeThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return asAssumption(ThrowableAssert.class, Throwable.class, catchThrowable(shouldRaiseThrowable));
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
    return assumeThat(catchThrowable(shouldRaiseOrNotThrowable));
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
  public static <T> ProxyableObjectAssert<T> assumeThatObject(T actual) {
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
  public static <T> ProxyablePredicateAssert<T> assumeThat(Predicate<T> actual) {
    return asAssumption(ProxyablePredicateAssert.class, Predicate.class, actual);
  }

  /**
   * Creates a new instance of {@link IntPredicateAssert} assumption.
   *
   * @param actual the IntPredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static IntPredicateAssert assumeThat(IntPredicate actual) {
    return asAssumption(IntPredicateAssert.class, IntPredicate.class, actual);
  }

  /**
   * Creates a new instance of {@link LongPredicateAssert} assumption.
   *
   * @param actual the LongPredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static LongPredicateAssert assumeThat(LongPredicate actual) {
    return asAssumption(LongPredicateAssert.class, LongPredicate.class, actual);
  }

  /**
   * Creates a new instance of {@link DoublePredicateAssert} assumption.
   *
   * @param actual the DoublePredicate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static DoublePredicateAssert assumeThat(DoublePredicate actual) {
    return asAssumption(DoublePredicateAssert.class, DoublePredicate.class, actual);
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
    return asAssumption(CompletableFutureAssert.class, CompletableFuture.class, actual);
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
    return asAssumption(CompletableFutureAssert.class, CompletionStage.class, actual);
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
    return asAssumption(OptionalAssert.class, Optional.class, actual);
  }

  /**
   * Creates a new instance of {@link OptionalDoubleAssert} assumption.
   *
   * @param actual the OptionalDouble to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static OptionalDoubleAssert assumeThat(OptionalDouble actual) {
    return asAssumption(OptionalDoubleAssert.class, OptionalDouble.class, actual);
  }

  /**
   * Creates a new instance of {@link OptionalIntAssert} assumption.
   *
   * @param actual the OptionalInt to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static OptionalIntAssert assumeThat(OptionalInt actual) {
    return asAssumption(OptionalIntAssert.class, OptionalInt.class, actual);
  }

  /**
   * Creates a new instance of {@link OptionalLongAssert} assumption.
   *
   * @param actual the OptionalLong to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static OptionalLongAssert assumeThat(OptionalLong actual) {
    return asAssumption(OptionalLongAssert.class, OptionalLong.class, actual);
  }

  /**
   * Creates a new instance of {@link ZonedDateTimeAssert} assumption.
   *
   * @param actual the ZonedDateTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractZonedDateTimeAssert<?> assumeThat(ZonedDateTime actual) {
    return asAssumption(ZonedDateTimeAssert.class, ZonedDateTime.class, actual);
  }

  /**
   * Creates a new instance of {@link LocalDateTimeAssert} assumption.
   *
   * @param actual the LocalDateTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractLocalDateTimeAssert<?> assumeThat(LocalDateTime actual) {
    return asAssumption(LocalDateTimeAssert.class, LocalDateTime.class, actual);
  }

  /**
   * Creates a new instance of {@link OffsetDateTimeAssert} assumption.
   *
   * @param actual the OffsetDateTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractOffsetDateTimeAssert<?> assumeThat(OffsetDateTime actual) {
    return asAssumption(OffsetDateTimeAssert.class, OffsetDateTime.class, actual);
  }

  /**
   * Creates a new instance of {@link OffsetTimeAssert} assumption.
   *
   * @param actual the LocalTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractOffsetTimeAssert<?> assumeThat(OffsetTime actual) {
    return asAssumption(OffsetTimeAssert.class, OffsetTime.class, actual);
  }

  /**
   * Creates a new instance of {@link LocalTimeAssert} assumption.
   *
   * @param actual the LocalTime to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractLocalTimeAssert<?> assumeThat(LocalTime actual) {
    return asAssumption(LocalTimeAssert.class, LocalTime.class, actual);
  }

  /**
   * Creates a new instance of {@link LocalDateAssert} assumption.
   *
   * @param actual the LocalDate to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractLocalDateAssert<?> assumeThat(LocalDate actual) {
    return asAssumption(LocalDateAssert.class, LocalDate.class, actual);
  }

  /**
   * Creates a new instance of {@link InstantAssert} assumption.
   *
   * @param actual the Instant to test
   * @return the created assumption for assertion object.
   * @since 3.9.0
   */
  public static AbstractInstantAssert<?> assumeThat(Instant actual) {
    return asAssumption(InstantAssert.class, Instant.class, actual);
  }

  /**
   * Creates a new instance of {@link DurationAssert} assumption.
   *
   * @param actual the Duration to test
   * @return the created assumption for assertion object.
   * @since 3.15.0
   */
  public static AbstractDurationAssert<?> assumeThat(Duration actual) {
    return asAssumption(DurationAssert.class, Duration.class, actual);
  }

  /**
   * Creates a new instance of {@link PeriodAssert} assumption.
   *
   * @param actual the Period to test
   * @return the created assumption for assertion object.
   * @since 3.17.0
   */
  public static AbstractPeriodAssert<?> assumeThat(Period actual) {
    return asAssumption(PeriodAssert.class, Period.class, actual);
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
    return asAssumption(ProxyableListAssert.class, Stream.class, actual);
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
    return asAssumption(ProxyableListAssert.class, DoubleStream.class, actual);
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
    return asAssumption(ProxyableListAssert.class, LongStream.class, actual);
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
    return asAssumption(ProxyableListAssert.class, IntStream.class, actual);
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
    return asAssumption(SpliteratorAssert.class, Spliterator.class, actual);
  }

  // private methods

  private static <ASSERTION, ACTUAL> ASSERTION asAssumption(Class<ASSERTION> assertionType,
                                                            Class<ACTUAL> actualType,
                                                            Object actual) {
    return asAssumption(assertionType, array(actualType), array(actual));
  }

  private static <ASSERTION> ASSERTION asAssumption(Class<ASSERTION> assertionType,
                                                    Class<?>[] constructorTypes,
                                                    Object... constructorParams) {
    try {
      Class<? extends ASSERTION> type = createAssumptionClass(assertionType);
      Constructor<? extends ASSERTION> constructor = type.getConstructor(constructorTypes);
      return constructor.newInstance(constructorParams);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <ASSERTION> Class<? extends ASSERTION> createAssumptionClass(Class<ASSERTION> assertClass) {
    SimpleKey cacheKey = new SimpleKey(assertClass);
    return (Class<ASSERTION>) CACHE.findOrInsert(Assumptions.class.getClassLoader(),
                                                 cacheKey,
                                                 () -> generateAssumptionClass(assertClass));
  }

  protected static <ASSERTION> Class<? extends ASSERTION> generateAssumptionClass(Class<ASSERTION> assertionType) {
    return BYTE_BUDDY.subclass(assertionType)
                     // TODO ignore non assertion methods ?
                     .method(any())
                     .intercept(ASSUMPTION)
                     .make()
                     .load(Assumptions.class.getClassLoader(), classLoadingStrategy(assertionType))
                     .getLoaded();
  }

  private static RuntimeException assumptionNotMet(AssertionError assertionError) throws ReflectiveOperationException {
    Class<?> assumptionClass = getAssumptionClass("org.junit.AssumptionViolatedException");
    if (assumptionClass != null) return assumptionNotMet(assumptionClass, assertionError);

    assumptionClass = getAssumptionClass("org.opentest4j.TestAbortedException");
    if (assumptionClass != null) return assumptionNotMet(assumptionClass, assertionError);

    assumptionClass = getAssumptionClass("org.testng.SkipException");
    if (assumptionClass != null) return assumptionNotMet(assumptionClass, assertionError);

    throw new IllegalStateException("Assumptions require JUnit, opentest4j or TestNG on the classpath");
  }

  private static Class<?> getAssumptionClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  private static RuntimeException assumptionNotMet(Class<?> exceptionClass,
                                                   AssertionError e) throws ReflectiveOperationException {
    return (RuntimeException) exceptionClass.getConstructor(String.class, Throwable.class)
                                            .newInstance("assumption was not met due to: " + e.getMessage(), e);
  }

  // for method that change the object under test (e.g. extracting)
  private static AbstractAssert<?, ?> asAssumption(AbstractAssert<?, ?> assertion) {
    // @format:off
    Object actual = assertion.actual;
    if (assertion instanceof StringAssert) return asAssumption(StringAssert.class, String.class, actual);
    if (assertion instanceof FactoryBasedNavigableListAssert) return asAssumption(ProxyableListAssert.class, List.class, actual);
    if (assertion instanceof ProxyableIterableAssert) return asAssumption(ProxyableIterableAssert.class, Iterable.class, actual);
    if (assertion instanceof ProxyableMapAssert) return asAssumption(ProxyableMapAssert.class, Map.class, actual);
    if (assertion instanceof AbstractObjectArrayAssert) return asAssumption(ProxyableObjectArrayAssert.class, Object[].class, actual);
    if (assertion instanceof IterableSizeAssert) return asIterableSizeAssumption(assertion);
    if (assertion instanceof MapSizeAssert) return asMapSizeAssumption(assertion);
    if (assertion instanceof ProxyableObjectAssert) return asAssumption(ObjectAssert.class, Object.class, actual);
    if (assertion instanceof ObjectAssert) return asAssumption(ObjectAssert.class, Object.class, actual);
    if (assertion instanceof RecursiveComparisonAssert) return asRecursiveComparisonAssumption(assertion);
    // @format:on
    // should not arrive here
    throw new IllegalArgumentException("Unsupported assumption creation for " + assertion.getClass());
  }

  private static AbstractAssert<?, ?> asRecursiveComparisonAssumption(AbstractAssert<?, ?> assertion) {
    RecursiveComparisonAssert<?> recursiveComparisonAssert = (RecursiveComparisonAssert<?>) assertion;
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = recursiveComparisonAssert.getRecursiveComparisonConfiguration();
    Class<?>[] constructorTypes = array(Object.class, RecursiveComparisonConfiguration.class);
    return asAssumption(RecursiveComparisonAssert.class, constructorTypes, assertion.actual, recursiveComparisonConfiguration);
  }

  private static AbstractAssert<?, ?> asMapSizeAssumption(AbstractAssert<?, ?> assertion) {
    MapSizeAssert<?, ?> mapSizeAssert = (MapSizeAssert<?, ?>) assertion;
    Class<?>[] constructorTypes = array(AbstractMapAssert.class, Integer.class);
    return asAssumption(MapSizeAssert.class, constructorTypes, mapSizeAssert.returnToMap(), assertion.actual);
  }

  private static AbstractAssert<?, ?> asIterableSizeAssumption(AbstractAssert<?, ?> assertion) {
    IterableSizeAssert<?> iterableSizeAssert = (IterableSizeAssert<?>) assertion;
    Class<?>[] constructorTypes = array(AbstractIterableAssert.class, Integer.class);
    return asAssumption(IterableSizeAssert.class, constructorTypes, iterableSizeAssert.returnToIterable(), assertion.actual);
  }
}
