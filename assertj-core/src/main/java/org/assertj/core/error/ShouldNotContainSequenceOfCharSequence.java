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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that {@link CharSequence} does not contain
 * a sequence of several {@code CharSequence} in order failed.
 */
public class ShouldNotContainSequenceOfCharSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new {@link ShouldNotContainSequenceOfCharSequence}.
   *
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of {@link CharSequence} expected not to be in {@code actual}.
   * @param index the index where the sequence was found in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@link ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSequence(CharSequence actual, CharSequence[] sequence, int index,
                                                             ComparisonStrategy comparisonStrategy) {
    return new ShouldNotContainSequenceOfCharSequence(actual, sequence, index, comparisonStrategy);
  }

  /**
   * Creates a new {@link ShouldNotContainSequenceOfCharSequence}.
   *
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of {@link CharSequence} expected not to be in {@code actual}.
   * @param index the index where the sequence was found in {@code actual}.
   * @return the created {@link ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSequence(CharSequence actual, CharSequence[] sequence, int index) {
    return new ShouldNotContainSequenceOfCharSequence(actual, sequence, index, StandardComparisonStrategy.instance());
  }

  private ShouldNotContainSequenceOfCharSequence(CharSequence actual, CharSequence[] sequence, int index,
                                                 ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto not contain sequence:%n  %s%nbut was found at index %s%n%s", actual, sequence, index,
          comparisonStrategy);
  }

}
