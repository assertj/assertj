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
package org.assertj.core.api.abstract_;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ConcreteAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for <code>{@link AbstractAssert#equals(Object)}</code> and <code>{@link AbstractAssert#hashCode()}</code>.
 * 
 * @author Nicolas François
 */
class AbstractAssert_equal_hashCode_Test {

  private ConcreteAssert assertions = new ConcreteAssert("myString");

  @Test
  @SuppressWarnings("deprecation")
  void should_fail_because_not_supported_operation() {
    assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> assertions.equals("anotherString"))
                                                                  .withMessageContaining("'equals' is not supported...maybe you intended to call 'isEqualTo'");
  }

  @Test
  @SuppressWarnings("deprecation")
  void should_not_fail_when_equals_exceptions_is_deactivated() {
    AbstractAssert.throwUnsupportedExceptionOnEquals = false;
    assertions.equals("anotherString");
  }

  @Test
  void shouldReturnOneAsHashCode() {
    assertThat(assertions.hashCode()).isEqualTo(1);
  }
  @Test
  void shouldReturnOneAsHashCodeCondition() {
    conditionalAssertThat(assertThat(assertions.hashCode()),true,1);
  }
  @Test
  void shouldReturnOneAsHashCodeNotNull() {
    notNullAssertThat(assertThat(assertions.hashCode()),1);
  }
}
