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

import java.util.StringJoiner;

/**
 * Creates an error message indicating that an assertion that verifies that a class has a given superclass failed.
 * 
 * @author Stefano Cordio
 */
public class ShouldHaveSuperclass extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_SUPERCLASS = new StringJoiner("%n", "%n", "").add("Expecting")
                                                                                       .add("  <%s>")
                                                                                       .add("to have superclass:")
                                                                                       .add("  <%s>")
                                                                                       .toString();

  private static final String BUT_HAD_NONE = new StringJoiner("%n", "%n", "").add("but had none.")
                                                                             .toString();

  private static final String BUT_HAD = new StringJoiner("%n", "%n", "").add("but had:")
                                                                        .add("  <%s>")
                                                                        .toString();

  /**
   * Creates a new <code>{@link ShouldHaveSuperclass}</code>.
   *
   * @param actual     the actual value in the failed assertion.
   * @param superclass expected superclass for this class.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSuperclass(Class<?> actual, Class<?> superclass) {
    Class<?> actualSuperclass = actual.getSuperclass();
    if (actualSuperclass == null) {
      return new ShouldHaveSuperclass(actual, superclass);
    }
    return new ShouldHaveSuperclass(actual, superclass, actualSuperclass);
  }

  private ShouldHaveSuperclass(Class<?> actual, Class<?> expectedSuperclass) {
    super(SHOULD_HAVE_SUPERCLASS + BUT_HAD_NONE, actual, expectedSuperclass);
  }

  private ShouldHaveSuperclass(Class<?> actual, Class<?> expectedSuperclass, Class<?> actualSuperclass) {
    super(SHOULD_HAVE_SUPERCLASS + BUT_HAD, actual, expectedSuperclass, actualSuperclass);
  }

}
