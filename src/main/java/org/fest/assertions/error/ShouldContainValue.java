/*
 * Created on Jun 3, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.error;

/**
 * Creates an error message indicating that an assertion that verifies a map contains a value.
 * 
 * @author Nicolas François
 */
public class ShouldContainValue extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldContainValue}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainValue(Object actual, Object value) {
    return new ShouldContainValue(actual, value);
  }

  private ShouldContainValue(Object actual, Object value) {
    super("expecting:\n<%s>\n to contain value:\n<%s>", actual, value);
  }
}
