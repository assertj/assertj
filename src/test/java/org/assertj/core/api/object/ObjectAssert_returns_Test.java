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
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.Test;

/**
 * @author Takuya "Mura-Mi" Murakami
 */
class ObjectAssert_returns_Test extends ObjectAssertBaseTest {

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.returns("Yoda", Jedi::getName);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions).getName(), "Yoda");
  }

  @Test
  void should_fail_with_throwing_NullPointerException_if_method_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.returns("May the force be with you.", null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The given getter method/Function must not be null");
  }

  @Test
  void perform_assertion_like_users() {
    // GIVEN
    Jedi yoda = new Jedi("Yoda", "Green");
    // WHEN/THEN
    assertThat(yoda).returns("Yoda", from(Jedi::getName))
                    .returns("Yoda", Jedi::getName);
  }

}
