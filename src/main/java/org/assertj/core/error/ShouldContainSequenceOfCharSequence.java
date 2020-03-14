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
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} contains a Sequence of
 * several {@code CharSequence}s in order failed.
 *
 * @author Billy Yuan
 */

public class ShouldContainSequenceOfCharSequence extends BasicErrorMessageFactory {
  private ShouldContainSequenceOfCharSequence(CharSequence actual, CharSequence[] sequence,
                                              ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto contain sequence:%n <%s>%n%s", actual, sequence, comparisonStrategy);
  }

  /**
   * Create a new <code>{@link ShouldContainSequenceOfCharSequence}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(CharSequence actual, CharSequence[] sequence) {
    return new ShouldContainSequenceOfCharSequence(actual, sequence, StandardComparisonStrategy.instance());
  }

  /**
   * Create a new <code>{@link ShouldContainSequenceOfCharSequence}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(CharSequence actual, CharSequence[] sequence,
                                                          ComparisonStrategy comparisonStrategy) {
    return new ShouldContainSequenceOfCharSequence(actual, sequence, comparisonStrategy);
  }
}
