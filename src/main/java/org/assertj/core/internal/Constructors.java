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

package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBePrivate.shouldBePrivate;
import static org.assertj.core.error.ShouldBeProtected.shouldBeProtected;
import static org.assertj.core.error.ShouldBePublic.shouldBePublic;
import static org.assertj.core.error.ShouldHaveParameter.shouldHaveParameter;

import java.lang.reflect.Constructor;
import org.assertj.core.api.AssertionInfo;

/**
 * Reusable assertions for <code>{@link Constructor}</code>s.
 *
 * @author phx
 */
public class Constructors {

  private static final Constructors INSTANCE = new Constructors();
  private final Failures failures = Failures.instance();

  /**
   * Returns the singleton instance of this constructor.
   *
   * @return the singleton instance of this constructor.
   */
  public static Constructors instance() {
    return INSTANCE;
  }

  private static void assertNotNull(AssertionInfo info, Constructor actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  /**
   * Verifies that the actual {@code Constructor} is public.
   *
   * @param info   contains information about the assertion.
   * @param actual the "actual" {@code Constructor}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} is not public.
   */
  public void assertIsPublic(AssertionInfo info, Constructor actual) {
    assertModifier(info, actual, "PUBLIC");
  }

  /**
   * Verifies that the actual {@code Constructor} is private.
   *
   * @param info   contains information about the assertion.
   * @param actual the "actual" {@code Constructor}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} is not private.
   */
  public void assertIsPrivate(AssertionInfo info, Constructor actual) {
    assertModifier(info, actual, "PRIVATE");
  }

  /**
   * Verifies that the actual {@code Constructor} is protected.
   *
   * @param info   contains information about the assertion.
   * @param actual the "actual" {@code Constructor}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} is not protected.
   */
  public void assertIsProtected(AssertionInfo info, Constructor actual) {
    assertModifier(info, actual, "PROTECTED");
  }

  private String getModifier(Constructor actual) {
    switch (actual.getModifiers()) {
      case 1:
        return "PUBLIC";
      case 2:
        return "PRIVATE";
      case 4:
        return "PROTECTED";
      default:
        return null;
    }
  }

  private void assertModifier(AssertionInfo info, Constructor actual, String exp) {
    assertNotNull(info, actual);
    String modifier = getModifier(actual);
    Objects.instance().assertNotNull(info, modifier);
    if (modifier.equals(exp)) {
      return;
    }
    switch (exp) {
      case "PUBLIC":
        throw failures.failure(info, shouldBePublic(actual, modifier));
      case "PROTECTED":
        throw failures.failure(info, shouldBeProtected(actual, modifier));
      case "PRIVATE":
        throw failures.failure(info, shouldBePrivate(actual, modifier));
      default:
        throw failures.failure(info, shouldBePublic(actual, modifier));
    }
  }

  /**
   * Verifies that the actual {@code Constructor} has the arguments classes.
   *
   * @param info      contains information about the assertion.
   * @param actual    the "actual" {@code Class}.
   * @param arguments arguments who to invoke the Constructor.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Constructor} do not contain these arguments.
   */
  public void hasArguments(AssertionInfo info, Constructor actual,
      Class<?>... arguments) {
    assertNotNull(info, actual);
    Class<?>[] parameterTypes = actual.getParameterTypes();
    if (arguments.length != parameterTypes.length) {
      throw failures.failure(info, shouldHaveParameter(actual, arguments, parameterTypes));
    }
    for (int i = 0; i < arguments.length; i++) {
      if (!parameterTypes[i].getName().equals(arguments[i].getName())) {
        throw failures.failure(info, shouldHaveParameter(actual, arguments, parameterTypes));
      }
    }
  }
}
