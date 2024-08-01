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

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.configuration.PreferredAssumptionException;
import org.assertj.core.util.CheckReturnValue;

/**
 * @author Gonzalo Müller
 * Behavior-driven development style entry point for assumption methods for different types, which allow to skip test execution when assumptions are not met.
 * <p>
 * The difference with the {@link Assumptions} class is that entry point methods are named <b><code>given</code></b> instead of
 * <code>assumeThat</code>.
 * <p>
 * {@link BDDAssumptions} and {@link BDDAssertions} complement each other to allow a fluent Behavior-driven development.
 * <p>
 * Examples:
 * <pre><code class='java'> String hobbit = "HOBBIT";
 * List&lt;String&gt; fellowshipOfTheRing = list("Aragorn", "Gandalf", "Frodo", "Legolas"); // and more
 *
 * {@literal @Test}
 * public void given_the_assumption_is_not_met_the_test_is_skipped() {
 *   given(hobbit).isEqualTo("ORC");
 *   // ... following code is not executed
 *   then(fellowshipOfTheRing).contains("Sauron");
 * }
 *
 * {@literal @Test}
 * public void given_the_assumption_is_met_the_test_is_executed() {
 *   given(hobbit).isEqualTo("HOBBIT");
 *   // ... following code is executed
 *   then(fellowshipOfTheRing).doesNotContain("Sauron");
 * }</code></pre>
 *
 * @since 3.14.0
 */
@CheckReturnValue
public final class BDDAssumptions extends Assumptions {

  private BDDAssumptions() {}

