package org.assertj.core.api;

public class BigDecimalScaleAssert<T> extends AbstractBigDecimalScaleAssert<BigDecimalAssert> {

  private AbstractBigDecimalAssert<BigDecimalAssert> bigDecimalAssert;

  public  BigDecimalScaleAssert (AbstractBigDecimalAssert<BigDecimalAssert> bigDecimalAssert) {
      super(bigDecimalAssert.actual.scale(), BigDecimalScaleAssert.class);
      this.bigDecimalAssert = bigDecimalAssert;
  }

  @Override
  public AbstractBigDecimalAssert<BigDecimalAssert> returnToBigDecimal() {
    return bigDecimalAssert;
  }
}
