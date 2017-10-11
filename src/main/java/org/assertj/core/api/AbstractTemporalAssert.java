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

import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

import org.assertj.core.data.TemporalOffset;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Temporal}s.
 * @since 3.7.0
 */
public abstract class AbstractTemporalAssert<SELF extends AbstractTemporalAssert<SELF, TEMPORAL>, TEMPORAL extends Temporal>
    extends AbstractAssert<SELF, TEMPORAL> {

  @VisibleForTesting
  Comparables comparables = new Comparables();

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractTemporalAssert}</code>.
   * @param selfType the "self type"
   * @param actual the actual value to verify
   */
  protected AbstractTemporalAssert(TEMPORAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  @VisibleForTesting
  protected TEMPORAL getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link Temporal} is close to the other according to the given {@link TemporalOffset}.
   * <p>
   * You can build the offset parameter using {@link Assertions#within(long, TemporalUnit)} or {@link Assertions#byLessThan(long, TemporalUnit)}.
   * <p>
   * Example:
   * <pre><code class='java'> LocalTime _07_10 = LocalTime.of(7, 10);
   * LocalTime _07_42 = LocalTime.of(7, 42);
   *
   * // assertions will pass
   * assertThat(_07_10).isCloseTo(_07_42, within(1, ChronoUnit.HOURS));
   * assertThat(_07_10).isCloseTo(_07_42, within(32, ChronoUnit.MINUTES));
   *
   * // assertions will fail
   * assertThat(_07_10).isCloseTo(_07_42, byLessThan(32, ChronoUnit.MINUTES));
   * assertThat(_07_10).isCloseTo(_07_42, within(10, ChronoUnit.SECONDS));</code></pre>
   * @param other the temporal to compare actual to
   * @param offset the offset used for comparison
   * @return this assertion object
   * @throws NullPointerException if {@code Temporal} or {@code TemporalOffset} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Temporal} is {@code null}.
   * @throws AssertionError if the actual {@code Temporal} is not close to the given for a provided offset.
   */
  public SELF isCloseTo(TEMPORAL other, TemporalOffset<? super TEMPORAL> offset) {
    Objects.instance().assertNotNull(info, actual);
    checkNotNull(other, "The temporal object to compare actual with should not be null");
    checkNotNull(offset, "The offset should not be null");
    if (offset.isBeyondOffset(actual, other)) {
      throw Failures.instance().failure(info,
                                        shouldBeCloseTo(actual, other,
                                                        offset.getBeyondOffsetDifferenceDescription(actual, other)));
    }

    return myself;
  }

  /**
   * Same assertion as {@link #isCloseTo(Temporal, TemporalOffset)} but the {@code TEMPORAL} is built from a given String that
   * follows predefined ISO date format <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html"
   * >Predefined Formatters</a> to allow calling {@link #parse(String)})} method.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(LocalTime.parse("07:10:30")).isCloseTo("07:12:11", within(5, ChronoUnit.MINUTES));</code></pre>
   * @param otherAsString String representing a {@code TEMPORAL}.
   * @param offset the offset used for comparison
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Temporal} is {@code null}.
   * @throws NullPointerException if temporal string representation or {@code TemporalOffset} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Temporal} is {@code null}.
   * @throws AssertionError if the actual {@code Temporal} is not close to the given for a provided offset.
   */
  public SELF isCloseTo(String otherAsString, TemporalOffset<? super TEMPORAL> offset) {
    checkNotNull(otherAsString,
                 "The String representing of the temporal object to compare actual with should not be null");
    return isCloseTo(parse(otherAsString), offset);
  }

  /**
   * Obtains an instance of {@code TEMPORAL} from a string representation in ISO date format.
   * @param temporalAsString the string to parse, not null
   * @return the parsed {@code TEMPORAL}, not null
   */
  protected abstract TEMPORAL parse(String temporalAsString);

}
