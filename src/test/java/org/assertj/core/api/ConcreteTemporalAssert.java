package org.assertj.core.api;

import java.time.ZonedDateTime;


public class ConcreteTemporalAssert extends AbstractTemporalAssert<ConcreteTemporalAssert, ZonedDateTime> {


  public ConcreteTemporalAssert(ZonedDateTime actual) {
    super(actual, ConcreteTemporalAssert.class);
  }

  @Override
  public ZonedDateTime parse(String temporalAsString) {
    return ZonedDateTime.parse(temporalAsString);
  }


}
