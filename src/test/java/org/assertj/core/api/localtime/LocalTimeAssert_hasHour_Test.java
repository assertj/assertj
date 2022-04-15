package org.assertj.core.api.localtime;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class LocalTimeAssert_hasHour_Test {
  @Test
  void should_pass_if_actual_is_in_given_hour(){
    //GIVEN
    LocalTime actual = LocalTime.of(23,59,59);
    //WHEN/THEN
    then(actual).hasHour(23);

  }

  @Test
  void should_fail_if_actual_is_not_in_given_hour(){
    //GIVEN
    LocalTime actual = LocalTime.of(23,59,59);
    int expectedHour = 22;
    //WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasHour(expectedHour));
    //THEN
    then(assertionError).hasMessage(shouldHaveDateField(actual, "hour", expectedHour).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasHour(LocalTime.now().getHour()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }


}
