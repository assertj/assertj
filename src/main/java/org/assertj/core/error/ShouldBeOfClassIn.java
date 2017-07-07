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
 * Creates an error message indicating that an assertion that verifies that an object is of type in group of types failed.
 * 
 * @author Nicolas François
 */
public class ShouldBeOfClassIn extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeOfClassIn}</code>.
   * @param actual the actual value in the failed assertion.
   * @param types contains the types {@code actual} is expected to be in.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeOfClassIn(Object actual, Object types) {
    return new ShouldBeOfClassIn(actual, types);
  }

  private ShouldBeOfClassIn(Object actual, Object types) {
    super("%nExpecting:%n <%s>%nto be of one these types:%n <%s>%nbut was:%n <%s>", actual, types, actual.getClass());
  }
}
