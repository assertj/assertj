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

import java.util.Collection;

/**
 * Creates an error message indicating that an assertion that verifies that a value satisfies a <code>{@link Class}</code>
 * failed.
 * @author phx
 */
public class ShouldHaveConstructors extends BasicErrorMessageFactory {
  /**
   * Creates a new <code>{@link ShouldHaveConstructors}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected expected parameters for this construction
   * @param missing missing parameters of the constructions for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveConstructors(Class<?> actual,
                                                          Collection<Class<?>[]> expected,
                                                            Collection<Class<?>[]> missing) {
    return new ShouldHaveConstructors(actual, expected, missing);
  }

  private ShouldHaveConstructors(Class<?> actual, Collection<Class<?>[]> expected,
                                  Collection<Class<?>[]> missing) {
    super("%nExpecting%n  <%s>%nto have parameters:%n  <%s>%nbut the following parameters were "
        + "not found:%n  <%s>", actual, expected, missing);
  }
}
