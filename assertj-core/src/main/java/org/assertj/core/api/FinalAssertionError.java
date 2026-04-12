package org.assertj.core.api;

public class FinalAssertionError extends AssertionError {
  private AbstractAssert<?, ?> anAssert;

  public <ASSERT extends AbstractAssert<?, ?>> FinalAssertionError(String message, ASSERT anAssert) {
    super(message);
    this.anAssert = anAssert;
  }

  public AbstractAssert<?, ?> getAssert() {
    return anAssert;
  }
}
