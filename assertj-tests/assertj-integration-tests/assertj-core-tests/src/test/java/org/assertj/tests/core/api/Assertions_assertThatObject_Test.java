/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.assertj.core.util.Lists.list;

import java.util.LinkedList;
import java.util.List;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class Assertions_assertThatObject_Test {

  @Test
  void should_create_Assert() {
    // GIVEN
    Object actual = new Object();
    // WHEN
    AbstractObjectAssert<?, Object> assertions = Assertions.assertThatObject(actual);
    // THEN
    assertThat(assertions).isNotNull();
  }

  @SuppressWarnings("unchecked")
  @Test
  void should_avoid_casting() {
    LinkedList<String> actual = new LinkedList<>(List.of("test"));
    // tests against actual require casts when using an overloaded assertThat that does not capture the type of actual
    assertThat(actual).matches(list -> ((LinkedList<String>) list).getFirst().equals("test"));
    // with assertThatObject we can force the generic version, but we lose the specific assertions for iterables
    assertThatObject(actual).matches(list -> list.getFirst().equals("test"));
  }

}
