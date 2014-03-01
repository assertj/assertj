package org.assertj.core.error;

import java.io.File;

/**
 * Creates an error message indicating that a {@code File} should have name.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveName extends BasicErrorMessageFactory {

  public static ShouldHaveName shouldHaveName(File actual, String expectedName) {
    return new ShouldHaveName(actual, expectedName);
  }

  private ShouldHaveName(File actual, String expectedName) {
    super("%nExpecting%n  <%s>%nto have name:%n  <%s>%nbut had:%n  <%s>.", actual, expectedName, actual.getName());
  }
}
