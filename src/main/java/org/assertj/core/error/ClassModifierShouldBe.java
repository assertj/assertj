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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import java.lang.reflect.Modifier;

/**
 * Error message factory for an assertion which checks that a class has (or has not) a specific modifier.
 *
 * @author Michal Kordas
 */
public class ClassModifierShouldBe extends BasicErrorMessageFactory {

  private ClassModifierShouldBe(Class<?> actual, boolean positive, String modifier) {
    super("%nExpecting:%n  <%s>%n" + (positive ? "to" : "not to") + " be a %s class but was %s.",
          actual, modifier, Modifier.toString(actual.getModifiers()));
  }

  /**
   * Creates a new instance for a positive check of the {@code final} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeFinal(Class<?> actual) {
    return new ClassModifierShouldBe(actual, true, Modifier.toString(Modifier.FINAL));
  }

  /**
   * Creates a new instance for a negative check of the {@code final} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeFinal(Class<?> actual) {
    return new ClassModifierShouldBe(actual, false, Modifier.toString(Modifier.FINAL));
  }

  /**
   * Creates a new instance for a positive check of the {@code public} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBePublic(Class<?> actual) {
    return new ClassModifierShouldBe(actual, true, Modifier.toString(Modifier.PUBLIC));
  }

  /**
   * Creates a new instance for a positive check of the {@code protected} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeProtected(Class<?> actual) {
    return new ClassModifierShouldBe(actual, true, Modifier.toString(Modifier.PROTECTED));
  }

  /**
   * Creates a new instance for a positive check of the {@code package-private} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBePackagePrivate(Class<?> actual) {
    return new ClassModifierShouldBe(actual, true, "package-private");
  }

}
