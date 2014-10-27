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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import static org.junit.Assert.fail;

import org.junit.Test;

import org.assertj.core.data.MapEntry;

public class MultimapAssert_contains_Test extends MultimapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_entries() {
    assertThat(actual).contains(entry("Bulls", "Derrick Rose"));
    assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).contains(entry("Lakers", "Kobe Bryant"));
  }

  @Test
  public void should_fail_if_entries_to_look_for_are_null() {
    expectException(IllegalArgumentException.class, "The entries to look for should not be null");
    assertThat(actual).contains((MapEntry[]) null);
  }

  @Test
  public void should_fail_if_entries_to_look_for_are_empty() {
    expectException(IllegalArgumentException.class, "The entries to look for should not be empty");
    assertThat(actual).contains();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_entries() {
    try {
      assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Derrick Rose"));
    } catch (AssertionError e) {
      assertThat(e)
          .hasMessage(
              "\nExpecting:\n"
                  + " <{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>\n"
                  + "to contain:\n"
                  + " <[MapEntry[key='Lakers', value='Kobe Bryant'], MapEntry[key='Spurs', value='Derrick Rose']]>\n"
                  + "but could not find:\n" +
                    " <[MapEntry[key='Spurs', value='Derrick Rose']]>\n");
      return;
    }
    fail("Assertion error expected");
  }

}
