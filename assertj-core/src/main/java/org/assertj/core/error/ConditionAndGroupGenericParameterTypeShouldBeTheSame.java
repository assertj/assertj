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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.api.Condition;

/**
 * Creates an error message indicating that an assertion that verifies type of elements of group and {@code Condition} A group of
 * elements can be a collection, an array.<br>
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ConditionAndGroupGenericParameterTypeShouldBeTheSame extends BasicErrorMessageFactory {

  public ConditionAndGroupGenericParameterTypeShouldBeTheSame(Object actual, Condition<?> condition) {
    super("%nExpecting actual: %s to have the same generic type as condition %s", actual, condition);
  }

  /**
   * Creates a new <code>{@link ConditionAndGroupGenericParameterTypeShouldBeTheSame}</code>
   * @param actual the actual value in the failed assertion.
   * @param condition the {@code Condition}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeSameGenericBetweenIterableAndCondition(Object actual, Condition<?> condition) {
    return new ConditionAndGroupGenericParameterTypeShouldBeTheSame(actual, condition);
  }

}
