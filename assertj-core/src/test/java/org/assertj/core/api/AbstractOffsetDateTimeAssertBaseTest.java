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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.OffsetDateTimeByInstantComparator;

public abstract class AbstractOffsetDateTimeAssertBaseTest extends TemporalAssertBaseTest<OffsetDateTimeAssert, OffsetDateTime> {

  private static final ZoneOffset OFFSET = ZoneOffset.ofHours(3);
  protected static final OffsetDateTime REFERENCE = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.UTC);
  protected static final OffsetDateTime BEFORE = OffsetDateTime.of(2000, 12, 13, 23, 59, 59, 999, ZoneOffset.UTC);
  protected static final OffsetDateTime AFTER = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 1, ZoneOffset.UTC);
  protected static final OffsetDateTime REFERENCE_WITH_DIFFERENT_OFFSET = OffsetDateTime.of(2000, 12, 14, 3, 0, 0, 0, OFFSET);
  protected static final OffsetDateTime BEFORE_WITH_DIFFERENT_OFFSET = OffsetDateTime.of(2000, 12, 14, 2, 59, 59, 999, OFFSET);
  protected static final OffsetDateTime AFTER_WITH_DIFFERENT_OFFSET = OffsetDateTime.of(2000, 12, 14, 3, 0, 0, 1, OFFSET);
  protected static final ComparatorBasedComparisonStrategy COMPARISON_STRATEGY = comparisonStrategy();

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
  }

  @Override
  protected OffsetDateTimeAssert create_assertions() {
    return new OffsetDateTimeAssert(REFERENCE);
  }

  @Override
  public Comparables getComparables(OffsetDateTimeAssert someAssertions) {
    return assertions.comparables;
  }

  private static ComparatorBasedComparisonStrategy comparisonStrategy() {
    OffsetDateTimeByInstantComparator comparator = OffsetDateTimeByInstantComparator.getInstance();
    return new ComparatorBasedComparisonStrategy(comparator, comparator.description());
  }
}
