/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2013 the original author or authors.
 */
package org.fest.assertions.api;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import static org.junit.Assert.fail;

import org.junit.Test;

public class MultimapAssert_containsKeys_Test extends MultimapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_keys() {
    assertThat(actual).containsKeys("Lakers", "Bulls");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).containsKeys("Nets", "Bulls", "Knicks");
  }

  @Test
  public void should_fail_if_keys_to_look_for_are_null() {
    expectException(IllegalArgumentException.class, "The keys to look for should not be null");
    assertThat(actual).containsKeys((String[]) null);
  }

  @Test
  public void should_fail_if_keys_to_look_for_are_empty() {
    expectException(IllegalArgumentException.class, "The keys to look for should not be empty");
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
                  + " to contain keys:\n<['Nets', 'Bulls', 'Knicks']>\n but could not find:\n<['Nets', 'Knicks']>");
      return;
    }
    fail("Assertion error expected");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_the_given_key() {
    try {
      assertThat(actual).containsKeys("Nets");
    } catch (AssertionError e) {
      // error message shows that we were looking for a unique key (not many)
      assertThat(e)
          .hasMessage(
              "expecting:\n"
                  + "<{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>\n"
                  + " to contain key:\n<'Nets'>");
      return;
    }
    fail("Assertion error expected");
  }

}
