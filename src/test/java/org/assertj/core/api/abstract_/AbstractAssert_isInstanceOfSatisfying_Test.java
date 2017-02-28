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
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Jedi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AbstractAssert_isInstanceOfSatisfying_Test extends AbstractAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();
  
  // init here to make it available in create_assertions() 
  private Jedi yoda = new Jedi("Yoda", "Green");
  private Jedi luke = new Jedi("Luke Skywalker", "Green");
  private Consumer<Jedi> jediRequirements;

  @Before
  public void setup() {
    jediRequirements = jedi -> {
      assertThat(jedi.lightSaberColor).as("check light saber").isEqualTo("Green");
      assertThat(jedi.getName()).as("check name").doesNotContain("Dark");
    };
  }
  
  @Override
  protected ConcreteAssert create_assertions() {
    return new ConcreteAssert(yoda);
  }
  
  @Override
  protected ConcreteAssert invoke_api_method() {
    return assertions.isInstanceOfSatisfying(Jedi.class, jediRequirements);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertIsInstanceOf(getInfo(assertions), getActual(assertions), Jedi.class);
  }
  
  @Test
  public void should_satisfy_single_requirement() {
    assertThat(yoda).isInstanceOfSatisfying(Jedi.class, jedi -> assertThat(jedi.lightSaberColor).isEqualTo("Green"));
  }

  @Test
  public void should_satisfy_multiple_requirements() {
    assertThat(yoda).isInstanceOfSatisfying(Jedi.class, jediRequirements);
    assertThat(luke).isInstanceOfSatisfying(Jedi.class, jediRequirements);
  }

  @Test
  public void should_fail_according_to_requirements() {
    thrown.expectAssertionError("[check light saber] expected:<\"[Green]\"> but was:<\"[Red]\">");
    assertThat(new Jedi("Vader", "Red")).isInstanceOfSatisfying(Jedi.class, jediRequirements);
  }

  @Test
  public void should_fail_if_consumer_is_null() {
    // then
    thrown.expectNullPointerException("The Consumer<T> expressing the assertions requirements must not be null");
    // when
    assertThat(yoda).isInstanceOfSatisfying(Jedi.class, null);
  }
  
  @Test
  public void should_fail_if_type_is_null() {
    // then
    thrown.expectNullPointerException("The given type should not be null");
    // when
    assertThat(yoda).isInstanceOfSatisfying(null, jediRequirements);
  }
}
