package org.assertj.core.internal.classes;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePackage.shouldHavePackage;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.stream.Stream;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Classes assertHasPackage")
class Classes_assertHasPackage_with_Package_Test extends ClassesBaseTest {

  private static class MyClass {
  }

  @ParameterizedTest
  @MethodSource("packages")
  void should_pass_if_actual_declares_given_package(Package aPackage) {
    // GIVEN
    Class<?> actual = Jedi.class;
    // WHEN/THEN
    classes.assertHasPackage(someInfo(), actual, aPackage);
  }

  @Test
  void sohould_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    Package aPackage = Object.class.getPackage();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(), actual, aPackage));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_null_package_is_given() {
    // GIVEN
    Class<?> actual = Jedi.class;
    Package aPackage = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> classes.assertHasPackage(someInfo(), actual, aPackage));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("aPackage").create());
  }

  @Test
  void should_fail_if_package_prefix_is_given() {
    // GIVEN
    Class<?> actual = MyClass.class;
    Package aPackage = Package.getPackage("org.assertj.core.internal");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(), actual, aPackage));
    // THEN
    then(assertionError).hasMessage(shouldHavePackage(actual, aPackage.getName()).create());
  }

  static Stream<Package> packages() {
    return Stream.of(Jedi.class.getPackage(), Package.getPackage("org.assertj.core.internal"));
  }
}
