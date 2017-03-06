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
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestData.someTextDescription;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Condition#as(Description)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Condition_as_Description_Test {

  @Rule
  public ExpectedException thrown = none();

  private static Description description;

  @BeforeClass
  public static void setUpOnce() {
    description = new TestDescription(someTextDescription());
  }

  private Condition<Object> condition;

  @Before
  public void setUp() {
    condition = new TestCondition<>();
  }

  @Test
  public void should_set_description() {
    condition.as(description);
    assertThat(condition.description()).isSameAs(description);
  }

  @Test
  public void should_replace_null_description_by_an_empty_one() {
    condition.as((Description) null);
    assertThat(condition.description()).isEqualTo(emptyDescription());
  }

  @Test
  public void should_return_same_condition() {
    Condition<Object> returnedCondition = condition.as(description);
    assertThat(returnedCondition).isSameAs(condition);
  }
}
