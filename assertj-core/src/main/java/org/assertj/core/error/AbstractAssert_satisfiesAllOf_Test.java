package org.assertj.core.error;

import org.assertj.core.api.Condition;
import org.assertj.core.condition.AllOf;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

public class AbstractAssert_satisfiesAllOf_Test {

  @Test
  void should_pass_when_all_of_the_condition_is_met() {
    Condition<String> conditionA = new Condition<>(text -> text != null, "Input not null");
    Condition<String> conditionB = new Condition<>(text -> text.equalsIgnoreCase("ABC"), "Input not matching");
    assertThat("ABC").satisfies(AllOf.allOf(conditionA, conditionB));
  }

  @Test
  void should_fail_if_one_of_the_condition_is_not_met() {
    Condition<String> condition1 = new Condition<>(text -> text != null, "Input not null");
    Condition<String> condition2 = new Condition<>(text -> text.equalsIgnoreCase("abc"), "Input not matching");
    try {
      assertThat("def").satisfies(AllOf.allOf(condition1, condition2));
    } catch (AssertionError e) {
      String expectedMessage = String.format("%nExpecting actual:%n  \"def\"%nto satisfy:%n  [✗] all of:[%n" +
                                             "   [✓] Input not null,%n" +
                                             "   [✗] Input not matching%n" +
                                             "]");
      then(e).hasMessage(expectedMessage);
    }
  }
}
