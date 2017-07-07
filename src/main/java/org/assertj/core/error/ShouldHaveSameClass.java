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

/**
 * Creates an error message indicating that an assertion that verifies that an object have same class as another instance failed.
 * 
 * @author Nicolas François
 */
public class ShouldHaveSameClass extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSameClass}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the type {@code actual} is expected to be.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSameClass(Object actual, Object other) {
    return new ShouldHaveSameClass(actual, other);
  }

  private ShouldHaveSameClass(Object actual, Object other) {
    super("%nExpecting%n <%s>%nto have the same class as:%n <%s>(<%s>) %nbut its class was:<%s>", actual, other, other.getClass(), actual
        .getClass());
  }
}
