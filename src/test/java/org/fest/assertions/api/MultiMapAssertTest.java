package org.fest.assertions.api;

import static com.google.common.collect.Lists.newArrayList;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.FailureMessages.actualIsNull;

import static org.junit.rules.ExpectedException.none;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MultiMapAssertTest {

  @Rule
  public ExpectedException thrown = none();
  private Multimap<String, String> actual;

  @Before
  public void setUp() {
    actual = ArrayListMultimap.create();
    actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
  }

  @Test
  public void should_pass_if_actual_contains_given_keys() {
    assertThat(actual).containsKeys("Lakers", "Bulls");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // TODO : use ExpectedException.expectAssertionError from Fest when moved to fest test.
    thrown.expect(AssertionError.class);
    thrown.expectMessage(actualIsNull());
    actual = null;
    assertThat(actual).containsKeys("Nets", "Bulls", "Knicks");
  }

  @Test
  public void should_fail_if_keys_to_look_for_are_null() {
    // TODO : use ExpectedException.expectIllegalArgumentException from Fest when moved to fest test.
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The keys to look for should not be null");
    assertThat(actual).containsKeys((String[]) null);
  }

  @Test
  public void should_fail_if_keys_to_look_for_are_empty() {
    // TODO : use ExpectedException.expectIllegalArgumentException from Fest when moved to fest test.
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The keys to look for should not be empty");
    assertThat(actual).containsKeys();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_keys() {
    try {
      assertThat(actual).containsKeys("Nets", "Bulls", "Knicks");
    } catch (AssertionError e) {
      assertThat(e)
          .hasMessage(
              "expecting:\n"
                  + "<{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>\n"
                  + " to contain keys:\n<['Nets', 'Knicks']>");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_the_given_key() {
    try {
      assertThat(actual).containsKeys("Nets");
    } catch (AssertionError e) {
      // error message shows that we were looking for a unique key
      assertThat(e)
          .hasMessage(
              "expecting:\n"
                  + "<{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>\n"
                  + " to contain key:\n<'Nets'>");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
