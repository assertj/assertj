/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.testkit.junit.jupiter;

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
