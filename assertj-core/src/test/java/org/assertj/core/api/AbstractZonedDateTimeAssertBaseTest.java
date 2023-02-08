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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;

import org.assertj.core.internal.ChronoZonedDateTimeByInstantComparator;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;

public abstract class AbstractZonedDateTimeAssertBaseTest extends TemporalAssertBaseTest<ZonedDateTimeAssert, ZonedDateTime> {

  protected static final ZonedDateTime NOW = ZonedDateTime.now();
  protected static final ZonedDateTime YESTERDAY = NOW.minusDays(1);
  protected static final ZonedDateTime TOMORROW = NOW.plusDays(1);
  protected static final ComparatorBasedComparisonStrategy COMPARISON_STRATEGY = comparisonStrategy();

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
  }

  @Override
  protected ZonedDateTimeAssert create_assertions() {
    return new ZonedDateTimeAssert(NOW);
  }

  @Override
  public Comparables getComparables(ZonedDateTimeAssert someAssertions) {
    return assertions.comparables;
  }

  private static ComparatorBasedComparisonStrategy comparisonStrategy() {
    ChronoZonedDateTimeByInstantComparator comparator = ChronoZonedDateTimeByInstantComparator.getInstance();
    return new ComparatorBasedComparisonStrategy(comparator, comparator.description());
  }
}
