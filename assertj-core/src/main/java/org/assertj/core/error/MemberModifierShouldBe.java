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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.StringJoiner;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isPublic;

/**
 * Error message factory for an assertion which checks that an member has (or has not) a specific modifier.
 *
 * @author William Bakker
 */
public class MemberModifierShouldBe extends BasicErrorMessageFactory {

  private static final String PACKAGE_PRIVATE = "package-private";

  private MemberModifierShouldBe(Member actual, boolean positive, String modifier) {
    super("%nExpecting actual:%n  %s%n" + (positive ? "to" : "not to") + " be a %s member but was %s.",
          actual, modifier, modifiers(actual));
  }

  /**
   * Creates a new instance for a positive check of the {@code final} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeFinal(Member actual) {
    return new MemberModifierShouldBe(actual, true, Modifier.toString(Modifier.FINAL));
  }

  /**
   * Creates a new instance for a negative check of the {@code final} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeFinal(Member actual) {
    return new MemberModifierShouldBe(actual, false, Modifier.toString(Modifier.FINAL));
  }

  /**
   * Creates a new instance for a positive check of the {@code public} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBePublic(Member actual) {
    return new MemberModifierShouldBe(actual, true, Modifier.toString(Modifier.PUBLIC));
  }

  /**
   * Creates a new instance for a positive check of the {@code protected} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeProtected(Member actual) {
    return new MemberModifierShouldBe(actual, true, Modifier.toString(Modifier.PROTECTED));
  }

  /**
   * Creates a new instance for a positive check of the {@code package-private} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBePackagePrivate(Member actual) {
    return new MemberModifierShouldBe(actual, true, PACKAGE_PRIVATE);
  }

  /**
   * Creates a new instance for a positive check of the {@code static} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeStatic(Member actual) {
    return new MemberModifierShouldBe(actual, true, Modifier.toString(Modifier.STATIC));
  }

  /**
   * Creates a new instance for a negative check of the {@code static} modifier.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeStatic(Member actual) {
    return new MemberModifierShouldBe(actual, false, Modifier.toString(Modifier.STATIC));
  }

  private static String modifiers(Member actual) {
    int modifiers = actual.getModifiers();
    boolean isPackagePrivate = !isPublic(modifiers) && !isProtected(modifiers) && !isPrivate(modifiers);
    String modifiersDescription = Modifier.toString(modifiers);
    StringJoiner sj = new StringJoiner(" ");

    if (isPackagePrivate) {
      sj.add(PACKAGE_PRIVATE);
    }
    if (!modifiersDescription.isEmpty()) {
      sj.add(modifiersDescription);
    }

    return sj.toString();
  }

}
