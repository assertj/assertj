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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.BeforeEach;

public class RecursiveComparisonAssert_isEqualTo_BaseTest extends ObjectsBaseTest {

  public static WritableAssertionInfo info;
  public RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  public void verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(Object actual, Object expected,
                                                                        ComparisonDifference... differences) {
    verify(failures).failure(info, shouldBeEqualByComparingFieldByFieldRecursively(actual,
                                                                                   expected,
                                                                                   list(differences),
                                                                                   recursiveComparisonConfiguration,
                                                                                   info.representation()));
  }

  public AssertionError compareRecursivelyFailsAsExpected(Object actual, Object expected) {
    RecursiveComparisonAssert<?> recursiveComparisonAssert = new RecursiveComparisonAssert<>(actual,
                                                                                             recursiveComparisonConfiguration);
    info = recursiveComparisonAssert.info;
    recursiveComparisonAssert.objects = objects;
    return expectAssertionError(() -> recursiveComparisonAssert.isEqualTo(expected));
  }

  public static ComparisonDifference diff(List<String> path, Object actual, Object other) {
    return new ComparisonDifference(new DualValue(path, actual, other));
  }

  public static ComparisonDifference diff(List<String> path, Object actual, Object other, String additionalInformation) {
    return new ComparisonDifference(new DualValue(path, actual, other), additionalInformation);
  }

  public static ComparisonDifference diff(String path, Object actual, Object other) {
    return new ComparisonDifference(new DualValue(list(path), actual, other));
  }

  public static ComparisonDifference diff(String path, Object actual, Object other, String additionalInformation) {
    DualValue dualValue = new DualValue(list(path), actual, other);
    return new ComparisonDifference(dualValue, additionalInformation);
  }
}
