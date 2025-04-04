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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that an object is an instance of one or more
 * types failed.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldBeInstanceOfAny extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeInstanceOfAny}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param types contains the type or types {@code actual} is expected to belong to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInstanceOfAny(Object actual, Class<?>[] types) {
    return actual instanceof Throwable throwable ? new ShouldBeInstanceOfAny(throwable, types)
        : new ShouldBeInstanceOfAny(actual, types);
  }

  private ShouldBeInstanceOfAny(Object actual, Class<?>[] types) {
    super("%nExpecting actual:%n  %s%nto be an instance of any of:%n  %s%nbut was instance of:%n  %s", actual, types,
          actual.getClass());
  }

  private ShouldBeInstanceOfAny(Throwable throwable, Class<?>[] types) {
    super("%nExpecting actual throwable to be an instance of any of the following types:%n  %s%nbut was:%n  %s", types,
          throwable);
  }
}
