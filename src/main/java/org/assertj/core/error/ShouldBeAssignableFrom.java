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

/**
 * Creates an error message indicating that an assertion that verifies that a class is assignable from.
 * 
 * @author William Delanoue
 */
public class ShouldBeAssignableFrom extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeAssignableFrom}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expectedAssignableFrom the expected assignable.
   * @param missingAssignableFrom the missing classes.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeAssignableFrom(Class<?> actual, Set<Class<?>> expectedAssignableFrom,
      Set<Class<?>> missingAssignableFrom) {
    return new ShouldBeAssignableFrom(actual, expectedAssignableFrom, missingAssignableFrom);
  }

  private ShouldBeAssignableFrom(Class<?> actual, Set<Class<?>> expectedAssignableFrom,
      Set<Class<?>> missingAssignableFrom) {
    super("%nExpecting%n  <%s>%nto be assignable from:%n  <%s>%nbut was not assignable from:%n  <%s>", actual,
          expectedAssignableFrom, missingAssignableFrom);
  }
}
