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
package org.assertj.core.internal;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonDifferenceCalculator;
import org.junit.jupiter.api.Test;

public class ConfigurableRecursiveFieldByFieldComparator_Test {

  private ConfigurableRecursiveFieldByFieldComparator configurableRecursiveFieldByFieldComparator;

  @Test
  public void should_delegate_comparison_to_recursiveComparisonDifferenceCalculator() {
    // GIVEN
    RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator = mock(RecursiveComparisonDifferenceCalculator.class);
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    configurableRecursiveFieldByFieldComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration,
                                                                                                  recursiveComparisonDifferenceCalculator);
    given(recursiveComparisonDifferenceCalculator.determineDifferences(any(), any(), any())).willReturn(emptyList());
    String actual = "foo";
    String other = "bar";
    // WHEN
    int compare = configurableRecursiveFieldByFieldComparator.compare(actual, other);
    // THEN
    verify(recursiveComparisonDifferenceCalculator).determineDifferences(actual, other, recursiveComparisonConfiguration);
    then(compare).isZero();
  }

  @Test
  public void should_return_0_when_both_values_are_null() {
    // GIVEN
    RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator = mock(RecursiveComparisonDifferenceCalculator.class);
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    configurableRecursiveFieldByFieldComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration,
                                                                                                  recursiveComparisonDifferenceCalculator);
    // WHEN
    int comparisonResult = configurableRecursiveFieldByFieldComparator.compare(null, null);
    // THEN
    verifyNoInteractions(recursiveComparisonDifferenceCalculator);
    then(comparisonResult).isZero();
  }

  @Test
  public void should_not_return_0_when_only_one_of_both_value_is_null() {
    // GIVEN
    RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator = mock(RecursiveComparisonDifferenceCalculator.class);
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    configurableRecursiveFieldByFieldComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration,
                                                                                                  recursiveComparisonDifferenceCalculator);
    // WHEN
    int comparisonResult1 = configurableRecursiveFieldByFieldComparator.compare(null, "foo");
    int comparisonResult2 = configurableRecursiveFieldByFieldComparator.compare("foo", null);
    // THEN
    verifyNoInteractions(recursiveComparisonDifferenceCalculator);
    then(comparisonResult1).isNotZero();
    then(comparisonResult2).isNotZero();
  }

  @Test
  public void should_throw_an_NPE_if_given_RecursiveComparisonConfiguration_is_null() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("RecursiveComparisonConfiguration must not be null");
  }

}
