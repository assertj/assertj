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

import java.util.StringJoiner;

/**
 * Creates an error message indicating that an assertion that verifies that a class is assignable to.
 *
 * @author Vikram Nithyanandam
 * @author Jessica Hamilton
 */
public class ShouldBeAssignableTo extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_ASSIGNABLE_TO = new StringJoiner("%n", "%n", "").add("Expecting")
                                                                                        .add("  %s")
                                                                                        .add("to be assignable to:")
                                                                                        .add("  %s")
                                                                                        .toString();

  /**
   * Creates a new <code>{@link ShouldBeAssignableTo}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param other the type {@code actual} should be assignable to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeAssignableTo(Class<?> actual, Class<?> other) {
    return new ShouldBeAssignableTo(actual, other);
  }

  private ShouldBeAssignableTo(Class<?> actual, Class<?> other) {
    super(SHOULD_BE_ASSIGNABLE_TO, actual, other);
  }

}
