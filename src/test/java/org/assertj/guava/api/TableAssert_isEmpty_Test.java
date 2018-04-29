/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.guava.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Jan Gorman
 */
public class TableAssert_isEmpty_Test extends TableAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_empty() {
    actual.clear();
    assertThat(actual).isEmpty();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).containsValues("Manu Ginobili", "Derrick Rose");
  }

  @Test
  public void should_fail_if_actual_is_not_empty() {
    try {
      assertThat(actual).isEmpty();
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("%nExpecting empty but was:<{1={3=Millard Fillmore, 4=Franklin Pierce}, 2={5=Grover Cleveland}}>"));
      return;
    }
    fail("Assertion error expected");
  }

}
