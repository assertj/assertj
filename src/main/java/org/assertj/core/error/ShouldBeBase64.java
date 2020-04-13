/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

/**
 * Creates an error message that indicates an assertion that verifies that a string is a valid Base64 encoded string failed.
 * 
 * @author Stefano Cordio
 */
public class ShouldBeBase64 extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeBase64}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeBase64(String actual) {
    return new ShouldBeBase64(actual);
  }

  private ShouldBeBase64(String actual) {
    super("%nExpecting <%s> to be a valid Base64 encoded string", actual);
  }

}
