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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;

/**
 * Base class to test the concrete methods of {@link AbstractAssert} (using a dummy implementation).
 * 
 * @author Olivier Michallat
 */
public abstract class AbstractAssertBaseTest extends BaseTestTemplate<ConcreteAssert, Object> {
  @Override
  protected ConcreteAssert create_assertions() {
    return new ConcreteAssert(6L);
  }

  protected ConcreteAssert applyIgnoreWithoutEffects() {
    return assertions.ignoreWhen(v -> (Long) v < 8);
  }

  protected ConcreteAssert applyIgnoreWithEffects() {
    return assertions.ignoreWhen(v -> (Long) v > 8);
  }

  @Test
  public void should_have_no_internal_effects_when_assertions_should_be_ignored() {
    this.assertions = applyIgnoreWithoutEffects();
    invoke_api_method();
    verify_no_internal_effects();
  }

  @Test
  public void should_have_internal_effects_when_assertions_should_not_be_ignored() {
    this.assertions = applyIgnoreWithEffects();
    invoke_api_method();
    verify_internal_effects();
  }

  @Test
  public void should_return_this_when_assertions_should_be_ignored() {
    this.assertions = applyIgnoreWithoutEffects();
    ConcreteAssert returned = invoke_api_method();
    assertThat(returned).isSameAs(assertions);
  }

  /**
   * Verifies that invoking the API method had the expected effects (usually, setting some internal state or invoking an internal
   * object).
   */
  protected void verify_no_internal_effects() {
    verifyNoInteractions(objects, conditions, assertionErrorCreator);
  }
}
