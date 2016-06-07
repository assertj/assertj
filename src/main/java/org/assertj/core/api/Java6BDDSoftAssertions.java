package org.assertj.core.api;

import java.util.List;

import static org.assertj.core.groups.Properties.extractProperty;

/**
 * BDD-style Android-compatible soft assertions. Duplicated from {@link BDDSoftAssertions}.
 *
 * @see BDDSoftAssertions
 */
public class Java6BDDSoftAssertions extends Java6AbstractBDDSoftAssertions {
  /**
   * Verifies that no proxied assertion methods have failed.
   *
   * @throws SoftAssertionError if any proxied assertion objects threw
   */
  public void assertAll() {
    List<Throwable> errors = errorsCollected();
    if (!errors.isEmpty()) throw new SoftAssertionError(extractProperty("message", String.class).from(errors));
  }
}