  /**
   * Creates a new assumption's instance for a <code>boolean</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(true).isTrue();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(true).isFalse();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>boolean</code> value to be validated.
   * @return the {@link AbstractBooleanAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractBooleanAssert<?> given(boolean actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Boolean} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Boolean.valueOf(true)).isTrue();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Boolean.valueOf(true)).isFalse();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Boolean} value to be validated.
   * @return the {@link AbstractBooleanAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractBooleanAssert<?> given(Boolean actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>boolean</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new boolean[] { true, true }).contains(true);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new boolean[] { true, true }).contains(false);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>boolean</code>s' array to be validated.
   * @return the {@link AbstractBooleanArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractBooleanArrayAssert<?> given(boolean[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>boolean</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new boolean[][] {{true, true}, {false, false}}).contains(new boolean[] {true, true}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new boolean[][] {{true, true}, {false, false}}).contains(new boolean[] {true, true}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>boolean</code>s' two-dimensional array to be validated.
   * @return the {@link Boolean2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Boolean2DArrayAssert given(boolean[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>byte</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given((byte) 1).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given((byte) 1).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>byte</code> value to be validated.
   * @return the {@link AbstractByteAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractByteAssert<?> given(byte actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Byte} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Byte.valueOf("1")).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Byte.valueOf("1")).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Byte} value to be validated.
   * @return the {@link AbstractByteAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractByteAssert<?> given(Byte actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>byte</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new byte[] { 1, 2 }).contains((byte) 1);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new byte[] { 1, 2 }).contains((byte) 0);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual bytes' array to be validated.
   * @return the {@link AbstractByteArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractByteArrayAssert<?> given(byte[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>byte</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new byte[][] {{1, 2}, {3, 4}}).contains(new byte[] {1, 2)}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new byte[][] {{1, 2}, {3, 4}}).contains(new byte[] {1, 2)}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual bytes' two-dimensional array to be validated.
   * @return the {@link Byte2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Byte2DArrayAssert given(byte[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>short</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given((short) 1).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given((short) 1).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>short</code> value to be validated.
   * @return the {@link AbstractShortAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractShortAssert<?> given(short actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Short} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Short.valueOf("1")).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Short.valueOf("1")).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Short} value to be validated.
   * @return the {@link AbstractShortAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractShortAssert<?> given(Short actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>short</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new short[] { 1, 2 }).contains((short) 1);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new short[] { 1, 2 }).contains((short) 0);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>short</code>s' array to be validated.
   * @return the {@link AbstractShortArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractShortArrayAssert<?> given(short[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>short</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new short[][] {{1, 2}, {3, 4}}).contains(new short[] {1, 2}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new short[][] {{1, 2}, {3, 4}}).contains(new short[] {1, 2}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>short</code>s' two-dimensional array to be validated.
   * @return the {@link Short2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Short2DArrayAssert given(short[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an <code>int</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(1).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(1).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>int</code> value to be validated.
   * @return the {@link AbstractIntegerAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractIntegerAssert<?> given(int actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link Integer} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Integer.valueOf("1")).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Integer.valueOf("1")).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Integer} value to be validated.
   * @return the {@link AbstractIntegerAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractIntegerAssert<?> given(Integer actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an <code>int</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new int[] { 1, 2 }).contains((short) 1);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new int[] { 1, 2 }).contains((short) 0);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>int</code>s' array to be validated.
   * @return the {@link AbstractIntArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractIntArrayAssert<?> given(int[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an <code>int</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new int[][] {{1, 2}, {3, 4}}).contains(new int[] {1, 2}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new int[][] {{1, 2}, {3, 4}}).contains(new int[] {1, 2}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>int</code>s' two-dimensional array to be validated.
   * @return the {@link Int2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Int2DArrayAssert given(int[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link BigInteger} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(BigInteger.valueOf(1L)).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(BigInteger.valueOf(1L)).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link BigInteger} value to be validated.
   * @return the {@link AbstractBigIntegerAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractBigIntegerAssert<?> given(BigInteger actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>long</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(1L).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(1L).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>long</code> value to be validated.
   * @return the {@link AbstractLongAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractLongAssert<?> given(long actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Long} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Long.valueOf(1L)).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Long.valueOf(1L)).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Long} value to be validated.
   * @return the {@link AbstractLongAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractLongAssert<?> given(Long actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>long</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new long[] { 1, 2 }).contains(1L);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new long[] { 1, 2 }).contains(0L);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>long</code>s' array to be validated.
   * @return the {@link AbstractLongArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractLongArrayAssert<?> given(long[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>long</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new long[][] {{1, 2}, {3, 4}}).contains(new long[] {1, 2}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new long[][] {{1, 2}, {3, 4}}).contains(new long[] {1, 2}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>long</code>s' two-dimensional array to be validated.
   * @return the {@link Long2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Long2DArrayAssert given(long[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>float</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(1.0f).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(1.0f).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>float</code> value to be validated.
   * @return the {@link AbstractFloatAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractFloatAssert<?> given(float actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Float} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Float.valueOf(1.0f)).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Float.valueOf(1.0f)).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Float} value to be validated.
   * @return the {@link AbstractFloatAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractFloatAssert<?> given(Float actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>float</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new float[] { 1.0f, 2.0f }).contains(1.0f);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new float[] { 1.0f, 2.0f }).contains(0.0f);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>float</code>s' array to be validated.
   * @return the {@link AbstractFloatArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractFloatArrayAssert<?> given(float[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>float</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}}).contains(new float[] {1.0f, 2.0f}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}}).contains(new float[] {1.0f, 2.0f}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>float</code>s' two-dimensional array to be validated.
   * @return the {@link Float2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Float2DArrayAssert given(float[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>double</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(1.0).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(1.0).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>double</code> value to be validated.
   * @return the {@link AbstractDoubleAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractDoubleAssert<?> given(double actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Double} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Double.valueOf(1.0f)).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Double.valueOf(1.0f)).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Double} value to be validated.
   * @return the {@link AbstractDoubleAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractDoubleAssert<?> given(Double actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an <code>double</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new double[] { 1.0, 2.0 }).contains(1.0);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new double[] { 1.0, 2.0 }).contains(0.0);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>double</code>s' array to be validated.
   * @return the {@link AbstractDoubleArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractDoubleArrayAssert<?> given(double[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an <code>double</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new double[][] {{1.0, 2.0}, {3.0, 4.0}}).contains(new double[] {1.0, 2.0}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new double[][] {{1.0, 2.0}, {3.0, 4.0}}).contains(new double[] {1.0, 2.0}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>double</code>s' two-dimensional array to be validated.
   * @return the {@link Double2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Double2DArrayAssert given(double[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link BigDecimal} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(BigDecimal.valueOf(1.0)).isOne();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(BigDecimal.valueOf(1.0)).isZero();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link BigDecimal} value to be validated.
   * @return the {@link AbstractBigDecimalAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractBigDecimalAssert<?> given(BigDecimal actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a <code>char</code> value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given('A').isUpperCase();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given('A').isLowerCase();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>char</code> value to be validated.
   * @return the {@link AbstractCharacterAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractCharacterAssert<?> given(char actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Character} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Character.valueOf('A')).isUpperCase();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Character.valueOf('A')).isLowerCase();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Character} value to be validated.
   * @return the {@link AbstractCharacterAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractCharacterAssert<?> given(Character actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an <code>char</code>s' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new char[] { 'A', 'B' }).contains('A');
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new char[] { 'A', 'B' }).contains('C');
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>char</code>s' array to be validated.
   * @return the {@link AbstractCharacterAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractCharArrayAssert<?> given(char[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an <code>char</code>s' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new char[][] {{'A', 'B'}, {'C', 'D'}}).contains(new char[] {'A', 'B'}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new char[][] {{'A', 'B'}, {'C', 'D'}}).contains(new char[] {'A', 'B'}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual <code>char</code>s' two-dimensional array to be validated.
   * @return the {@link Char2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static Char2DArrayAssert given(char[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link CharSequence} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given((CharSequence) "Yoda").isNotEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given((CharSequence) "Yoda").isNullOrEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link CharSequence} value to be validated.
   * @return the {@link AbstractCharSequenceAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> given(CharSequence actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link CharSequence} value.
   * <p>
   * Use this over {@link #given(CharSequence)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param actual the actual {@link CharSequence} value to be validated.
   * @return the {@link AbstractCharSequenceAssert} assertion object to be used for assumptions.
   * @since 3.25.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> givenCharSequence(CharSequence actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link String} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given("Yoda").isNotEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given("Yoda").isNullOrEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link String} value to be validated.
   * @return the {@link AbstractStringAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractStringAssert<?> given(String actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link StringBuilder} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new StringBuilder("Yoda")).isNotEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new StringBuilder("Yoda")).isNullOrEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link StringBuilder} value to be validated.
   * @return the {@link AbstractCharSequenceAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> given(StringBuilder actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link StringBuffer} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new StringBuffer("Yoda")).isNotEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new StringBuffer("Yoda")).isNullOrEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link StringBuffer} value to be validated.
   * @return the {@link AbstractCharSequenceAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractCharSequenceAssert<?, ? extends CharSequence> given(StringBuffer actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Class} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Number.class).isAssignableFrom(Long.class);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Number.class).isInterface();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Class} value to be validated.
   * @return the {@link AbstractClassAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static ClassAssert given(Class<?> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an object value.
   * <p>
   * Examples:
   * <pre>{@code
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 33, HOBBIT);
   * }</pre>
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(frodo).hasNoNullFieldsOrProperties();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(mysteriousHobbit).hasNoNullFieldsOrProperties();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <T> the type of the actual object.
   * @param actual the actual object to be validated.
   * @return the {@link AbstractObjectAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <T> ObjectAssert<T> given(T actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an objects' array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new String[] { "A", "B" }).hasSizeGreaterThan(1);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new String[] { "A", "B" }).hasSizeGreaterThan(2);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <T> the type of elements of the actual objects' array.
   * @param actual the actual objects' array to be validated..
   * @return the {@link AbstractObjectArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <T> ObjectArrayAssert<T> given(T[] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an objects' two-dimensional array.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new String[][] {{"A", "B"}, {"C", "D"}}).contains(new String[] {"A", "B"}, atIndex(0));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *  given(new String[][] {{"A", "B"}, {"C", "D"}}).contains(new String[] {"A", "B"}, atIndex(1));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <T> the type of elements of the actual objects' two-dimensional array.
   * @param actual the actual objects' two-dimensional array to be validated..
   * @return the {@link Object2DArrayAssert} assertion object to be used for assumptions.
   * @since 3.17.0
   */
  public static <T> Object2DArrayAssert<T> given(T[][] actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an object value.
   * <p>
   * This overload is useful, when an overloaded method of given(...) takes precedence over the generic {@link #given(Object) given(T)}, and the assumption requires to access some general assertion methods.
   * <p>
   * Example:
   * <p>
   * {@link #given(List)} takes precedence over the generic {@link #given(Object) given(T)}
   * <p>
   * then when using some base general assert methods, e.g. {@link AbstractAssert#matches(Predicate)}, cast is necessary because {@link #given(List)} "forgets" actual type:
   * <pre>{@code given(new LinkedList<>(asList("abc"))).matches(list -> ((Deque<String>) list).getFirst().equals("abc")); }</pre>
   * with <b><code>givenObject</code></b>  no cast is needed:
   * <pre>{@code givenObject(new LinkedList<>(asList("abc"))).matches(list -> list.getFirst().equals("abc")); }</pre>
   *
   * @param <T> the type of the actual object.
   * @param actual the actual object to be validated.
   * @return the {@link AbstractObjectAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <T> ObjectAssert<T> givenObject(T actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Comparable} value.
   * <p>
   * Examples:
   * <pre>{@code
   *  class Yoda implements Comparable<Yoda> {
   *    public int compareTo(Yoda to) {
   *      return 0;
   *    }
   *  }
   * }</pre>
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new Yoda()).isEqualByComparingTo(new Yoda());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new Yoda()).isNotEqualByComparingTo(new Yoda());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <T> the type of the actual comparable value.
   * @param actual the actual {@link Comparable} value to be validated.
   * @return the {@link AbstractComparableAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> given(T actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of a <code>{@link Comparable}</code> value.
   * <p>
   * Use this over {@link #given(Comparable)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <T extends Comparable<? super T>> AbstractComparableAssert<?, T> givenComparable(T actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Throwable} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new Exception("Yoda time")).hasMessage("Yoda time");
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new Exception("Yoda time")).hasMessage("");
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <T> the type of the actual throwable.
   * @param actual the actual {@link Throwable} value to be validated.
   * @return the {@link AbstractThrowableAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <T extends Throwable> AbstractThrowableAssert<?, T> given(T actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Throwable} value.
   *
   * @param <T> the exception type.
   * @param exceptionType the exception type class.
   * @return the created {@link ThrowableTypeAssert}.
   * @since 3.23.0
   */
  public static <T extends Throwable> ThrowableTypeAssert<T> givenExceptionOfType(final Class<? extends T> exceptionType) {
    return assumeThatExceptionOfType(exceptionType);
  }

  /**
   * Alias for {@link #givenExceptionOfType(Class)} for {@link Exception}.
   *
   * @return the {@link ThrowableAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<Exception> givenException() {
    return assumeThatException();
  }

  /**
   * Alias for {@link #givenExceptionOfType(Class)} for {@link RuntimeException}.
   *
   * @return the {@link ThrowableAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<RuntimeException> givenRuntimeException() {
    return assumeThatRuntimeException();
  }

  /**
   * Alias for {@link #givenExceptionOfType(Class)} for {@link NullPointerException}.
   *
   * @return the {@link ThrowableAssert} assertion object to be used for assumptions.
   *
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<NullPointerException> givenNullPointerException() {
    return assumeThatNullPointerException();
  }

  /**
   * Alias for {@link #givenExceptionOfType(Class)} for {@link IllegalArgumentException}.
   *
   * @return the {@link ThrowableAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<IllegalArgumentException> givenIllegalArgumentException() {
    return assumeThatIllegalArgumentException();
  }

  /**
   * Alias for {@link #givenExceptionOfType(Class)} for {@link IOException}.
   *
   * @return the {@link ThrowableAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<IOException> givenIOException() {
    return assumeThatIOException();
  }

  /**
   * Alias for {@link #givenExceptionOfType(Class)} for {@link IndexOutOfBoundsException}.
   *
   * @return the {@link ThrowableAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<IndexOutOfBoundsException> givenIndexOutOfBoundsException() {
    return assumeThatIndexOutOfBoundsException();
  }

  /**
   * Alias for {@link #givenExceptionOfType(Class)} for {@link ReflectiveOperationException}.
   *
   * @return the {@link ThrowableAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static ThrowableTypeAssert<ReflectiveOperationException> givenReflectiveOperationException() {
    return assumeThatReflectiveOperationException();
  }

  /**
   * Creates a new assumption's instance from a no parameters lambda expression, <code>{@literal () ->} { /* some code {@literal *}/ }</code>.
   * <p>
   * Examples:
   * <p>
   * <u>No Exception required</u>:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal givenCode(() ->} {{@literal /* some code *}/ }).doesNotThrowAnyException();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal givenCode(() ->} {{@literal /* some code *}/ }).hasMessage("Yoda time");
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   * <p>
   * <u>Exception required</u>:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal givenCode(() -> {throw new Exception("Yoda time");}).hasMessage("Yoda time");
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal givenCode(() -> {throw new Exception("Yoda time");}).doesNotThrowAnyException();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param lambda the {@link ThrowingCallable} or lambda with the code that may raise a throwable to be validated.
   * @return the {@link AbstractThrowableAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractThrowableAssert<?, ? extends Throwable> givenCode(ThrowingCallable lambda) {
    return assumeThatCode(lambda);
  }

  /**
   * Creates a new assumption's instance for an {@link Iterable} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *  {@literal given((Iterable<Integer>)(Arrays.asList(1, 2))).contains(2);}
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *  {@literal given((Iterable<Integer>)(Arrays.asList(1, 2))).containsOnly(2);}
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <ELEMENT> the type of elements of actual iterable value.
   * @param actual the actual {@link Iterable} value to be validated.
   * @return the {@link AbstractIterableAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <ELEMENT> IterableAssert<ELEMENT> given(Iterable<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of an <code>{@link Iterable}</code> value.
   * <p>
   * Use this over {@link #given(Iterable)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param <ELEMENT> the type of elements of actual iterable value.
   * @param actual the actual {@link Iterable} value to be validated.
   * @return the {@link AbstractIterableAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static <ELEMENT> IterableAssert<ELEMENT> givenIterable(Iterable<? extends ELEMENT> actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link Iterator} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Arrays.asList(1, 2).iterator()).hasNext();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Arrays.asList(1, 2).iterator()).isExhausted();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <ELEMENT> the type of elements of actual iterator value.
   * @param actual the actual {@link Iterator} value to be validated.
   * @return the {@link AbstractIteratorAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <ELEMENT> IteratorAssert<ELEMENT> given(Iterator<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of an <code>{@link Iterator}</code> value.
   * <p>
   * Use this over {@link #given(Iterator)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param <ELEMENT> the type of elements of actual iterable value.
   * @param actual the actual {@link Iterator} value to be validated.
   * @return the {@link AbstractIteratorAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static <ELEMENT> IteratorAssert<ELEMENT> givenIterator(Iterator<? extends ELEMENT> actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Collection} value.
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @see Assumptions#assumeThat(Collection)
   * @since 3.21.0
   */
  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> given(Collection<? extends E> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of a <code>{@link Collection}</code> value.
   * <p>
   * Use this over {@link #given(Collection)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param <E> the type of elements.
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   * @since 3.23.0
   */
  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> givenCollection(Collection<? extends E> actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link List} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Arrays.asList(1, 2)).contains(2);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Arrays.asList(1, 2)).containsOnly(2);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <ELEMENT> the type of elements of actual list value.
   * @param actual the actual {@link List} value to be validated.
   * @return the {@link AbstractListAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <ELEMENT> FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> given(List<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of a <code>{@link List}</code> value.
   * <p>
   * Use this over {@link #given(List)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual value.
   * @return the {@link AbstractListAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static <ELEMENT> FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> givenList(List<? extends ELEMENT> actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Map} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Collections.singletonMap(1, 2)).containsEntry(1, 2);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Collections.singletonMap(1, 2)).containsEntry(2, 1);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <K> the type of keys in the actual map value.
   * @param <V> the type of values in the actual map value.
   * @param actual the actual {@link Map} value to be validated.
   * @return the {@link AbstractMapAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <K, V> MapAssert<K, V> given(Map<K, V> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Predicate} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal given((Predicate<Integer>)(value -> value > 0)).accepts(1, 2);}
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal given((Predicate<Integer>)(value -> value > 0)).accepts(-2, -1);}
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <T> the type of the value contained in the actual predicate value.
   * @param actual the actual {@link Predicate} value to be validated.
   * @return the {@link AbstractPredicateAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <T> PredicateAssert<T> given(Predicate<T> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of a <code>{@link Predicate}</code> value.
   * <p>
   * Use this over {@link #given(Predicate)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param <T> the type of elements.
   * @param actual the actual value.
   * @return the {@link AbstractPredicateAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static <T> PredicateAssert<T> givenPredicate(Predicate<T> actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link IntPredicate} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal given((IntPredicate)(value -> value > 0)).accepts(1, 2);}
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal given((IntPredicate)(value -> value > 0)).accepts(-2, -1);}
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link IntPredicate} value to be validated.
   * @return the {@link IntPredicateAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static IntPredicateAssert given(IntPredicate actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link LongPredicate} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal given((LongPredicate)(value -> value > 0)).accepts(1, 2);}
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal given((LongPredicate)(value -> value > 0)).accepts(-2, -1);}
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link LongPredicate} value to be validated.
   * @return the {@link LongPredicateAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static LongPredicateAssert given(LongPredicate actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link DoublePredicate} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal given((DoublePredicate)(value -> value > 0)).accepts(1.0, 2.0);}
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal given((DoublePredicate)(value -> value > 0)).accepts(-2.0, -1.0);}
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link DoublePredicate} value to be validated.
   * @return the {@link DoublePredicateAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static DoublePredicateAssert given(DoublePredicate actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link Optional} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Optional.empty()).isEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Optional.empty()).isNotEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <VALUE> the type of the value contained in the actual optional value.
   * @param actual the actual {@link Optional} value to be validated.
   * @return the {@link OptionalAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <VALUE> OptionalAssert<VALUE> given(Optional<VALUE> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link OptionalInt} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(OptionalInt.empty()).isEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(OptionalInt.empty()).isNotEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link OptionalInt} value to be validated.
   * @return the {@link OptionalIntAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static OptionalIntAssert given(OptionalInt actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link Matcher}.
   * <p>
   *
   * @param actual the actual {@link Matcher} value to be validated.
   * @return the {@link Matcher} assertion object to be used for assumptions.
   */
  public static MatcherAssert given(Matcher actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link OptionalLong} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(OptionalLong.empty()).isEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(OptionalLong.empty()).isNotEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link OptionalLong} value to be validated.
   * @return the {@link OptionalLongAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static OptionalLongAssert given(OptionalLong actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link OptionalDouble} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(OptionalDouble.empty()).isEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(OptionalDouble.empty()).isNotEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link OptionalDouble} value to be validated.
   * @return the {@link OptionalDoubleAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static OptionalDoubleAssert given(OptionalDouble actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Stream} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Stream.of(1, 2)).contains(2);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Stream.of(1, 2)).containsOnly(2);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <ELEMENT> the type of the value contained in the actual stream value.
   * @param actual the actual {@link Stream} value to be validated.
   * @return the {@link AbstractListAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> given(Stream<? extends ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of a <code>{@link Stream}</code> value.
   * <p>
   * Use this over {@link #given(Stream)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param <ELEMENT> the type of elements.
   * @param actual the actual {@link Stream} value to be validated.
   * @return the {@link AbstractListAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> givenStream(Stream<? extends ELEMENT> actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link IntStream} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(IntStream.of(1, 2)).contains(2);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(IntStream.of(1, 2)).containsOnly(2);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link IntStream} value to be validated.
   * @return the {@link AbstractListAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractListAssert<?, List<? extends Integer>, Integer, ObjectAssert<Integer>> given(IntStream actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Spliterator} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Stream.of(1, 2).spliterator()).hasCharacteristics(Spliterator.SIZED)
   *   // the remaining code is executed
   *   ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Stream.of(1, 2).spliterator()).hasCharacteristics(Spliterator.DISTINCT)
   *   // the remaining code is NOT executed.
   * }</code></pre>
   *
   * @param <ELEMENT> the type of the elements
   * @param actual the actual {@link Spliterator} value to be validated.
   * @return the {@link AbstractSpliteratorAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <ELEMENT> AbstractSpliteratorAssert<?, ELEMENT> given(Spliterator<ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link LongStream} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(LongStream.of(1, 2)).contains(2);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(LongStream.of(1, 2)).containsOnly(2);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link LongStream} value to be validated.
   * @return the {@link AbstractListAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractListAssert<?, List<? extends Long>, Long, ObjectAssert<Long>> given(LongStream actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link DoubleStream} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(DoubleStream.of(1.0, 2.0)).contains(2.0);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(DoubleStream.of(1.0, 2.0)).containsOnly(2.0);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link DoubleStream} value to be validated.
   * @return the {@link AbstractListAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractListAssert<?, List<? extends Double>, Double, ObjectAssert<Double>> given(DoubleStream actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Future} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal given(Executors.newSingleThreadExecutor().submit(() -> {})).isNotCancelled();}
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal given(Executors.newSingleThreadExecutor().submit(() -> {})).isCancelled();}
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <RESULT> the type of the value contained in the actual future value.
   * @param future the {@link Future} value to be validated.
   * @return the {@link AbstractFutureAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <RESULT> AbstractFutureAssert<?, ? extends Future<? extends RESULT>, RESULT> given(Future<RESULT> future) {
    return assumeThat(future);
  }

  /**
   * Creates a new assumption's instance for a {@link CompletableFuture} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(CompletableFuture.completedFuture​(1)).isDone();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(CompletableFuture.completedFuture​(1)).isNotDone();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <RESULT> the type of the value contained in the actual future value.
   * @param future the {@link CompletableFuture} value to be validated.
   * @return the {@link AbstractCompletableFutureAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <RESULT> CompletableFutureAssert<RESULT> given(CompletableFuture<RESULT> future) {
    return assumeThat(future);
  }

  /**
   * Creates a new assumption's instance for a {@link CompletionStage} value.
   * <p>
   * Converts the {@link CompletionStage} into a {@link CompletableFuture}.
   * If the given {@link CompletionStage} is null, the associated {@link CompletableFuture} will also be null.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   {@literal given((CompletionStage<Integer>) CompletableFuture.completedFuture​(1)).isDone();}
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   {@literal given((CompletionStage<Integer>) CompletableFuture.completedFuture​(1)).isNotDone();}
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <RESULT> the type of the value contained in the actual future value.
   * @param stage the {@link CompletionStage} value to be validated.
   * @return the {@link AbstractCompletableFutureAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <RESULT> CompletableFutureAssert<RESULT> given(CompletionStage<RESULT> stage) {
    return assumeThat(stage);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicBoolean} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicBoolean(true)).isTrue();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicBoolean(true)).isFalse();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link AtomicBoolean} value to be validated.
   * @return the {@link AtomicBooleanAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AtomicBooleanAssert given(AtomicBoolean actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicInteger} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicInteger(1)).hasNonNegativeValue();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicInteger(1)).hasNegativeValue();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link AtomicInteger} value to be validated.
   * @return the {@link AtomicIntegerAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AtomicIntegerAssert given(AtomicInteger actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicIntegerArray} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicIntegerArray(0)).isEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicIntegerArray(0)).isNotEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link AtomicIntegerArray} value to be validated.
   * @return the {@link AtomicIntegerArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AtomicIntegerArrayAssert given(AtomicIntegerArray actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicIntegerFieldUpdater} value.
   * <p>
   * Examples:
   * <pre>{@code
   * class Yoda {
   *   public volatile int field = 0;
   * }
   * }</pre>
   * <pre>{@code
   * AtomicIntegerFieldUpdater actual = AtomicIntegerFieldUpdater.newUpdater​(Yoda.class, "field");
   * Yoda value = new Yoda();
   * actual.set(value, 1);
   * }</pre>
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(actual).hasValue(1, value);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(actual).hasValue(2, value));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <OBJECT> the type of the object holding the updatable field which gets updated by the actual value.
   * @param actual the actual {@link AtomicIntegerFieldUpdater} value to be validated.
   * @return the {@link AtomicIntegerFieldUpdaterAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <OBJECT> AtomicIntegerFieldUpdaterAssert<OBJECT> given(AtomicIntegerFieldUpdater<OBJECT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link LongAdder} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new LongAdder()).isNotNegative();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new LongAdder()).isNegative();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link LongAdder} value to be validated.
   * @return the {@link LongAdderAssert} assertion object to be used for assumptions.
   * @since 3.16.0
   */
  public static LongAdderAssert given(LongAdder actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicLong} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicLong(1L)).hasNonNegativeValue();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicLong(1L)).hasNegativeValue();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link AtomicLong} value to be validated.
   * @return the {@link AtomicLongAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AtomicLongAssert given(AtomicLong actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicLongArray} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicLongArray(0)).isEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicLongArray(0)).isNotEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link AtomicLongArray} value to be validated.
   * @return the {@link AtomicLongArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AtomicLongArrayAssert given(AtomicLongArray actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicLongFieldUpdater} value.
   * <p>
   * Examples:
   * <pre>{@code
   * class Yoda {
   *   public volatile long field = 0L;
   * }
   * }</pre>
   * <pre>{@code
   * AtomicLongFieldUpdater actual = AtomicLongFieldUpdater.newUpdater​(Yoda.class, "field");
   * Yoda value = new Yoda();
   * actual.set(value, 1L);
   * }</pre>
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(actual).hasValue(1L, value);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(actual).hasValue(2L, value));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <OBJECT> the type of the object holding the updatable field which gets updated by the actual value.
   * @param actual the actual {@link AtomicLongFieldUpdater} value to be validated.
   * @return the {@link AtomicLongFieldUpdaterAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <OBJECT> AtomicLongFieldUpdaterAssert<OBJECT> given(AtomicLongFieldUpdater<OBJECT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicReference} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicReference("Yoda")).hasValue("Yoda");
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicReference("Yoda")).doesNotHaveValue("Yoda");
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <VALUE> the type of the value contained by the actual reference.
   * @param actual the actual {@link AtomicReference} to be validated.
   * @return the {@link AtomicReferenceAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <VALUE> AtomicReferenceAssert<VALUE> given(AtomicReference<VALUE> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicReferenceArray} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicReferenceArray(0)).isEmpty();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicReferenceArray(0)).isNotEmpty();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <ELEMENT> the type of the value contained in the actual references' array.
   * @param actual the actual {@link AtomicReferenceArray} to be validated.
   * @return the {@link AtomicReferenceArrayAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <ELEMENT> AtomicReferenceArrayAssert<ELEMENT> given(AtomicReferenceArray<ELEMENT> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicReferenceFieldUpdater} value.
   * <p>
   * Examples:
   * <pre>{@code
   * class Yoda {
   *   public volatile String field = "";
   * }
   * }</pre>
   * <pre>{@code
   * AtomicReferenceFieldUpdater actual = AtomicReferenceFieldUpdater.newUpdater​(Yoda.class, String.class, "field");
   * Yoda value = new Yoda();
   * actual.set(value, "Yoda");
   * }</pre>
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(actual).hasValue("Yoda", value));
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(actual).hasValue("", value));
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <FIELD> the type of the field which gets updated by the actual updater.
   * @param <OBJECT> the type of the object holding the updatable field which gets updated by the actual updater.
   * @param actual the actual {@link AtomicReferenceFieldUpdater} value to be validated.
   * @return the {@link AtomicReferenceFieldUpdaterAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <FIELD, OBJECT> AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> given(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicMarkableReference} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicMarkableReference("Yoda", true)).isMarked();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicMarkableReference("Yoda", true)).isNotMarked();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <VALUE> the type of the value contained by the actual reference.
   * @param actual the actual {@link AtomicMarkableReference} to be validated.
   * @return the {@link AtomicMarkableReferenceAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <VALUE> AtomicMarkableReferenceAssert<VALUE> given(AtomicMarkableReference<VALUE> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link AtomicStampedReference} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new AtomicStampedReference("Yoda", 1)).hasStamp(1);
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new AtomicStampedReference("Yoda", 1)).hasStamp(0);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param <VALUE> the type of the value contained by the actual reference.
   * @param actual the actual {@link AtomicStampedReference} to be validated.
   * @return the {@link AtomicStampedReferenceAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static <VALUE> AtomicStampedReferenceAssert<VALUE> given(AtomicStampedReference<VALUE> actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Date} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Date.from(Instant.parse("2014-12-03T10:15:30Z"))).isBefore("2016-12-03T10:15:30Z");
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Date.from(Instant.parse("2014-12-03T10:15:30Z"))).isAfter("2016-12-03T10:15:30Z");
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Date} value to be validated.
   * @return the {@link AbstractDateAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractDateAssert<?> given(Date actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link LocalDate} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(LocalDate.now()).isBeforeOrEqualTo(LocalDate.now());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(LocalDate.now()).isAfter(LocalDate.now());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link LocalDate} value to be validated.
   * @return the {@link AbstractLocalDateAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractLocalDateAssert<?> given(LocalDate actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link LocalTime} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(LocalTime.now()).isBeforeOrEqualTo(LocalTime.now());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(LocalTime.now()).isAfter(LocalTime.now());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link LocalTime} value to be validated.
   * @return the {@link AbstractLocalTimeAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractLocalTimeAssert<?> given(LocalTime actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link OffsetTime} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(OffsetTime.now()).isBeforeOrEqualTo(OffsetTime.now());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(OffsetTime.now()).isAfter(OffsetTime.now());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link OffsetTime} value to be validated.
   * @return the {@link AbstractOffsetTimeAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractOffsetTimeAssert<?> given(OffsetTime actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link LocalDateTime} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(LocalDateTime.now()).isBeforeOrEqualTo(LocalDateTime.now());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(LocalDateTime.now()).isAfter(LocalDateTime.now());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link LocalDateTime} value to be validated.
   * @return the {@link AbstractLocalDateTimeAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractLocalDateTimeAssert<?> given(LocalDateTime actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link Instant} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(Instant.now()).isBeforeOrEqualTo(Instant.now());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(Instant.now()).isAfter(Instant.now());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Instant} value to be validated.
   * @return the {@link AbstractInstantAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractInstantAssert<?> given(Instant actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Instant} value.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.15.0
   */
  public static AbstractDurationAssert<?> given(Duration actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link java.time.Period} value.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   * @since 3.17.0
   */
  public static AbstractPeriodAssert<?> given(Period actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link OffsetDateTime} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(OffsetDateTime.now()).isBeforeOrEqualTo(OffsetDateTime.now());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(OffsetDateTime.now()).isAfter(OffsetDateTime.now());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link OffsetDateTime} value to be validated.
   * @return the {@link AbstractOffsetDateTimeAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractOffsetDateTimeAssert<?> given(OffsetDateTime actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link ZonedDateTime} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(ZonedDateTime.now()).isBeforeOrEqualTo(ZonedDateTime.now());
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(ZonedDateTime.now()).isAfter(ZonedDateTime.now());
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link ZonedDateTime} value to be validated.
   * @return the {@link AbstractZonedDateTimeAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractZonedDateTimeAssert<?> given(ZonedDateTime actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link InputStream} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new ByteArrayInputStream​("A".getBytes())).hasContent("A");
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new ByteArrayInputStream​("A".getBytes())).hasContent("B");
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link InputStream} value to be validated.
   * @return the {@link AbstractInputStreamAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractInputStreamAssert<?, ? extends InputStream> given(InputStream actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link File} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new File("file.ext")).isRelative();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new File("file.ext")).isAbsolute();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link File} value to be validated.
   * @return the {@link AbstractFileAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractFileAssert<?> given(File actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for a {@link Path} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new File("file.ext").toPath()).isRelative();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new File("file.ext").toPath()).isAbsolute();
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link Path} value to be validated.
   * @return the {@link AbstractPathAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractPathAssert<?> given(Path actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance of a <code>{@link Path}</code> value.
   * <p>
   * Use this over {@link #given(Path)} in case of ambiguous method resolution when the object under test
   * implements several interfaces Assertj provides <code>given</code> for.
   *
   * @param actual the actual {@link Path} value to be validated.
   * @return the {@link AbstractPathAssert} assertion object to be used for assumptions.
   * @since 3.23.0
   */
  public static AbstractPathAssert<?> givenPath(Path actual) {
    return given(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link URI} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new URI("http://assertj.org")).hasNoPort();
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new URI("http://assertj.org")).hasPort(80);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link URI} value to be validated.
   * @return the {@link AbstractUriAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractUriAssert<?> given(URI actual) {
    return assumeThat(actual);
  }

  /**
   * Creates a new assumption's instance for an {@link URL} value.
   * <p>
   * Examples:
   * <p>
   * Executed test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_met_the_test_is_executed() {
   *   given(new URL("http://assertj.org")).hasProtocol("http");
   *   // the remaining code is executed
   *   // ...
   * }</code></pre>
   * <p>
   * Skipped test:
   * <pre><code class='java'> {@literal @Test}
   * public void given_the_assumption_is_not_met_the_test_is_skipped() {
   *   given(new URL("http://assertj.org")).hasPort(80);
   *   // the remaining code is NOT executed.
   *   // ...
   *}</code></pre>
   *
   * @param actual the actual {@link URL} value to be validated.
   * @return the {@link AbstractUrlAssert} assertion object to be used for assumptions.
   * @since 3.14.0
   */
  public static AbstractUrlAssert<?> given(URL actual) {
    return assumeThat(actual);
  }

  /**
   * Sets which exception is thrown if an assumption is not met.
   * <p>
   * This method is useful if you are using a testing framework that supports assumptions and expect a specific exception to be thrown when an assumption is not met.
   * <p>
   * You can choose one of:
   * <ul>
   * <li>{@link PreferredAssumptionException#TEST_NG} to throw a {@code org.testng.SkipException} if you are using TestNG</li>
   * <li>{@link PreferredAssumptionException#JUNIT4} to throw a {@code org.junit.AssumptionViolatedException} if you are using JUnit 4</li>
   * <li>{@link PreferredAssumptionException#JUNIT5} a {@code org.opentest4j.TestAbortedException} if you are using JUnit 5</li>
   * <li>{@link PreferredAssumptionException#AUTO_DETECT} to get the default behavior where AssertJ tries different exception (explained later on)</li>
   * </ul>
   * <p>
   * Make sure that the exception you choose can be found in the classpath otherwise AssertJ will throw an {@link IllegalStateException}.
   * <p>
   * For example JUnit4 expects {@code org.junit.AssumptionViolatedException}, you can tell AssertJ to use it as shown below:
   * <pre><code class='java'> // after this call, AssertJ will throw an org.junit.AssumptionViolatedException when an assumption is not met
   * Assertions.setPreferredAssumptionExceptions(PreferredAssumptionException.JUNIT4);
   * </code></pre>
   * <p>
   * By default, AssertJ uses the {@link PreferredAssumptionException#AUTO_DETECT AUTO_DETECT} mode and tries to throw one of the following exceptions, in this order:
   * <ol>
   * <li>{@code org.testng.SkipException} for TestNG (if available in the classpath)</li>
   * <li>{@code org.junit.AssumptionViolatedException} for JUnit 4 (if available in the classpath)</li>
   * <li>{@code org.opentest4j.TestAbortedException} for JUnit 5</li>
   * </ol>
   *
   * @param preferredAssumptionException the preferred exception to use with {@link BDDAssumptions}.
   * @since 3.21.0
   */
  public static void setPreferredAssumptionException(PreferredAssumptionException preferredAssumptionException) {
    Assumptions.setPreferredAssumptionException(preferredAssumptionException);
  }

}
