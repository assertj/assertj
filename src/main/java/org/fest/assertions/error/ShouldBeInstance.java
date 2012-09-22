/*
 * Created on Dec 26, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

/**
 * Creates an error message indicating that an assertion that verifies that an object is an instance of some type
 * failed.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldBeInstance extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldBeInstance}</code>.
   * 
   * @param object the object value in the failed assertion.
   * @param type the type {@code object} is expected to belong to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInstance(Object object, Class<?> type) {
    return new ShouldBeInstance(object, type);
  }

  /**
   * Creates a new </code>{@link ShouldBeInstance}</code> when object we want to check type is null.
   * 
   * @param objectDescription the description of the null object we wanted to check type.
   * @param type the expected type.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInstanceButWasNull(String objectDescription, Class<?> type) {
    return new ShouldBeInstance(objectDescription, type);
  }

  private ShouldBeInstance(Object object, Class<?> type) {
    super("expected <%s> to be an instance of:\n<%s>\nbut was instance of:\n<%s>", object, type, object.getClass());
  }

  private ShouldBeInstance(String objectDescription, Class<?> type) {
    super("expected %s object to be an instance of:<%s> but was null", objectDescription, type);
  }
}
