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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#toString()}.
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_toString_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void toString_with_anonymous_comparator() {
    ComparatorBasedComparisonStrategy lengthComparisonStrategy = new ComparatorBasedComparisonStrategy(new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    });
    assertThat(lengthComparisonStrategy).hasToString("'anonymous comparator class'");
  }

  @Test
  public void toString_with_non_anonymous_comparator() {
    assertThat(caseInsensitiveComparisonStrategy).hasToString("'CaseInsensitiveStringComparator'");
  }

}
