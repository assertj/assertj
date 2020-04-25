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
 * Creates an error message indicating that an assertion that verifies that {@link Object} is not in its correct format fail.
 */
public class ShouldBeCorrectFormat extends BasicErrorMessageFactory {
  /**
   * @param which badContent or expected is not in its correct format.
   * @param badContent the content what is in bad format.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeCorrectFormat(String which, Object badContent) {
    return new ShouldBeCorrectFormat(which, badContent);
  }

  /**
   * @param badContent the content what is in bad format.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeCorrectFormat(Object badContent) {
    return new ShouldBeCorrectFormat(badContent);
  }

  private ShouldBeCorrectFormat(String which, Object badContent) {
    super("%nExpecting " + which + ":%n <%s>%nto be correct format but was not.", badContent);
  }

  private ShouldBeCorrectFormat(Object badContent) {
    super("%nExpecting:%n <%s>%nto be correct format but was not.", badContent);
  }
}
