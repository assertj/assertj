/*
 * Created on Dec 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for <code>{@link MultimapAssert#hasSize(int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class MultimapAssert_hasSize_Test extends MultimapAssertBaseTest {

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    assertThat(actual).hasSize(9);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expect(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).hasSize(2);
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    try {
      assertThat(actual).hasSize(3);
    } catch (AssertionError e) {
      assertThat(e)
          .hasMessage(
              "expected size:<3> but was:<9> in:<{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>");
      return;
    }
    fail("Assertion error expected");
  }
}
