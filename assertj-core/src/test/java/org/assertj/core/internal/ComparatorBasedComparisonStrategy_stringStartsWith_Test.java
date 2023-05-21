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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#stringContains(String, String)}.
 * 
 * @author Joel Costigliola
 */
class ComparatorBasedComparisonStrategy_stringStartsWith_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  void should_pass() {
    assertThat(caseInsensitiveComparisonStrategy.stringStartsWith("Frodo", "Fro")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.stringStartsWith("Frodo", "FRO")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.stringStartsWith("rodo", "Fro")).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.stringStartsWith("rodo", "rodoo")).isFalse();
  }

}
