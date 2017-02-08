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

import java.lang.reflect.Modifier;

/**
 * Creates an error message indicating that an assertion that verifies that a class is (or is not) final.
 *
 * @author Michal Kordas
 */
public class ClassModifierShouldBe extends BasicErrorMessageFactory {

  private ClassModifierShouldBe(Class<?> actual, boolean positive, String modifier) {
    super("%nExpecting:%n  <%s>%n" + (positive ? "to" : "not to") + " be a %s class.", actual, modifier);
  }

  private ClassModifierShouldBe(Class<?> actual, String modifier) {
    this(actual, true, modifier);
  }

  /**
   * Creates a new {@link ClassModifierShouldBe}.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeFinal(Class<?> actual) {
    return new ClassModifierShouldBe(actual, true, Modifier.toString(Modifier.FINAL));
  }

  /**
   * Creates a new {@link ClassModifierShouldBe}.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeFinal(Class<?> actual) {
    return new ClassModifierShouldBe(actual, false, Modifier.toString(Modifier.FINAL));
  }

  public static ErrorMessageFactory shouldBePublic(Class<?> actual) {
    return new ClassModifierShouldBe(actual, Modifier.toString(Modifier.PUBLIC));
  }

  public static ErrorMessageFactory shouldBeProtected(Class<?> actual) {
    return new ClassModifierShouldBe(actual, Modifier.toString(Modifier.PROTECTED));
  }

}
