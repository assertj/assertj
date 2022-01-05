package org.assertj.core.internal.classes;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ClassModifierShouldBe.shouldNotBeStatic;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class Classes_assertIsNotStatic_Test extends ClassesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsNotStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_not_a_static_class() {
    // GIVEN
    Class<?> actual = Math.class;
    // WHEN/THEN
    classes.assertIsNotStatic(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_an_interface() {
    // GIVEN
    Class<?> actual = Classes_assertIsStatic_Test.NestedInterface.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsNotStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeStatic(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_static_class() {
    // GIVEN
    Class<?> actual = Classes_assertIsStatic_Test.StaticNestedClass.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsNotStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeStatic(actual).create());
  }

}
