package org.assertj.core.api;

/**
 * Base class for BigDecimal scale assertions.
 *
 * @since 3.22.0
 */
public abstract class AbstractBigDecimalScaleAssert<SELF extends AbstractBigDecimalAssert<SELF>>
    extends AbstractIntegerAssert<AbstractBigDecimalScaleAssert<SELF>>{

  protected AbstractBigDecimalScaleAssert(Integer actualScale, Class<?> selfType) {
    super(actualScale, selfType);
  }

  /**
   * Returns to the BigDecimal on which we ran scale assertions on.
   * <p>
   * Example:
   * <pre><code class='java'>
   * assertThat(new BigDecimal(&quot;2.313&quot;)).scale().isGreaterThan(1L).isLessThan(5L)
   *                 .returnToBigDecimal().hasScaleOf(3);</code></pre>
   *
   * @return BigDecimal assertions.
   */
  public abstract AbstractBigDecimalAssert<SELF> returnToBigDecimal();

}
