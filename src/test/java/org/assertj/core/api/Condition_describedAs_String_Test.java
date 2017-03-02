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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestData.someTextDescription;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Condition#describedAs(String)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Condition_describedAs_String_Test {

  @Rule
  public ExpectedException thrown = none();

  private Condition<Object> condition;

  @Before
  public void setUp() {
    condition = new TestCondition<>();
  }

  @Test
  public void should_set_description() {
    String description = someTextDescription();
    condition.describedAs(description);
    assertThat(condition.description.value()).isEqualTo(description);
  }

  @Test
  public void should_set_empty_description_if_description_is_null() {
    String description = null;
    condition.describedAs(description);
    assertThat(condition.description.value()).isEmpty();
  }

  @Test
  public void should_return_same_condition() {
    Condition<Object> returnedCondition = condition.describedAs(someTextDescription());
    assertThat(returnedCondition).isSameAs(condition);
  }
}
