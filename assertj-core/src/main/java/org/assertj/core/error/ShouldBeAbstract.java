/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

/** Creates errors for classes expected to be abstract. */
public class ShouldBeAbstract extends BasicErrorMessageFactory {

  /**
   * Creates an error for a class expected to be abstract.
   *
   * @param actual the actual class
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldBeAbstract(Class<?> actual) {
    return new ShouldBeAbstract(actual);
  }

  private ShouldBeAbstract(Class<?> actual) {
    super("%nExpecting actual:%n  %s%nto be abstract", actual);
  }
}
