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
 * Creates an error message indicating that an assertion that verifies that a class has a given enclosing class failed.
 *
 * @author Chanwon Lee
 */
public class ShouldHaveEnclosingClass extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_ENCLOSING_CLASS = new StringJoiner("%n", "%n", "").add("Expecting")
                                                                                            .add("  %s")
                                                                                            .add("to have enclosing class:")
                                                                                            .add("  %s")
                                                                                            .toString();

  private static final String BUT_HAD_NONE = new StringJoiner("%n", "%n", "").add("but had none.")
                                                                             .toString();

  private static final String BUT_HAD = new StringJoiner("%n", "%n", "").add("but had:")
                                                                        .add("  %s")
                                                                        .toString();

  /**
   * Creates a new <code>{@link ShouldHaveEnclosingClass}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param enclosingClass expected enclosing class for this class.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveEnclosingClass(Class<?> actual, Class<?> enclosingClass) {
    Class<?> actualEnclosingClass = actual.getEnclosingClass();
    return (actualEnclosingClass == null)
        ? new ShouldHaveEnclosingClass(actual, enclosingClass)
        : new ShouldHaveEnclosingClass(actual, enclosingClass, actualEnclosingClass);
  }

  private ShouldHaveEnclosingClass(Class<?> actual, Class<?> expectedEnclosingClass) {
    super(SHOULD_HAVE_ENCLOSING_CLASS + BUT_HAD_NONE, actual, expectedEnclosingClass);
  }

  private ShouldHaveEnclosingClass(Class<?> actual, Class<?> expectedEnclosingClass, Class<?> actualEnclosingClass) {
    super(SHOULD_HAVE_ENCLOSING_CLASS + BUT_HAD, actual, expectedEnclosingClass, actualEnclosingClass);
  }
}
