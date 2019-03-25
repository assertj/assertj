package org.assertj.core.api;

import org.assertj.core.internal.Comparables;

import java.time.ZonedDateTime;

public abstract class AbstractTemporalAssertBaseTest extends TemporalAssertBaseTest<ConcreteTemporalAssert, ZonedDateTime> {

  @Override
  protected ConcreteTemporalAssert create_assertions() {
    return new ConcreteTemporalAssert(ZonedDateTime.now());
  }

  @Override
  protected Comparables getComparables(ConcreteTemporalAssert someAssertions) {
    return someAssertions.comparables;
  }

}
