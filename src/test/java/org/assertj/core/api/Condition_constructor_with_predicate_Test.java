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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

public class Condition_constructor_with_predicate_Test {

  @Test
  public void should_create_condition_with_predicate() {
    // GIVEN
    Condition<String> fairyTale = new Condition<>(s -> s.startsWith("Once upon a time"), "a %s tale", "fairy");
    String littleRedCap = "Once upon a time there was a dear little girl ...";
    // THEN
    assertThat(littleRedCap).is(fairyTale);
  }

  @Test
  public void should_throw_error_if_predicate_is_null() {
    assertThatNullPointerException().isThrownBy(() -> new Condition<>((Predicate<Object>) null, ""));
  }
}
