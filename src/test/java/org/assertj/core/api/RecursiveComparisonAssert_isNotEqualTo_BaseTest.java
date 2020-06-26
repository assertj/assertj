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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.error.ShouldNotBeEqualComparingFieldByFieldRecursively.shouldNotBeEqualComparingFieldByFieldRecursively;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

public class RecursiveComparisonAssert_isNotEqualTo_BaseTest extends ObjectsBaseTest {
  public static WritableAssertionInfo info;
  public RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  public void verifyShouldNotBeEqualComparingFieldByFieldRecursivelyCall(Object actual, Object other) {
    verify(failures).failure(info, shouldNotBeEqualComparingFieldByFieldRecursively(actual,
                                                                                    other,
                                                                                    recursiveComparisonConfiguration,
                                                                                    info.representation()));
  }

  public AssertionError areNotEqualRecursiveComparisonFailsAsExpected(Object actual, Object other) {
    RecursiveComparisonAssert<?> recursiveComparisonAssert = new RecursiveComparisonAssert<>(actual,
                                                                                             recursiveComparisonConfiguration);
    info = recursiveComparisonAssert.info;
    recursiveComparisonAssert.objects = objects;
    return expectAssertionError(() -> recursiveComparisonAssert.isNotEqualTo(other));
  }
}
