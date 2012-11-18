package org.fest.assertions.api;

import static com.google.common.collect.Lists.newArrayList;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.spy;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.fest.assertions.internal.Failures;

public class MultiMapAssertTest {

  @Rule
  public ExpectedException thrown = none();
  private Failures failures;
  private Multimap<String, List<String>> actual;

  @Before
  public void setUp() {
    failures = spy(Failures.instance());
    actual = HashMultimap.create();
    actual.put("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    actual.put("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    actual.put("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
  }

  @Test
  public void should_pass_if_actual_contains_given_keys() {
    assertThat(actual).containsKeys("Lakers", "Bulls");
  }

  // @Test
  // public void should_fail_if_actual_does_not_contain_all_given_keys() {
  // MultimapAssert<String, List<String>> multimapAssert = assertThat(actual);
  // try {
  // multimapAssert.failures = failures;
  // multimapAssert.containsKeys("Nets", "Bulls", "Knicks");
  // } catch (AssertionError e) {
  // verify(failures).failure(multimapAssert.info, shouldContainKeys(actual, "Nets", "Knicks"));
  // return;
  // }
  // failBecauseExpectedAssertionErrorWasNotThrown();
  // }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_keys2() {
    try {
      assertThat(actual).containsKeys("Nets", "Bulls", "Knicks");
    } catch (AssertionError e) {
      assertThat(e)
          .hasMessage(
              "expecting:\n"
                  + "<{Lakers=[[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar]], Bulls=[[Michael Jordan, Scottie Pippen, Derrick Rose]], Spurs=[[Tony Parker, Tim Duncan, Manu Ginobili]]}>\n"
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
      assertThat(e)
          .hasMessage(
              "expecting:\n"
                  + "<{Lakers=[[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar]], Bulls=[[Michael Jordan, Scottie Pippen, Derrick Rose]], Spurs=[[Tony Parker, Tim Duncan, Manu Ginobili]]}>\n"
                  + " to contain key:\n<'Nets'>");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
