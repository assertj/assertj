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
package org.assertj.core.api.method;

import org.assertj.core.api.MethodAssert;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAbstract.shouldBeAbstract;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

/**
 * Tests for <code>{@link MethodAssert#isAbstract()}</code>.
 *
 * @author William Bakker
 */
class MethodAssert_isAbstract_Test {
  abstract class AbstractContainer {
    abstract void abstractMethod();

    void notAbstractMethod() {}
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Method actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAbstract());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_not_abstract() {
    // GIVEN
    Method actual = getMethod("notAbstractMethod");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAbstract());
    // THEN
    then(assertionError).hasMessage(shouldBeAbstract(actual).create());
  }

  @Test
  void should_pass_if_actual_is_abstract() {
    // GIVEN
    Method actual = getMethod("abstractMethod");
    // WHEN/THEN
    assertThat(actual).isAbstract();
  }

  private static Method getMethod(String name) {
    try {
      return MethodAssert_isAbstract_Test.AbstractContainer.class.getDeclaredMethod(name);
    } catch (NoSuchMethodException exception) {
      throw new RuntimeException("failed to get method", exception);
    }
  }

}
