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
package org.assertj.core.data;

import static java.lang.Math.abs;
import static java.lang.String.format;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

/**
 * Base class for {@link TemporalOffset} on basis of {@link TemporalUnit}.
 * @since 3.7.0
 */
public abstract class TemporalUnitOffset implements TemporalOffset<Temporal> {

  private final TemporalUnit unit;
  protected final long value;

  /**
   * Creates a new temporal offset for a given temporal unit.
   * @param value the value of the offset.
   * @param unit temporal unit of the offset.
   * @throws NullPointerException if the given unit is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public TemporalUnitOffset(long value, TemporalUnit unit) {
    checkNotNull(unit);
    checkThatValueIsPositive(value);
    this.value = value;
    this.unit = unit;
  }

  private void checkThatValueIsPositive(long value) {
    checkArgument(value >= 0, "The value of the offset should be greater than zero");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBeyondOffsetDifferenceDescription(Temporal temporal1, Temporal temporal2) {
    return format("%s %s but difference was %s %s", value, unit, getDifference(temporal1, temporal2), unit);
  }

  /**
   * Returns absolute value of the difference according to time unit.
   * 
   * @param temporal1 the first {@link Temporal}
   * @param temporal2 the second {@link Temporal}
   * @return absolute value of the difference according to time unit.
   */
  protected long getDifference(Temporal temporal1, Temporal temporal2) {
    return abs(unit.between(temporal1, temporal2));
  }

  public TemporalUnit getUnit() {
    return unit;
  }

  public long getValue() {
    return value;
  }

}
