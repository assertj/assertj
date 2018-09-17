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
package org.assertj.core.api.abstract_;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.function.Consumer;

import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractAssert_satisfies_with_Consumer_Test {

  private Jedi yoda;
  private Jedi luke;
  private Consumer<Jedi> jediRequirements;

  @BeforeEach
  public void setup() {
    yoda = new Jedi("Yoda", "Green");
    luke = new Jedi("Luke Skywalker", "Green");
    jediRequirements = jedi -> {
      assertThat(jedi.lightSaberColor).as("check light saber").isEqualTo("Green");
      assertThat(jedi.getName()).as("check name").doesNotContain("Dark");
    };
  }

  @Test
  public void should_satisfy_single_requirement() {
    assertThat(yoda).satisfies(jedi -> assertThat(jedi.lightSaberColor).isEqualTo("Green"));
  }

  @Test
  public void should_satisfy_multiple_requirements() {
    assertThat(yoda).satisfies(jediRequirements);
    assertThat(luke).satisfies(jediRequirements);
  }

  @Test
  public void should_fail_according_to_requirements() {
    Jedi vader = new Jedi("Vader", "Red");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(vader).satisfies(jediRequirements))
                                                   .withMessage(format("[check light saber] %nExpecting:%n <\"Red\">%nto be equal to:%n <\"Green\">%nbut was not."));
  }

  @Test
  public void should_fail_if_consumer_is_null() {
    Consumer<Jedi> nullRequirements = null;
    assertThatNullPointerException().isThrownBy(() -> assertThat(yoda).satisfies(nullRequirements))
                                    .withMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }
}
