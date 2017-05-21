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

import static org.assertj.core.util.Throwables.getStackTrace;

/**
 * Creates an error message indicating that an assertion that verifies that an object is an instance of some type
 * failed.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldBeInstance extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeInstance}</code>.
   * 
   * @param object the object value in the failed assertion.
   * @param type the type {@code object} is \nExpecting:\n to belong to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInstance(Object object, Class<?> type) {
    return object instanceof Throwable ? new ShouldBeInstance((Throwable) object, type)
        : new ShouldBeInstance(object, type);
  }

  /**
   * Creates a new <code>{@link ShouldBeInstance}</code> when object we want to check type is null.
   * 
   * @param objectDescription the description of the null object we wanted to check type.
   * @param type the \nExpecting:\n type.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInstanceButWasNull(String objectDescription, Class<?> type) {
    return new ShouldBeInstance(objectDescription, type);
  }

  private ShouldBeInstance(Object object, Class<?> type) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to be an instance of:%n" +
          "  <%s>%n" +
          "but was instance of:%n" +
          "  <%s>",
          object, type, object.getClass());
  }

  private ShouldBeInstance(Throwable throwable, Class<?> type) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to be an instance of:%n" +
          "  <%s>%n" +
          "but was:%n" +
          "  <%s>",
          throwable, type, getStackTrace(throwable));
  }

  private ShouldBeInstance(String objectDescription, Class<?> type) {
    super("%n" +
          "Expecting object:%n" +
          "  %s%n" +
          "to be an instance of:%n" +
          "  <%s>%n" +
          "but was null",
          objectDescription, type);
  }
}
