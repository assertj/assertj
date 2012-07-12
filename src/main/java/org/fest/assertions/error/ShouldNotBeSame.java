/*
 * Created on Sep 17, 2010
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
 * Creates an error message indicating an assertion that verifies that two objects do not refer to the same object failed.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ShouldNotBeSame extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeSame}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeSame(Object actual) {
    return new ShouldNotBeSame(actual);
  }

  private ShouldNotBeSame(Object actual) {
    super("expected not same:<%s>", actual);
  }
}
