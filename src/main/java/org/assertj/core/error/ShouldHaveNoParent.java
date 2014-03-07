package org.assertj.core.error;

import java.io.File;

/**
 * Creates an error message when a {@code File} should not have a parent.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveNoParent extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldHaveNoParent}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldHaveNoParent shouldHaveNoParent(File actual) {
    return new ShouldHaveNoParent(actual);
  }

  private ShouldHaveNoParent(File actual) {
    super("%nExpecting file (or directory) without parent, but parent was:%n  <%s>", actual.getParentFile());
  }
}
