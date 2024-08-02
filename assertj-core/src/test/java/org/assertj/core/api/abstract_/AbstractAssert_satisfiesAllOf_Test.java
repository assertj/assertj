package org.assertj.core.api.abstract_;

import org.assertj.core.api.Condition;
import org.assertj.core.condition.AllOf;
import org.junitpioneer.vintage.Test;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

public class AbstractAssert_satisfiesAllOf_Test {
  @Test
  void should_pass_when_all_of_the_condition_is_met() {
    // GIVEN
    Condition<String> conditionA = new Condition<>(text -> text != null, "Input not null");
    Condition<String> conditionB = new Condition<>(text -> text.equalsIgnoreCase("ABC"), "Input not matching");
    // WHEN/THEN
    then("ABC").satisfies(AllOf.allOf(conditionA, conditionB));
  }

  @Test
  void should_fail_if_one_of_the_condition_is_not_met() {
    // GIVEN
    Condition<String> condition1 = new Condition<>(text -> text != null, "Input not null");
    Condition<String> condition2 = new Condition<>(text -> text.equalsIgnoreCase("abc"), "Input not matching");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("def").satisfies(AllOf.allOf(condition1, condition2)));
    // THEN
    then(assertionError).hasMessage(String.format("%nExpecting actual:%n  \"def\"%nto satisfy:%n  [✗] all of:[%n" +
                                                  "   [✓] Input not null,%n" +
                                                  "   [✗] Input not matching%n" +
                                                  "]"));
  }

  @Test
  void should_fail_if_all_the_condition_is_not_met() {
    // GIVEN
    Condition<String> condition1 = new Condition<>(text -> text.length() > 10, "Input is short");
    Condition<String> condition2 = new Condition<>(text -> text.equalsIgnoreCase("abc"), "Input not matching");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("def").satisfies(AllOf.allOf(condition1, condition2)));
    // THEN
    String expectedMessage = String.format("%nExpecting actual:%n  \"def\"%nto satisfy:%n  [✗] all of:[%n" +
                                           "   [✗] Input is short,%n" +
                                           "   [✗] Input not matching%n" +
                                           "]");
    then(assertionError).hasMessage(expectedMessage);
  }
}
