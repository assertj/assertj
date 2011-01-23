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
 * Creates an error message indicating that an assertion that verifies that an object is an instance of some type failed.
 *
 * @author Alex Ruiz
 */
public class IsNotInstanceOf extends BasicErrorMessage {

  /**
   * Creates a new </code>{@link IsNotInstanceOf}</code>.
   * @param actual the actual value in the failed assertion.
   * @param type the type {@code actual} is expected to belong to.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessage isNotInstanceOf(Object actual, Class<?> type) {
    return new IsNotInstanceOf(actual, type);
  }

  private IsNotInstanceOf(Object actual, Class<?> type) {
    super("expected <%s> to be an instance of:<%s> but was instance of:<%s>", actual, type, actual.getClass());
  }
}
