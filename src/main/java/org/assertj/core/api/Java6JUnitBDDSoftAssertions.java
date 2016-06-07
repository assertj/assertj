package org.assertj.core.api;

import java.util.List;

import org.assertj.core.util.VisibleForTesting;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

/**
 * Duplicate of {@link JUnitBDDSoftAssertions} compatible with Android.
 */
public class Java6JUnitBDDSoftAssertions extends Java6AbstractBDDSoftAssertions
    implements TestRule {

  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        base.evaluate();
        MultipleFailureException.assertEmpty(errorsCollected());
      }
    };
  }

  @VisibleForTesting
  List<Throwable> getErrors() {
    return proxies.errorsCollected();
  }
}
