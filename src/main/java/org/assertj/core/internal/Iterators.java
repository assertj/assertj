package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import java.util.Iterator;

import static org.assertj.core.error.ShouldBeExhausted.shouldBeExhausted;
import static org.assertj.core.error.ShouldHaveNext.shouldHaveNext;
import static org.assertj.core.internal.Comparables.assertNotNull;

/**
 * Reusable assertions for <code>{@link Iterator}</code>s.
 *
 * @author Stephan Windmüller
 */
public class Iterators {

  private static final Iterators INSTANCE = new Iterators();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static Iterators instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Iterators() {
    super();
  }

  public void assertHasNext(AssertionInfo info, Iterator<?> actual) {
    assertNotNull(info, actual);
    if (actual.hasNext()) return;
    throw failures.failure(info, shouldHaveNext(actual));
  }

  public void assertIsExhausted(AssertionInfo info, Iterator<?> actual) {
    assertNotNull(info, actual);
    if (!actual.hasNext()) return;
    throw failures.failure(info, shouldBeExhausted(actual));
  }

}
