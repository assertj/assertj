package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;

/**
 * Reusable assertions for immutables objects.
 */
public class Immutables {

  private static final Immutables INSTANCE = new Immutables();

  public static Immutables instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  public void expectUnsupportedOperationException(AssertionInfo info, Runnable runnable, String method) {
    try {
      runnable.run();
      throw failures.failure(info, shouldBeUnmodifiable(method));
    } catch (UnsupportedOperationException e) {
      // happy path
    } catch (RuntimeException e) {
      throw failures.failure(info, (shouldBeUnmodifiable(method, e)));
    }
  }

}
