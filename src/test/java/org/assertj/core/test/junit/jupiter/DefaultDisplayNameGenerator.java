package org.assertj.core.test.junit.jupiter;

import org.junit.jupiter.api.DisplayNameGenerator;

public class DefaultDisplayNameGenerator extends DisplayNameGenerator.ReplaceUnderscores {

  private static final String TEST_SUFFIX = " Test";

  @Override
  public String generateDisplayNameForClass(Class<?> testClass) {
    return removeTestSuffixIfExists(super.generateDisplayNameForClass(testClass));
  }

  private static String removeTestSuffixIfExists(String displayName) {
    return displayName.endsWith(TEST_SUFFIX)
        ? displayName.substring(0, displayName.lastIndexOf(TEST_SUFFIX))
        : displayName;
  }

}
