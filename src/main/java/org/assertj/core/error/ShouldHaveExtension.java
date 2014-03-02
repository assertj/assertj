package org.assertj.core.error;

import java.io.File;

/**
 * Creates an error message indicating that a {@code File} should have extension.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveExtension extends BasicErrorMessageFactory {

  public static ShouldHaveExtension shouldHaveExtension(File actual, String actualExtension, String expectedExtension) {
    return actualExtension == null ? new ShouldHaveExtension(actual, expectedExtension) : new ShouldHaveExtension(
        actual, actualExtension, expectedExtension);
  }

  private ShouldHaveExtension(File actual, String actualExtension, String expectedExtension) {
    super("%nExpecting%n <%s>%nto have extension:%n <%s>%nbut have:%n <%s>.", actual, expectedExtension,
        actualExtension);
  }

  private ShouldHaveExtension(File actual, String expectedExtension) {
    super("%nExpecting%n <%s>%nto have extension:%n <%s>%nbut current file does not have extension.", actual,
        expectedExtension);
  }
}
