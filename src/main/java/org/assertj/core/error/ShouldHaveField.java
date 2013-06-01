/*
 * Created on Jul 20, 2012
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
package org.assertj.core.error;

import java.util.Set;

/**
 * Creates an error message indicating that an assertion that verifies that a class have field.
 * 
 * @author William Delanoue
 */
public class ShouldHaveField extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link org.assertj.core.error.ShouldHaveField}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expected expected fields for this class
   * @param missing missing fields for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveField(Class<?> actual, Set<String> expected, Set<String> missing) {
    return new ShouldHaveField(actual, expected, missing, false);
  }

  /**
   * Creates a new </code>{@link org.assertj.core.error.ShouldHaveField}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected expected fields for this class
   * @param missing missing fields for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveDeclaredField(Class<?> actual, Set<String> expected, Set<String> missing) {
    return new ShouldHaveField(actual, expected, missing, false);
  }

  private ShouldHaveField(Class<?> actual, Set<String> expected, Set<String> missing, boolean declared) {
    super("\nExpecting\n <%s>\nto have " + (declared ? "declared " : "")
        + "fields :  \n<%s>\nbut be doesn't have \n<%s>", actual, expected, missing);
  }
}
