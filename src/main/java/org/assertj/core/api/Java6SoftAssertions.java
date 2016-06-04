package org.assertj.core.api;

import java.util.List;

import static org.assertj.core.groups.Properties.extractProperty;

/**
 * Soft assertions backwards compatible with Android. Duplicated from {@link SoftAssertions}.
 *
 * @see SoftAssertions
 */
public class Java6SoftAssertions extends Java6AbstractStandardSoftAssertions {
  /**
   * Verifies that no proxied assertion methods have failed.
   *
   * @throws SoftAssertionError if any proxied assertion objects threw
   */
  public void assertAll() {
    List<Throwable> errors = errorsCollected();
    if (!errors.isEmpty()) {
      throw new SoftAssertionError(extractProperty("message", String.class).from(errors));
    }
  }
}
