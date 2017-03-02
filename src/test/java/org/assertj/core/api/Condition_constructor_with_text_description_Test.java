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

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Condition#Condition(String)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Condition_constructor_with_text_description_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_set_description() {
    String text = "your eyes can deceive you; don't trust them";
    Condition<Object> condition = new Condition<Object>(text) {
      @Override
      public boolean matches(Object value) {
        return false;
      }
    };
    assertThat(condition.description.value()).isEqualTo(text);
  }

  @Test
  public void should_set_empty_description_if_description_is_null() {
    Condition<Object> condition = new Condition<Object>((String) null) {
      @Override
      public boolean matches(Object value) {
        return false;
      }
    };
    assertThat(condition.description.value()).isEmpty();
  }
}
