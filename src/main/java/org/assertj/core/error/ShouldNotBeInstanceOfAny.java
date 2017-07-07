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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.Throwables.getStackTrace;

/**
 * Creates an error message indicating that an assertion that verifies that an object is not an instance of one or more types
 * failed.
 * 
 * @author Nicolas François
 */
public class ShouldNotBeInstanceOfAny extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeInstanceOfAny}</code>.
   * @param actual the actual value in the failed assertion.
   * @param types contains the type or types {@code actual} is expected to belong to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeInstanceOfAny(Object actual, Class<?>[] types) {
    return actual instanceof Throwable ?
      new ShouldNotBeInstanceOfAny((Throwable) actual, types) : new ShouldNotBeInstanceOfAny(actual, types);
  }

  private ShouldNotBeInstanceOfAny(Object actual, Class<?>[] types) {
    super("%nExpecting:%n <%s>%nnot to be an instance of any of these types:%n <%s>", actual, types);
  }

  private ShouldNotBeInstanceOfAny(Throwable throwable, Class<?>[] types) {
    super("%nExpecting:%n <%s>%nnot to be an instance of any of these types:%n <%s>", getStackTrace(throwable), types);
  }
}
