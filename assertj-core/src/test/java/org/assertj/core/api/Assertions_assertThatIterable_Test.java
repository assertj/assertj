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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatIterable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.junit.jupiter.api.Test;

class Assertions_assertThatIterable_Test {

  @Test
  void should_create_Assert() {
    // GIVEN
    Iterable<Object> actual = newLinkedHashSet();
    // WHEN
    AbstractIterableAssert<?, Iterable<?>, Object, ObjectAssert<Object>> assertThat = assertThatIterable(actual);
    // THEN
    then(assertThat).isNotNull();
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    Iterable<String> names = newLinkedHashSet("Luke");
    // WHEN
    Iterable<? extends String> actual = assertThatIterable(names).actual;
    // THEN
    then(actual).isSameAs(names);
  }
}
