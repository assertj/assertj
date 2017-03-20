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

import org.assertj.core.presentation.StandardRepresentation;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains elements that
 * are not an instance of one of the given types. A group of elements can be an iterable or an array of objects.
 * 
 * @author Martin Winandy
 */
public class ShouldContainOnlyInstances extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link shouldContainOnlyInstances}</code>.
   * 
   * @param object the object value in the failed assertion.
   * @param types the expected classes and interfaces.
   * @param dismatches elements that are not an instance of one of the given types.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldContainOnlyInstances shouldContainOnlyInstances(Object actual, Class<?>[] types,
                                                                      Iterable<?> dismatches) {
    return new ShouldContainOnlyInstances(actual, types, dismatches);
  }

  private ShouldContainOnlyInstances(Object actual, Class<?>[] types, Iterable<?> dismatches) {
    super("%n" +
        "Expecting:%n" +
        "  <%s>%n" +
        "to contain only instances of:%n" +
        "  <%s>%n" +
        "elements are not instances:%n" +
        "  <[" + resolveClassNames(dismatches) + "]>",
        actual, types);
  }

  private static String resolveClassNames(Iterable<?> instances) {
    StringBuilder builder = new StringBuilder();
    
    for (Object instance : instances) {
      if (builder.length() > 0) {
        builder.append(", ");
      }
      
      String formatted = StandardRepresentation.STANDARD_REPRESENTATION.toStringOf(instance);
      builder.append(formatted);
      
      if (instance != null && !formatted.contains(instance.getClass().getName())) {
        builder.append(" (").append(instance.getClass().getName()).append(")");
      }
    }
    
    return builder.toString();
  }

}
