/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.kotlin.testkit.junit.jupiter

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores

private const val TEST_SUFFIX = " Test"

class DefaultDisplayNameGenerator : ReplaceUnderscores() {

  override fun generateDisplayNameForClass(testClass: Class<*>): String {
    return removeTestSuffixIfExists(super.generateDisplayNameForClass(testClass))
  }

  private fun removeTestSuffixIfExists(displayName: String): String {
    return if (displayName.endsWith(TEST_SUFFIX))
      displayName.take(displayName.lastIndexOf(TEST_SUFFIX))
    else
      displayName
  }

}
