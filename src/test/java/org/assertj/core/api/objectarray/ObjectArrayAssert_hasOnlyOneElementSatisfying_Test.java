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
package org.assertj.core.api.objectarray;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_hasOnlyOneElementSatisfying_Test {

  @Test
  void succeeds_if_array_has_only_one_element_and_that_element_statisfies_the_given_assertion() {
    Jedi[] jedis = { new Jedi("Yoda", "red") };
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> assertThat(yoda.getName()).startsWith("Y"));
  }

  @Test
  void succeeds_if_arry_has_only_one_element_and_that_element_statisfies_the_given_assertions() {
    assertThat(new Jedi[] { new Jedi("Yoda", "red") }).hasOnlyOneElementSatisfying(yoda -> {
      assertThat(yoda.getName()).isEqualTo("Yoda");
      assertThat(yoda.lightSaberColor).isEqualTo("red");
    });
  }

  @Test
  void fails_if_arry_has_only_one_element_and_that_element_does_not_satisfy_the_given_assertion() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Jedi[] jedis = { new Jedi("Yoda", "red") };
      assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> assertThat(yoda.getName()).startsWith("L"));
    }).withMessage(format("%nExpecting actual:%n  \"Yoda\"%nto start with:%n  \"L\"%n"));
  }

  @Test
  void fails_if_iterable_has_only_one_element_and_that_element_does_not_satisfy_one_of_the_given_assertion() {
    Jedi[] jedis = { new Jedi("Yoda", "red") };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> {
      assertThat(yoda.getName()).startsWith("Y");
      assertThat(yoda.getName()).startsWith("L");
    })).withMessage(String.format("%nExpecting actual:%n  \"Yoda\"%nto start with:%n  \"L\"%n"));
  }

  @Test
  void fails_if_iterable_has_only_one_element_and_that_element_does_not_satisfy_the_soft_assertion() {
    Jedi[] jedis = { new Jedi("Yoda", "red") };

    Throwable assertionError = catchThrowable(() -> {
      assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(yoda.getName()).startsWith("L");
        softly.assertThat(yoda.getName()).startsWith("M");
        softly.assertAll();
      });
    });

    assertThat(assertionError).hasMessageContaining(format("Expecting actual:%n  \"Yoda\"%nto start with:%n  \"L\""))
                              .hasMessageContaining(format("Expecting actual:%n  \"Yoda\"%nto start with:%n  \"M\""));
  }

  @Test
  void fails_if_arry_has_more_than_one_element() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Jedi[] jedis = { new Jedi("Yoda", "red"), new Jedi("Luke", "green") };
      assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> assertThat(yoda.getName()).startsWith("Y"));
    }).withMessageContaining("Expected size: 1 but was: 2");
  }
}
