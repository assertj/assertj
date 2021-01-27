package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

/**
 * Reusable assertions for <code>{@link java.util.Collection}</code>
 */
public class Collection {

  private static final Collection INSTANCE = new Collection();

  @VisibleForTesting
  Failures failures = Failures.instance();

  /**
   * Returns singleton instance of {@link java.util.Collection}
   * @return the singleton instance of {@link Collection}
   */
  public static Collection instance() {
    return INSTANCE;
  }

  public void assertIsUnmodifiable(AssertionInfo info, java.util.Collection<?> actual) {
    assertNotNull(info, actual);
    assertUnsupportedOperationExceptionIsThrown(info, actual.getClass(), () -> actual.add(null));
  }

  private void assertUnsupportedOperationExceptionIsThrown(AssertionInfo info, Class<?> clazz, Runnable runnable) {
    try {
      runnable.run();
    } catch (Exception ex) {
      if (ex instanceof UnsupportedOperationException) {
        return;
      }
    }
    failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
  }

  private void assertNotNull(AssertionInfo info, java.util.Collection<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

}
