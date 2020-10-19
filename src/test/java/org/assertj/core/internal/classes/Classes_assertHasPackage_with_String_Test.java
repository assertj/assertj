package org.assertj.core.internal.classes;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePackage.shouldHavePackage;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Classes assertHasPackage")
class Classes_assertHasPackage_with_String_Test extends ClassesBaseTest {

  private static class MyClass {
  }

  @Test
  void should_pass_if_actual_declares_given_package_name() {
    // GIVEN
    Class<?> actual = Jedi.class;
    // WHEN/THEN
    classes.assertHasPackage(someInfo(), actual, "org.assertj.core.internal");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(), actual, "java.lang"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_null_package_name_is_given() {
    // GIVEN
    Class<?> actual = Jedi.class;
    String packageName = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> classes.assertHasPackage(someInfo(), actual, packageName));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("packageName").create());
  }

  @Test
  void should_fail_if_package_name_prefix_is_given() {
    // GIVEN
    Class<?> actual = MyClass.class;
    String packageName = "org.assertj.core.internal";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(), actual, packageName));
    // THEN
    then(assertionError).hasMessage(shouldHavePackage(actual, packageName).create());
  }

}
