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
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} contains a sequence of
 * several {@code CharSequence}s in order failed.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldContainCharSequenceSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainCharSequenceSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param firstBadOrderIndex the first incorrect value's index.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(CharSequence actual, CharSequence[] strings, int firstBadOrderIndex) {
    return shouldContainSequence(actual, strings, firstBadOrderIndex, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequenceSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @param badOrderIndex the incorrect value's index.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(CharSequence actual, CharSequence[] strings, int badOrderIndex,
      ComparisonStrategy comparisonStrategy) {

    return new ShouldContainCharSequenceSequence(
                                           "%nExpecting:%n <%s>%nto contain the following CharSequences in this order:%n <%s>%nbut <%s> was found before <%s>%n%s",
                                           actual, strings, strings[badOrderIndex + 1], strings[badOrderIndex],
                                           comparisonStrategy);
  }

  private ShouldContainCharSequenceSequence(String format, CharSequence actual, CharSequence[] strings, CharSequence foundButBadOrder,
      CharSequence foundButBadOrder2, ComparisonStrategy comparisonStrategy) {
    super(format, actual, strings, foundButBadOrder, foundButBadOrder2, comparisonStrategy);
  }

}
