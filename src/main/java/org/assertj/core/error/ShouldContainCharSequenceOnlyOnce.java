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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.*;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} contains another {@code CharSequence} only
 * once failed.
 * 
 * @author Pauline Iogna
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldContainCharSequenceOnlyOnce extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainCharSequenceOnlyOnce}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param sequence the String expected to be in {@code actual} only once.
   * @param occurences the number of occurrences of sequence in actual.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyOnce(CharSequence actual, CharSequence sequence, int occurences,
      ComparisonStrategy comparisonStrategy) {
    if (occurences == 0) return new ShouldContainCharSequenceOnlyOnce(actual, sequence, comparisonStrategy);
    return new ShouldContainCharSequenceOnlyOnce(actual, sequence, occurences, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequenceOnlyOnce}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param sequence the String expected to be in {@code actual} only once.
   * @param occurences the number of occurrences of sequence in actual.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyOnce(CharSequence actual, CharSequence sequence, int occurences) {
    if (occurences == 0) return new ShouldContainCharSequenceOnlyOnce(actual, sequence, StandardComparisonStrategy.instance());
    return new ShouldContainCharSequenceOnlyOnce(actual, sequence, occurences, StandardComparisonStrategy.instance());
  }

  private ShouldContainCharSequenceOnlyOnce(CharSequence actual, CharSequence expected, int occurences, ComparisonStrategy comparisonStrategy) {
    super("\nExpecting:\n <%s>\nto appear only once in:\n <%s>\nbut it appeared %s times %s", expected, actual,
        occurences,
        comparisonStrategy);
  }

  private ShouldContainCharSequenceOnlyOnce(CharSequence actual, CharSequence expected, ComparisonStrategy comparisonStrategy) {
    super("\nExpecting:\n <%s>\nto appear only once in:\n <%s>\nbut it did not appear %s", expected, actual,
        comparisonStrategy);
  }
}
