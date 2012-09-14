/*
 * Created on Dec 26, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.internal.*;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code String} contains another {@code String}
 * failed.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldContainString extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainString}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(String actual, String sequence) {
    return new ShouldContainString("expecting:<%s> to contain:<%s>%s", actual, sequence, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainString}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(String actual, String sequence, ComparisonStrategy comparisonStrategy) {
    return new ShouldContainString("expecting:<%s> to contain:<%s>%s", actual, sequence, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainString}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainIgnoringCase(String actual, String sequence) {
    return new ShouldContainString("expecting:<%s> to contain:<%s> (ignoring case)", actual, sequence,
        StandardComparisonStrategy.instance());
  }

  private ShouldContainString(String format, String actual, String sequence, ComparisonStrategy comparisonStrategy) {
    super(format, actual, sequence, comparisonStrategy);
  }
}
