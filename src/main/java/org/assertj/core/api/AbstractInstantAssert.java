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


import org.assertj.core.error.ShouldBeAfter;
import org.assertj.core.error.ShouldBeAfterOrEqualsTo;
import org.assertj.core.error.ShouldBeBefore;
import org.assertj.core.error.ShouldBeBeforeOrEqualsTo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;

import java.time.Instant;
import java.util.Arrays;

import static org.assertj.core.util.Preconditions.checkArgument;

/**
 * Assertions for {@link Instant} type from new Date &amp; Time API introduced in Java 8.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @since 3.7.0
 */
public class AbstractInstantAssert<SELF extends AbstractInstantAssert<SELF>> extends AbstractTemporalAssert<SELF, Instant> {

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractInstantAssert}</code>.
   *
   * @param selfType the "self type"
   * @param actual the actual value to verify
   */
  protected AbstractInstantAssert(Instant actual, Class<?> selfType) {
    super(actual, selfType);
  }


  /**
   * Verifies that the actual {@code Instant} is <b>strictly</b> before the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2007-12-03T10:15:30.00Z")).isBefore(parse("2007-12-03T10:15:31.00Z"));</code></pre>
   *
   * @param other the given {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Instant} is not strictly before the given one.
   * @since 3.7.0
   */
  public SELF isBefore(Instant other) {
    assertNotNull(info, actual);
    assertInstantParameterIsNotNull(other);
    if (!actual.isBefore(other)) {
      throw Failures.instance().failure(info, ShouldBeBefore.shouldBeBefore(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isBefore(Instant)} but the {@link Instant} is built from given String, which
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2007-12-03T10:15:30.00Z").isBefore("2007-12-03T10:15:31.00Z");</code></pre>
   *
   * @param instantAsString String representing a {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code Instant} is not strictly before the {@link Instant} built
   *           from given String.
   * @since 3.7.0
   */
  public SELF isBefore(String instantAsString) {
    assertInstantAsStringParameterIsNotNull(instantAsString);
    return isBefore(parse(instantAsString));
  }

  /**
   * Verifies that the actual {@code Instant} is before or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2007-12-03T10:15:30.00Z")).isBeforeOrEqualTo(parse("2007-12-03T10:15:30.00Z"))
   *                                .isBeforeOrEqualTo(parse("2007-12-03T10:15:31.00Z"));</code></pre>
   *
   * @param other the given {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Instant} is not before or equals to the given one.
   * @since 3.7.0
   */
  public SELF isBeforeOrEqualTo(Instant other) {
    assertNotNull(info, actual);
    assertInstantParameterIsNotNull(other);
    if (actual.isAfter(other)) {
      throw Failures.instance().failure(info, ShouldBeBeforeOrEqualsTo.shouldBeBeforeOrEqualsTo(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualTo(Instant)} but the {@link Instant} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2007-12-03T10:15:30.00Z")).isBeforeOrEqualTo("2007-12-03T10:15:30.00Z")
   *                                .isBeforeOrEqualTo("2007-12-03T10:15:31.00Z");</code></pre>
   *
   * @param instantAsString String representing a {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code Instant} is not before or equals to the {@link Instant} built from
   *           given String.
   * @since 3.7.0
   */
  public SELF isBeforeOrEqualTo(String instantAsString) {
    assertInstantAsStringParameterIsNotNull(instantAsString);
    return isBeforeOrEqualTo(parse(instantAsString));
  }

  /**
   * Verifies that the actual {@code Instant} is after or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2007-12-03T10:15:30.00Z")).isAfterOrEqualTo(parse("2007-12-03T10:15:30.00Z"))
   *                                .isAfterOrEqualTo(parse("2007-12-03T10:15:27.00Z"));</code></pre>
   *
   * @param other the given {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Instant} is not after or equals to the given one.
   * @since 3.7.0
   */
  public SELF isAfterOrEqualTo(Instant other) {
    assertNotNull(info, actual);
    assertInstantParameterIsNotNull(other);
    if (actual.isBefore(other)) {
      throw Failures.instance().failure(info, ShouldBeAfterOrEqualsTo.shouldBeAfterOrEqualsTo(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualTo(Instant)} but the {@link Instant} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2007-12-03T10:15:30.00Z")).isAfterOrEqualTo("2007-12-03T10:15:30.00Z")
   *                                .isAfterOrEqualTo("2007-12-03T10:15:27.00Z");</code></pre>
   *
   * @param instantAsString String representing a {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code Instant} is not after or equals to the {@link Instant} built from
   *           given String.
   * @since 3.7.0
   */
  public SELF isAfterOrEqualTo(String instantAsString) {
    assertInstantAsStringParameterIsNotNull(instantAsString);
    return isAfterOrEqualTo(parse(instantAsString));
  }

  /**
   * Verifies that the actual {@code Instant} is <b>strictly</b> after the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2007-12-03T10:15:30.00Z").isAfter(parse("2007-12-03T10:15:27.00Z"));</code></pre>
   *
   * @param other the given {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Instant} is not strictly after the given one.
   * @since 3.7.0
   */
  public SELF isAfter(Instant other) {
    assertNotNull(info, actual);
    assertInstantParameterIsNotNull(other);
    if (!actual.isAfter(other)) {
      throw Failures.instance().failure(info, ShouldBeAfter.shouldBeAfter(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isAfter(Instant)} but the {@link Instant} is built from given a String that
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2007-12-03T10:15:30.00Z")).isAfter("2007-12-03T10:15:27.00Z");</code></pre>
   *
   * @param instantAsString String representing a {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code LocalDate} is not strictly after the {@link Instant} built
   *           from given String.
   * @since 3.7.0
   */
  public SELF isAfter(String instantAsString) {
    assertInstantAsStringParameterIsNotNull(instantAsString);
    return isAfter(parse(instantAsString));
  }

  /**
   * Same assertion as {@link #isEqualTo(Object)} (where Object is expected to be {@link Instant}) but here you
   * pass {@link Instant} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use directly String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2007-12-03T10:15:30.00Z")).isEqualTo("2007-12-03T10:15:30.00Z");</code></pre>
   *
   * @param instantAsString String representing a {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code Instant} is not equal to the {@link Instant} built from
   *           given String.
   * @since 3.7.0
   */
  public SELF isEqualTo(String instantAsString) {
    assertInstantAsStringParameterIsNotNull(instantAsString);
    return isEqualTo(parse(instantAsString));
  }

  /**
   * Same assertion as {@link #isNotEqualTo(Object)} (where Object is expected to be {@link Instant}) but here you
   * pass {@link Instant} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use directly String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2007-12-03T10:15:30.00Z")).isNotEqualTo("2007-12-03T10:15:00.00Z");</code></pre>
   *
   * @param instantAsString String representing a {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code Instant} is equal to the {@link Instant} built from given
   *           String.
   * @since 3.7.0
   */
  public SELF isNotEqualTo(String instantAsString) {
    assertInstantAsStringParameterIsNotNull(instantAsString);
    return isNotEqualTo(parse(instantAsString));
  }

  /**
   * Same assertion as {@link #isIn(Object...)} (where Objects are expected to be {@link Instant}) but here you
   * pass {@link Instant} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of LocalDate
   * assertThat(parse("2007-12-03T10:15:30.00Z")).isIn("2007-12-03T10:15:30.00Z", "2007-12-03T10:15:35.00Z");</code></pre>
   *
   * @param instantsAsString String array representing {@link Instant}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code Instant} is not in the {@link Instant}s built from given
   *           Strings.
   * @since 3.7.0
   */
  public SELF isIn(String... instantsAsString) {
    checkIsNotNullAndNotEmpty(instantsAsString);
    return isIn(convertToInstantArray(instantsAsString));
  }

  /**
   * Same assertion as {@link #isNotIn(Object...)} (where Objects are expected to be {@link Instant}) but here you
   * pass {@link Instant} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT"
   * >ISO Instant format</a> to allow calling {@link Instant#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of LocalDate
   * assertThat(parse("2007-12-03T10:15:30.00Z")).isNotIn("2007-12-03T10:15:35.00Z", "2007-12-03T10:15:25.00Z");</code></pre>
   *
   * @param instantsAsString Array of String representing a {@link Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Instant} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link Instant}.
   * @throws AssertionError if the actual {@code Instant} is in the {@link Instant}s built from given
   *           Strings.
   * @since 3.7.0
   */
  public SELF isNotIn(String... instantsAsString) {
    checkIsNotNullAndNotEmpty(instantsAsString);
    return isNotIn(convertToInstantArray(instantsAsString));
  }

  private static Object[] convertToInstantArray(String[] instantsAsString) {
    return Arrays.stream(instantsAsString).map(Instant::parse).toArray();
  }

  @Override
  protected Instant parse(String instantAsString) {
    return Instant.parse(instantAsString);
  }

  private static void assertNotNull(AssertionInfo info, Instant actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    checkArgument(values != null, "The given Instant array should not be null");
    checkArgument(values.length > 0, "The given Instant array should not be empty");
  }

  /**
   * Check that the {@link Instant} string representation to compare actual {@link Instant} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message
   *
   * @param instantAsString String representing the {@link Instant} to compare actual with
   * @throws IllegalArgumentException with an explicit message if the given {@link String} is null
   */
  private static void assertInstantAsStringParameterIsNotNull(String instantAsString) {
    checkArgument(instantAsString != null,
      "The String representing the Instant to compare actual with should not be null");
  }

  private static void assertInstantParameterIsNotNull(Instant instant) {
    checkArgument(instant != null, "The Instant to compare actual with should not be null");
  }
}
