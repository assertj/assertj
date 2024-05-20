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
package org.assertj.tests.core.data;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link Index}.
 *
 * @author Alex Ruiz
 */
class Index_Test {

  @Test
  void should_honor_equals_contract() {
    // WHEN/THEN
    EqualsVerifier.forClass(Index.class)
                  .verify();
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 1 })
  void atIndex_should_succeed(int value) {
    // WHEN
    Index index = Index.atIndex(value);
    // THEN
    then(index.value).isEqualTo(value);
  }

  @Test
  void atIndex_should_fail_if_value_is_negative() {
    // WHEN
    Throwable thrown = catchThrowable(() -> Index.atIndex(-1));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The value of the index should not be negative");
  }

  @Test
  void should_implement_toString() {
    // GIVEN
    Index underTest = Index.atIndex(8);
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("Index[value=8]");
  }

}
