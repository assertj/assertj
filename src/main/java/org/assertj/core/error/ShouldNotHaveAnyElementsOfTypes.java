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
import java.util.Map;

public class ShouldNotHaveAnyElementsOfTypes extends BasicErrorMessageFactory {

  private ShouldNotHaveAnyElementsOfTypes(Object actual, Class<?>[] unexpectedTypes,
                                          Map<Class<?>, List<Object>> nonMatchingElementsByType) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to not have any elements of the following types:%n" +
          "  <%s>%n" +
          "but found:%n" +
          "  <%s>",
          actual, unexpectedTypes, nonMatchingElementsByType);
  }

  /**
   * Creates a new <code>{@link ShouldNotHaveAnyElementsOfTypes}</code>.
   * @param actual array or Iterable
   * @param unexpectedTypes the not expected types of all elements
   * @param nonMatchingElementsByType the elements with unexpected types.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldNotHaveAnyElementsOfTypes shouldNotHaveAnyElementsOfTypes(Object actual,
                                                                                Class<?>[] unexpectedTypes,
                                                                                Map<Class<?>, List<Object>> nonMatchingElementsByType) {
    return new ShouldNotHaveAnyElementsOfTypes(actual, unexpectedTypes, nonMatchingElementsByType);
  }

}
