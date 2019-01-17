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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.BeforeEach;

public class Objects_assertIsEqualToUsingRecursiveComparison_BaseTest extends ObjectsBaseTest {

  static final AssertionInfo INFO = someInfo();
  RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  void verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(Object actual, Object expected,
                                                                 ComparisonDifference... differences) {
    verify(failures).failure(INFO, shouldBeEqualByComparingFieldByFieldRecursively(actual,
                                                                                   expected,
                                                                                   list(differences),
                                                                                   recursiveComparisonConfiguration,
                                                                                   INFO.representation()));
  }

  void areEqualsByRecursiveComparison(Object actual, Object expected,
                         RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    objects.assertIsEqualToUsingRecursiveComparison(INFO, actual, expected, recursiveComparisonConfiguration);
  }

  static ComparisonDifference diff(String path, Object actual, Object other) {
    return new ComparisonDifference(list(path), actual, other);
  }

  static ComparisonDifference diff(String path, Object actual, Object other, String additionalInformation) {
    return new ComparisonDifference(list(path), actual, other, additionalInformation);
  }
}
