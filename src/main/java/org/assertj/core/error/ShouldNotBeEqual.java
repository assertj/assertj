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

import org.assertj.core.internal.*;

/**
 * Creates an error message indicating that an assertion that verifies that two objects are not equal failed.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldNotBeEqual extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeEqual}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to compare actual with expected.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeEqual(Object actual, Object other, ComparisonStrategy comparisonStrategy) {
    return new ShouldNotBeEqual(actual, other, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldNotBeEqual}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeEqual(Object actual, Object other) {
    return new ShouldNotBeEqual(actual, other, StandardComparisonStrategy.instance());
  }

  private ShouldNotBeEqual(Object actual, Object other, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nnot to be equal to:%n <%s>%n%s", actual, other, comparisonStrategy);
  }

}
