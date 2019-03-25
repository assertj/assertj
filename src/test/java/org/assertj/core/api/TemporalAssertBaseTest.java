package org.assertj.core.api;

import org.assertj.core.internal.Comparables;

import java.time.temporal.Temporal;

import static org.mockito.Mockito.mock;

public abstract class TemporalAssertBaseTest<ASSERT extends AbstractTemporalAssert<ASSERT, TEMPORAL>, TEMPORAL extends Temporal>
  extends BaseTestTemplate<ASSERT, TEMPORAL> {

  protected Comparables comparables;

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
  }

  protected abstract Comparables getComparables(ASSERT someAssertions);
}
