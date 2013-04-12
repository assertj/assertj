/*
 * Created on Dec 26, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code String} contains aa sequence of
 * several {@code String} in order failed.
 * 
 * @author Joel Costigliola
 */
public class ShouldContainStringSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainStringSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(String actual, String[] strings, int firstBadOrderIndex) {
    return shouldContainSequence(actual, strings, firstBadOrderIndex, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainStringSequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(String actual, String[] strings, int badOrderIndex,
      ComparisonStrategy comparisonStrategy) {

    return new ShouldContainStringSequence(
                                           "\nExpecting:\n <%s>\nto contain the following Strings in this order:\n <%s>\nbut <%s> was found before <%s>\n%s",
                                           actual, strings, strings[badOrderIndex + 1], strings[badOrderIndex],
                                           comparisonStrategy);
  }

  private ShouldContainStringSequence(String format, String actual, String[] strings, String foundButBadOrder,
      String foundButBadOrder2, ComparisonStrategy comparisonStrategy) {
    super(format, actual, strings, foundButBadOrder, foundButBadOrder2, comparisonStrategy);
  }

}
