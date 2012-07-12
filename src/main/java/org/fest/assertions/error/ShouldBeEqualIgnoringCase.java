/*
 * Created on Dec 24, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

/**
 * Creates an error message indicating that an assertion that verifies that two {@code String}s are equal, ignoring case
 * considerations, failed.
 * 
 * @author Alex Ruiz
 */
public class ShouldBeEqualIgnoringCase extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEqualIgnoringCase}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqual(String actual, String expected) {
    return new ShouldBeEqualIgnoringCase(actual, expected);
  }

  private ShouldBeEqualIgnoringCase(String actual, String expected) {
    super("expecting:\n<%s>\n to be equal to:\n<%s>\n ignoring case considerations", actual, expected);
  }
}
