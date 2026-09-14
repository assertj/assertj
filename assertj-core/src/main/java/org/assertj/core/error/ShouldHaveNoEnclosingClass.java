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

import java.util.StringJoiner;

/**
 * Creates an error message indicating that an assertion that verifies that a class has no enclosing class failed.
 *
 * @author Chanwon Lee
 */
public class ShouldHaveNoEnclosingClass extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_NO_ENCLOSING_CLASS = new StringJoiner("%n", "%n",
                                                                                "").add("Expecting")
                                                                                   .add("  %s")
                                                                                   .add("to have no enclosing class, but had:")
                                                                                   .add("  %s")
                                                                                   .toString();

  /**
   * Creates a new <code>{@link ShouldHaveNoEnclosingClass}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveNoEnclosingClass(Class<?> actual) {
    return new ShouldHaveNoEnclosingClass(actual);
  }

  private ShouldHaveNoEnclosingClass(Class<?> actual) {
    super(SHOULD_HAVE_NO_ENCLOSING_CLASS, actual, actual.getEnclosingClass());
  }
}
