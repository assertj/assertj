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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.*;

import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#isGreaterThan(Object, Object)}.
 * 
 * @author Joel Costigliola
 */
class ComparatorBasedComparisonStrategy_isGreaterThan_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  void verify_that_isGreaterThan_delegates_to_compare_method() {
    caseInsensitiveStringComparator = mock(CaseInsensitiveStringComparator.class);
    caseInsensitiveComparisonStrategy = new ComparatorBasedComparisonStrategy(caseInsensitiveStringComparator);
    String s1 = "string1";
    String s2 = "string2";
    caseInsensitiveComparisonStrategy.isGreaterThan(s1, s2);
    verify(caseInsensitiveStringComparator).compare(s1, s2);
  }

  @Test
  void should_pass() {
    String string = "stringA";
    String lesserUpperString = "STRING";
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThan(string, lesserUpperString)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThan(lesserUpperString, string)).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThan(string, string)).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThan(string, "STRINGA")).isFalse();
    String lowerLesserString = "string";
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThan(string, lowerLesserString)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThan(lowerLesserString, string)).isFalse();
  }

  @Test
  void should_fail_if_a_parameter_is_not_comparable() {
    assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> caseInsensitiveComparisonStrategy.isGreaterThan(new Rectangle(),
                                                                                                                         new Rectangle()));
  }

}
