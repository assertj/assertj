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

import java.util.Set;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} contains another
 * {@code CharSequence} failed.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldContainCharSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence sequence) {
    return new ShouldContainCharSequence("%nExpecting:%n <%s>%nto contain:%n <%s> %s", actual, sequence,
                                   StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence sequence, ComparisonStrategy comparisonStrategy) {
    return new ShouldContainCharSequence("%nExpecting:%n <%s>%nto contain:%n <%s> %s", actual, sequence, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param notFound the values not found.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence[] strings, Set<? extends CharSequence> notFound,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldContainCharSequence("%nExpecting:%n <%s>%nto contain:%n <%s>%nbut could not find:%n <%s>%n %s", actual,
                                   strings, notFound, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param notFound the values not found.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence[] strings, Set<? extends CharSequence> notFound) {
    return shouldContain(actual, strings, notFound, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainIgnoringCase(CharSequence actual, CharSequence sequence) {
    return new ShouldContainCharSequence("%nExpecting:%n <%s>%nto contain:%n <%s>%n (ignoring case)", actual, sequence,
                                   StandardComparisonStrategy.instance());
  }

  private ShouldContainCharSequence(String format, CharSequence actual, CharSequence sequence, ComparisonStrategy comparisonStrategy) {
    super(format, actual, sequence, comparisonStrategy);
  }

  private ShouldContainCharSequence(String format, CharSequence actual, CharSequence[] values, Set<? extends CharSequence> notFound,
      ComparisonStrategy comparisonStrategy) {
    super(format, actual, values, notFound, comparisonStrategy);
  }
}
