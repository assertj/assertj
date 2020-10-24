package org.assertj.core.internal.classes;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePackage.shouldHavePackage;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Collection;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Classes assertHasPackage(Package)")
class Classes_assertHasPackage_with_Package_Test extends ClassesBaseTest {

  @Test
  void should_pass_if_actual_declares_given_package() {
    // WHEN/THEN
    classes.assertHasPackage(someInfo(), Object.class, Object.class.getPackage());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(),
                                                                                        null,
                                                                                        Object.class.getPackage()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_aPackage_is_null() {
    // GIVEN
    Package aPackage = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> classes.assertHasPackage(someInfo(), Object.class, aPackage));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("aPackage").create());
  }

  @Test
  void should_fail_if_package_does_not_match() {
    // GIVEN
    Class<?> actual = Object.class;
    Package aPackage = Collection.class.getPackage();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(), actual, aPackage));
    // THEN
    then(assertionError).hasMessage(shouldHavePackage(actual, aPackage).create());
  }

}
