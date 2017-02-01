/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#stringContains(String, String)}.
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_stringContains_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void should_pass() {
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "ro")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "RO")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "Fra")).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "Frodoo")).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "Froda")).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "abcdefg")).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.stringContains("Frodo", "a")).isFalse();
  }

}
