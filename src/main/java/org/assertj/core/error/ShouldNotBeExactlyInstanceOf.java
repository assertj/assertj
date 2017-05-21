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
 * Creates an error message indicating that an assertion that verifies that an object is not exactly an instance of some type
 * failed.
 * 
 * @author Joel Costigliola
 */
public class ShouldNotBeExactlyInstanceOf extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeExactlyInstanceOf}</code>.
   * @param actual the actual value in the failed assertion.
   * @param type the type {@code actual} is expected to be.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeExactlyInstance(Object actual, Class<?> type) {
    return actual instanceof Throwable ?
      new ShouldNotBeExactlyInstanceOf((Throwable) actual, type) : new ShouldNotBeExactlyInstanceOf(actual, type);
  }

  private ShouldNotBeExactlyInstanceOf(Object actual, Class<?> type) {
    super("%nExpecting%n <%s>%nnot to be of exact type:%n <%s>", actual, type);
  }

  private ShouldNotBeExactlyInstanceOf(Throwable throwable, Class<?> type) {
    super("%nExpecting%n <%s>%nnot to be of exact type:%n <%s>", getStackTrace(throwable), type);
  }
}
