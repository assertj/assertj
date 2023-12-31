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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

import java.util.Set;

/**
 * Creates an error message indicating that an assertion that verifies a map does not contain a values.
 *
 * @author Ilya Koshaleu
 */
public class ShouldNotContainValues extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotContainValues}</code>.
   *
   * @param <V> value type
   * @param actual the actual value in the failed assertion.
   * @param values the unexpected values
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <V> ErrorMessageFactory shouldNotContainValues(Object actual, Set<V> values) {
    if (values.size() == 1) return shouldNotContainValue(actual, values.iterator().next());
    return new ShouldNotContainValues(actual, values);
  }

  public <V> ShouldNotContainValues(Object actual, Set<V> values) {
    super("%nExpecting actual:%n  %s%nnot to contain values:%n  %s", actual, values);
  }
}
