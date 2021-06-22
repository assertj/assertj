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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.error;

/**
 * Creates an error message that indicates an assertion that verifies that a character is mixed case failed.
 * 
 * @author Andrey Kuzmin
 */
public class ShouldBeMixedCase extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeMixedCase}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeMixedCase(Object actual) {
    return new ShouldBeMixedCase(actual);
  }

  private ShouldBeMixedCase(Object actual) {
    super("%nExpecting %s to be mixed case", actual);
  }
}
