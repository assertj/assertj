/*
 * Created on Sep 17, 2010
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
 * Creates an error message indicating that an assertion that verifies that two object refer to same object failed.
 *
 * @author Alex Ruiz
 */
public class IsNotSame extends BasicErrorMessage {

  /**
   * Creates a new <code>{@link IsNotSame}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessage isNotSame(Object actual, Object expected) {
    return new IsNotSame(actual, expected);
  }

  private IsNotSame(Object actual, Object expected) {
    super("%sexpected:<%s> and actual:<%s> should refer to the same object", expected, actual);
  }
}
