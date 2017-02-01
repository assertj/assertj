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
 * Creates an error message indicating that a group does not have an element of the given type.
 */
public class ShouldHaveOnlyElementsOfType extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveOnlyElementsOfType}</code>.
   * @param actual array or Iterable
   * @param expectedType the expected type of all elements
   * @param unexpectedType the type of one element that is not expectedType or it subclasses.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldHaveOnlyElementsOfType shouldHaveOnlyElementsOfType(Object actual, Class<?> expectedType, Class<?> unexpectedType) {
    return new ShouldHaveOnlyElementsOfType(actual, expectedType, unexpectedType);
  }

  private ShouldHaveOnlyElementsOfType(Object actual, Class<?> expectedType, Class<?> unexpectedType) {
    super("%nExpecting:%n  <%s>%nto only have elements of type:%n  <%s>%nbut found:%n  <%s>", actual, expectedType, unexpectedType);
  }
}
