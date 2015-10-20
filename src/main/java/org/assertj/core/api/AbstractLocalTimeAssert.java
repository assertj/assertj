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

import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.error.ShouldBeAfterOrEqualsTo.shouldBeAfterOrEqualsTo;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.error.ShouldBeBeforeOrEqualsTo.shouldBeBeforeOrEqualsTo;
import static org.assertj.core.error.ShouldBeEqualIgnoringNanos.shouldBeEqualIgnoringNanos;
import static org.assertj.core.error.ShouldBeEqualIgnoringSeconds.shouldBeEqualIgnoringSeconds;
import static org.assertj.core.error.ShouldHaveSameHourAs.shouldHaveSameHourAs;

import java.time.LocalTime;

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;

/**
 * Assertions for {@link LocalTime} type from new Date &amp; Time API introduced in Java 8.
 */
public abstract class AbstractLocalTimeAssert<S extends AbstractLocalTimeAssert<S>>
    extends AbstractAssert<S, LocalTime> {

  public static final String NULL_LOCAL_TIME_PARAMETER_MESSAGE = "The LocalTime to compare actual with should not be null";

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractLocalTimeAssert}</code>.
   * 
   * @param selfType the "self type"
   * @param actual the actual value to verify
   */
  protected AbstractLocalTimeAssert(LocalTime actual, Class<?> selfType) {
	super(actual, selfType);
  }

  // visible for test
  protected LocalTime getActual() {
	return actual;
  }

  /**
   * Verifies that the actual {@code LocalTime} is <b>strictly</b> before the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("12:00:00")).isBefore(parse("13:00:00"));</code></pre>
   * 
   * @param other the given {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalTime} is not strictly before the given one.
   */
  public S isBefore(LocalTime other) {
	Objects.instance().assertNotNull(info, actual);
	assertLocalTimeParameterIsNotNull(other);
	if (!actual.isBefore(other)) {
	  throw Failures.instance().failure(info, shouldBeBefore(actual, other));
	}
	return myself;
  }

  /**
   * Same assertion as {@link #isBefore(LocalTime)} but the {@link LocalTime} is built from given String, which
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTime as String (AssertJ taking care of the conversion)
   * assertThat(parse("12:59")).isBefore("13:00");</code></pre>
   * 
   * @param localTimeAsString String representing a {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is not strictly before the {@link LocalTime} built
   *           from given String.
   */
  public S isBefore(String localTimeAsString) {
	assertLocalTimeAsStringParameterIsNotNull(localTimeAsString);
	return isBefore(LocalTime.parse(localTimeAsString));
  }

  /**
   * Verifies that the actual {@code LocalTime} is before or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("12:00:00")).isBeforeOrEqualTo(parse("12:00:00"))
   *                                        .isBeforeOrEqualTo(parse("12:00:01"));</code></pre>
   * 
   * @param other the given {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalTime} is not before or equals to the given one.
   */
  public S isBeforeOrEqualTo(LocalTime other) {
	Objects.instance().assertNotNull(info, actual);
	assertLocalTimeParameterIsNotNull(other);
	if (actual.isAfter(other)) {
	  throw Failures.instance().failure(info, shouldBeBeforeOrEqualsTo(actual, other));
	}
	return myself;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualTo(LocalTime)} but the {@link LocalTime} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTime as String (AssertJ taking care of the conversion)
   * assertThat(parse("12:00:00")).isBeforeOrEqualTo("12:00:00")
   *                              .isBeforeOrEqualTo("13:00:00");</code></pre>
   * 
   * @param localTimeAsString String representing a {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is not before or equals to the {@link LocalTime} built from
   *           given String.
   */
  public S isBeforeOrEqualTo(String localTimeAsString) {
	assertLocalTimeAsStringParameterIsNotNull(localTimeAsString);
	return isBeforeOrEqualTo(LocalTime.parse(localTimeAsString));
  }

  /**
   * Verifies that the actual {@code LocalTime} is after or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("13:00:00")).isAfterOrEqualTo(parse("13:00:00"))
   *                              .isAfterOrEqualTo(parse("12:00:00"));</code></pre>
   * 
   * @param other the given {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalTime} is not after or equals to the given one.
   */
  public S isAfterOrEqualTo(LocalTime other) {
	Objects.instance().assertNotNull(info, actual);
	assertLocalTimeParameterIsNotNull(other);
	if (actual.isBefore(other)) {
	  throw Failures.instance().failure(info, shouldBeAfterOrEqualsTo(actual, other));
	}
	return myself;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualTo(LocalTime)} but the {@link LocalTime} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTime as String (AssertJ taking care of the conversion)
   * assertThat(parse("13:00:00")).isAfterOrEqualTo("13:00:00")
   *                              .isAfterOrEqualTo("12:00:00");</code></pre>
   * 
   * @param localTimeAsString String representing a {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is not after or equals to the {@link LocalTime} built from
   *           given String.
   */
  public S isAfterOrEqualTo(String localTimeAsString) {
	assertLocalTimeAsStringParameterIsNotNull(localTimeAsString);
	return isAfterOrEqualTo(LocalTime.parse(localTimeAsString));
  }

  /**
   * Verifies that the actual {@code LocalTime} is <b>strictly</b> after the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("13:00:00")).isAfter(parse("12:00:00"));</code></pre>
   * 
   * @param other the given {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalTime} is not strictly after the given one.
   */
  public S isAfter(LocalTime other) {
	Objects.instance().assertNotNull(info, actual);
	assertLocalTimeParameterIsNotNull(other);
	if (!actual.isAfter(other)) {
	  throw Failures.instance().failure(info, shouldBeAfter(actual, other));
	}
	return myself;
  }

  /**
   * Same assertion as {@link #isAfter(LocalTime)} but the {@link LocalTime} is built from given a String that
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTime as String (AssertJ taking care of the conversion)
   * assertThat(parse("13:00:00")).isAfter("12:00:00");</code></pre>
   * 
   * @param localTimeAsString String representing a {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is not strictly after the {@link LocalTime} built
   *           from given String.
   */
  public S isAfter(String localTimeAsString) {
	assertLocalTimeAsStringParameterIsNotNull(localTimeAsString);
	return isAfter(LocalTime.parse(localTimeAsString));
  }

  /**
   * Same assertion as {@link #isEqualTo(Object)} (where Object is expected to be {@link LocalTime}) but here you
   * pass {@link LocalTime} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTime as String (AssertJ taking care of the conversion)
   * assertThat(parse("13:00:00")).isEqualTo("13:00:00");</code></pre>
   * 
   * @param localTimeAsString String representing a {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is not equal to the {@link LocalTime} built from
   *           given String.
   */
  public S isEqualTo(String localTimeAsString) {
	assertLocalTimeAsStringParameterIsNotNull(localTimeAsString);
	return isEqualTo(LocalTime.parse(localTimeAsString));
  }

  /**
   * Same assertion as {@link #isNotEqualTo(Object)} (where Object is expected to be {@link LocalTime}) but here you
   * pass {@link LocalTime} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTime as String (AssertJ taking care of the conversion)
   * assertThat(parse("13:00:00")).isNotEqualTo("12:00:00");</code></pre>
   * 
   * @param localTimeAsString String representing a {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is equal to the {@link LocalTime} built from given
   *           String.
   */
  public S isNotEqualTo(String localTimeAsString) {
	assertLocalTimeAsStringParameterIsNotNull(localTimeAsString);
	return isNotEqualTo(LocalTime.parse(localTimeAsString));
  }

  /**
   * Same assertion as {@link #isIn(Object...)} (where Objects are expected to be {@link LocalTime}) but here you
   * pass {@link LocalTime} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTimes as String (AssertJ taking care of the conversion)
   * assertThat(parse("13:00:00")).isIn("12:00:00", "13:00:00");</code></pre>
   * 
   * @param localTimesAsString String array representing {@link LocalTime}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is not in the {@link LocalTime}s built from given
   *           Strings.
   */
  public S isIn(String... localTimesAsString) {
	checkIsNotNullAndNotEmpty(localTimesAsString);
	return isIn(convertToLocalTimeArray(localTimesAsString));
  }

  /**
   * Same assertion as {@link #isNotIn(Object...)} (where Objects are expected to be {@link LocalTime}) but here you
   * pass {@link LocalTime} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_TIME"
   * >ISO LocalTime format</a> to allow calling {@link LocalTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // you can express expected LocalTimes as String (AssertJ taking care of the conversion)
   * assertThat(parse("13:00:00")).isNotIn("12:00:00", "14:00:00");</code></pre>
   * 
   * @param localTimesAsString Array of String representing a {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalTime}.
   * @throws AssertionError if the actual {@code LocalTime} is in the {@link LocalTime}s built from given
   *           Strings.
   */
  public S isNotIn(String... localTimesAsString) {
	checkIsNotNullAndNotEmpty(localTimesAsString);
	return isNotIn(convertToLocalTimeArray(localTimesAsString));
  }

  private static Object[] convertToLocalTimeArray(String... localTimesAsString) {
	LocalTime[] dates = new LocalTime[localTimesAsString.length];
	for (int i = 0; i < localTimesAsString.length; i++) {
	  dates[i] = LocalTime.parse(localTimesAsString[i]);
	}
	return dates;
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
	if (values == null) throw new IllegalArgumentException("The given LocalTime array should not be null");
	if (values.length == 0) throw new IllegalArgumentException("The given LocalTime array should not be empty");
  }

  /**
   * Check that the {@link LocalTime} string representation to compare actual {@link LocalTime} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message
   * 
   * @param localTimeAsString String representing the {@link LocalTime} to compare actual with
   * @throws IllegalArgumentException with an explicit message if the given {@link String} is null
   */
  private static void assertLocalTimeAsStringParameterIsNotNull(String localTimeAsString) {
	// @format:off
	if (localTimeAsString == null) throw new IllegalArgumentException("The String representing the LocalTime to compare actual with should not be null");
	// @format:on
  }

  /**
   * Check that the {@link LocalTime} to compare actual {@link LocalTime} to is not null, in that case throws a
   * {@link IllegalArgumentException} with an explicit message
   * 
   * @param other the {@link LocalTime} to check
   * @throws IllegalArgumentException with an explicit message if the given {@link LocalTime} is null
   */
  private static void assertLocalTimeParameterIsNotNull(LocalTime other) {
	if (other == null) throw new IllegalArgumentException("The LocalTime to compare actual with should not be null");
  }

  /**
   * Verifies that actual and given {@code LocalTime} have same hour, minute and second fields (nanosecond fields are
   * ignored in comparison).
   * <p>
   * Assertion can fail with localTimes in same chronological nanosecond time window, e.g :
   * <p>
   * 23:00:<b>01.000000000</b> and 23:00:<b>00.999999999</b>.
   * <p>
   * Assertion fails as second fields differ even if time difference is only 1ns.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * LocalTime localTime1 = LocalTime.of(12, 0, 1, 0);
   * LocalTime localTime2 = LocalTime.of(12, 0, 1, 456);
   * assertThat(localTime1).isEqualToIgnoringNanos(localTime2);
   * 
   * // failing assertions (even if time difference is only 1ns)
   * LocalTime localTimeA = LocalTime.of(12, 0, 1, 0);
   * LocalTime localTimeB = LocalTime.of(12, 0, 0, 999999999);
   * assertThat(localTimeA).isEqualToIgnoringNanos(localTimeB);</code></pre>
   * 
   * @param other the given {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalTime} is are not equal with nanoseconds ignored.
   */
  public S isEqualToIgnoringNanos(LocalTime other) {
	Objects.instance().assertNotNull(info, actual);
	assertLocalTimeParameterIsNotNull(other);
	if (!areEqualIgnoringNanos(actual, other)) {
	  throw Failures.instance().failure(info, shouldBeEqualIgnoringNanos(actual, other));
	}
	return myself;
  }

  /**
   * Verifies that actual and given {@link LocalTime} have same hour and minute fields (second and nanosecond fields are
   * ignored in comparison).
   * <p>
   * Assertion can fail with LocalTimes in same chronological second time window, e.g :
   * <p>
   * 23:<b>01:00</b>.000 and 23:<b>00:59</b>.000.
   * <p>
   * Assertion fails as minute fields differ even if time difference is only 1s.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * LocalTime localTime1 = LocalTime.of(23, 50, 0, 0);
   * LocalTime localTime2 = LocalTime.of(23, 50, 10, 456);
   * assertThat(localTime1).isEqualToIgnoringSeconds(localTime2);
   * 
   * // failing assertions (even if time difference is only 1ms)
   * LocalTime localTimeA = LocalTime.of(23, 50, 00, 000);
   * LocalTime localTimeB = LocalTime.of(23, 49, 59, 999);
   * assertThat(localTimeA).isEqualToIgnoringSeconds(localTimeB);</code></pre>
   * 
   * @param other the given {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalTime} is are not equal with second and nanosecond fields
   *           ignored.
   */
  public S isEqualToIgnoringSeconds(LocalTime other) {
	Objects.instance().assertNotNull(info, actual);
	assertLocalTimeParameterIsNotNull(other);
	if (!areEqualIgnoringSeconds(actual, other)) {
	  throw Failures.instance().failure(info, shouldBeEqualIgnoringSeconds(actual, other));
	}
	return myself;
  }

  /**
   * Verifies that actual and given {@code LocalTime} have same hour fields (minute, second and nanosecond fields are
   * ignored in comparison).
   * <p>
   * Assertion can fail with localTimes in same chronological second time window, e.g :
   * <p>
   * <b>01:00</b>:00.000 and <b>00:59:59</b>.000.
   * <p>
   * Time difference is only 1s but hour fields differ.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * LocalTime localTime1 = LocalTime.of(23, 50, 0, 0);
   * LocalTime localTime2 = LocalTime.of(23, 00, 2, 7);
   * assertThat(localTime1).hasSameHourAs(localTime2);
   * 
   * // failing assertions (even if time difference is only 1ms)
   * LocalTime localTimeA = LocalTime.of(01, 00, 00, 000);
   * LocalTime localTimeB = LocalTime.of(00, 59, 59, 999);
   * assertThat(localTimeA).hasSameHourAs(localTimeB);</code></pre>
   * 
   * @param other the given {@link LocalTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalTime} is are not equal ignoring minute, second and nanosecond
   *           fields.
   */
  public S hasSameHourAs(LocalTime other) {
	Objects.instance().assertNotNull(info, actual);
	assertLocalTimeParameterIsNotNull(other);
	if (!haveSameHourField(actual, other)) {
	  throw Failures.instance().failure(info, shouldHaveSameHourAs(actual, other));
	}
	return myself;
  }

  /**
   * Returns true if both localtime are in the same year, month and day of month, hour, minute and second, false
   * otherwise.
   * 
   * @param actual the actual localtime. expected not be null
   * @param other the other localtime. expected not be null
   * @return true if both localtime are in the same year, month and day of month, hour, minute and second, false
   *         otherwise.
   */
  private static boolean areEqualIgnoringNanos(LocalTime actual, LocalTime other) {
	return areEqualIgnoringSeconds(actual, other) && actual.getSecond() == other.getSecond();
  }

  /**
   * Returns true if both localtime are in the same year, month, day of month, hour and minute, false otherwise.
   * 
   * @param actual the actual localtime. expected not be null
   * @param other the other localtime. expected not be null
   * @return true if both localtime are in the same year, month, day of month, hour and minute, false otherwise.
   */
  private static boolean areEqualIgnoringSeconds(LocalTime actual, LocalTime other) {
	return haveSameHourField(actual, other) && actual.getMinute() == other.getMinute();
  }

  private static boolean haveSameHourField(LocalTime actual, LocalTime other) {
	return actual.getHour() == other.getHour();
  }

}