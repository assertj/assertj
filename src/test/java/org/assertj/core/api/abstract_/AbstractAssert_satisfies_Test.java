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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import java.util.function.Consumer;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Jedi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AbstractAssert_satisfies_Test {

  @Rule
  public ExpectedException thrown = none();
  private Jedi yoda;
  private Jedi luke;
  private Consumer<Jedi> jediRequirements;

  @Before
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
    thrown.expectAssertionError("[check light saber] expected:<\"[Green]\"> but was:<\"[Red]\">");
    assertThat(new Jedi("Vader", "Red")).satisfies(jediRequirements);
  }

  @Test
  public void should_fail_if_consumer_is_null() {
    // then
    thrown.expectNullPointerException("The Consumer<T> expressing the assertions requirements must not be null");
    // when
    assertThat(yoda).satisfies(null);
  }
}
