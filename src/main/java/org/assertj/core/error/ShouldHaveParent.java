package org.assertj.core.error;

import java.io.File;

/**
 * Creates an error message indicating that a {@code File} should have a parent.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveParent extends BasicErrorMessageFactory {

  public static ShouldHaveParent shouldHaveParent(File actual, File expected) {
    return actual.getParentFile() == null ? new ShouldHaveParent(actual, expected) : new ShouldHaveParent(actual,
        actual.getParentFile(), expected);
  }

  private ShouldHaveParent(File actual, File expected) {
    super("%nExpecting file%n <%s>%nto have parent:%n <%s>%nbut does not have one.", actual, expected);
  }

  private ShouldHaveParent(File actual, File actualParent, File expected) {
    super("%nExpecting file%n <%s>%nto have parent:%n <%s>%nbut have:%n <%s>.", actual, expected, actualParent);
  }
}
