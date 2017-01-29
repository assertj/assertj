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

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

/**
 * {@link TemporalUnitOffset} with strict less than condition.
 * @since 3.7.0
 */
public class TemporalUnitLessThanOffset extends TemporalUnitOffset {

  public TemporalUnitLessThanOffset(long value, TemporalUnit unit) {
    super(value, unit);
  }

  /**
   * Checks if difference between temporal values is less then offset.
   * @param temporal1 first temporal value to be validated against second temporal value.
   * @param temporal2 second temporal value.
   * @return true if difference between temporal values is more or equal to offset value.
   */
  @Override
  public boolean isBeyondOffset(Temporal temporal1, Temporal temporal2) {
    return getDifference(temporal1, temporal2) >= value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBeyondOffsetDifferenceDescription(Temporal temporal1, Temporal temporal2) {
    return "by less than " + super.getBeyondOffsetDifferenceDescription(temporal1, temporal2);
  }
}
