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

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains elements that
 * are not an instance of one of the given types. A group of elements can be an iterable or an array of objects.
 * 
 * @author Martin Winandy
 */
public class ShouldOnlyHaveElementsOfTypes extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldOnlyHaveElementsOfTypes}</code>.
   * 
   * @param actual the object value in the failed assertion.
   * @param types the expected classes and interfaces.
   * @param dismatches elements that are not an instance of one of the given types.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldOnlyHaveElementsOfTypes shouldOnlyHaveElementsOfTypes(Object actual, Class<?>[] types,
                                                                              Iterable<?> dismatches) {
    return new ShouldOnlyHaveElementsOfTypes(actual, types, dismatches);
  }

  private ShouldOnlyHaveElementsOfTypes(Object actual, Class<?>[] types, Iterable<?> nonMatchingElements) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to only have instances of:%n" +
          "  <%s>%n" +
          "but these elements are not:%n" +
          "  <[" + resolveClassNames(nonMatchingElements) + "]>",
          actual, types);
  }

  private static String resolveClassNames(Iterable<?> elements) {
    StringBuilder builder = new StringBuilder();

    for (Object element : elements) {
      if (builder.length() > 0) {
        builder.append(", ");
      }

      String formatted = CONFIGURATION_PROVIDER.representation().toStringOf(element);
      builder.append(formatted);

      if (element != null && !formatted.contains(element.getClass().getName())) {
        builder.append(" (").append(element.getClass().getName()).append(")");
      }
    }

    return builder.toString();
  }

}
