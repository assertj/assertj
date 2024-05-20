/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

  protected static final OffsetDateTime REFERENCE = OffsetDateTime.now(ZoneOffset.UTC);
  protected static final OffsetDateTime BEFORE = REFERENCE.minusHours(1);
  protected static final OffsetDateTime AFTER = REFERENCE.plusHours(1);

  protected static final OffsetDateTime REFERENCE_WITH_DIFFERENT_OFFSET = REFERENCE.withOffsetSameInstant(OFFSET);
  protected static final OffsetDateTime BEFORE_WITH_DIFFERENT_OFFSET = REFERENCE_WITH_DIFFERENT_OFFSET.minusHours(1);
  protected static final OffsetDateTime AFTER_WITH_DIFFERENT_OFFSET = REFERENCE_WITH_DIFFERENT_OFFSET.plusHours(1);

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
