package org.assertj.core.internal.classes;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBeStatic;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class Classes_assertIsStatic_Test extends ClassesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_a_static_class() {
    // GIVEN
    Class<?> actual = StaticNestedClass.class;
    // WHEN/THEN
    classes.assertIsStatic(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_an_interface() {
    // GIVEN
    Class<?> actual = NestedInterface.class;
    // WHEN/THEN
    classes.assertIsStatic(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_not_a_static_class() {
    // GIVEN
    Class<?> actual = Math.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeStatic(actual).create());
  }

  static class StaticNestedClass {
  }

  interface NestedInterface {
  }

}
