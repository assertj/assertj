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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.extractor.Extractors.byName;

/**
 * Creates an error message indicating that an assertion that verifies that a class have field with value.
 * 
 * @author Libor Ondrusek
 */
public class ShouldHaveFieldWithValue extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldHaveFieldWithValue}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param name expected name of field for this class
   * @param value expected value of field for class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveFieldWithValue(Object actual, String name, Object value) {
    return new ShouldHaveFieldWithValue(actual, name, value);
  }

  private ShouldHaveFieldWithValue(Object actual, String name, Object value) {
    super("%nExpecting%n  <%s>%nto have "
          + "field:%n  <%s> with value <%s>", actual, name, value);
  }
}
