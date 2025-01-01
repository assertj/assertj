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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.ConcreteAssert;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 */
class AbstractAssert_isNotEqualTo_Test extends AbstractAssertBaseTest {

  @Override
  protected ConcreteAssert invoke_api_method() {
    return assertions.isNotEqualTo(Long.valueOf(8L));
  }

  @Test
  void should_fail_because_called_on_assertion_directly() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(assertions).isNotEqualTo(assertions));
    // THEN
    then(thrown).isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Attempted to compare an assertion object to another object using 'isNotEqualTo'. "
                            + "This is not supported. Perhaps you meant 'isNotSameAs' instead?");
  }

  @Test
  void should_not_fail_when_equals_exceptions_is_deactivated() {
    AbstractAssert.throwUnsupportedExceptionOnEquals = false;
    try {
      assertions.isNotEqualTo(new ConcreteAssert("potato"));
    } finally {
      AbstractAssert.throwUnsupportedExceptionOnEquals = true;
    }
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertNotEqual(getInfo(assertions), getActual(assertions), 8L);
  }
}
