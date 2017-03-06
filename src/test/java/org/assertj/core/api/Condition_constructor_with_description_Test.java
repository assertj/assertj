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

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Condition#Condition(Description)}</code>
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Condition_constructor_with_description_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_set_description() {
    Description d = new TextDescription("always in motion is the future");
    Condition<Object> condition = new Condition<Object>(d) {
      @Override
      public boolean matches(Object value) {
        return false;
      }
    };
    assertThat(condition.description).isSameAs(d);
  }

  @Test
  public void should_set_empty_description_if_description_is_null() {
    Condition<Object> condition = new Condition<Object>((Description) null) {
      @Override
      public boolean matches(Object value) {
        return false;
      }
    };
    assertThat(condition.description.value()).isEmpty();
  }
}
