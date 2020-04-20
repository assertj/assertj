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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ProxyableObjectChangingMethodTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractAssert#asInstanceOf(InstanceOfAssertFactory)}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("AbstractAssert asInstanceOf(InstanceOfAssertFactory)")
class AbstractAssert_asInstanceOf_with_InstanceOfAssertFactory_Test extends AbstractAssertBaseTest
    implements ProxyableObjectChangingMethodTest<ConcreteAssert> {

  @Override
  protected ConcreteAssert invoke_api_method() {
    assertions.asInstanceOf(LONG);
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertIsInstanceOf(getInfo(assertions), getActual(assertions), Long.class);
  }

  @Override
  public void should_return_this() {
    // Test disabled since asInstanceOf does not return this.
  }

  @Override
  public ConcreteAssert getAssertion() {
    return assertions;
  }

  @Override
  public ConcreteAssert getSoftAssertion(AbstractSoftAssertions softly) {
    return softly.proxy(ConcreteAssert.class, Object.class, getActual(assertions));
  }

  @Override
  public AbstractAssert<?, ?> invoke_object_changing_method(ConcreteAssert assertion) {
    return assertion.asInstanceOf(LONG);
  }

  @Test
  void should_throw_npe_if_no_factory_is_given() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.asInstanceOf(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());
  }

  @Test
  void should_return_narrowed_assert_type() {
    // WHEN
    AbstractAssert<?, ?> result = assertions.asInstanceOf(LONG);
    // THEN
    then(result).isInstanceOf(AbstractLongAssert.class);
  }

}
