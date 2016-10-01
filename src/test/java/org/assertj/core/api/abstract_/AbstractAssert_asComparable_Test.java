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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import java.util.Date;

import org.junit.Test;

public class AbstractAssert_asComparable_Test {

  @Test
  public void should_allow_to_perform_Comparable_assertions_on_Comparable_variable_declared_as_Object() {
    Object comparableAsObject = "abc";
    assertThat(comparableAsObject).asComparable(String.class).isLessThan("xyz");

    // try with another comparable type
    comparableAsObject = new Date(10);
    assertThat(comparableAsObject).asComparable(Date.class).isLessThan(new Date(100000));
  }
  
  @Test
  public void should_fail_as_variable_runtime_type_is_not_Comparable() {
    // GIVEN
    Object npe = new NullPointerException();
    // WHEN
    try {
      assertThat(npe).asComparable(String.class).isLessThan("xyz");
    } catch (AssertionError e) {
      // THEN
      assertThat(e).hasMessageContaining(format("Expecting:%n" +
                                                " <java.lang.NullPointerException>%n" +
                                                "to be an instance of:%n" +
                                                " <java.lang.Comparable>%n" +
                                                "but was instance of:%n" +
                                                " <java.lang.NullPointerException>"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
