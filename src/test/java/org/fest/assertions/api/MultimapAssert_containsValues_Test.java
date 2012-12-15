package org.fest.assertions.api;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import static org.junit.Assert.fail;

import org.junit.Test;

public class MultimapAssert_containsValues_Test extends MultimapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
    assertThat(actual).containsValues("Manu Ginobili");
    assertThat(actual).containsValues("Magic Johnson", "Derrick Rose");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expect(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).containsValues("Manu Ginobili", "Derrick Rose");
  }

  @Test
  public void should_fail_if_values_to_look_for_are_null() {
    thrown.expect(IllegalArgumentException.class, "The values to look for should not be null");
    assertThat(actual).containsValues((String[]) null);
  }

  @Test
  public void should_fail_if_values_to_look_for_are_empty() {
    thrown.expect(IllegalArgumentException.class, "The values to look for should not be empty");
    assertThat(actual).containsValues();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_values() {
    try {
      assertThat(actual).containsValues("Magic Johnson", "Lebron James");
    } catch (AssertionError e) {
      assertThat(e)
          .hasMessage(
              "expecting:\n"
                  + "<{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>\n"
                  + " to contain values:\n<['Magic Johnson', 'Lebron James']>\n but could not find:\n<['Lebron James']>");
      return;
    }
    fail("Assertion error expected");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_the_given_value() {
    try {
      assertThat(actual).containsValues("Lebron James");
    } catch (AssertionError e) {
      // error message shows that we were looking for a unique value (not many)
      assertThat(e)
          .hasMessage(
              "expecting:\n"
                  + "<{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>\n"
                  + " to contain value:\n<'Lebron James'>");
      return;
    }
    fail("Assertion error expected");
  }

}
