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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;

public class RecursiveComparisonAssert_BaseTest {
  public static WritableAssertionInfo info = new WritableAssertionInfo();
  public RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void beforeEachTest() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
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

  public static ComparisonDifference javaTypeDiff(String path, Object actual, Object expected) {
    String actualTypeCanonicalName = actual.getClass().getCanonicalName();
    String expectedTypeCanonicalName = expected.getClass().getCanonicalName();
    return RecursiveComparisonAssert_BaseTest.diff(path, actual, expected, actual.getClass().equals(expected.getClass())
        ? "Actual and expected value are both java types (%s) and thus were compared to with equals".formatted(actualTypeCanonicalName)
        : "Actual and expected value are both java types (%s and %s) and thus were compared to with actual equals method".formatted(actualTypeCanonicalName,
                                                                                                                                    expectedTypeCanonicalName));
  }

  public static ComparisonDifference javaTypeDiff(List<String> path, Object actual, Object other) {
    return RecursiveComparisonAssert_BaseTest.diff(path, actual, other,
                                                   "Actual value is a java type and thus was compared to the expected value with its equals method");
  }

  public void compareRecursivelyFailsWithDifferences(Object actual, Object expected, ComparisonDifference... differences) {
    var recursiveComparisonAssert = new RecursiveComparisonAssert<>(actual, recursiveComparisonConfiguration);
    var errorMessageFactory = shouldBeEqualByComparingFieldByFieldRecursively(actual,
                                                                              expected,
                                                                              list(differences),
                                                                              recursiveComparisonConfiguration,
                                                                              info.representation());
    var assertionError = expectAssertionError(() -> recursiveComparisonAssert.isEqualTo(expected));
    assertThat(assertionError).message().isEqualTo(errorMessageFactory.create());
  }
}
