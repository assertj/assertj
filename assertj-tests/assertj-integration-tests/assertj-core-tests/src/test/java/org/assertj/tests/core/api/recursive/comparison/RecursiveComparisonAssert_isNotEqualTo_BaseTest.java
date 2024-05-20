/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.util.AssertionsUtil;
import org.junit.jupiter.api.BeforeEach;

public class RecursiveComparisonAssert_isNotEqualTo_BaseTest {
  public static WritableAssertionInfo info;
  public RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  public void verifyShouldNotBeEqualComparingFieldByFieldRecursivelyCall(Object actual, Object other) {
    // verify(failures).failure(info, shouldNotBeEqualComparingFieldByFieldRecursively(actual,
    // other,
    // recursiveComparisonConfiguration,
    // info.representation()));
  }

  public AssertionError areNotEqualRecursiveComparisonFailsAsExpected(Object actual, Object other) {
    RecursiveComparisonAssert<?> recursiveComparisonAssert = new RecursiveComparisonAssert<>(actual,
                                                                                             recursiveComparisonConfiguration);
    info = recursiveComparisonAssert.info;
    return AssertionsUtil.expectAssertionError(() -> recursiveComparisonAssert.isNotEqualTo(other));
  }
}
