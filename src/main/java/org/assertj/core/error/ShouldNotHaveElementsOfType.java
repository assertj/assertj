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

import java.util.List;

/**
 * Creates an error message indicating that a group have elements of the given types.
 */
public class ShouldNotHaveElementsOfType extends BasicErrorMessageFactory {

  private ShouldNotHaveElementsOfType(Object actual, Class<?>[] notExpectedTypes, List<Class<?>> notExpectedTypesOccurred) {
    super("%n" +
     "Expecting:%n" +
     "  <%s>%n" +
     "to not have any elements of the following types:%n" +
     "  <%s>%nbut found:%n" +
     "  <%s>", actual, notExpectedTypes, notExpectedTypesOccurred);
  }

  /**
   * Creates a new <code>{@link ShouldNotHaveElementsOfType}</code>.
   * @param actual array or Iterable
   * @param notExpectedTypes the not expected types of all elements
   * @param notExpectedTypeOccurred the types of elements that are not expected or it's subclasses.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldNotHaveElementsOfType shouldNotHaveElementsOfType(Object actual, Class<?> []notExpectedTypes, List<Class<?>> notExpectedTypeOccurred) {
    return new ShouldNotHaveElementsOfType(actual, notExpectedTypes, notExpectedTypeOccurred);
  }
}
