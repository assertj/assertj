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

import java.awt.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#isGreaterThanOrEqualTo(Object, Object)}.
 * 
 * @author Joel Costigliola
 */
class ComparatorBasedComparisonStrategy_isGreaterThanOrEqualTo_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  void should_pass() {
    String string = "stringA";
    String lesserUpperString = "STRING";
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThanOrEqualTo(string, string)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThanOrEqualTo(string, "STRINGA")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThanOrEqualTo(string, lesserUpperString)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThanOrEqualTo(lesserUpperString, string)).isFalse();
    String lesserLowerString = "string";
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThanOrEqualTo(string, lesserLowerString)).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.isGreaterThanOrEqualTo(lesserLowerString, string)).isFalse();
  }

  @Test
  void should_fail_if_a_parameter_is_not_comparable() {
    assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> caseInsensitiveComparisonStrategy.isGreaterThanOrEqualTo(new Rectangle(),
                                                                                                                                  new Rectangle()));
  }

}
